package hr.fer.zemris.optjava.dz10;

import java.util.*;

/**
 * Created by zac on 02.01.17..
 */
public class NSGA {

    private static final double MUTATION = 0.1;
    private static final int TOURNAMENT = 3;
    private final MOOPProblem problem;
    private final int populationSize;
    private final int maxIter;
    private final Random random;
    private final CrowdingSortComparator crowdingComparator;
    private List<Solution> population;
    private List<Solution> previousPopulation;
    private List<List<Solution>> fronts;

    public NSGA(MOOPProblem problem, int populationSize, int maxIter, Random random) {
        this.problem = problem;
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.random = random;
        this.crowdingComparator = new CrowdingSortComparator();

        this.population = new ArrayList<>();

        int dimension = this.problem.getDimension();
        while (population.size() != populationSize) {
            double[] x = new double[dimension];
            for (int i = 0; i < dimension; i++) {
                x[i] = (this.problem.upperBound(i) - this.problem.lowerBound(i)) * random.nextDouble() + this.problem.lowerBound(i);
            }
            double[] z = this.problem.evaluateSolution(x);
            Solution t = new Solution(x, z);
            population.add(t);
        }
    }

    public List<List<Solution>> run() {

        for (int iteration = 0; iteration < maxIter; iteration++) {

            previousPopulation = new ArrayList<>(population);
            nonDominatedSort();
            updateDistance();

            List<Solution> newSolutions = new ArrayList<>();
            while (newSolutions.size() < populationSize) {
                Solution child = tournamentChild();
                newSolutions.add(child);
            }

            population = new ArrayList<>();
            population.addAll(previousPopulation);
            population.addAll(newSolutions);
            nonDominatedSort();
            updateDistance();

            ArrayList<Solution> newPopulation = new ArrayList<>();
            int counter = populationSize;

            for (List<Solution> front : fronts) {
                if (front.size() <= counter) {
                    newPopulation.addAll(front);
                    counter -= front.size();
                } else {
                    // front is sorted, [worse, ..., best]
                    for (int j = front.size() - 1; counter > 0 && j > 0; j--) {
                        newPopulation.add(front.get(j));
                        counter--;
                    }
                    break;
                }
            }

            population = newPopulation;

        }

        nonDominatedSort();

        return fronts;
    }

    private void nonDominatedSort() {

        fronts = new ArrayList<>();

        fronts.add(new ArrayList<>());
        for (Solution p : population) {
            p.S.clear();
            p.eta = 0;

            for (Solution q : population) {
                if (p != q) {
                    if (dominates(p, q)) {
                        p.S.add(q);
                    } else if (dominates(q, p)) {
                        p.eta += 1;
                    }
                }
            }

            if (p.eta == 0) {
                fronts.get(0).add(p);
            }
        }

        population.removeAll(fronts.get(0));

        while (!population.isEmpty()) {
            List<Solution> last = fronts.get(fronts.size() - 1);
            List<Solution> current = new ArrayList<>();
            for (Solution i : last) {
                for (Solution j : i.S) {
                    j.eta -= 1;
                    if (j.eta == 0) {
                        current.add(j);
                        population.remove(j);
                    }
                }
            }
            fronts.add(current);
        }

    }

    private boolean dominates(Solution p, Solution q) {
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            if (p.z[i] > q.z[i]) {
                return false;
            }
        }
        return true;
    }

    private void updateDistance() {
        int frontIndex = 0;
        for (List<Solution> front : fronts) {
            int l = front.size();
            for (Solution s : front) {
                s.front = frontIndex;
                s.crowdDistance = 0.;
            }
            for (int m = 0; m < problem.getNumberOfObjectives(); m++) {
                final int mFinal = m;
                front.sort((Solution s1, Solution s2) -> Double.compare(s1.z[mFinal], s2.z[mFinal]));
                double min = front.get(0).z[m];
                double max = front.get(l - 1).z[m];
                front.get(0).crowdDistance = Double.POSITIVE_INFINITY;
                front.get(l - 1).crowdDistance = Double.POSITIVE_INFINITY;
                for (int i = 1; i < l - 1; i++) {
                    double diff = Math.abs(front.get(i + 1).z[m] - front.get(i - 1).z[m]) / (max - min);
                    front.get(i).crowdDistance += diff;
                }
            }
            front.sort(crowdingComparator);
            frontIndex += 1;
        }
    }

    private Solution tournamentChild() {

        Solution p1 = tournamentSelection();
        Solution p2 = tournamentSelection();

        int dimension = problem.getDimension();
        double[] x = new double[dimension];
        for (int i = 0; i < p1.x.length; i++) {
            x[i] = random.nextBoolean() ? p1.x[i] : p2.x[i];
        }

        int index = random.nextInt(dimension);
        x[index] += random.nextGaussian() * MUTATION;

        if (x[index] < problem.lowerBound(index) || x[index] > problem.upperBound(index)) {
            x[index] = random.nextDouble() * (problem.upperBound(index) - problem.lowerBound(index)) + problem.lowerBound(index);
        }

        double[] z = problem.evaluateSolution(x);

        return new Solution(x, z);
    }

    private Solution tournamentSelection() {
        Set<Integer> set = new HashSet<>();
        while (set.size() < TOURNAMENT) {
            set.add(random.nextInt(populationSize));
        }
        List<Solution> candidates = new ArrayList<>();
        for (Integer i : set) {
            candidates.add(previousPopulation.get(i));
        }
        return Collections.max(candidates, crowdingComparator);
    }

    private class CrowdingSortComparator implements Comparator<Solution> {
        @Override
        public int compare(Solution o1, Solution o2) {
            if (o1.front != o2.front) {
                if (o1.front < o2.front) {
                    return 1;
                } else if (o1.front > o2.front) {
                    return -1;
                }
            }
            if (o1.crowdDistance > o2.crowdDistance) {
                return 1;
            } else if (o1.crowdDistance < o2.crowdDistance) {
                return -1;
            }
            return 0;
        }
    }

}
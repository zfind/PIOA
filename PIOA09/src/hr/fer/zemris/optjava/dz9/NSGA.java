package hr.fer.zemris.optjava.dz9;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 02.01.17..
 */
public class NSGA {

    public static final double SIGMA_SHARE = 2.0;
    public static final double ALPHA = 2.0;
    public static final double MUTATION = 0.1;
    private final MOOPProblem problem;
    private final IDistance distance;
    private final int populationSize;
    private final int maxIter;
    private final Random random;
    private double totalFitness;
    private List<Solution> population;
    private List<Solution> previousPopulation;
    private List<List<Solution>> fronts;

    public NSGA(MOOPProblem problem, IDistance distance, int populationSize, int maxIter, Random random) {
        this.problem = problem;
        this.distance = distance;
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.random = random;

        this.totalFitness = 0.;

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
            assignFitness();

            List<Solution> newSolutions = new ArrayList<>();
            while (newSolutions.size() < populationSize) {
                Solution child = proportionalGeneration();
                newSolutions.add(child);
            }

            population = new ArrayList<>();
            population.addAll(previousPopulation);
            population.addAll(newSolutions);
            nonDominatedSort();
            assignFitness();

            ArrayList<Solution> newPopulation = new ArrayList<>();
            int counter = populationSize;

            for (List<Solution> front : fronts) {
                if (front.size() <= counter) {
                    newPopulation.addAll(front);
                    counter -= front.size();
                } else {
                    population = front;
                    nonDominatedSort();
                    assignFitness();
                    for (int i = 0; counter > 0 && i < fronts.size(); i++) {
                        for (int j = 0; counter > 0 && j < fronts.get(i).size(); j++) {
                            newPopulation.add(fronts.get(i).get(j));
                            counter--;
                        }
                    }
                    break;
                }
            }

            population = newPopulation;

        }

        nonDominatedSort();

        return fronts;
    }

    private Solution proportionalGeneration() {

        Solution p1 = null;
        double pick = totalFitness * random.nextDouble();
        for (Solution s : previousPopulation) {
            pick -= s.fitness;
            if (pick < 0.) {
                p1 = s;
                break;
            }
        }

        Solution p2 = null;
        pick = totalFitness * random.nextDouble();
        for (Solution s : previousPopulation) {
            pick -= s.fitness;
            if (pick < 0.) {
                p2 = s;
                break;
            }
        }

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

    private void assignFitness() {
        double Fmin = Double.MAX_VALUE;
        double F = fronts.get(0).size();
        totalFitness = 0.;

        for (List<Solution> front : fronts) {
            for (Solution s : front) {
                s.fitness = F / density(s);
                if (Fmin < s.fitness) {
                    Fmin = s.fitness;
                }
                totalFitness += s.fitness;
            }
            F = Fmin * 0.99;
        }
    }

    private double density(Solution s) {
        double nc = 0.;
        for (Solution j : previousPopulation) {
            double dist = distance.distance(s, j);
            if (dist < SIGMA_SHARE) {
                nc += 1. - Math.pow(dist / SIGMA_SHARE, ALPHA);
            }
        }
        return nc;
    }

    private boolean dominates(Solution p, Solution q) {
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            if (p.z[i] > q.z[i]) {
                return false;
            }
        }
        return true;
    }

}
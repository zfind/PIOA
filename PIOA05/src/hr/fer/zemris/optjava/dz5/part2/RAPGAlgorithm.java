package hr.fer.zemris.optjava.dz5.part2;

import java.util.*;

/**
 * Created by zac on 12.11.16..
 */
public class RAPGAlgorithm {

    public static final int MIN_POPULATION = 10;
    public static final int MAX_POPULATION = 30;
    public static final int MAX_EFFORT = 200;
    public static final double SUCC_RATIO = 0.5;
    public static final int CHANGE_COMP_FACTOR_AFTER_ITERS = 2000;
    public static final double COMP_FACTOR_CHANGE = 0.2;
    public static final int TOURNAMENT = 2;
    private final double MUTATION;
    private final int N;
    private final Random random;
    private final ISelection selection1;
    private final ISelection selection2;
    private final IFunction function;
    private double comparisonFactor;
    private Population population;

    public RAPGAlgorithm(Random random, int N, IFunction function, Population population,
                         ISelection selection1, ISelection selection2, double mutation) {
        this.N = N;
        this.random = random;
        this.function = function;
        this.comparisonFactor = 0.;
        this.selection1 = selection1;
        this.selection2 = selection2;
        this.MUTATION = mutation;
        this.population = population;
    }


    public Population run() {
        int iterationsAfterCompFactorChange = 0;
        Solution bestSolution = population.getBest();
        Population bestPopulation = population;

        while (true) {

            Population newPopulation = nextPopulationWithSelection(selection1, selection2);
            if (newPopulation == null) {
                return bestPopulation;
            } else {
                population = newPopulation;
            }

            if (iterationsAfterCompFactorChange > CHANGE_COMP_FACTOR_AFTER_ITERS) {
                iterationsAfterCompFactorChange = 0;
                if (comparisonFactor + COMP_FACTOR_CHANGE <= 1.) {
                    comparisonFactor += COMP_FACTOR_CHANGE;
//                    System.out.println(comparisonFactor);
                }
            } else {
                iterationsAfterCompFactorChange++;
            }

            Solution currentBest = population.getBest();
            if (bestSolution.getFitness() > currentBest.getFitness()) {
                bestSolution = currentBest;
                bestPopulation = population;
                System.out.println("-->\t" + bestSolution.getFitness() + "\t" + bestSolution);
            }
        }

    }

    private Population nextPopulationWithSelection(ISelection selection1, ISelection selection2) {
        Set<Solution> nextPopulation = new HashSet<>();
        Set<Solution> geneticPool = new HashSet<>();
        int iteration = 0;

        while (iteration < MAX_EFFORT) {
            Solution p1 = selection1.select(population);
            Solution p2 = selection2.select(population);
            Solution ch = cross(p1, p2);
            ch = mutate(ch);
            iteration++;

            if (nextPopulation.size() >= MAX_POPULATION) {
                break;
            }

            if (isSuccessful(ch, p1, p2)) {
                nextPopulation.add(ch);
            } else {
                if (geneticPool.size() < MAX_POPULATION) {
                    geneticPool.add(ch);
                }
                if (nextPopulation.size() >= population.size() * SUCC_RATIO) {
                    for (Solution s : geneticPool) {
                        if (nextPopulation.size() >= MAX_POPULATION) {
                            break;
                        } else {
                            nextPopulation.add(s);
                        }
                    }
                    return new Population(nextPopulation);
                }
            }
        }

        if (nextPopulation.size() >= MIN_POPULATION) {
            return new Population(nextPopulation);
        } else {
            return null;
        }
    }

    private boolean isSuccessful(Solution child, Solution p1, Solution p2) {
        double best = Math.min(p1.getFitness(), p2.getFitness());
        double worst = Math.max(p1.getFitness(), p2.getFitness());
        return child.getFitness() < (worst + comparisonFactor * (best - worst));
    }

    // ox1 iz nenr-a
    private Solution cross(Solution p1, Solution p2) {
        List<Integer> child = new ArrayList<>(p1.getValues());
        List<Integer> p2Values = p2.getValues();
        int breakpoint = random.nextInt(N);
        for (int location = 0; location < breakpoint; location++) {
            int p1Factory = child.get(location);
            int p2Factory = p2Values.get(location);
            int p2FactoryLocationInP1 = child.indexOf(p2Factory);
            child.set(location, p2Factory);
            child.set(p2FactoryLocationInP1, p1Factory);
        }
        return new Solution(child, function);
    }

    private Solution mutate(Solution child) {
        if (random.nextDouble() < MUTATION) {
            List<Integer> locations = new ArrayList<>(child.getValues());
            int location1 = random.nextInt(N);
            int location2 = random.nextInt(N);
            int location1Factory = locations.get(location1);
            int location2Factory = locations.get(location2);
            locations.set(location1, location2Factory);
            locations.set(location2, location1Factory);
            return new Solution(locations, function);
        } else {
            return child;
        }
    }
}

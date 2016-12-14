package hr.fer.zemris.optjava.dz5.part1;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by zac on 12.11.16..
 */
public class GeneticAlgorithm {

    private static final int MIN_POPULATION = 10;
    private static final int MAX_POPULATION = 100;
    private static final double MAX_SEL_PRESS = 200;
    private static final double SUCC_RATIO = 0.5;
    private static final double COMP_FACTOR_CHANGE = 0.1;
    private static final int CHANGE_COMP_FACTOR_AFTER_ITERS = 1000;
    private static final int TOURNAMENT = 2;
    private static final double MUTATION = 0.5;
    private final int N;
    private final Random random;
    private final ISelection selection1;
    private final ISelection selection2;
    private final IFunction function;
    private double comparisonFactor;
    private Population population;

    public GeneticAlgorithm(Random random, int N, IFunction function,
                            Population population,
                            ISelection selection1, ISelection selection2) {
        this.N = N;
        this.random = random;
        this.function = function;
        this.selection1 = selection1;
        this.selection2 = selection2;
        this.population = population;
        this.comparisonFactor = 0.;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Greska. Krivi argumenti");
            System.out.println("npr. 100");
            System.exit(-1);
        }
        int N = Integer.parseInt(args[0]);
        Random random = new Random();
        IFunction function = new MaxOnes();
        ISelection selection1 = new TournamentSelection(TOURNAMENT, random);
        ISelection selection2 = new TournamentSelection(TOURNAMENT, random);
        Population population = new Population(N, function, MIN_POPULATION, random);

        GeneticAlgorithm algorithm = new GeneticAlgorithm(random, N, function, population, selection1, selection2);
        algorithm.run();

    }

    public void run() {
        int numberOfIterations = 0;
        Solution bestSolution = population.getBest();
        while (true) {

            population = nextPopulationWithSelection(selection1, selection2);
            if (population == null) {
                break;
            }

            if (numberOfIterations >= CHANGE_COMP_FACTOR_AFTER_ITERS) {
                numberOfIterations = 0;
                if (comparisonFactor + COMP_FACTOR_CHANGE <= 1.) {
                    comparisonFactor += COMP_FACTOR_CHANGE;
                }
            } else {
                numberOfIterations++;
            }

            Solution currentBest = population.getBest();
            if (bestSolution.getFitness() < currentBest.getFitness()) {
                bestSolution = currentBest;
                System.out.println("-->\t" + bestSolution.getFitness() + "\t" + bestSolution);
            }

        }
    }

    private Population nextPopulationWithSelection(ISelection selection1, ISelection selection2) {
        Set<Solution> nextPopulation = new HashSet<>();
        Set<Solution> geneticPool = new HashSet<>();
        int poolCounter = 0;

        while ((nextPopulation.size() + poolCounter) < population.size() * MAX_SEL_PRESS) {
            Solution p1 = selection1.select(population);
            Solution p2 = selection2.select(population);
            Solution ch = cross(p1, p2);
            ch = mutate(ch);

            if (nextPopulation.size() >= MAX_POPULATION) {
                break;
            }

            if (isSuccessful(ch, p1, p2)) {
                nextPopulation.add(ch);
            } else {
                poolCounter++;
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
        double best = Math.max(p1.getFitness(), p2.getFitness());
        double worst = Math.min(p1.getFitness(), p2.getFitness());
        return child.getFitness() > (worst + comparisonFactor * (best - worst));
    }

    private Solution cross(Solution p1, Solution p2) {
        byte[] val1 = p1.getValues();
        byte[] val2 = p2.getValues();
        byte[] values = new byte[N];
        int breakpoint = random.nextInt(N);
        System.arraycopy(val1, 0, values, 0, breakpoint);
        System.arraycopy(val2, breakpoint, values, breakpoint, N - breakpoint);
        return new Solution(values, function);
    }

    private Solution mutate(Solution child) {
        if (random.nextDouble() < MUTATION) {
            int index = random.nextInt(N);
            byte[] values = child.getValues();
            values[index] ^= 1;
            return new Solution(values, function);
        } else {
            return child;
        }
    }
}

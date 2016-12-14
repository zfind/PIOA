package hr.fer.zemris.optjava.dz7.clonalg;

import hr.fer.zemris.optjava.dz7.net.*;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by zac on 12.12.16..
 */
public class ClonAlg {

    private static final double WEIGHT_LOWER_BOUND = -1.;
    private static final double WEIGHT_UPPER_BOUND = 1.;
    private static final double BETA = 1.;
    private static final int D = 10;
    private final int maxIters;
    private final int populationSize;
    private final double minimalFitness;
    private final int dimensions;
    private final FFANNEvaluator evaluator;
    private final Random random;
    private Solution[] population;

    public ClonAlg(int populationSize, double minimalFitness, int maxIters, int dimensions,
                   FFANNEvaluator evaluator, Random random) {
        this.evaluator = evaluator;
        this.populationSize = populationSize;
        this.population = new Solution[populationSize];
        this.minimalFitness = minimalFitness;
        this.maxIters = maxIters;
        this.random = random;

        this.dimensions = dimensions;

        for (int i = 0; i < populationSize; i++) {
            double[] weights = new double[dimensions];
            for (int j = 0; j < dimensions; j++) {
                weights[j] = random.nextDouble() * (WEIGHT_UPPER_BOUND - WEIGHT_LOWER_BOUND) + WEIGHT_LOWER_BOUND;
            }
            double fitness = evaluator.evaluate(weights);
            population[i] = new Solution(weights, fitness);
        }
    }

    public static void main(String[] args) {
        Dataset dataset = new Dataset("data/07-iris-formatirano.data");
        ICostFunction errorFunction = new RMSError();

        FFANN net = new FFANN(new int[]{4, 5, 3, 3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                });

        FFANNEvaluator evaluator = new FFANNEvaluator(net, dataset, errorFunction);

        ClonAlg alg = new ClonAlg(40, 0.1, 100, net.getWeightsCount(), evaluator, new Random());
        double[] weights = alg.run();
        net.printResult(weights, dataset, errorFunction);
    }

    public double[] run() {
        Solution best;
        for (int i = 0; i < maxIters; i++) {
            Solution[] clones = clonePopulation();
            Solution[] mutated = mutate(clones);
            population = select(mutated);
            Solution[] newSolutions = create();
            addNewSolutions(newSolutions);
            best = pickBest();
            if (best.fitness <= minimalFitness) {
                break;
            }
            if (i % 10 == 0) {
                System.out.println(i + ": " + best.fitness);
            }
        }
        best = pickBest();
        System.out.println("Rjesenje: " + best.fitness);
        return best.weights;
    }

    private Solution[] clonePopulation() {
        int maxClones = 0;
        for (int i = 1; i <= populationSize; i++) {
            maxClones += (int) ((BETA * populationSize) / (double) i);
        }
        Solution[] clones = new Solution[maxClones];
        Arrays.sort(population);
        int current = 0;
        for (int i = 1; i <= population.length; i++) {
            Solution origin = population[i - 1];
            int numOfClones = (int) ((BETA * populationSize) / (double) i);
            for (int j = 0; j < numOfClones; j++) {
                clones[current++] = new Solution(origin);
            }
        }
        return clones;
    }

    private Solution[] mutate(Solution[] clones) {
        for (int i = 0; i < clones.length; i++) {
            Solution current = clones[i];
            int mutationCount = (int) ((i / (double) clones.length) * dimensions);
            for (int j = 0; j < mutationCount; j++) {
                int index = random.nextInt(dimensions);
                current.weights[index] += random.nextGaussian();
            }
            current.fitness = evaluator.evaluate(current.weights);
        }
        return clones;
    }

    private Solution[] select(Solution[] mutated) {
        Solution[] solutions = new Solution[populationSize];
        Arrays.sort(mutated);
        for (int i = 0; i < populationSize; i++) {
            solutions[i] = new Solution(mutated[i]);
        }
        return solutions;
    }

    private Solution[] create() {
        Solution[] solutions = new Solution[D];
        for (int i = 0; i < D; i++) {
            double[] weights = new double[dimensions];
            for (int j = 0; j < dimensions; j++) {
                weights[j] = random.nextDouble() * (WEIGHT_UPPER_BOUND - WEIGHT_LOWER_BOUND) + WEIGHT_LOWER_BOUND;
            }
            double fitness = evaluator.evaluate(weights);
            solutions[i] = new Solution(weights, fitness);
        }
        return solutions;
    }

    private void addNewSolutions(Solution[] solutions) {
        Arrays.sort(solutions);
        int offset = populationSize - D;
        System.arraycopy(solutions, 0, population, offset, D);
    }

    private Solution pickBest() {
        double bestFitness = 1E7;
        Solution best = null;
        for (int i = 0; i < populationSize; i++) {
            if (population[i].fitness <= bestFitness) {
                best = population[i];
                bestFitness = best.fitness;
            }
        }
        return best;
    }

}
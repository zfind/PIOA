package hr.fer.zemris.optjava.dz8.de;


import hr.fer.zemris.optjava.dz8.net.NNEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 19.12.16..
 */
public class DifferentialEvolution {

    private static final double WEIGHT_LOWER_BOUND = -1.;
    private static final double WEIGHT_UPPER_BOUND = 1.;

    private final int populationSize;
    private final int dimensions;
    private final double minimalFitness;
    private final int maxIter;
    private final NNEvaluator evaluator;
    private final AbstractStrategy strategy;
    private final ICrossOperator crossOp;
    private List<Solution> population;

    public DifferentialEvolution(int populationSize, int dimensions, double minimalFitness, int maxIter,
                                 NNEvaluator evaluator, AbstractStrategy strategy, ICrossOperator crossOperator,
                                 Random random) {
        this.populationSize = populationSize;
        this.dimensions = dimensions;
        this.minimalFitness = minimalFitness;
        this.maxIter = maxIter;
        this.evaluator = evaluator;
        this.strategy = strategy;
        this.crossOp = crossOperator;

        this.population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            double[] weights = new double[this.dimensions];
            for (int j = 0; j < this.dimensions; j++) {
                weights[j] = random.nextDouble() * (WEIGHT_UPPER_BOUND - WEIGHT_LOWER_BOUND) + WEIGHT_LOWER_BOUND;
            }
            population.add(new Solution(weights, evaluator));
        }
    }

    public Solution run() {
        Solution bestSolution = Collections.min(population);

        for (int i = 0; i < maxIter && bestSolution.fitness > minimalFitness; i++) {
            Solution iterationBest = bestSolution;

            List<Solution> trials = new ArrayList<>();
            for (int j = 0; j < populationSize; j++) {
                Solution target = population.get(j);

                double[] mutant = strategy.mutate(population, bestSolution, j);
                double[] trial = crossOp.cross(target.weights, mutant);
                Solution trialSol = new Solution(trial, evaluator);

                if (trialSol.fitness <= target.fitness) {
                    trials.add(trialSol);
                    if (trialSol.fitness <= iterationBest.fitness) {
                        iterationBest = trialSol;
                        System.out.println(i + ": " + iterationBest.fitness);
                    }
                } else {
                    trials.add(target);
                }
            }
            population = trials;
            bestSolution = iterationBest;

            if (i % 100 == 0) {
                System.out.println(i + ": " + bestSolution.fitness);
            }
        }

        System.out.println("Rjesenje: " + bestSolution.fitness);

        return bestSolution;
    }

}
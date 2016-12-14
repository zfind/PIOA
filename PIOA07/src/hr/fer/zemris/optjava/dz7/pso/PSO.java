package hr.fer.zemris.optjava.dz7.pso;

import hr.fer.zemris.optjava.dz7.net.FFANNEvaluator;

import java.util.Random;

/**
 * Created by zac on 12.12.16..
 */
public abstract class PSO {
    public static final double WEIGHT_LOWER_BOUND = -1.;
    public static final double WEIGHT_UPPER_BOUND = 1.;
    public static final double V_MIN = -1.;
    public static final double V_MAX = 1.;
    public static final double C1 = 2.05;
    public static final double C2 = 2.05;
    public static final double W_INITIAL = 1.;
    public static final double W_MAX = 1.;
    public static final double W_MIN = 0.5;
    public static final double W_T = 1000;
    public static final double M = 1e7;

    protected final int populationSize;
    protected final int dimensions;
    protected final int maxIter;
    protected final double minimalFitness;
    protected final FFANNEvaluator evaluator;
    protected final Random random;
    protected Particle[] population;
    protected double[] bestPosition;
    protected double bestFitness;

    public PSO(int populationSize, int dimensions,
               int maxIter, double minimalFitness,
               FFANNEvaluator evaluator, Random random) {
        this.populationSize = populationSize;
        this.dimensions = dimensions;
        this.maxIter = maxIter;
        this.minimalFitness = minimalFitness;
        this.evaluator = evaluator;
        this.random = random;

        this.population = new Particle[populationSize];
        this.bestFitness = M;
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Particle(dimensions, evaluator, random);
            if (population[i].personalBestFitness < bestFitness) {
                bestPosition = population[i].getPosition();
                bestFitness = population[i].personalBestFitness;
            }
        }
    }

    public double[] run() {
        double fitness = M;
        double w = W_INITIAL;
        for (int i = 0; i < maxIter && bestFitness >= minimalFitness; i++) {
            updatePosition(w);
            w = (i <= W_T) ? i / (W_T) * (W_MIN - W_MAX) + W_MAX : W_MIN;

            if (i % 10 == 0) {
                System.out.println(i + ": " + bestFitness);
            }
        }

        System.out.println("Rjesenje: " + bestFitness);
        return bestPosition;
    }

    public abstract void updatePosition(double w);

}

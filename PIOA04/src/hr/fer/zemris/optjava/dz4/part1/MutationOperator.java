package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;

/**
 * Created by zac on 02.11.16..
 */
public class MutationOperator {
    private final Random random;
    private final double sigma;
    private final int maxIter;
    private final int populationSize;

    public MutationOperator(Random random, double sigma, int maxIter, int populationSize) {
        this.random = random;
        this.sigma = sigma;
        this.maxIter = maxIter;
        this.populationSize = populationSize;
    }

    public void mutate(Solution ch, int currentIteration) {
        for (int i = 0; i < ch.values.length; i++) {
            ch.values[i] += random.nextGaussian() * sigma
                    * Math.pow(
                    (1. - ((double) currentIteration) / maxIter),
                    populationSize);
        }
    }

}

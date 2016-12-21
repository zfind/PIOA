package hr.fer.zemris.optjava.dz8.de;

import java.util.Random;

/**
 * Created by zac on 19.12.16..
 */
public class UniformCrossOperator implements ICrossOperator {

    private final double CR;
    private final Random random;

    public UniformCrossOperator(double CR, Random random) {
        this.CR = CR;
        this.random = random;
    }

    @Override
    public double[] cross(double[] target, double[] mutant) {
        int dimensions = target.length;
        double[] trial = new double[dimensions];

        int start = random.nextInt(dimensions);
        trial[start] = mutant[start];
        for (int i = start+1; i < start + dimensions; i++) {
            if (random.nextDouble() <= CR) {
                trial[i % dimensions] = mutant[i % dimensions];
            } else {
                trial[i % dimensions] = target[i % dimensions];
            }
        }

        return trial;
    }

}
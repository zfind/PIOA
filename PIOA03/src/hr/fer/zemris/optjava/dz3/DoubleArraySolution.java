package hr.fer.zemris.optjava.dz3;

import java.util.Random;

/**
 * Created by zac on 23.10.16..
 */
public class DoubleArraySolution extends SingleObjectiveSolution {
    public double[] values;

    public DoubleArraySolution(int n) {
        values = new double[n];
    }

    public DoubleArraySolution newLikeThis() {
        return new DoubleArraySolution(values.length);
    }

    public DoubleArraySolution duplicate() {
        DoubleArraySolution doubleArraySolution = newLikeThis();
        System.arraycopy(values, 0, doubleArraySolution.values, 0, values.length);
        return doubleArraySolution;
    }

    public void randomize(Random random, double[] lowerLimit, double[] upperLimit) {
        for (int i = 0; i < values.length; i++) {
            values[i] = (upperLimit[i] - lowerLimit[i]) * random.nextDouble() + lowerLimit[i];
        }
    }
}

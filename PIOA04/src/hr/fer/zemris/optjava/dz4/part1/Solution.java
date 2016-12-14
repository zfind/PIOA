package hr.fer.zemris.optjava.dz4.part1;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by zac on 23.10.16..
 */
public class Solution implements Comparable<Solution> {
    public final double[] values;
    public double fitness;
    public double value;

    public Solution(int n) {
        values = new double[n];
    }

    public Solution newLikeThis() {
        return new Solution(values.length);
    }

    public Solution duplicate() {
        Solution doubleArraySolution = newLikeThis();
        System.arraycopy(values, 0, doubleArraySolution.values, 0, values.length);
        return doubleArraySolution;
    }

    public void randomize(Random random, double[] lowerLimit, double[] upperLimit) {
        for (int i = 0; i < values.length; i++) {
            values[i] = (upperLimit[i] - lowerLimit[i]) * random.nextDouble() + lowerLimit[i];
        }
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');

        int last = values.length - 1;
        for (int i = 0; i < last; i++) {
            stringBuilder.append(df.format(values[i]));
            stringBuilder.append(" ");
        }
        stringBuilder.append(df.format(values[last]));
        stringBuilder.append(')');

        return stringBuilder.toString();
    }


    public int compareTo(Solution o) {
        if (this.fitness < o.fitness) {
            return -1;
        } else if (this.fitness == o.fitness) {
            return 0;
        } else {
            return 1;
        }
    }

}

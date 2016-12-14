package hr.fer.zemris.optjava.dz5.part1;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by zac on 12.11.16..
 */
public class Solution implements Comparable<Solution> {

    private byte[] values;
    private double fitness;
    private IFunction function;

    public Solution(int n, IFunction function, Random random) {
        this.values = new byte[n];
        this.function = function;
        randomize(random);
        calculateFitness();
    }

    public Solution(byte[] values, IFunction function) {
        this.values = values;
        this.function = function;
        calculateFitness();
    }

    // copy constructor
    public Solution(Solution solution) {
        this.values = solution.values.clone();
        this.fitness = solution.fitness;
        this.function = solution.function;
    }

    private void randomize(Random random) {
        for (int i = 0; i < values.length; i++) {
            if (random.nextBoolean()) {
                values[i] = 1;
            }
        }
    }

    private void calculateFitness() {
        this.fitness = function.valueAt(this);
    }

    public double getFitness() {
        return fitness;
    }

    public byte[] getValues() {
        return values;
    }

    public int getNumberOfOnes() {
        int k = 0;
        for (byte bit : values) {
            if (bit == 1) {
                k++;
            }
        }
        return k;
    }

    public int getNumberOfBits() {
        return values.length;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Solution) {
            if (Arrays.equals(this.values, ((Solution) other).values)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    @Override
    public int compareTo(Solution o) {
        if (this.fitness == o.fitness) {
            return 0;
        } else if (this.fitness > o.fitness) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte value : values) {
            sb.append(value);
        }
        return sb.toString();
    }
}

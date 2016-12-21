package hr.fer.zemris.optjava.dz8.net;

import Jama.Matrix;

import java.util.Arrays;

/**
 * Created by zac on 12.12.16..
 */
public class Sample {
    public final double[] input;
    public final Matrix trainingInput;
    public final double[] output;

    public Sample(double[] input, double[] output) {
        this.input = input;
        this.output = output;
        this.trainingInput = addBias(input);
    }

    private static Matrix addBias(double[] vector) {
        double[] arr = new double[vector.length + 1];
        arr[0] = 1;
        System.arraycopy(vector, 0, arr, 1, vector.length);
        return new Matrix(arr, arr.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(input) + " " + Arrays.toString(output);
    }

}

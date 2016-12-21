package hr.fer.zemris.optjava.dz8.net;

/**
 * Created by zac on 18.12.16..
 */

import Jama.Matrix;

import java.util.Arrays;

public class ElmanNN implements INeuralNetwork {

    private final int[] layers;
    private final ITransferFunction[] activationFunctions;
    private double[] context;

    public ElmanNN(int[] layers, ITransferFunction[] activationFunctions) {
        this.layers = layers;
        this.activationFunctions = activationFunctions;
        this.layers[0] = this.layers[0] + this.layers[1];
    }

    public void resetContext(double[] weights) {
        this.context = new double[layers[1]];
        for (int i = weights.length - context.length, currentIndex = 0; i < weights.length; i++) {
            context[currentIndex++] = weights[i];
        }
    }

    public double evaluate(double[] weights, Dataset dataset, ICostFunction errorFunction) {
        int size = dataset.size();
        double[][] y = new double[size][dataset.getOutputDimension()];
        int i = 0;
        for (Sample sample : dataset) {
            y[i++] = this.calcOutputs(sample.input, weights);
        }
        return 1. / dataset.size() * errorFunction.valueAt(dataset, y);
    }

    public double[] calcOutputs(double[] inputs, double[] weights) {
        Matrix input = addBiasAndContext(inputs);
        return calcOutputs(input, weights);
    }

    public double[] calcOutputs(Matrix inputVector, double[] weights) {
        int numberOfLayers = layers.length;
        int breakpoint = 0;
        Matrix output = inputVector;
        for (int i = 1; i < numberOfLayers; i++) {
            if (i > 1) {
                output = addBias(output);
            }
            int rows = layers[i - 1] + 1;
            int cols = layers[i];
            Matrix W = decodeWeightsVector(weights, breakpoint, rows, cols);
            breakpoint += rows * cols;
            output = output.transpose().times(W).transpose();
            output = activationFunctions[i].valueAt(output);
            if (i == 1) {
                for (int j = 0; j < output.getRowDimension(); j++) {
                    context[j] = output.get(j, 0);
                }
            }
        }
        return output.getColumnPackedCopy();
    }


    private Matrix addBias(double[] vector) {
        double[] arr = new double[vector.length + 1];
        arr[0] = 1;
        System.arraycopy(vector, 0, arr, 1, vector.length);
        return new Matrix(arr, arr.length);
    }

    private Matrix addBiasAndContext(double[] inputs) {
        double[] arr = new double[inputs.length + 1 + layers[1]];
        arr[0] = 1;
        System.arraycopy(inputs, 0, arr, 1, inputs.length);
        System.arraycopy(context, 0, arr, 1 + inputs.length, context.length);
        return new Matrix(arr, arr.length);
    }

    private Matrix addBias(Matrix matrix) {
        double[][] arr = matrix.getArray();
        int rows = arr.length + 1;
        int cols = arr[0].length;
        double[][] newArr = new double[rows][cols];
        newArr[0][0] = 1;
        System.arraycopy(arr, 0, newArr, 1, arr.length);
        return new Matrix(newArr);
    }

    private Matrix decodeWeightsVector(double[] weights, int breakpoint, int rows, int cols) {
        double[][] W = new double[rows][cols];
        int weight_counter = breakpoint;
        for (int i = 0; i < rows; i++, weight_counter += cols) {
            System.arraycopy(weights, weight_counter, W[i], 0, cols);
        }
        return new Matrix(W);
    }

    public int getWeightsCount() {
        int count = 0;
        for (int i = 1; i < layers.length; i++) {
            count += (layers[i - 1] + 1) * layers[i];
        }
        count += layers[1];
        return count;
    }

}

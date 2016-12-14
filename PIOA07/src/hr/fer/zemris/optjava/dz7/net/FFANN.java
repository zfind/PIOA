package hr.fer.zemris.optjava.dz7.net;

import Jama.Matrix;

import java.util.Arrays;

/**
 * Created by zac on 12.12.16..
 */
public class FFANN {

    private final int[] layers;
    private final ITransferFunction[] activationFunctions;

    public FFANN(int[] layers, ITransferFunction[] activationFunctions) {
        this.layers = layers;
        this.activationFunctions = activationFunctions;
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

        double[] weights = new double[net.getWeightsCount()];
        Arrays.fill(weights, 0.1);
        System.out.println(net.evaluate(weights, dataset, errorFunction));
        System.out.println(0.8365366587431725 + "\n");


        net = new FFANN(new int[]{4, 3, 3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                });
        weights = new double[net.getWeightsCount()];
        Arrays.fill(weights, 0.1);
        System.out.println(net.evaluate(weights, dataset, errorFunction));
        System.out.println(0.8566740399081082 + "\n");

        Arrays.fill(weights, -0.2);
        System.out.println(net.evaluate(weights, dataset, errorFunction));
        System.out.println(0.7019685477806382 + "\n");

        net.printResult(weights, dataset, errorFunction);
    }

    private Matrix addBias(double[] vector) {
        double[] arr = new double[vector.length + 1];
        arr[0] = 1;
        System.arraycopy(vector, 0, arr, 1, vector.length);
        return new Matrix(arr, arr.length);
    }

    public double evaluate(double[] weights, Dataset dataset, ICostFunction errorFunction) {
        int size = dataset.size();
        double[][] y = new double[size][dataset.getOutputDimension()];
        int i = 0;
        for (Sample sample : dataset) {
            y[i++] = this.calcOutputs(sample.trainingInput, weights);
        }
        return 1. / dataset.size() * errorFunction.valueAt(dataset, y);
    }

    public void printResult(double[] weights, Dataset dataset, ICostFunction errorFunction) {
        int size = dataset.size();
        int wrong = 0;
        for (Sample sample : dataset) {
            double[] output = this.calcOutputs(sample.trainingInput, weights);
            for (int j = 0; j < output.length; j++) {
                output[j] = output[j] >= 0.5 ? 1. : 0.;
            }
            for (int j = 0; j < output.length; j++) {
                if (output[j] != sample.output[j]) {
                    System.out.println("predicted: " + Arrays.toString(output)
                            + " real: " + Arrays.toString(sample.output));
                    wrong++;
                    break;
                }
            }
        }

        System.out.println("Krivo klasificirano " + wrong + " od " + size + " primjera");
        System.out.println("Tocnost: " + (size-wrong) / (double)size);
    }

    public double[] calcOutputs(double[] inputs, double[] weights) {
        Matrix input = addBias(inputs);
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
        }
        return output.getColumnPackedCopy();
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
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                W[i][j] = weights[weight_counter++];
            }
        }
        return new Matrix(W);
    }

    public int getWeightsCount() {
        int count = 0;
        for (int i = 1; i < layers.length; i++) {
            count += (layers[i - 1] + 1) * layers[i];
        }
        return count;
    }
}

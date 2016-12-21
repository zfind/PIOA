package hr.fer.zemris.optjava.dz8.net;

import java.util.Arrays;

/**
 * Created by zac on 12.12.16..
 */
public class NNEvaluator {
    private INeuralNetwork net;
    private Dataset dataset;
    private ICostFunction errorFunction;

    public NNEvaluator(INeuralNetwork net, Dataset dataset, ICostFunction errorFunction) {
        this.net = net;
        this.dataset = dataset;
        this.errorFunction = errorFunction;
    }

    public double evaluate(double[] weights) {
        net.resetContext(weights);
        return net.evaluate(weights, dataset, errorFunction);
    }

    public void printResult(double[] weights, Dataset dataset, ICostFunction errorFunction, double errorThreshold) {
        int size = dataset.size();
        int wrong = 0;
        for (Sample sample : dataset) {
            double[] output = net.calcOutputs(sample.input, weights);
            for (int j = 0; j < output.length; j++) {
                if (Math.abs(output[j] - sample.output[j]) <= errorThreshold) {
                    System.out.println("predicted: " + Arrays.toString(output)
                            + " real: " + Arrays.toString(sample.output));
                    wrong++;
                    break;
                }
            }
        }

        System.out.println("Krivo predvideno " + wrong + " od " + size + " primjera");
        System.out.println("Tocnost: " + (size - wrong) / (double) size);
    }
}

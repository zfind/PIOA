package hr.fer.zemris.optjava.dz8.net;

/**
 * Created by zac on 20.12.16..
 */
public interface INeuralNetwork {

    int getWeightsCount();

    void resetContext(double[] weights);

    double evaluate(double[] weights, Dataset dataset, ICostFunction errorFunction);

    double[] calcOutputs(double[] inputs, double[] weights);

}

package hr.fer.zemris.optjava.dz7.net;

/**
 * Created by zac on 12.12.16..
 */
public class FFANNEvaluator {
    private FFANN net;
    private Dataset dataset;
    private ICostFunction errorFunction;

    public FFANNEvaluator(FFANN net, Dataset dataset, ICostFunction errorFunction) {
        this.net = net;
        this.dataset = dataset;
        this.errorFunction = errorFunction;
    }

    public double evaluate(double[] weights) {
        return net.evaluate(weights, dataset, errorFunction);
    }
}

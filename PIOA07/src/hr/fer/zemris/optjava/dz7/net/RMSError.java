package hr.fer.zemris.optjava.dz7.net;

/**
 * Created by zac on 12.12.16..
 */
public class RMSError implements ICostFunction {

    @Override
    public double valueAt(Dataset dataset, double[][] outputs) {
        int size = dataset.size();
        double error = 0.;

        for (int i = 0; i < size; i++) {
            double[] y_ = dataset.getOutput(i);
            double[] y = outputs[i];
            for (int j = 0; j < y_.length; j++) {
                double diff = y[j] - y_[j];
                error += diff * diff;
            }
        }

        return error;
    }

}

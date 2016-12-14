package hr.fer.zemris.optjava.dz7.net;

import Jama.Matrix;

/**
 * Created by zac on 12.12.16..
 */
public class SigmoidTransferFunction implements ITransferFunction {

    @Override
    public Matrix valueAt(Matrix matrix) {
        double[][] vector = matrix.getArray();
        for (int i = 0; i < vector.length; i++) {
            vector[i][0] = 1. / (1. + Math.exp((-1.) * vector[i][0]));
        }
        return new Matrix(vector);
    }

}

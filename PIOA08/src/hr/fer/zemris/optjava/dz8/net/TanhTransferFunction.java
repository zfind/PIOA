package hr.fer.zemris.optjava.dz8.net;

import Jama.Matrix;

/**
 * Created by zac on 17.12.16..
 */
public class TanhTransferFunction implements ITransferFunction {

    @Override
    public Matrix valueAt(Matrix matrix) {
        double[][] vector = matrix.getArray();
        for (int i = 0; i < vector.length; i++) {
            vector[i][0] = 1. / (1. + Math.exp((-1.) * vector[i][0]));
            vector[i][0] = 2 * vector[i][0] - 1;
        }
        return new Matrix(vector);
    }

}

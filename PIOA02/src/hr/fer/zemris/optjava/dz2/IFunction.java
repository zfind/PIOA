package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by zac on 14.10.16..
 * Interface with methods which define optimization problem
 */
public interface IFunction {

    int getNumberOfVariables();

    double getValueAt(Matrix matrix);

    Matrix getGradientAt(Matrix matrix);

}

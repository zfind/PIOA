package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by zac on 14.10.16..
 * Additional method for Newthon-Raphson's method
 */
public interface IHFunction extends IFunction {

    Matrix getHessianAt(Matrix matrix);

}

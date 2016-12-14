package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by zac on 15.10.16..
 * Class represents function f1 = (x1-1)^2 + 10(x2-2)^2
 */
public class F2 implements IHFunction {

    @Override
    public Matrix getHessianAt(Matrix matrix) {
        Matrix hessian = new Matrix(2, 2);
        hessian.set(0, 0, 2);
        hessian.set(0, 1, 0);
        hessian.set(1, 0, 0);
        hessian.set(1, 1, 20);
        return hessian;
    }

    @Override
    public int getNumberOfVariables() {
        return 2;
    }

    @Override
    public double getValueAt(Matrix vector) {
        double x1 = vector.get(0, 0);
        double x2 = vector.get(1, 0);
        return (x1 - 1) * (x1 - 1) + 10 * (x2 - 2) * (x2 - 2);
    }

    @Override
    public Matrix getGradientAt(Matrix vector) {
        double x1 = vector.get(0, 0);
        double x2 = vector.get(1, 0);
        Matrix gradient = new Matrix(2, 1);
        gradient.set(0, 0, 2 * (x1 - 1));
        gradient.set(1, 0, 20 * (x2 - 2));
        return gradient;
    }
}

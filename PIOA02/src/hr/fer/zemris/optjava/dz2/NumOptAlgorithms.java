package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Created by zac on 14.10.16..
 * Class provides methods for solving optimization problems
 * Includes gradient descent and Newton-Raphson method
 */
public class NumOptAlgorithms {
    private static final double epsilon = 0.01;

    /**
     * Method for solving optimization problem using gradient descent
     * @param function function with appropriate methods
     * @param maxNumOfIterations maximum number of iterations
     * @param x starting vector
     * @return optimum vector
     */
    public static Matrix gradientDescent(IFunction function, int maxNumOfIterations, Matrix x) {
        System.out.println("grad, pocetni vektor: ");
        x.print(x.getRowDimension(), x.getColumnDimension());

        for (int k = 0; k < maxNumOfIterations; k++) {

            double error = function.getGradientAt(x).normF();
            System.out.println("k: " + k + " err: " + error);
//            System.out.println(vectorToString(x));
            if (error < epsilon) {
                break;
            }

            Matrix d = function.getGradientAt(x).times(-1);
            double lambda = bisect(function, x, d);
            x = x.plus(d.times(lambda));
        }

        return x;
    }

    /**
     * Method for solving optimization problems using Newton-Raphson method
     * @param function function which defines optimization problem
     * @param maxNumOfIterations maximum number of iterations
     * @param x starting vector
     * @return optimum vector
     */
    public static Matrix newtonMethod(IHFunction function, int maxNumOfIterations, Matrix x) {
        System.out.println("newton, pocetni vektor: ");
        x.print(x.getRowDimension(), x.getColumnDimension());

        for (int k = 0; k < maxNumOfIterations; k++) {

            double error = function.getGradientAt(x).normF();
            System.out.println("k: " + k + " err: " + error);
//            System.out.println(vectorToString(x));
            if (error < epsilon) {
                break;
            }

            Matrix d = function.getHessianAt(x).inverse().times(-1).times(function.getGradientAt(x));
            double lambda = bisect(function, x, d);
            x = x.plus(d.times(lambda));
        }

        return x;
    }

    /**
     * Method for bisection, sweeps through line defined with direction vector
     * @param function function which defines optimization problem
     * @param x starting vector
     * @param direction vector pointing direction of minimum
     * @return optimal lambda which defines how much current solution should be modified
     */
    private static double bisect(IFunction function, Matrix x, Matrix direction) {
        double lambdaLower = 1E-6;
        double lambdaUpper = 0.5;

        while (true) {
            Matrix nextX = x.plus(direction.times(lambdaUpper));
            double df = getDerivationInDirection(function, nextX, direction);
            if (df > 0.) {
                break;
            } else {
                lambdaUpper *= 2;
            }
        }

        while (true) {
            double lambda = (lambdaLower + lambdaUpper) / 2;
            Matrix nextX = x.plus(direction.times(lambda));
            double df = getDerivationInDirection(function, nextX, direction);
            if (Math.abs(df) < 1E-6 || lambdaLower >= lambda || lambdaUpper <= lambda) {
                return lambda;
            } else if (df < 0.) {
                lambdaLower = lambda;
            } else {
                lambdaUpper = lambda;
            }
        }
    }

    private static double getDerivationInDirection(IFunction function, Matrix x, Matrix direction) {
        return direction.transpose().times(function.getGradientAt(x)).get(0,0);
    }

    private static String vectorToString(Matrix x) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<x.getRowDimension(); i++) {
            stringBuilder.append(x.get(i, 0) + " ");
        }
        return stringBuilder.toString();
    }

}

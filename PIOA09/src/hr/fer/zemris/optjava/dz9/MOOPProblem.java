package hr.fer.zemris.optjava.dz9;

/**
 * Created by zac on 02.01.17..
 */
public interface MOOPProblem {

    int getNumberOfObjectives();

    double[] evaluateSolution(double[] solution);

    public int getDimension();

    double lowerBound(int i);

    double upperBound(int i);

}
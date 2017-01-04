package hr.fer.zemris.optjava.dz9;

/**
 * Created by zac on 02.01.17..
 */
public class Problem2 implements MOOPProblem {

    public final int decisionDimension;
    public final int objectivesDimension;
    public double[] xlower;
    public double[] xupper;

    public Problem2() {
        this.decisionDimension = 2;
        this.objectivesDimension = 2;
        this.xlower = new double[decisionDimension];
        this.xupper = new double[objectivesDimension];
        xlower[0] = 0.1;
        xupper[0] = 1.0;
        xlower[1] = 0.0;
        xupper[1] = 10.0;
    }

    @Override
    public int getNumberOfObjectives() {
        return objectivesDimension;
    }

    @Override
    public double[] evaluateSolution(double[] solution) {
        double[] z = new double[objectivesDimension];
        z[0] = solution[0];
        z[1] = (1 + solution[1]) / solution[0];
        return z;
    }

    @Override
    public int getDimension() {
        return decisionDimension;
    }

    @Override
    public double lowerBound(int i) {
        return xlower[i];
    }

    @Override
    public double upperBound(int i) {
        return xupper[i];
    }

}
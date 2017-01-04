package hr.fer.zemris.optjava.dz9;

/**
 * Created by zac on 02.01.17..
 */
public class Problem1 implements MOOPProblem {

    public final int decisionDimension;
    public final int objectivesDimension;
    public double[] xlower;
    public double[] xupper;

    public Problem1() {
        this.decisionDimension = 4;
        this.objectivesDimension = 4;
        this.xlower = new double[decisionDimension];
        this.xupper = new double[decisionDimension];
        for (int i=0; i<decisionDimension; i++) {
            xlower[i] = -5.;
            xupper[i] = 5.;
        }
    }

    @Override
    public int getNumberOfObjectives() {
        return objectivesDimension;
    }

    @Override
    public double[] evaluateSolution(double[] solution) {
        double[] z = new double[objectivesDimension];
        for (int i=0; i<objectivesDimension; i++) {
            z[i] = solution[i] * solution[i];
        }
        return z;
    }

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
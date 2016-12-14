package hr.fer.zemris.optjava.dz3;

/**
 * Created by zac on 24.10.16..
 */
public class GeometricTempSchedule implements ITempSchedule {
    private double alpha;
    private double tInitial;
    private double tCurrent;
    private int innerLimit;
    private int outerLimit;

    public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimmit) {
        this.alpha = alpha;
        this.tInitial = tInitial;
        this.tCurrent = tInitial;
        this.innerLimit = innerLimit;
        this.outerLimit = outerLimmit;
    }

    @Override
    public double getNextTemperature() {
        return tCurrent *= alpha;
    }

    @Override
    public int getInnerLoopCounter() {
        return innerLimit;
    }

    @Override
    public int getOuterLoopCounter() {
        return outerLimit;
    }

}

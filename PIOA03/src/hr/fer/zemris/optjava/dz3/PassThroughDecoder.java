package hr.fer.zemris.optjava.dz3;

/**
 * Created by zac on 23.10.16..
 */
public class PassThroughDecoder implements IDecoder<DoubleArraySolution> {

    public PassThroughDecoder() {
    }

    @Override
    public double[] decode(DoubleArraySolution solution) {
        return solution.values;
    }

    @Override
    public void decode(DoubleArraySolution solution, double[] decoded) {
        System.arraycopy(solution.values, 0, decoded, 0, solution.values.length);
    }

}

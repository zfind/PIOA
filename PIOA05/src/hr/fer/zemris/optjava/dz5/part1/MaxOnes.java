package hr.fer.zemris.optjava.dz5.part1;

/**
 * Created by zac on 12.11.16..
 */
public class MaxOnes implements IFunction {
    @Override
    public double valueAt(Solution solution) {
        int k = solution.getNumberOfOnes();
        int n = solution.getNumberOfBits();
        if (k <= 0.8 * n) {
            return ((double) k) / n;
        } else if (k > 0.8 * n && k < 0.9 * n) {
            return 0.8;
        } else {
            return (2. * k / n) - 1.;
        }
    }
}

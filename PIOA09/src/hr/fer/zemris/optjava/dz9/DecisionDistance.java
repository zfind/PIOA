package hr.fer.zemris.optjava.dz9;

/**
 * Created by zac on 02.01.17..
 */
public class DecisionDistance implements IDistance {

    @Override
    public double distance(Solution s1, Solution s2) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int dimension = s1.x.length;
        double sum = 0.;
        for (int i = 0; i < dimension; i++) {
            if (min < s1.x[i]) {
                min = s1.x[i];
            }
            if (min < s2.x[i]) {
                min = s2.x[i];
            }
            if (max > s1.x[i]) {
                max = s1.x[i];
            }
            if (max > s2.x[i]) {
                max = s2.x[i];
            }
            double val = (s1.x[i] - s2.x[i]) / (max - min);
            sum += val * val;
        }
        return Math.sqrt(sum);
    }


}
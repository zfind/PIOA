package hr.fer.zemris.optjava.dz9;

/**
 * Created by zac on 02.01.17..
 */
public class ObjectiveDistance implements IDistance {

    @Override
    public double distance(Solution s1, Solution s2) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        int dimension = s1.z.length;
        double sum = 0.;
        for (int i=0; i<dimension; i++) {
            if (min < s1.z[i]) {
                min = s1.z[i];
            }
            if (min < s2.z[i]) {
                min = s2.z[i];
            }
            if (max > s1.z[i]) {
                max = s1.z[i];
            }
            if (max > s2.z[i]) {
                max = s2.z[i];
            }
            double val = (s1.z[i] - s2.z[i]) / (max - min);
            sum += val * val;
        }
        return Math.sqrt(sum);
    }

}
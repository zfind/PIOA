package hr.fer.zemris.optjava.dz7.clonalg;

/**
 * Created by zac on 12.12.16..
 */
public class Solution implements Comparable<Solution> {

    public double[] weights;
    public double fitness;

    public Solution(double[] weights, double fitness) {
        this.weights = weights;
        this.fitness = fitness;
    }

    public Solution(Solution s) {
        this.weights = s.weights.clone();
        this.fitness = s.fitness;
    }

    @Override
    public int compareTo(Solution s) {
        if (this.fitness < s.fitness) {
            return -1;
        } else if (this.fitness > s.fitness) {
            return 1;
        } else {
            return 0;
        }
    }

}

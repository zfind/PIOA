package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.net.NNEvaluator;

/**
 * Created by zac on 19.12.16..
 */
public class Solution implements Comparable<Solution> {

    public final double[] weights;
    public final double fitness;

    public Solution(double[] weights, NNEvaluator evaluator) {
        this.weights = weights;
        this.fitness = evaluator.evaluate(weights);
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
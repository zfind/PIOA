package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.net.NNEvaluator;

import java.util.List;
import java.util.Random;

/**
 * Created by zac on 19.12.16..
 */
public class DEBest1 extends AbstractStrategy {

    private final double F;

    public DEBest1(double F, NNEvaluator evaluator, Random random) {
        super(evaluator, random);
        this.F = F;
    }

    @Override
    public double[] mutate(List<Solution> population, Solution bestSolution, int targetIndex) {
        Solution[] solutions = getNDistinct(2, population, targetIndex);

        double[] best = bestSolution.weights;
        double[] r1 = solutions[0].weights;
        double[] r2 = solutions[1].weights;

        double[] mutant = new double[best.length];
        for (int i = 0; i < mutant.length; i++) {
            mutant[i] = best[i] + F * (r1[i] - r2[i]);
        }

        return mutant;
    }

}
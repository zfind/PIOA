package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.net.NNEvaluator;

import java.util.List;
import java.util.Random;

/**
 * Created by zac on 19.12.16..
 */
public class DETargetToBest extends AbstractStrategy {

    private final double F0;

    public DETargetToBest(double F, NNEvaluator evaluator, Random random) {
        super(evaluator, random);
        this.F0 = F;
    }

    @Override
    public double[] mutate(List<Solution> population, Solution bestSolution, int targetIndex) {
        double F = F0 + random.nextGaussian() * 0.1;

        Solution[] solutions = getNDistinct(2, population, targetIndex);

        double[] target = population.get(targetIndex).weights;
        double[] best = bestSolution.weights;
        double[] r1 = solutions[0].weights;
        double[] r2 = solutions[1].weights;

        double[] mutant = new double[best.length];
        for (int i = 0; i < mutant.length; i++) {
            mutant[i] = target[i] + F * (best[i] - target[i]) + F * (r1[i] - r2[i]);
        }

        return mutant;
    }

}
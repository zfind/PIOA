package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.net.NNEvaluator;

import java.util.List;
import java.util.Random;

/**
 * Created by zac on 19.12.16..
 */
public class DERand1 extends AbstractStrategy {

    private final double F;

    public DERand1(double F, NNEvaluator evaluator, Random random) {
        super(evaluator, random);
        this.F = F;
    }

    @Override
    public double[] mutate(List<Solution> population, Solution bestSolution, int targetIndex) {
        Solution[] solutions = getNDistinct(3, population, targetIndex);

        double[] r0 = bestSolution.weights;
        double[] r1 = solutions[0].weights;
        double[] r2 = solutions[1].weights;

        double[] mutant = new double[r0.length];
        for (int i = 0; i < mutant.length; i++) {
            mutant[i] = r0[i] + F * (r1[i] - r2[i]);
        }

        return mutant;
    }

}
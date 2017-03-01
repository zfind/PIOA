package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.generic.ga.IntArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Created by zac on 24.01.17..
 */
public class NormalMutation implements IMutationOperator {

    private double prob;
    private double sigma;

    public NormalMutation(double prob, double sigma) {
        this.prob = prob;
        this.sigma = sigma;
    }

    @Override
    public void mutate(IntArraySolution child) {
        IRNG random = RNG.getRNG();

        int dimension = child.data.length;
        int numberOfMutations = random.nextInt(0, (int) (dimension * prob));

        for (int i = 0; i < numberOfMutations; i++) {
            int position = random.nextInt(0, dimension);
            child.data[position] += Math.round(random.nextGaussian() * sigma);
        }
    }

}

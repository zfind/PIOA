package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.generic.ga.IntArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Created by zac on 24.01.17..
 */
public class UniformCrossOperator implements ICrossOperator {

    @Override
    public IntArraySolution cross(IntArraySolution p1, IntArraySolution p2) {
        IRNG random = RNG.getRNG();

        IntArraySolution child = (IntArraySolution) p1.duplicate();

        int dimension = p1.data.length;
        int numberOfCrossings = random.nextInt(0, dimension);

        for (int i = 0; i < numberOfCrossings; i++) {
            int position = random.nextInt(0, dimension);
            child.data[position] = p2.data[position];
        }

        return child;
    }

}

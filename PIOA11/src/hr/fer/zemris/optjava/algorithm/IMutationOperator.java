package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.generic.ga.IntArraySolution;

/**
 * Created by zac on 24.01.17..
 */
public interface IMutationOperator {

    void mutate(IntArraySolution child);

}

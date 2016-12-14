package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by zac on 24.10.16..
 */
public class DoubleArrayNormNeighborhood implements INeighborhood<DoubleArraySolution> {
    private double[] deltas;
    private Random rand;

    public DoubleArrayNormNeighborhood(double[] deltas) {
        this.deltas = Arrays.copyOf(deltas, deltas.length);
        this.rand = new Random();
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        DoubleArraySolution neighbor = solution.duplicate();
        for (int i = 0; i < neighbor.values.length; i++) {
            neighbor.values[i] += rand.nextGaussian() * deltas[i];
        }
        return neighbor;
    }
}

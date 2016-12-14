package hr.fer.zemris.optjava.dz3;

import java.util.Random;

/**
 * Created by zac on 24.10.16..
 */
public class BitVectorNeihgborhood implements INeighborhood<BitVectorSolution> {
    private final int totalBits;
    private final int n;
    private final Random rand;

    public BitVectorNeihgborhood(int totalBits, int n) {
        this.totalBits = totalBits;
        this.n = n;
        rand = new Random();
    }

    @Override
    public BitVectorSolution randomNeighbor(BitVectorSolution solution) {
        BitVectorSolution candidate = solution.duplicate();
        for (int i = 0; i < totalBits / n; i++) {
            int position = i * n + rand.nextInt(n);
            byte bitAtPosition = candidate.bits[position];
            if (bitAtPosition != 1) {
                candidate.bits[position] = 1;
            } else {
                candidate.bits[position] = 0;
            }
        }
        return candidate;
    }
}

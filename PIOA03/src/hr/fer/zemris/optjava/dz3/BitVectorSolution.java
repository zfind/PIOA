package hr.fer.zemris.optjava.dz3;

import java.util.Random;

/**
 * Created by zac on 23.10.16..
 */
public class BitVectorSolution extends SingleObjectiveSolution {
    public byte[] bits;

    public BitVectorSolution(int length) {
        bits = new byte[length];
    }

    public BitVectorSolution newLikeThis() {
        return new BitVectorSolution(bits.length);
    }

    public BitVectorSolution duplicate() {
        BitVectorSolution bitVectorSolution = newLikeThis();
        System.arraycopy(bits, 0, bitVectorSolution.bits, 0, bits.length);
        return bitVectorSolution;
    }

    public void randomize(Random random) {
        random.nextBytes(bits);
    }
}

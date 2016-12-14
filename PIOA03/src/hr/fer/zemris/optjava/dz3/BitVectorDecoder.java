package hr.fer.zemris.optjava.dz3;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by zac on 23.10.16..
 */
public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {
    protected double[] mins;    // lower limits
    protected double[] maxs;    // upper limits
    protected int[] bits;       // bits representing one solution
    protected int n;            // number of variables in optimization
    protected int totalBits;    // total size of solution
    protected int bitsPerVariable;

    protected BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        this.mins = Arrays.copyOf(mins, mins.length);
        this.maxs = Arrays.copyOf(maxs, maxs.length);
        this.bits = Arrays.copyOf(bits, bits.length);
        this.n = n;
        this.totalBits = bits.length;
        this.bitsPerVariable = totalBits / n;
    }

    protected BitVectorDecoder(double min, double max, int totalBits, int n) {
        this.mins = new double[totalBits];
        this.maxs = new double[totalBits];
        this.bits = new int[totalBits];
        Arrays.fill(this.mins, min);
        Arrays.fill(this.maxs, max);
        Arrays.fill(this.bits, totalBits);
        this.n = n;
        this.totalBits = totalBits;
        this.bitsPerVariable = totalBits / n;
    }

    @Override
    public void decode(BitVectorSolution solution, double[] decoded) {
        double[] tmp = decode(solution);
        if (tmp.length != decoded.length) {
            System.err.println("Error. Decoder: result array too small");
            System.exit(-1);
        }
        System.arraycopy(tmp, 0, decoded, 0, tmp.length);
    }

    protected double interpolation(long decimal, int ith) {
        return mins[ith]
                + (decimal / (Math.pow(2, (bitsPerVariable)) - 1))
                * (maxs[ith] - mins[ith]);
    }

    protected int[] getIthVarBits(BitVectorSolution solution, int ith) {
        int begin = bitsPerVariable * ith;
        int end = bitsPerVariable * (ith + 1);
        int[] bytes = new int[end - begin];
        for (int i = 0, j = begin; j < end; i++, j++) {
            bytes[i] = solution.bits[j];
        }
        return bytes;
    }

    protected long bitsToDecimal(int[] bits) {
        long decimal = 0;
        for (int i = bits.length - 1, exponent = 0; i >= 0; i--, exponent++) {
            if (bits[i] != 0) {
                decimal += 1 << exponent;
            }
        }
        return decimal;
    }

    public int getTotalBits() {
        return totalBits;
    }

    public int getDimensions() {
        return n;
    }

    public static void main(String[] args) {
        byte[] bits = {0x00, 0x01, 0x00, 0x01};
        ByteBuffer wrapped = ByteBuffer.wrap(bits);
        System.out.println();
    }
}

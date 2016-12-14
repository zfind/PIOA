package hr.fer.zemris.optjava.dz3;

/**
 * Created by zac on 24.10.16..
 */
public class NaturalBinaryDecoder extends BitVectorDecoder {

    public NaturalBinaryDecoder(double min, double max, int totalBits, int n) {
        super(min, max, totalBits, n);
    }

    public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public double[] decode(BitVectorSolution solution) {
        double[] doubles = new double[n];
        for (int i = 0; i < n; i++) {
            int[] ithVarBits = getIthVarBits(solution, i);
            long decimal = bitsToDecimal(ithVarBits);
            doubles[i] = interpolation(decimal, i);
        }
        return doubles;
    }

}

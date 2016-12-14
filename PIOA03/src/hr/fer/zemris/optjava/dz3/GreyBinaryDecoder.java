package hr.fer.zemris.optjava.dz3;

/**
 * Created by zac on 23.10.16..
 */
public class GreyBinaryDecoder extends BitVectorDecoder {

    public GreyBinaryDecoder(double min, double max, int totalBits, int n) {
        super(min, max, totalBits, n);
    }

    public GreyBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public double[] decode(BitVectorSolution solution) {
        double[] doubles = new double[n];
        for (int i = 0; i < n; i++) {
            int[] ithVarBits = getIthVarBits(solution, i);
            int[] greyToBits = greyToBits(ithVarBits);
            long decimal = bitsToDecimal(greyToBits);
            doubles[i] = interpolation(decimal, i);
        }
        return doubles;
    }

    private int[] greyToBits(int[] grey) {
        int[] bits = new int[grey.length];
        for (int i = 0; i < grey.length; i++) {
            if (i == 0) {
                bits[0] = grey[0];
            } else {
                if (bits[i - 1] != grey[i]) {
                    bits[i] = 1;
                } else {
                    bits[i] = 0;
                }
            }
        }
        return bits;
    }

}

package hr.fer.zemris.trisat;

/**
 * Created by zac on 10.10.16..
 * Algorithm checks all possible combinations to solve satisfiability
 */
public class Algorithm1 implements Algorithm {

    @Override
    public void solve(SATFormula formula) {
        int m = formula.getNumberOfVariables();
        int n = (int) Math.pow(2, m);

        for (int i = 0; i < n; i++) {
            String tmp = Integer.toBinaryString(i);
            int offset = m - tmp.length();
            boolean[] bits = new boolean[m];
            for (int j = offset; j < m; j++) {
                if (tmp.charAt(j - offset) == '1') {
                    bits[j] = true;
                }
            }

            BitVector vector = new BitVector(bits);
//            System.out.println(vector.toString());
            if (formula.isSatisfied(vector)) {
                System.out.println(vector.toString());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(String.format("%16s", Integer.toBinaryString(144)).replace(' ', '0'));
        System.out.println(String.format("%0" + 10 + "d", 0) + Integer.toBinaryString(144));
    }

}

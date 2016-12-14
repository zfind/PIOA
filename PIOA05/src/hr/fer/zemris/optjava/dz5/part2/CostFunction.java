package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zac on 14.11.16..
 */
public class CostFunction implements IFunction {
    private final int n;
    private final double[][] distanceLookup;
    private final double[][] costLookup;

    public CostFunction(double[][] distanceLookup, double[][] costLookup) {
        this.distanceLookup = distanceLookup;
        this.costLookup = costLookup;
        this.n = distanceLookup.length;
    }

    public CostFunction(String path) {
        double[][][] result = Parser.parse(path);
        this.distanceLookup = result[0];
        this.costLookup = result[1];
        this.n = distanceLookup.length;
    }

    @Override
    public double valueAt(Solution solution) {
        double fitness = 0.;
        for (int i = 0; i < n; i++) {
            int ithL = solution.get(i);
            for (int j = 0; j < n; j++) {
                int jthL = solution.get(j);
                fitness += costLookup[i][j] * distanceLookup[ithL][jthL];
            }
        }
        return fitness;
    }


    public static void main(String[] args) {
        double[][][] result = Parser.parse("data/nug12.dat");
        double[][] distanceLookup = result[0];
        double[][] costLookup = result[1];
        int N = distanceLookup.length;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(distanceLookup[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(costLookup[i][j] + " ");
            }
            System.out.println();
        }

        Integer[] list = new Integer[]{1, 0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        List<Integer> locations = new ArrayList<>(Arrays.asList(list));
        IFunction function = new CostFunction(distanceLookup, costLookup);
        System.out.println(function.valueAt(new Solution(locations, function)));
    }

}

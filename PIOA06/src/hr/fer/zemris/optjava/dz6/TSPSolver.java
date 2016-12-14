package hr.fer.zemris.optjava.dz6;

import java.util.Random;

/**
 * Created by zac on 03.12.16..
 */
public class TSPSolver {
    private static final double ALPHA = 2.;
    private static final double BETA = 3.;
    private static final double RO = 0.02;


    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Greska. Krivi argumenti");
            System.out.println("Npr. data/ch150.tsp 8 30 20000");
            System.exit(-1);
        }

        String file = args[0];
        int k = Integer.parseInt(args[1]);
        int l = Integer.parseInt(args[2]);
        int maxIter = Integer.parseInt(args[3]);

        Random random = new Random();

        double[][] distances = Parser.parse(file);
        int n = distances.length;

        double[][] heuristics = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                heuristics[i][j] = Math.pow(1. / distances[i][j], BETA);
                heuristics[j][i] = heuristics[i][j];
            }
        }

        MaxMinAntSystem algorithm = new MaxMinAntSystem(n, k, l, maxIter, random, distances, heuristics, ALPHA, RO);

        Solution best = algorithm.run();

        System.out.println(best);
        System.out.println(best.getTourLength());

    }
}

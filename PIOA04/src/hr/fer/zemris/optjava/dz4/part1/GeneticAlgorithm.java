package hr.fer.zemris.optjava.dz4.part1;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by zac on 02.11.16..
 */
public class GeneticAlgorithm {
    private static final int NUMBER_OF_PARAMETERS = 6;

    private static final double LOWER_LIMIT = -5.0;
    private static final double UPPER_LIMIT = 5.0;
    private static final double ALPHA = 0.5;
    private static int POPULATION_SIZE;
    private static int MAX_LOOP_ITERATIONS;
    private static double EPSILON;
    private static int SELECTION; // 0-roulette; 1-tournament
    private static int TOURNAMENT_N;
    private static double SIGMA;

    public static void main(String[] args) {
        if (!(args.length == 6)) {
            System.out.println("Error. Krivi argumenti");
            System.out.println("potrebno: datoteka vel_populacije max_pogreska max_iteracija selekcija param_mutacije_sigma");
            System.out.println("primjer: zad-prijenosna.txt 20 0.0001 200000 tournament:4 1");
            System.exit(-1);
        }

        String path = args[0];
        IFunction function = new Zadatak4(path);
//        function.print();

        POPULATION_SIZE = Integer.parseInt(args[1]);
        EPSILON = Double.parseDouble(args[2]);
        MAX_LOOP_ITERATIONS = Integer.parseInt(args[3]);
        if (args[4].equals("rouletteWheel")) {
            SELECTION = 0;
        } else if (args[4].contains("tournament:")) {
            SELECTION = 1;
            String[] tmp = args[4].split(":");
            TOURNAMENT_N = Integer.parseInt(tmp[1]);
        }
        SIGMA = Double.parseDouble(args[5]);

        Random random = new Random();
        double[] lowerLimits = new double[NUMBER_OF_PARAMETERS];
        double[] upperLimits = new double[NUMBER_OF_PARAMETERS];
        Arrays.fill(lowerLimits, LOWER_LIMIT);
        Arrays.fill(upperLimits, UPPER_LIMIT);

        Population population = new Population(POPULATION_SIZE, NUMBER_OF_PARAMETERS, random, lowerLimits, upperLimits, function);

        ISelection selectionOperator;
        if (SELECTION == 0) {
            selectionOperator = new RouletteWheelSelection(random);
        } else {
            selectionOperator = new TournamentSelection(TOURNAMENT_N, random);
        }
        CrossingOperator crossingOperator = new CrossingOperator(random, ALPHA);
        MutationOperator mutationOperator = new MutationOperator(random, SIGMA, MAX_LOOP_ITERATIONS, POPULATION_SIZE);

        run(population, function, selectionOperator, crossingOperator, mutationOperator);

    }

    public static void run(Population population, IFunction function, ISelection selectionOp, CrossingOperator crossingOp, MutationOperator mutationOp) {

        for (int iter = 0; iter < MAX_LOOP_ITERATIONS; iter++) {
            if (Math.abs(population.getBestFitness()) <= EPSILON) {
                System.out.println("Rjesenje: " + population.getBestFitness() + "\n" + population.getBestSolution().toString());
                return;
            }
            System.out.println(iter + " " + population.getBestFitness() + "\t" + population.getBestSolution().toString());

            Solution best = population.getBestSolution();
            Solution[] next = new Solution[population.size()];
            next[0] = best;
            for (int i = 1; i < population.size(); i++) {
                Solution first = selectionOp.select(population);
                Solution second = selectionOp.select(population);
                Solution child = crossingOp.cross(first, second);
                mutationOp.mutate(child, iter);
                next[i] = child;
            }

            population = new Population(next, function);
        }
    }
}

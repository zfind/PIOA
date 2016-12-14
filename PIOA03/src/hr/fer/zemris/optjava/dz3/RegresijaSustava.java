package hr.fer.zemris.optjava.dz3;

import Jama.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 14.10.16..
 * Class defines system's transfer function as problem of
 * optimization coefficients a, b, c, d, e, f
 */
public class RegresijaSustava implements IFunction {
    private static final int NUMBER_OF_VARIABLES_X = 5;
    private static final int NUMBER_OF_PARAMETERS = 6;
    private static int numberOfPointsInSample;

    private Matrix matrixX;
    private Matrix matrixY;

    private static final double LOWER_LIMIT = -10.0;
    private static final double UPPER_LIMIT = 10.0;
    private static final double DELTA = 1.0;
    private static final double INITIAL_TEMPERATURE = 1000;
    private static final double FINAL_TEMPERATURE = 0.001;
    private static final int OUTER_LOOP_ITERATIONS = 3000;
    private static final int INNER_LOOP_ITERATIONS = 2000;

    public RegresijaSustava(String path) {
        Matrix[] matrices = Parser.parse(path);
        this.matrixX = matrices[0];
        this.matrixY = matrices[1];
    }

    public static void main(String[] args) {

        if (!(args.length == 2)) {
            System.out.println("Error. Krivi argumenti");
            System.exit(-1);
        }

        String path = args[0];
        String method = args[1];
        Random random = new Random();

        IFunction function = new RegresijaSustava(path);

        double alpha = Math.pow(FINAL_TEMPERATURE / INITIAL_TEMPERATURE,
                1.0 / (OUTER_LOOP_ITERATIONS - 1.0));
        ITempSchedule schedule = new GeometricTempSchedule(alpha, INITIAL_TEMPERATURE,
                INNER_LOOP_ITERATIONS, OUTER_LOOP_ITERATIONS);

        if (method.contains("decimal")) {
            double[] lower = new double[NUMBER_OF_PARAMETERS];
            double[] upper = new double[NUMBER_OF_PARAMETERS];
            Arrays.fill(lower, LOWER_LIMIT);
            Arrays.fill(upper, UPPER_LIMIT);

            DoubleArraySolution solution = new DoubleArraySolution(NUMBER_OF_PARAMETERS);
            solution.randomize(random, lower, upper);

            IDecoder decoder = new PassThroughDecoder();

            double deltas[] = new double[NUMBER_OF_PARAMETERS];
            Arrays.fill(deltas, DELTA);
            INeighborhood neighborhood = new DoubleArrayNormNeighborhood(deltas);

            @SuppressWarnings("unchecked")
			SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(
                    decoder,
                    neighborhood,
                    solution,
                    function,
                    schedule,
                    true
            );

            simulatedAnnealing.run();

            System.out.printf("Solution:    %s    error: %f\n", decoder.toString(simulatedAnnealing.getBest()), Math.sqrt(simulatedAnnealing.getBest().value));

        } else if (method.contains("binary:")) {
            String[] tmp = method.split(":");
            if (tmp.length != 2) {
                System.err.println("Error. Wrong argument, usage: binary:precision");
                System.exit(-1);
            }
            int precision = Integer.parseInt(tmp[1]);

            int totalBits = NUMBER_OF_PARAMETERS * precision;

            int[] bits = new int[totalBits];
            double[] mins = new double[totalBits];
            double[] maxs = new double[totalBits];
            Arrays.fill(mins, LOWER_LIMIT);
            Arrays.fill(maxs, UPPER_LIMIT);

            BitVectorSolution startWith = new BitVectorSolution(totalBits);
            startWith.randomize(random);

            IDecoder<BitVectorSolution> decoder = new GreyBinaryDecoder(mins, maxs, bits, NUMBER_OF_PARAMETERS);

            INeighborhood<BitVectorSolution> neighborhood = new BitVectorNeihgborhood(totalBits, precision);

            SimulatedAnnealing<BitVectorSolution> simulatedAnnealing = new SimulatedAnnealing<>(
                    decoder,
                    neighborhood,
                    startWith,
                    function,
                    schedule,
                    true
            );

            simulatedAnnealing.run();

            System.out.printf("Solution:    %s    error: %f\n", decoder.toString(simulatedAnnealing.getBest()), Math.sqrt(simulatedAnnealing.getBest().value));

        }
    }


    public int getNumberOfVariables() {
        return NUMBER_OF_PARAMETERS;
    }

    @Override
    public double valueAt(double[] vector) {
        double[][] tmp = new double[vector.length][1];
        for (int i = 0; i < vector.length; i++) {
            tmp[i][0] = vector[i];
        }
        Matrix matrix = new Matrix(tmp);

        double sum = 0.;

        for (int i = 0; i < numberOfPointsInSample; i++) {
            sum += getValueForSample(matrix, i, -1);
        }

        return sum;
    }

    /**
     * Helper method
     *
     * @param matrix    point (x1, x2, x3, x4, x5)
     * @param sample    index of ith row in samples
     * @param dimension
     * @return if dimension -1: function value; else value of gradient at some variable (a, ..., f)
     */
    private double getValueForSample(Matrix matrix, int sample, int dimension) {
        double a = matrix.get(0, 0);
        double b = matrix.get(1, 0);
        double c = matrix.get(2, 0);
        double d = matrix.get(3, 0);
        double e = matrix.get(4, 0);
        double f = matrix.get(5, 0);

        double x1 = matrixX.get(sample, 0);
        double x2 = matrixX.get(sample, 1);
        double x3 = matrixX.get(sample, 2);
        double x4 = matrixX.get(sample, 3);
        double x5 = matrixX.get(sample, 4);

        double error = a * x1
                + b * x1 * x1 * x1 * x2
                + c * Math.exp(d * x3) * (1 + Math.cos(e * x4))
                + f * x4 * x5 * x5
                - matrixY.get(sample, 0);

        double result = 0.;
        if (dimension == -1) {
            result = Math.pow(error, 2);
        } else {
            double df = 2 * error;
            if (dimension == 0) {
                result = df * x1;
            } else if (dimension == 1) {
                result = df * Math.pow(x1, 3) * x2;
            } else if (dimension == 2) {
                result = df * Math.exp(d * x3) * (1 + Math.cos(e * x4));
            } else if (dimension == 3) {
                result = df * c * x3 * Math.exp(d * x3) * (1 + Math.cos(e * x4));
            } else if (dimension == 4) {
                result = df * -1. * c * Math.exp(d * x3) * Math.sin(e * x4) * x4;
            } else if (dimension == 5) {
                result = df * x4 * x5 * x5;
            } else {
                System.out.println("Greska!!");
                System.exit(-1);
            }
        }
        return result;
    }


    /**
     * Helper class for parsing file
     */
    private static class Parser {
        static Matrix[] parse(String path) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String line;
            List<String[]> lines = new ArrayList<>();
            try {
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    //System.out.println(line);
                    if (line.startsWith("#")) {
                        continue; //comment
                    } else if (line.startsWith("[")) {
                        line = line.substring(1, line.length() - 1);
                        String[] xx = line.split(",( )+");
                        lines.add(xx);
                    } else {
                        System.out.println("Unknown content. " + line);
                    }
                }

                reader.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


            numberOfPointsInSample = lines.size();


            double[][] matA = new double[numberOfPointsInSample][NUMBER_OF_VARIABLES_X];
            double[][] vecb = new double[numberOfPointsInSample][1];
            int currentRow = 0;

            for (String xx[] : lines) {
                for (int i = 0; i < NUMBER_OF_VARIABLES_X; i++) {
                    matA[currentRow][i] = Double.parseDouble(xx[i]);
                }
                vecb[currentRow][0] = Double.parseDouble(xx[NUMBER_OF_VARIABLES_X]);
                currentRow++;
            }

            return new Matrix[]{new Matrix(matA), new Matrix(vecb)};
        }
    }

}
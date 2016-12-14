package hr.fer.zemris.optjava.dz4.part1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zac on 14.10.16..
 * Class defines system's transfer function as problem of
 * optimization coefficients a, b, c, d, e, f
 */
public class Zadatak4 implements IFunction {
    private static final int NUMBER_OF_VARIABLES_X = 5;
    private static final int NUMBER_OF_PARAMETERS = 6;
    private static int numberOfPointsInSample;
    private final double[][] matrixX;
    private final double[][] matrixY;

    public Zadatak4(String path) {
        double[][][] matrices = Parser.parse(path);
        this.matrixX = matrices[0];
        this.matrixY = matrices[1];
    }

    public static void main(String[] args) {

        if (!(args.length == 1)) {
            System.out.println("Error. Krivi argumenti");
            System.exit(-1);
        }

        String path = args[0];
        Zadatak4 function = new Zadatak4(path);
        function.print();

    }

    private void print() {
        for (int i = 0; i < matrixX.length; i++) {
            for (int j = 0; j < matrixX[0].length; j++) {
                System.out.print(matrixX[i][j] + " ");
            }
            System.out.print("\n");
        }
        for (int i = 0; i < matrixY.length; i++) {
            System.out.println(matrixY[i][0]);
        }
    }

    public int getNumberOfVariables() {
        return NUMBER_OF_PARAMETERS;
    }

    @Override
    public double valueAt(double[] vector) {
        double[][] matrix = new double[vector.length][1];
        for (int i = 0; i < vector.length; i++) {
            matrix[i][0] = vector[i];
        }

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
    private double getValueForSample(double[][] matrix, int sample, int dimension) {
        double a = matrix[0][0];
        double b = matrix[1][0];
        double c = matrix[2][0];
        double d = matrix[3][0];
        double e = matrix[4][0];
        double f = matrix[5][0];

        double x1 = matrixX[sample][0];
        double x2 = matrixX[sample][1];
        double x3 = matrixX[sample][2];
        double x4 = matrixX[sample][3];
        double x5 = matrixX[sample][4];

        double error = a * x1
                + b * x1 * x1 * x1 * x2
                + c * Math.exp(d * x3) * (1 + Math.cos(e * x4))
                + f * x4 * x5 * x5
                - matrixY[sample][0];

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
        static double[][][] parse(String path) {
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

            return new double[][][]{matA, vecb};
        }
    }

}
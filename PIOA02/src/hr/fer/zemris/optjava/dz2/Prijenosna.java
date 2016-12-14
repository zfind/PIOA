package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zac on 14.10.16..
 * Class defines system's transfer function as problem of
 * optimization coefficients a, b, c, d, e, f
 */
public class Prijenosna implements IHFunction {
    private static final int numberOfVariablesX = 5;
    private static final int numberOfParameters = 6;
    private static int numberOfPointsInSample;

    private Matrix matrixX;
    private Matrix matrixY;

    public Prijenosna(String path) {
        Matrix[] matrices = Parser.parse(path);
        this.matrixX = matrices[0];
        this.matrixY = matrices[1];
    }

    public static void main(String[] args) {
    /*    String path = "/home/zac/Projekti/IdeaProjects/PIOA/PIOA2/zad-prijenosna.txt";
        Matrix[] matrix = Parser.parse(path);
        Matrix mat = matrix[0];
        mat.print(mat.getRowDimension(), mat.getColumnDimension());*/

        if (!(args.length == 3)) {
            System.out.println("Error. Krivi argumenti");
            System.exit(-1);
        }

        int maxIter = Integer.parseInt(args[1]);

        String task = args[0];
        String path = args[2];
        IHFunction function = new Prijenosna(path);

        Matrix x = Matrix.random(numberOfParameters, 1);
//        Matrix x = new Matrix(new double[numberOfParameters][1]); // nul-vektor
//        Matrix x = new Matrix(new double[][]{{7}, {-3}, {2}, {1}, {3}, {3}}); // rjesenje
//        Matrix x = new Matrix(new double[][]{{0.5}, {.9}, {.6}, {.1}, {.9}, {.3}}); // najbolje, debug

        if (task.equals("grad")) {
            x = NumOptAlgorithms.gradientDescent(function, maxIter, x);
        } else if (task.equals("newton")) {
            x = NumOptAlgorithms.newtonMethod(function, maxIter, x);
        } else {
            System.out.println("Error. Nepoznata metoda");
        }

        System.out.println("Rjesenje: ");
        x.print(x.getRowDimension(), x.getColumnDimension());
    }

    /**
     * Method for calculating Hessian for defined function
     * @param matrix point
     * @return Hessian at point
     */
    @Override
    public Matrix getHessianAt(Matrix matrix) {
        Matrix hessian = new Matrix(new double[numberOfParameters][numberOfParameters]);
        double a = matrix.get(0, 0);
        double b = matrix.get(1, 0);
        double c = matrix.get(2, 0);
        double d = matrix.get(3, 0);
        double e = matrix.get(4, 0);
        double f = matrix.get(5, 0);

        for (int i = 0; i < numberOfPointsInSample; i++) {
            double x1 = matrixX.get(i, 0);
            double x2 = matrixX.get(i, 1);
            double x3 = matrixX.get(i, 2);
            double x4 = matrixX.get(i, 3);
            double x5 = matrixX.get(i, 4);
            double y = matrixY.get(i, 0);

            double dada = 2 * x1 * x1;
            double dadb = 2 * Math.pow(x1, 4) * x2;
            double xx1 = 2 * Math.exp(d * x3) * (Math.cos(e * x4) + 1);
            double dadc = xx1 * x1;
            double dadd = xx1 * c * x1 * x3;
            double xx2 = -2 * c * Math.exp(d * x3) * Math.sin(e * x4) * x4;
            double dade = xx2 * x1;
            double dadf = 2 * x1 * x4 * x5 * x5;

            double dbdb = 2 * Math.pow(x1, 6) * x2 * x2;
            double dbdc = xx1 * Math.pow(x1, 3) * x2;
            double dbdd = xx1 * c * Math.pow(x1, 3) * x2 * x3;
            double dbde = xx2 * Math.pow(x1, 3) * x2;
            double dbdf = 2 * Math.pow(x1, 3) * x2 * x4 * Math.pow(x5, 2);

            double dcdc = 2 * Math.exp(2 * d * x3) * Math.pow((Math.cos(e * x4) + 1), 2);
            double xx3 = (b * x2 * Math.pow(x1, 3) + a * x1 + f * x4 * Math.pow(x5, 2) + c * xx1 / 2. - y);
            double dcdd = dcdc * c * x3 + xx1 * x3 * xx3;
            double dcde = -dcdc * Math.sin(e * x4) * x4 * c + xx2 / c * xx3;
            double dcdf = xx1 * x4 * Math.pow(x5, 2);

            double dddd = Math.pow(dcdc, 2) * Math.pow(x3, 2) * c * c + xx1 * c * xx3 * Math.pow(x3, 2);
            double ddde = -dcdc * Math.sin(e * x4) * x3 * x4 * Math.pow(c, 2) + xx2 / c * x3 * xx3 * c;
            double dddf = xx1 * c * x3 * x4 * Math.pow(x5, 2);

            double dede = 2 * Math.exp(2 * d * x3) * Math.pow(c * Math.sin(e * x4) * x4, 2) - 2 * c * Math.exp(d * x3) * Math.cos(e * x4) * Math.pow(x4, 2) * xx3;
            double dedf = xx2 * x4 * Math.pow(x5, 2);

            double dfdf = 2 * Math.pow(x4, 2) * Math.pow(x5, 4);

            double[][] hesse = new double[][]{
                    {dada, dadb, dadc, dadd, dade, dadf},
                    {dadb, dbdb, dbdc, dbdd, dbde, dbdf},
                    {dadc, dbdc, dcdc, dcdd, dcde, dcdf},
                    {dadd, dbdd, dcdd, dddd, ddde, dddf},
                    {dade, dbde, dcde, ddde, dede, dedf},
                    {dadf, dbdf, dcdf, dddf, dedf, dfdf}
            };

            hessian.plusEquals(new Matrix(hesse));
        }
        return hessian;
    }

    @Override
    public int getNumberOfVariables() {
        return numberOfParameters;
    }

    @Override
    public double getValueAt(Matrix matrix) {
        double sum = 0.;

        for (int i = 0; i < numberOfPointsInSample; i++) {
            sum += getValueForSample(matrix, i, -1);
        }

        return sum;
    }

    /**
     * Helper method
     * @param matrix point (x1, x2, x3, x4, x5)
     * @param sample index of ith row in samples
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
     * Gradient at point
     * @param matrix point
     * @return value of gradient through all samples
     */
    @Override
    public Matrix getGradientAt(Matrix matrix) {
        Matrix gradient = new Matrix(numberOfParameters, 1);

        for (int d = 0; d < numberOfParameters; d++) {
            double xx = 0.;
            for (int i = 0; i < numberOfPointsInSample; i++) {
                xx += getValueForSample(matrix, i, d);
            }
            gradient.set(d, 0, xx);
        }

        return gradient;
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


            double[][] matA = new double[numberOfPointsInSample][numberOfVariablesX];
            double[][] vecb = new double[numberOfPointsInSample][1];
            int currentRow = 0;

            for (String xx[] : lines) {
                for (int i = 0; i < numberOfVariablesX; i++) {
                    matA[currentRow][i] = Double.parseDouble(xx[i]);
                }
                vecb[currentRow][0] = Double.parseDouble(xx[numberOfVariablesX]);
                currentRow++;
            }

            return new Matrix[]{new Matrix(matA), new Matrix(vecb)};
        }
    }

}

package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.io.*;

/**
 * Created by zac on 14.10.16..
 * Class which solves linear system problem as optimization problem
 * Appropriate loss functions are defined in methods proposed by IHFunction interface
 */
public class Sustav implements IHFunction {
    private static final int NUM_OF_VARIABLES = 10;

    private Matrix matrixA;
    private Matrix vectorB;

    public Sustav(String path) {
        Matrix[] matrices = Parser.parse(path);
        this.matrixA = matrices[0];
        this.vectorB = matrices[1];
    }

    public static void main(String[] args) {
    /*    String path = "/home/zac/Projekti/IdeaProjects/PIOA/PIOA2/zad-sustav.txt";
        Matrix[] matrix = Parser.parse(path);
        Matrix mat = matrix[1];
        mat.print(mat.getRowDimension(), mat.getColumnDimension());
    */
        if (!(args.length == 3)) {
            System.out.println("Error. Krivi argumenti");
            System.exit(-1);
        }

        int maxIter = Integer.parseInt(args[1]);

        String task = args[0];
        String path = args[2];
        IHFunction function = new Sustav(path);

        Matrix x = Matrix.random(NUM_OF_VARIABLES, 1);
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
     * 2 * A.T * A
     */
    @Override
    public Matrix getHessianAt(Matrix matrix) {
        return matrixA.transpose().times(matrixA).times(2);
    }

    @Override
    public int getNumberOfVariables() {
        return matrixA.getRowDimension();
    }

    /**
     * A * x
     */
    @Override
    public double getValueAt(Matrix vector) {
        Matrix xx = matrixA.times(vector);
        return xx.times(xx).get(0, 0);
    }

    /**
     * 2 * A.T * (Ax - b)
     */
    @Override
    public Matrix getGradientAt(Matrix vector) {
        Matrix xx = matrixA.times(vector).minus(vectorB);
        return matrixA.transpose().times(xx).times(2);
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
            double[][] matA = new double[NUM_OF_VARIABLES][NUM_OF_VARIABLES];
            double[][] vecb = new double[NUM_OF_VARIABLES][1];
            int currentRow = 0;
            try {
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    //System.out.println(line);
                    if (line.startsWith("#")) {
                        continue; //comment
                    } else if (line.startsWith("[")) {
                        line = line.substring(1, line.length() - 1);
                        String[] xx = line.split(",( )+");
                        for (int i = 0; i < NUM_OF_VARIABLES; i++) {
                            matA[currentRow][i] = Double.parseDouble(xx[i]);
                        }
                        vecb[currentRow][0] = Double.parseDouble(xx[NUM_OF_VARIABLES]);
                        currentRow++;
                    } else {
                        System.out.println("Unknown content. " + line);
                    }
                }

                reader.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

            return new Matrix[]{new Matrix(matA), new Matrix(vecb)};
        }
    }
}

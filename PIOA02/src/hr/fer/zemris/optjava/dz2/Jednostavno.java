package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

import java.util.Random;

/**
 * Created by zac on 14.10.16..
 */
public class Jednostavno {

    public static void main(String[] args) {
        if (!(args.length == 2 || args.length == 4)) {
            System.out.println("Error. Krivi argumenti");
            System.exit(-1);
        }

        int maxIter = Integer.parseInt(args[1]);
        Matrix x;
        if (args.length == 4) {
            double x1 = Double.parseDouble(args[2]);
            double x2 = Double.parseDouble(args[3]);
            double[][] vals = {{x1}, {x2}};
            x = new Matrix(vals);
        } else {
            Random random = new Random();
            double x1 = -5 + (5 - -5) * random.nextDouble();
            double x2 = -5 + (5 - -5) * random.nextDouble();
            x = new Matrix(new double[][]{{x1}, {x2}});
        }

        String task = args[0];
        IHFunction function = null;
        if (task.length() != 2) {
            System.out.println("Error. Nepoznata funkcija");
            System.exit(-1);
        } else if (task.charAt(0) == '1') {
            function = new F1();
        } else if (task.charAt(0) == '2') {
            function = new F2();
        }

        if (task.charAt(1) == 'a') {
            x = NumOptAlgorithms.gradientDescent(function, maxIter, x);
        } else if (task.charAt(1) == 'b') {
            x = NumOptAlgorithms.newtonMethod(function, maxIter, x);
        } else {
            System.out.println("Error. Nepoznata metoda");
            System.exit(-1);
        }

        System.out.println("Rjesenje: ");
        x.print(x.getRowDimension(), x.getColumnDimension());
    }
}

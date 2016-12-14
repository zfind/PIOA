package hr.fer.zemris.trisat;

/**
 * Created by zac on 11.10.16..
 * Main class
 */
public class TriSatSolver {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error. Wrong arguments, expected [algNumber][filePath]");
            System.exit(-1);
        }

        int alg = Integer.parseInt(args[0]);
        String filePath = args[1];
        SATFormula formula = Parser.parseFormula(filePath);

        Algorithm algorithm = null;

        if (alg == 1) {
            algorithm = new Algorithm1();
        } else if (alg == 2) {
            algorithm = new Algorithm2();
        } else if (alg == 3) {
            algorithm = new Algorithm3();
        } else {
            System.out.println("Error. Wrong algorithm");
            System.exit(-1);
        }

        algorithm.solve(formula);
    }
}

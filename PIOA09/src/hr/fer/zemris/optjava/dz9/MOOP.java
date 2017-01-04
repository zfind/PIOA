package hr.fer.zemris.optjava.dz9;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 02.01.17..
 */
public class MOOP {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

        if (args.length != 4) {
            System.out.println("Error. Krivi argumenti");
            System.out.println("Npr. 2 20 decision-space 5000");
            System.exit(-1);
        }

        int problemIndex = Integer.parseInt(args[0]);
        MOOPProblem problem = null;
        if (problemIndex == 1) {
            problem = new Problem1();
        } else if (problemIndex == 2) {
            problem = new Problem2();
        } else {
            System.out.println("Error. Nepoznat problem");
            System.exit(-1);
        }

        int populationSize = Integer.parseInt(args[1]);

        IDistance distance = null;
        if (args[2].equals("decision-space")) {
            distance = new DecisionDistance();
        } else if (args[2].equals("objective-space")) {
            distance = new DecisionDistance();
        } else {
            System.out.println("Error. Distance");
            System.exit(-1);
        }

        int maxIter = Integer.parseInt(args[3]);

        Random random = new Random();


        NSGA algorithm = new NSGA(problem, distance, populationSize, maxIter, random);
        List<List<Solution>> fronts = algorithm.run();


        PrintWriter decision = new PrintWriter("izlaz-dec.txt", "utf-8");
        PrintWriter objective = new PrintWriter("izlaz-obj.txt", "utf-8");

        for (List<Solution> front : fronts) {
            System.out.println("------------ velicina: " + front.size() + " ------------");
            for (Solution s : front) {
                for (int i = 0; i < s.x.length; i++) {
                    decision.print(s.x[i] + "\t");
                }
                decision.println();
                for (int i = 0; i < s.z.length; i++) {
                    objective.print(s.z[i] + "\t");
                    System.out.print(s.z[i] + "\t");
                }
                objective.println();
                System.out.println();
            }
        }

        decision.close();
        objective.close();

    }

}
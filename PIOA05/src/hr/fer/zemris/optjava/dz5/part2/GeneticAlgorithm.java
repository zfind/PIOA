package hr.fer.zemris.optjava.dz5.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 14.11.16..
 */
public class GeneticAlgorithm {

    private static final double MUTATION = 1.;
    private static int N;
    private static int numberOfPopulationsStart;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Greska. Krivi argumenti");
            System.out.println("npr. 25 data/nug25.dat");
            System.exit(-1);
        }
        numberOfPopulationsStart = Integer.parseInt(args[0]);
        String path = args[1];

        List<Population> populations = new ArrayList<>();

        Random random = new Random();
        double[][][] tables = Parser.parse(path);
        N = tables[0].length;
        IFunction function = new CostFunction(tables[0], tables[1]);
        ISelection selection1 = new TournamentSelection(RAPGAlgorithm.TOURNAMENT, random);
        ISelection selection2 = new TournamentSelection(RAPGAlgorithm.TOURNAMENT, random);

        run(numberOfPopulationsStart, random, populations, function, selection1, selection2);
    }

    private static void run(int numOfPopulations, Random random, List<Population> populations,
                            IFunction function, ISelection selection1, ISelection selection2) {

        for (int i = 0; i < numOfPopulations; i++) {
            populations.add(new Population(N, function, RAPGAlgorithm.MIN_POPULATION, random));
        }

        while (true) {
            List<Solution> newSolutions = new ArrayList<>();
            for (int i = 0; i < numOfPopulations; i++) {
                System.out.println("Populations: " + numOfPopulations + " Algorithm: " + i);
//                Solution b = populations.get(i).getBest();
//                System.out.println("Current best: " + b.getFitness() + " " + b);
                RAPGAlgorithm algorithm = new RAPGAlgorithm(random, N, function, populations.get(i), selection1, selection2,
                        MUTATION * (1 - numOfPopulations / (double) numberOfPopulationsStart));
                newSolutions.addAll(algorithm.run().getSolutions());
            }
            Solution epochBestSolution = Collections.min(newSolutions);
            System.out.println("Best after epoch: " + epochBestSolution.getFitness() + " " + epochBestSolution);

            populations.clear();
            if (numOfPopulations > 2) {
                numOfPopulations--;
                int solutionsPerPop = newSolutions.size() / numOfPopulations;
                for (int i = 0; i < numOfPopulations; i++) {
                    List<Solution> xx = new ArrayList<>();
                    for (int j = 0; j < solutionsPerPop && i * solutionsPerPop + j < newSolutions.size(); j++) {
                        xx.add(newSolutions.get(i * solutionsPerPop + j));
                    }
//                    System.out.println(xx.size());    // DBG
                    populations.add(new Population(xx));
                }
            } else {
                populations.add(new Population(newSolutions));
                break;
            }
        }

        System.out.println("Last run...");
        RAPGAlgorithm algorithm = new RAPGAlgorithm(random, N, function, populations.get(0), selection1, selection2, 0);
        Population bestPopulation = algorithm.run();
        Solution bestSolution = bestPopulation.getBest();
        System.out.println("Best solution: " + bestSolution.getFitness() + " " + bestSolution);
    }

}

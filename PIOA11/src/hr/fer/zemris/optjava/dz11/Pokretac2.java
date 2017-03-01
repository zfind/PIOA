package hr.fer.zemris.optjava.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.ImageFitnessCalculator;
import hr.fer.zemris.generic.ga.IntArraySolution;
import hr.fer.zemris.optjava.algorithm.*;
import hr.fer.zemris.optjava.rng.EVOThread;

import java.io.*;

/**
 * Created by zac on 02.02.17..
 */
public class Pokretac2 {
    private static final int GRAYSCALE_MIN = 0;
    private static final int GRAYSCALE_MAX = 255;
    private static final int TOURNAMENT = 3;
    private static final double MUTATION_PROB = 0.01;
    private static final double SIGMA = 100.;

    public static void main(String[] args) {
        if (args.length != 7) {
            System.err.println("Error. Krivi argumenti");
            System.err.println("Npr. 11-kuca-200-133.png 200 30 10000 10 parametri.txt slika.png");
            System.exit(-1);
        }

        GrayScaleImage original = null;
        try {
            original = GrayScaleImage.load(new File(args[0]));
        } catch (IOException e) {
            error("Greska. Nije moguce ucitati sliku");
        }


        final int numberOfSquares = Integer.parseInt(args[1]);
        final int populationSize = Integer.parseInt(args[2]);
        final int maxIter = Integer.parseInt(args[3]);
        final double minimalFitness = Double.parseDouble(args[4]);

        ImageFitnessCalculator fitnessEvaluator = new ImageFitnessCalculator(original);

        ISelection selection = new TournamentSelection(TOURNAMENT);

        ICrossOperator crossOperator = new UniformCrossOperator();

//        IMutationOperator mutationOperator = new NormalMutation(MUTATION_PROB, SIGMA);
        IMutationOperator mutationOperator = new UniformMutation(MUTATION_PROB, GRAYSCALE_MIN, GRAYSCALE_MAX);

        int dimensions = 1 + 5 * numberOfSquares;

        EVOThread thread = new EVOThread(() -> {
            IOptAlgorithm algorithm = new ParallelGenerationGA(
                    fitnessEvaluator, selection, crossOperator, mutationOperator,
                    populationSize, maxIter, minimalFitness,
                    dimensions, GRAYSCALE_MIN, GRAYSCALE_MAX
            );

            IntArraySolution solution = algorithm.run();

            System.out.println("Najbolje rjesenje: " + solution.fitness);

            String paramOutputFilename = args[5];
            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(paramOutputFilename)));
                for (int param : solution.data) {
                    out.write(param + "\n");
                }
                out.close();
            } catch (IOException e) {
                error("Greska prilikom zapisivanja parametara");
            }

            try {
                String imageOutputFile = args[6];
                fitnessEvaluator.draw(solution).save(new File(imageOutputFile));
            } catch (IOException e) {
                error("Greska prilikom zapisivanja slike");
            }
        });

        thread.start();

    }

    private static void error(String e) {
        System.err.println(e);
        System.exit(-1);
    }

}

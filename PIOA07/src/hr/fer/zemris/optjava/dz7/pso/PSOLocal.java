package hr.fer.zemris.optjava.dz7.pso;

import hr.fer.zemris.optjava.dz7.net.*;

import java.util.Random;

/**
 * Created by zac on 12.12.16..
 */
public class PSOLocal extends PSO {
    private final int neighbourhood;

    public PSOLocal(int populationSize, int dimensions,
                    int maxIter, double minimalFitness,
                    FFANNEvaluator evaluator, Random random, int neighbourhood) {
        super(populationSize, dimensions,
                maxIter, minimalFitness,
                evaluator, random);
        this.neighbourhood = neighbourhood;
    }

    public static void main(String[] args) {
        Dataset dataset = new Dataset("data/07-iris-formatirano.data");
        ICostFunction errorFunction = new RMSError();

        FFANN net = new FFANN(new int[]{4, 5, 3, 3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                });

        FFANNEvaluator evaluator = new FFANNEvaluator(net, dataset, errorFunction);

        PSO alg = new PSOLocal(40, net.getWeightsCount(), 1000,
                0.01, evaluator, new Random(), 3);

        double[] weights = alg.run();
        net.printResult(weights, dataset, errorFunction);
    }

    @Override
    public void updatePosition(double w) {
        for (int i = 0; i < populationSize; i++) {
            Particle particle = population[i];
            double[] localBestPosition = null;
            double localBestFitness = M;
            for (int j = 0; j < (2 * neighbourhood + 1); j++) {
                Particle current = population[((i - neighbourhood + j) + populationSize) % populationSize];
                if (current.personalBestFitness < localBestFitness) {
                    localBestPosition = current.personalBest;
                    localBestFitness = current.personalBestFitness;
                }
            }

            particle.nextPosition(localBestPosition, w, random);

            if (bestFitness > particle.fitness) {
                bestPosition = particle.getPosition();
                bestFitness = particle.fitness;
            }
        }
    }
}

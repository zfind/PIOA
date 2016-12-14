package hr.fer.zemris.optjava.dz7.pso;

import hr.fer.zemris.optjava.dz7.net.*;

import java.util.Random;

/**
 * Created by zac on 12.12.16..
 */
public class PSOGlobal extends PSO {

    public PSOGlobal(int populationSize, int dimensions,
                     int maxIter, double minimalFitness,
                     FFANNEvaluator evaluator, Random random) {
        super(populationSize, dimensions,
                maxIter, minimalFitness,
                evaluator, random);
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

        PSO alg = new PSOGlobal(40, net.getWeightsCount(), 1000,
                0.01, evaluator, new Random());

        double[] weights = alg.run();
        net.printResult(weights, dataset, errorFunction);
    }

    @Override
    public void updatePosition(double w) {
        for (Particle particle : population) {
            particle.nextPosition(bestPosition, w, random);

            if (bestFitness > particle.fitness) {
                bestPosition = particle.getPosition();
                bestFitness = particle.fitness;
            }
        }
    }
}

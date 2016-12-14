package hr.fer.zemris.optjava.dz7;

import hr.fer.zemris.optjava.dz7.clonalg.ClonAlg;
import hr.fer.zemris.optjava.dz7.net.*;
import hr.fer.zemris.optjava.dz7.pso.PSO;
import hr.fer.zemris.optjava.dz7.pso.PSOGlobal;
import hr.fer.zemris.optjava.dz7.pso.PSOLocal;

import java.util.Random;

/**
 * Created by zac on 11.12.16..
 */
public class ANNTrainer {

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Error. Krivi argumenti");
            System.out.println("npr. data/07-iris-formatirano.data clonalg 40 0.02 1000");
            System.exit(-1);
        }

        String file = args[0];
        String algorithm = args[1];
        int populationSize = Integer.parseInt(args[2]);
        double minimalFitness = Double.parseDouble(args[3]);
        int maxIter = Integer.parseInt(args[4]);

        Dataset dataset = new Dataset(file);
        ICostFunction errorFunction = new RMSError();

        FFANN net = new FFANN(new int[]{4, 5, 3, 3},
                new ITransferFunction[]{
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                        new SigmoidTransferFunction(),
                });

        FFANNEvaluator evaluator = new FFANNEvaluator(net, dataset, errorFunction);
        int dimensions = net.getWeightsCount();

        Random random = new Random();

        double[] weigths = null;

        if (algorithm.startsWith("pso")) {
            if (algorithm.equals("pso-a")) {
                PSO alg = new PSOGlobal(populationSize, dimensions, maxIter, minimalFitness, evaluator, random);
                weigths = alg.run();
            } else if (algorithm.startsWith("pso-b-")) {
                String[] xx = algorithm.split("-");
                int neighbourhood = Integer.parseInt(xx[2]);
                PSO alg = new PSOLocal(populationSize, dimensions, maxIter, minimalFitness, evaluator, random, neighbourhood);
                weigths = alg.run();
            } else {
                System.out.println("Error. nepoznati algoritam");
                System.exit(-1);
            }
        } else if (algorithm.equals("clonalg")) {
            ClonAlg alg = new ClonAlg(populationSize, minimalFitness, maxIter, dimensions, evaluator, random);
            weigths = alg.run();
        } else {
            System.out.println("Error. nepoznati algoritam");
            System.exit(-1);
        }

        net.printResult(weigths, dataset, errorFunction);
    }
}

package hr.fer.zemris.optjava.dz8;


import hr.fer.zemris.optjava.dz8.de.*;
import hr.fer.zemris.optjava.dz8.net.*;

import java.util.Random;


public class ANNTrainer {

    private static final double CR = 0.6;
    private static final double F = 0.5;
    private static final int DATASET_SIZE = 600;


    public static void main(String[] args) {

        if (args.length != 5) {
            System.out.println("Error. Krivi argumenti");
            System.out.println("npr. data/08-Laser-generated-data.txt elman-1x10x1 20 0.01 1000");
            System.exit(-1);
        }

        String file = args[0];

        String architecture = args[1];
        String[] networkType = architecture.split("-");
        String[] networkLayers = networkType[1].split("x");
        int[] layers = new int[networkLayers.length];
        for (int i = 0; i < networkLayers.length; i++) {
            layers[i] = Integer.parseInt(networkLayers[i]);
        }
        int populationSize = Integer.parseInt(args[2]);
        double minimalFitness = Double.parseDouble(args[3]);
        int maxIter = Integer.parseInt(args[4]);

        Random random = new Random();

        Dataset dataset = new Dataset(file, layers[0], DATASET_SIZE);
        ITransferFunction[] transferFunctions = new ITransferFunction[layers.length];
        for (int i = 0; i < layers.length; i++) {
            transferFunctions[i] = new TanhTransferFunction();
        }

        INeuralNetwork net = null;
        if (networkType[0].equals("tdnn")) {
            net = new TDNN(layers, transferFunctions);
        } else if (networkType[0].equals("elman")) {
            net = new ElmanNN(layers, transferFunctions);
        } else {
            System.out.println("Greska. Nepoznata mreza");
            System.exit(-1);
        }

        ICostFunction errorFunction = new RMSError();
        NNEvaluator evaluator = new NNEvaluator(net, dataset, errorFunction);
        AbstractStrategy strategy = new DETargetToBest(F, evaluator, random);
        ICrossOperator crossOperator = new UniformCrossOperator(CR, random);
        int dimensions = net.getWeightsCount();

        DifferentialEvolution algorithm = new DifferentialEvolution(populationSize, dimensions, minimalFitness, maxIter,
                evaluator, strategy, crossOperator, random);

        Solution solution = algorithm.run();

        evaluator.printResult(solution.weights, dataset, errorFunction, 1E-3);

    }

}
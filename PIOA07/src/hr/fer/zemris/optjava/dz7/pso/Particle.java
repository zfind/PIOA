package hr.fer.zemris.optjava.dz7.pso;

import hr.fer.zemris.optjava.dz7.net.FFANNEvaluator;

import java.util.Random;

/**
 * Created by zac on 12.12.16..
 */
public class Particle {


    private final int dimensions;
    private final FFANNEvaluator evaluator;
    double[] personalBest;
    double fitness;
    double personalBestFitness;
    private double[] position;
    private double[] velocity;

    public Particle(int dimensions, FFANNEvaluator evaluator, Random random) {
        this.dimensions = dimensions;
        this.evaluator = evaluator;
        this.position = new double[dimensions];
        this.velocity = new double[dimensions];
        this.personalBest = position.clone();
        this.personalBestFitness = evaluator.evaluate(personalBest);
        this.fitness = this.personalBestFitness;

        for (int i = 0; i < dimensions; i++) {
            double a = PSO.WEIGHT_LOWER_BOUND;
            double diff = PSO.WEIGHT_UPPER_BOUND - PSO.WEIGHT_LOWER_BOUND;
            position[i] = random.nextDouble() * diff + a;
            velocity[i] = random.nextDouble() * diff + a;
        }
    }

    public void nextPosition(double[] otherBest, double w, Random random) {
        double U1 = random.nextDouble();
        double U2 = random.nextDouble();
        for (int i = 0; i < dimensions; i++) {
            velocity[i] = w * velocity[i]
                    + PSO.C1 * U1 * (personalBest[i] - position[i])
                    + PSO.C2 * U2 * (otherBest[i] - position[i]);
            velocity[i] = velocity[i] > PSO.V_MIN ? velocity[i] : PSO.V_MIN;
            velocity[i] = velocity[i] < PSO.V_MAX ? velocity[i] : PSO.V_MAX;
            position[i] = position[i] + velocity[i];
        }
        fitness = evaluator.evaluate(position);
        if (fitness < personalBestFitness) {
            personalBest = position.clone();
            personalBestFitness = fitness;
        }
    }

    public double[] getPosition() {
        return position.clone();
    }
}

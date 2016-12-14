package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;

/**
 * Created by zac on 02.11.16..
 */
public class RouletteWheelSelection implements ISelection {
    final Random random;

    public RouletteWheelSelection(Random random) {
        this.random = random;
    }


    @Override
    public Solution select(Population population) {
        double sumFitness = 0;
        double minimalFitness = Double.MAX_VALUE;

        for (Solution solution : population.getSolutions()) {
            sumFitness += solution.fitness;
            if (solution.fitness < minimalFitness) {
                minimalFitness = solution.fitness;
            }
        }
        sumFitness = sumFitness - population.size() * minimalFitness;

        double pick = random.nextDouble() * sumFitness;

        double boundary = 0;
        for (Solution solution : population.getSolutions()) {
            boundary += solution.fitness - minimalFitness;
            if (boundary > pick) {
                return solution;
            }
        }

        return population.getSolutions()[population.size() - 1];
    }
}

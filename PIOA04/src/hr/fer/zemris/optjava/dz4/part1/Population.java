package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;

/**
 * Created by zac on 31.10.16..
 */
public class Population {
    private Solution[] solutions;
    private final int size;
    private double bestFitness;
    private Solution bestSolution;

    public Population(int size, int number_of_variables, Random random, double[] lowerLimits, double[] upperLimits, IFunction function) {
        this.solutions = new Solution[size];
        this.size = size;
        for (int i = 0; i < size; i++) {
            solutions[i] = new Solution(number_of_variables);
            solutions[i].randomize(random, lowerLimits, upperLimits);
        }
        evaluate(function);
    }

    public Population(Solution[] c, IFunction function) {
        this.solutions = c;
        this.size = c.length;
        evaluate(function);
    }

    public int size() {
        return size;
    }

    public Solution[] getSolutions() {
        return solutions;
    }

    public Solution getBestSolution() {
        return bestSolution;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    private void evaluate(IFunction function) {
        bestFitness = -Double.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            Solution c = solutions[i];
            c.value = function.valueAt(c.values);
            c.fitness = -c.value;

            if (c.fitness > bestFitness) {
                bestFitness = c.fitness;
                bestSolution = c;
            }
        }
    }
}

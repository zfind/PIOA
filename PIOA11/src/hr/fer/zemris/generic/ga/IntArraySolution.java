package hr.fer.zemris.generic.ga;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.Arrays;

/**
 * Created by zac on 24.01.17..
 */
public class IntArraySolution extends GASolution<int[]> {

    public IntArraySolution() {
        this(0);
    }

    public IntArraySolution(int dimensions) {
        this.data = new int[dimensions];
    }

    public IntArraySolution(int[] data) {
        this.data = data;
    }

    public static IntArraySolution[] newPopulation(int populationSize, int dimensions, int min, int max) {
        IRNG random = RNG.getRNG();
        IntArraySolution[] population = new IntArraySolution[populationSize];

        for (int i = 0; i < populationSize; i++) {
            int[] data = new int[dimensions];

            for (int j = 0; j < dimensions; j++) {
                data[j] = random.nextInt(min, max);
            }

            population[i] = new IntArraySolution(data);
        }

        return population;
    }

    @Override
    public GASolution<int[]> duplicate() {
        IntArraySolution duplicate = new IntArraySolution(data.clone());
        duplicate.fitness = fitness;
        duplicate.value = value;

        return duplicate;
    }

    @Override
    public String toString() {
        return Arrays.toString(data);
    }
}

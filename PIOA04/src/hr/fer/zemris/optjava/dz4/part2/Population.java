package hr.fer.zemris.optjava.dz4.part2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 05.11.16..
 */
public class Population {

    private Solution[] solutions;

    public Population(List<Integer> sticks, int populationSize, int maxHeight, Random random) {
        this.solutions = new Solution[populationSize];
        for (int i = 0; i < populationSize; i++) {
            Collections.shuffle(sticks);
            solutions[i] = new Solution(sticks, maxHeight, random);
        }
    }

    public Solution getSolution(int i) {
        return solutions[i];
    }

    public int size() {
        return solutions.length;
    }

    public Solution getBest() {
        return Arrays.stream(solutions).max((Solution s1, Solution s2) -> Integer.compare(s1.getFitness(), s2.getFitness())).get();
    }

}

package hr.fer.zemris.optjava.dz5.part1;

import java.util.*;

/**
 * Created by zac on 13.11.16..
 */
public class Population {
    private List<Solution> solutions;

    public Population(int n, IFunction function, int min, Random random) {
        this.solutions = new ArrayList<>();
        for (int i = 0; i < min; i++) {
            this.solutions.add(new Solution(n, function, random));
        }
    }

    public Population(Set<Solution> solutions) {
        this.solutions = new ArrayList<>();
        this.solutions.addAll(solutions);
    }

    public Solution getBest() {
        return Collections.max(solutions);
    }

    public int size() {
        return solutions.size();
    }

    public Solution getSolution(int i) {
        return solutions.get(i);
    }

}

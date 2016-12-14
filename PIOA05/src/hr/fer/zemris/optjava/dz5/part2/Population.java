package hr.fer.zemris.optjava.dz5.part2;

import java.util.*;

/**
 * Created by zac on 14.11.16..
 */
public class Population {
    private List<Solution> solutions;

    public Population(int n, IFunction function, int minimalPopulation, Random random) {
        this.solutions = new ArrayList<>();
        List<Integer> locations = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            locations.add(i);
        }
        for (int i = 0; i < minimalPopulation; i++) {
            Collections.shuffle(locations);
            this.solutions.add(new Solution(new ArrayList<>(locations), function));
        }
    }

    public Population(Collection<Solution> solutions) {
        this.solutions = new ArrayList<>();
        this.solutions.addAll(solutions);
    }

    public int size() {
        return solutions.size();
    }

    public Solution getBest() {
        return Collections.min(solutions);
    }

    public Solution getSolution(int i) {
        return solutions.get(i);
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

}

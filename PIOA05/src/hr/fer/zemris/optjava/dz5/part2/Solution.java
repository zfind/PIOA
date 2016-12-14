package hr.fer.zemris.optjava.dz5.part2;

import java.util.List;

/**
 * Created by zac on 14.11.16..
 */
public class Solution implements Comparable<Solution> {
    private List<Integer> locations;
    private double fitness;
    private IFunction function;

    public Solution(List<Integer> locations, IFunction function) {
        this.locations = locations;
        this.function = function;
        evaluate();
    }

    public List<Integer> getValues() {
        return locations;
    }

    public double getFitness() {
        return fitness;
    }

    private void evaluate() {
        this.fitness = function.valueAt(this);
    }

    public int size() {
        return locations.size();
    }

    public int get(int i) {
        return locations.get(i);
    }


    @Override
    public int compareTo(Solution o) {
        if (this.fitness < o.fitness) {
            return -1;
        } else if (this.fitness > o.fitness) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Solution && this.locations.equals(((Solution) o).locations);
    }

    @Override
    public String toString() {
        return locations.toString();
    }

}

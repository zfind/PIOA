package hr.fer.zemris.optjava.dz3;

/**
 * Created by zac on 23.10.16..
 */
public class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution> {
    public double fitness;
    public double value;

    public SingleObjectiveSolution() {

    }

    public int compareTo(SingleObjectiveSolution o) {
        if (this.fitness < o.fitness) {
            return -1;
        } else if (this.fitness == o.fitness) {
            return 0;
        } else {
            return 1;
        }
    }

}

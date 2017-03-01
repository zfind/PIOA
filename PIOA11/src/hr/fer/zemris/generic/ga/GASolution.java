package hr.fer.zemris.generic.ga;

public abstract class GASolution<T> implements Comparable<GASolution<T>> {
    public T data;
    public double fitness;
    public double value;

    public GASolution() {
        this(0, 0);
    }

    public GASolution(double fitness, double value) {
        this.fitness = fitness;
        this.value = value;
    }

    public T getData() {
        return data;
    }

    public abstract GASolution<T> duplicate();

    public void updateValue(double value) {
        this.value = value;
        this.fitness = -value;
    }

    @Override
    public int compareTo(GASolution<T> o) {
        return Double.compare(this.fitness, o.fitness);
    }
}
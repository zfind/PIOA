package hr.fer.zemris.optjava.dz3;

import java.util.Random;

/**
 * Created by zac on 24.10.16..
 */
public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<T> {
    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;
    private Random rand;
    private ITempSchedule tempSchedule;

    private T best;

    public SimulatedAnnealing(IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith, IFunction function, ITempSchedule schedule, boolean minimize) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
        this.tempSchedule = schedule;
        this.rand = new Random();
    }

    @Override
    public void run() {
        T current = startWith;
        T currentBest = startWith;
        setPenalty(current);

        int outerIterations = tempSchedule.getOuterLoopCounter();
        int innerIterations = tempSchedule.getInnerLoopCounter();
        double nextTemperature = tempSchedule.getNextTemperature();

        for (int i = 0; i < outerIterations; i++) {
            System.out.printf("i: %4d  T: %.6f | best: %f  %s | current: %f  %s\n",
                    i, nextTemperature,
                    Math.sqrt(currentBest.value), decoder.toString(currentBest),
                    Math.sqrt(current.value), decoder.toString(current));

            for (int j = 0; j < innerIterations; j++) {
                T neighbor = neighborhood.randomNeighbor(current);
                setPenalty(neighbor);
                if (neighbor.compareTo(current) >= 0 || acceptCondition(current, neighbor, nextTemperature)) {
                    current = neighbor;
                    if (current.compareTo(currentBest) >= 0) {
                        System.out.printf("current: %f  ->  new: %f  %s\n",
                                Math.sqrt(currentBest.value), Math.sqrt(current.value),
                                decoder.toString(currentBest));
                        currentBest = current;
                    }
                }
            }

            nextTemperature = tempSchedule.getNextTemperature();
        }

        best = currentBest;
    }

    private void setPenalty(T candidate) {
        double value = function.valueAt(decoder.decode(candidate));
        candidate.value = value;
        candidate.fitness = minimize ? -value : value;
    }

    private boolean acceptCondition(T candidate, T neighbor, double tk) {
        double probability = Math.exp((neighbor.fitness - candidate.fitness) / tk);
        return rand.nextDouble() < probability;
    }

    public T getBest() {
        return best;
    }

}

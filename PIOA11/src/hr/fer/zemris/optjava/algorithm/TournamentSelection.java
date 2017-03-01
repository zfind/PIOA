package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.generic.ga.IntArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Created by zac on 24.01.17..
 */
public class TournamentSelection implements ISelection {

    private int k;

    public TournamentSelection(int k) {
        this.k = k;
    }

    @Override
    public int select(IntArraySolution[] population) {
        IRNG random = RNG.getRNG();
        int size = population.length;

        int selected = random.nextInt(0, size);

        for (int i = 1; i < k; i++) {
            int next = random.nextInt(0, size);
            IntArraySolution s = population[next];
            if (s.compareTo(population[selected]) > 0) {
                selected = next;
            }
        }

        return selected;
    }

}

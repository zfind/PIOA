package hr.fer.zemris.optjava.dz5.part1;

import java.util.Random;

/**
 * Created by zac on 13.11.16..
 */
public class TournamentSelection implements ISelection {

    private final int n;
    private final Random random;

    public TournamentSelection(int n, Random random) {
        this.n = n;
        this.random = random;
    }

    @Override
    public Solution select(Population population) {
        Solution[] solutions = pickNSolutions(population);
        return pick(solutions);
    }

    private Solution[] pickNSolutions(Population population) {
        int populationSize = population.size();
        Solution[] solutions = new Solution[n];
        for (int i = 0; i < n; i++) {
            solutions[i] = population.getSolution(random.nextInt(populationSize));
        }
        return solutions;
    }

    private Solution pick(Solution[] solutions) {
        Solution result = solutions[0];
        for (int i = 0; i < n; i++) {
            if (solutions[i].getFitness() > result.getFitness()) {
                result = solutions[i];
            }
        }
        return result;
    }

}

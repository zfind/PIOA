package hr.fer.zemris.optjava.dz4.part2;

import java.util.Random;

/**
 * Created by zac on 05.11.16..
 */
public class Tournament implements ISelection {

    private int n;
    private boolean returnWorst;
    private Random random;

    public Tournament(int n, boolean returnWorst, Random random) {
        this.n = n;
        this.returnWorst = returnWorst;
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
        for (int i=0; i<n; i++) {
            solutions[i] = population.getSolution(random.nextInt(populationSize));
        }
        return solutions;
    }

    private Solution pick(Solution[] solutions) {
        Solution result = solutions[0];
        for (int i=0; i<n; i++) {
            if (returnWorst) {
                if (solutions[i].getFitness() < result.getFitness()) {
                    result = solutions[i];
                }
            } else {
                if (solutions[i].getFitness() > result.getFitness()) {
                    result = solutions[i];
                }
            }
        }
        return result;
    }
}

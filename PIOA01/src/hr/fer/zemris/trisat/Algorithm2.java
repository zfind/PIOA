package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 10.10.16..
 * Algorithm uses iterative search with simple fitness function.
 * Often stuck in local optimum or iteration limit reached.
 */
public class Algorithm2 implements Algorithm {
    private static final int MAX_ITER = 100000;
    private Random random = new Random(System.currentTimeMillis());


    @Override
    public void solve(SATFormula formula) {
        BitVector x = new BitVector(random, formula.getNumberOfVariables());

        int i = 0;
        do {
            MutableBitVector[] neighbourhood = new BitVectorNGenerator(x).createNeighbourhood();
            List<MutableBitVector> bestNeighbours = new ArrayList<>();
            int currentFit = fitness(x, formula);
            int bestFit = -1;

            for (MutableBitVector neighbour : neighbourhood) {
                int tmp = fitness(neighbour, formula);
                if (tmp > bestFit) {
                    bestFit = tmp;
                    bestNeighbours.clear();
                    bestNeighbours.add(neighbour);
                } else if (tmp == bestFit) {
                    bestNeighbours.add(neighbour);
                }
            }

            if (bestFit < currentFit) {
                System.out.println("Local optimum. Exit");
                return;
            }

            int next = random.nextInt(bestNeighbours.size());
            x = bestNeighbours.get(next);

            if (formula.isSatisfied(x)) {
                System.out.println("Satisfied.\n" + x.toString() + "\nIteration: " + i);
                return;
            }
        } while (i++ < MAX_ITER);

        System.out.println("Iteration limit reached");

    }

    /**
     * Fitness function
     * @param x given assignment
     * @param formula formula which should be solved
     * @return number of satisfied clauses in given formula
     */
    private int fitness(BitVector x, SATFormula formula) {
        int numberOfSatisfied = 0;
        for (int i = 0; i < formula.getNumberOfClauses(); i++) {
            if (formula.getClause(i).isSatisfied(x)) {
                numberOfSatisfied++;
            }
        }
        return numberOfSatisfied;
    }
}

package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 10.10.16..
 * Algorithm uses iterative search with advanced fitness function
 */
public class Algorithm3 implements Algorithm {
    private static final int MAX_ITER = 100000;
    private static final int numberOfBest = 2;
    private Random random = new Random(System.currentTimeMillis());
    private Comparator<VectorFitnessPair> comparator = (VectorFitnessPair o1, VectorFitnessPair o2) -> Double.compare(o2.fitness, o1.fitness);


    @Override
    public void solve(SATFormula formula) {
        BitVector x = new BitVector(random, formula.getNumberOfVariables());
        SATFormulaStats stats = new SATFormulaStats(formula);
        stats.setAssignment(x, true);

        int i = 0;
        do {
            if (formula.isSatisfied(x)) {
                System.out.println("Satisfied.\n" + x.toString() + "\nIteration: " + i);
                return;
            }

            List<VectorFitnessPair> neighbours = new ArrayList<>();

            BitVectorNGenerator generator = new BitVectorNGenerator(x);
            for (BitVector bitVector : generator) {
                stats.setAssignment(bitVector, false);
                double fitness = stats.getNumberOfSatisfied() + stats.getPercentageBonus();
                neighbours.add(new VectorFitnessPair(bitVector, fitness));
            }

            neighbours.sort(comparator);
            x = neighbours.get(random.nextInt(numberOfBest)).bitVector;
            stats.setAssignment(x, true);
        } while (i++ < MAX_ITER);

        System.out.println("Iteration limit reached");

    }

    /**
     * Helper class, for easier sorting of assignments according to fitness value
     * @author zac
     */
    class VectorFitnessPair {
        BitVector bitVector;
        double fitness;

        VectorFitnessPair(BitVector bitVector, double fitness) {
            this.bitVector = bitVector;
            this.fitness = fitness;
        }
    }
    
}

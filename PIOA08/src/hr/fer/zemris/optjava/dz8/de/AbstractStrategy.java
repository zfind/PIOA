package hr.fer.zemris.optjava.dz8.de;

import hr.fer.zemris.optjava.dz8.net.NNEvaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by zac on 19.12.16..
 */
public abstract class AbstractStrategy {
    protected NNEvaluator evaluator;
    protected Random random;

    public AbstractStrategy(NNEvaluator evaluator, Random random) {
        this.evaluator = evaluator;
        this.random = random;
    }

    public abstract double[] mutate(List<Solution> population, Solution bestSolution, int targetIndex);

    public Solution[] getNDistinct(int n, List<Solution> population, int targetIndex) {
        int populationSize = population.size();
        Set<Integer> set = new HashSet<>();
        set.add(targetIndex);
        while (set.size() <= n) {
            set.add(random.nextInt(populationSize));
        }
        set.remove(new Integer(targetIndex));

        Solution[] solutions = new Solution[n];
        int currentIndex = 0;
        for (Integer index : set) {
            solutions[currentIndex++] = population.get(index);
        }

        return solutions;
    }

}
package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.generic.ga.ImageFitnessCalculator;
import hr.fer.zemris.generic.ga.IntArraySolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zac on 24.01.17..
 */
public class ParallelEvaluationGA implements IOptAlgorithm {

    private ISelection selection;
    private ICrossOperator crossOperator;
    private IMutationOperator mutationOperator;
    private int populationSize;
    private int maxIter;
    private double minimalFitness;

    private ParallelEvaluator evaluator;
    private IntArraySolution[] population;

    public ParallelEvaluationGA(
            ImageFitnessCalculator fitnessEvaluator,
            ISelection selection,
            ICrossOperator crossOperator,
            IMutationOperator mutationOperator,
            int populationSize,
            int maxIter,
            double minimalFitness,
            int dimensions,
            int min,
            int max) {
        this.selection = selection;
        this.crossOperator = crossOperator;
        this.mutationOperator = mutationOperator;
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.minimalFitness = minimalFitness;

        this.evaluator = new ParallelEvaluator(fitnessEvaluator);

        this.population = IntArraySolution.newPopulation(populationSize, dimensions, min, max);

    }


    @Override
    public IntArraySolution run() {
        IRNG random = RNG.getRNG();
        IntArraySolution[] nextPopulation = new IntArraySolution[populationSize];
        IntArraySolution best = evaluate(population);

        for (int iteration = 1; iteration <= maxIter; iteration++) {
            if (best.fitness >= minimalFitness) {
                break;
            }
            if (iteration % 100 == 0) {
                System.out.println(iteration + ":\t" + best.value);
            }

            nextPopulation[0] = best;

            for (int i = 1; i < populationSize; i++) {
                Set<Integer> set = new HashSet<>();
                while (set.size() < 2) {
                    set.add(selection.select(population));
                }
                List<Integer> tmp = new ArrayList<>(set);
                IntArraySolution p1 = population[tmp.get(0)];
                IntArraySolution p2 = population[tmp.get(1)];

                IntArraySolution child = crossOperator.cross(p1, p2);

                mutationOperator.mutate(child);

                nextPopulation[i] = child;
            }

            population = nextPopulation;
            nextPopulation = new IntArraySolution[populationSize];

            best = evaluate(population);
        }

        evaluator.shutdown();

        return best;
    }

    private IntArraySolution evaluate(IntArraySolution[] population) {
        IntArraySolution best = null;

        for (IntArraySolution solution : population) {
            evaluator.put(solution);
        }

        for (int i = 0; i < populationSize; i++) {
            population[i] = evaluator.take();
            if (best == null || population[i].compareTo(best) > 0) {
                best = population[i];
            }
        }

        return best;
    }

}

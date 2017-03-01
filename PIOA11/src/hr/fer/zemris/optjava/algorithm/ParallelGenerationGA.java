package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.generic.ga.ImageFitnessCalculator;
import hr.fer.zemris.generic.ga.IntArraySolution;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zac on 02.02.17..
 */
public class ParallelGenerationGA implements IOptAlgorithm {

    private ImageFitnessCalculator fitnessEvaluator;
    private final int populationSize;
    private final int maxIter;
    private final double minimalFitness;

    private IntArraySolution[] population;

    private Queue<Job> in;
    private Queue<IntArraySolution> out;
    private final Job poison;
    private final Thread[] threads;


    public ParallelGenerationGA(
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
        this.fitnessEvaluator = fitnessEvaluator;
        this.populationSize = populationSize;
        this.maxIter = maxIter;
        this.minimalFitness = minimalFitness;

        this.population = IntArraySolution.newPopulation(populationSize, dimensions, min, max);

        this.in = new ConcurrentLinkedQueue<>();
        this.out = new ConcurrentLinkedQueue<>();
        this.poison = new Job();
        this.threads = new Thread[Runtime.getRuntime().availableProcessors()];

        for (int t = 0; t < threads.length; t++) {
            threads[t] = new EVOThread(() -> {

                while (true) {
                    Job job = in.poll();

                    if (job == null) {
                        Thread.yield();
                    } else {

                        if (job == poison) {
                            return;
                        }

                        IRNG rng = RNG.getRNG();
                        for (int i = 0; i < job.solsToGenerate; i++) {
                            Set<Integer> set = new HashSet<>();
                            while (set.size() < 2) {
                                set.add(selection.select(job.population));
                            }
                            List<Integer> tmp = new ArrayList<>(set);
                            IntArraySolution p1 = job.population[tmp.get(0)];
                            IntArraySolution p2 = job.population[tmp.get(1)];

                            IntArraySolution child = crossOperator.cross(p1, p2);

                            mutationOperator.mutate(child);

                            fitnessEvaluator.evaluate(child);

                            out.add(child);
                        }
                    }
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public IntArraySolution run() {
        IntArraySolution[] nextPopulation = new IntArraySolution[populationSize];
        IntArraySolution best = evaluate(population);

        final int jobsPerThread = (populationSize - 1) / threads.length;   // elitizam

        for (int iteration = 1; iteration <= maxIter; iteration++) {
            if (best.fitness >= minimalFitness) {
                break;
            }
            if (iteration % 100 == 0) {
                System.out.println(iteration + ":\t" + best.value);
            }

            int remainder = (populationSize - 1) % threads.length;
            for (Thread thread : threads) {
                int jobs = remainder > 0 ? jobsPerThread + 1 : jobsPerThread;
                Job job = new Job(jobs, population);
                in.add(job);
                remainder--;
            }

            nextPopulation[0] = best;

            for (int i = 1; i < populationSize; i++) {
                nextPopulation[i] = take();

                if (best == null || nextPopulation[i].compareTo(best) > 0) {
                    best = nextPopulation[i];
                }
            }

            population = nextPopulation;
            nextPopulation = new IntArraySolution[populationSize];
        }

        shutdown();

        return best;
    }


    private IntArraySolution evaluate(IntArraySolution[] population) {
        IntArraySolution best = null;

        for (IntArraySolution solution : population) {
            fitnessEvaluator.evaluate(solution);
            if (best == null || solution.compareTo(best) > 0) {
                best = solution;
            }
        }

        return best;
    }

    private IntArraySolution take() {
        while (true) {
            IntArraySolution solution = out.poll();

            if (solution == null) {
                Thread.yield();
            } else {
                return solution;
            }
        }
    }

    private void shutdown() {
        for (Thread thread : threads) {
            in.add(poison);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Job {

        private final IntArraySolution[] population;
        private final int solsToGenerate;

        public Job(int solsToGenerate, IntArraySolution[] population) {
            this.solsToGenerate = solsToGenerate;
            this.population = population;
        }

        // poison
        public Job() {
            this(0, null);
        }

    }

}

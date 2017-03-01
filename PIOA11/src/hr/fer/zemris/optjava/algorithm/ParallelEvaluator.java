package hr.fer.zemris.optjava.algorithm;

import hr.fer.zemris.generic.ga.ImageFitnessCalculator;
import hr.fer.zemris.generic.ga.IntArraySolution;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zac on 24.01.17..
 */
public class ParallelEvaluator {

    private Queue<IntArraySolution> in;
    private Queue<IntArraySolution> out;
    private final IntArraySolution poison;
    private final Thread[] threads;

    public ParallelEvaluator(ImageFitnessCalculator fitnessEvaluator) {
        this.in = new ConcurrentLinkedQueue<>();
        this.out = new ConcurrentLinkedQueue<>();
        this.poison = new IntArraySolution();
        this.threads = new Thread[Runtime.getRuntime().availableProcessors()];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {

                while (true) {
                    IntArraySolution solution = in.poll();

                    if (solution == null) {
                        Thread.yield();
                    } else {
                        if (solution == poison) {
                            return;
                        }
                        fitnessEvaluator.evaluate(solution);
                        out.offer(solution);
                    }
                }

            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void put(IntArraySolution solution) {
        in.add(solution);
    }

    public IntArraySolution take() {
        while (true) {
            IntArraySolution solution = out.poll();

            if (solution == null) {
                // continue;
                Thread.yield(); // ovo je brze
            } else {
                return solution;
            }
        }
    }

    public void shutdown() {
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

}

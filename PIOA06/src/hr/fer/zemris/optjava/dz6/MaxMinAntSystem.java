package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 03.12.16..
 */
public class MaxMinAntSystem {
    private final int n;
    private final int k;
    private final int l;
    private final int maxIter;
    private final double alpha;
    private final double ro;
    private final double a;
    private Random random;
    private final double[][] distances;
    private final double[][] heuristics;
    private double tMin;
    private double tMax;
    private double[][] chemTrails;
    private final int[][] candidates;



    public MaxMinAntSystem(int n, int k, int l, int maxIter, Random random,
                           double[][] distances, double[][] heuristics,
                           double alpha, double ro) {
        this.random = random;
        this.n = n;
        this.k = k;
        this.l = l;
        this.maxIter = maxIter;
        this.distances = distances;
        this.heuristics = heuristics;
        this.alpha = alpha;
        this.ro = ro;
        this.a = n / (double) k;

        candidates = new int[n][k];
        for (int i = 0; i < n; i++) {
            List<CityDistancePair> neighbours = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    neighbours.add(new CityDistancePair(j, distances[i][j]));
                }
            }
            Collections.sort(neighbours);
            for (int j = 0; j < k; j++) {
                candidates[i][j] = neighbours.get(j).city;
            }
        }
    }

    public Solution run() {
        int currentBestUpdates = (int) (maxIter * 1. / 3.);
        int stagnationCounter = 0;
        int stagnationLimit = 1000;

        Solution best = greedy();
        System.out.println("greedy: " + best.getTourLength());

        tMax = 1. / (ro * best.getTourLength());
        tMin = tMax / a;
        initTrails();

        for (int i = 0; i < maxIter; i++) {
            List<Solution> ants = new ArrayList<>();
            for (int j = 0; j < l; j++) {
                Solution ant = new Solution(n, random);
                ant.doWalk(distances, chemTrails, heuristics, candidates, alpha);
                ants.add(ant);
            }

            Solution currentBest = Collections.min(ants);
            stagnationCounter++;
            if (stagnationCounter >= stagnationLimit) {
                stagnationCounter = 0;
                initTrails();
            }
            if (currentBest.compareTo(best) < 0) {
                tMax = 1 / (ro * best.getTourLength());
                tMin = tMin / a;
                best = currentBest;
                System.out.println(i + ": " + best.getTourLength());
            }

            evaporateTrails();

            if (i < currentBestUpdates) {
                refreshTrails(currentBest);
            } else {
                refreshTrails(best);
            }

        }

        return best;
    }

    private void initTrails() {
        chemTrails = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                chemTrails[i][j] = tMax;
                chemTrails[j][i] = tMax;
            }
        }
    }

    private void refreshTrails(Solution ant) {
        double delta = 1. / ant.getTourLength();
        for (int i = 0; i < n - 1; i++) {
            int a = ant.indexes[i];
            int b = ant.indexes[i + 1];
            double trail = chemTrails[a][b] + delta;
            chemTrails[a][b] = trail < tMax ? trail : tMax;
            chemTrails[b][a] = chemTrails[a][b];
        }
    }

    private void evaporateTrails() {
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                double trail = chemTrails[i][j] * (1 - ro);
                chemTrails[i][j] = trail < tMin ? tMin : trail;
                chemTrails[j][i] = chemTrails[i][j];
            }
        }
    }

    private Solution greedy() {
        List<Integer> rest = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            rest.add(i);
        }
        Collections.shuffle(rest);
        Integer start = rest.get(0);
        Integer index = start;
        rest.remove(0);
        int[] indexes = new int[n];
        indexes[0] = start;
        double totalDistance = 0.;

        for (int i = 1; i < n; i++) {
            List<CityDistancePair> neighbours = new ArrayList<>();
            for (Integer neighbour : rest) {
                neighbours.add(new CityDistancePair(neighbour, distances[index][neighbour]));
            }
            CityDistancePair bestNeighbour = Collections.min(neighbours);
            index = bestNeighbour.city;
            totalDistance += bestNeighbour.distance;
            rest.remove(index);
            indexes[i] = index;
        }
        totalDistance += distances[index][start];

        return new Solution(n, indexes, totalDistance);
    }


    public class CityDistancePair implements Comparable<CityDistancePair> {

        final int city;
        final double distance;

        public CityDistancePair(int city, double distance) {
            this.city = city;
            this.distance = distance;
        }

        @Override
        public int compareTo(CityDistancePair p) {
            if (this.distance < p.distance) {
                return -1;
            } else if (this.distance > p.distance) {
                return 1;
            } else {
                return 0;
            }
        }

    }
}

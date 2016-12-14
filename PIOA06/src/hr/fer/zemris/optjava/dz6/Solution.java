package hr.fer.zemris.optjava.dz6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 03.12.16..
 */
public class Solution implements Comparable<Solution> {
    private final int n;
    public int[] indexes;
    public double tourLength;
    private Random random;

    public Solution(int n, Random random) {
        indexes = new int[n];
        indexes[0] = random.nextInt(n);
        this.n = n;
        this.random = random;
    }

    public Solution(int n, int[] indexes, double tourLength) {
        this.n = n;
        this.indexes = indexes;
        this.tourLength = tourLength;
    }

    public void doWalk(double[][] distances, double[][] chemTrails, double[][] heuristics, int[][] candidateList, double alpha) {
        for (int currentIndex = 0; currentIndex < n - 1; currentIndex++) {
            int currentCity = indexes[currentIndex];

            List<Integer> possibleNeighbours = new ArrayList<>();
            for (int i : candidateList[currentCity]) {
                boolean contains = false;
                for (int j : indexes) {
                    if (i == j) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    possibleNeighbours.add(i);
                }
            }

            if (possibleNeighbours.isEmpty()) {
                for (int i = 0; i < n; i++) {
                    possibleNeighbours.add(i);
                }
                for (int i = 0; i <= currentIndex; i++) {
                    possibleNeighbours.remove((Integer) indexes[i]);
                }
            }

            int candidate = pickCandidate(currentCity, possibleNeighbours, chemTrails, heuristics, alpha);
            indexes[currentIndex + 1] = candidate;
            tourLength += distances[currentCity][candidate];
        }

        tourLength += distances[indexes[0]][indexes[n - 1]];
    }

    private int pickCandidate(int currentCity, List<Integer> possibleNeighbours, double[][] chemTrails, double[][] heuristics, double alpha) {
        double sum = 0.;
        List<Double> probs = new ArrayList<>();
        for (Integer i : possibleNeighbours) {
            double xx = Math.pow(chemTrails[currentCity][i], alpha) * heuristics[currentCity][i];
            probs.add(xx);
            sum += xx;
        }
        double probability = random.nextDouble();
        double xx = 0.;
        for (int i = 0; i < possibleNeighbours.size(); i++) {
            xx += probs.get(i) / sum;
            if (probability <= xx) {
                return possibleNeighbours.get(i);
            }
        }
        System.out.println("PAZI!");
        System.out.println(this);
        System.out.println(probs);
        return -1;
    }

    @Override
    public int compareTo(Solution s) {
        if (this.tourLength < s.tourLength) {
            return -1;
        } else if (this.tourLength > s.tourLength) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(indexes[0]);
        for (int i = 1; i < n; i++) {
            sb.append(", ");
            sb.append(indexes[i]);
        }
        sb.append(")");
        return sb.toString();
    }

    public double getTourLength() {
        return tourLength;
    }

}

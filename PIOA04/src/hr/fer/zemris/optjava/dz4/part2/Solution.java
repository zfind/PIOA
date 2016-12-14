package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 05.11.16..
 */
public class Solution {

    private final int maxHeight;
    private List<Bin> bins;
    private int fitness;


    public Solution(List<Integer> sticks, int maxHeight, Random random) {
        bins = new ArrayList<>();
        bins.add(new Bin(maxHeight));
        for (Integer stick : sticks) {
            int i = random.nextInt(bins.size());
            boolean added = bins.get(i).add(stick);
            if (!added) {
                bins.add(new Bin(stick, maxHeight));
            }
        }
        this.maxHeight = maxHeight;
        evaluate();
    }


    public Solution(Solution solution) {
        this.bins = new ArrayList<>();
        for (Bin bin : solution.getBins()) {
            this.bins.add(new Bin(bin));
        }
        this.fitness = solution.getFitness();
        this.maxHeight = solution.getMaxHeight();
    }

    public Solution(Solution solution, List<Integer> sticks) {
        this.maxHeight = solution.getMaxHeight();
        this.bins = new ArrayList<>();
        for (Bin bin : solution.getBins()) {
            bins.add(new Bin(bin));
        }

        Collections.sort(bins, (Bin b1, Bin b2) -> Integer.compare(b1.getCurrentHeight(), b2.getCurrentHeight()));
        Collections.sort(sticks, (Integer i1, Integer i2) -> Integer.compare(i2, i1));
        for (Integer stick : sticks) {
            boolean added = false;
            for (Bin bin : bins) {
                added = bin.add(stick);
                if (added) {
                    break;
                }
            }
            if (!added) {
                bins.add(new Bin(stick, maxHeight));
            }
        }
//        Collections.shuffle(bins);

        evaluate();
    }


    public void addBins(List<Bin> bins) {
        this.bins.addAll(bins);
        evaluate();
    }

    public Bin remove(int index) {
        Bin bin = bins.get(index);
        bins.remove(index);
        return bin;
    }

    public void swapWith(Solution solution) {
        this.bins = solution.bins;
        evaluate();
    }

    public int size() {
        return bins.size();
    }

    public Bin getBin(int i) {
        return bins.get(i);
    }

    public List<Bin> getBins() {
        return bins;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getFitness() {
        return fitness;
    }

    private void evaluate() {
        int sum = 0;
        for (Bin bin : bins) {
            sum += bin.getFreeSpace();
        }
        fitness = -sum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Bin bin : bins) {
            sb.append(bin.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}

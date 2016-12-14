package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zac on 05.11.16..
 */
public class Bin {

    private final List<Integer> sticks;
    private final int maxHeight;
    private int currentHeight;

    public Bin(int maxHeight) {
        this.sticks = new ArrayList<>();
        this.maxHeight = maxHeight;
        this.currentHeight = 0;
    }


    public Bin(Integer stick, int maxHeight) {
        this.sticks = new ArrayList<>();
        this.sticks.add(stick);
        this.maxHeight = maxHeight;
        this.currentHeight = stick;
    }

    /* Copy constructor */
    public Bin(Bin bin) {
        this.sticks = new ArrayList<>();
        this.sticks.addAll(bin.getSticks());
        this.maxHeight = bin.getMaxHeight();
        this.currentHeight = bin.getCurrentHeight();
    }

    private int getMaxHeight() {
        return maxHeight;
    }

    public boolean add(Integer stick) {
        if (currentHeight + stick > maxHeight) {
            return false;
        }
        sticks.add(stick);
        currentHeight += stick;
        return true;
    }

    public List<Integer> getSticks() {
        return sticks;
    }

    public boolean remove(Integer stick) {
        if (sticks.remove(stick)) {
            currentHeight -= stick;
            return true;
        }
        return false;
    }

    public double getPercentUsed() {
        return (double) (currentHeight) / maxHeight;
    }

    public int getFreeSpace() {
        return maxHeight - currentHeight;
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    @Override
    public String toString() {
        int size = sticks.size();
        if (size == 0) {
            return "( )";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < size - 1; i++) {
            sb.append(sticks.get(i));
            sb.append(" ");
        }
        sb.append(sticks.get(size - 1));
        sb.append(")");
        return sb.toString();
    }

}

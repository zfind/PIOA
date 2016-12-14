package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 05.11.16..
 */
public class CrossingOperator {

    private final double crossingParam;
    private final Random random;

    public CrossingOperator(double crossingParam, Random random) {
        this.crossingParam = crossingParam;
        this.random = random;
    }

    public Solution cross(Solution p1, Solution p2) {
        Solution p1cpy = new Solution(p1);
        Solution p2cpy = new Solution(p2);

        int breakpoint = random.nextInt((int) (p2cpy.size() * crossingParam));
        List<Bin> p2ChosenBins = p2cpy.getBins().subList(0, breakpoint);
        List<Integer> p2SticksFromChosen = new ArrayList<>();
        for (Bin bin : p2ChosenBins) {
            p2SticksFromChosen.addAll(bin.getSticks());
        }

        List<Bin> p1Bins = p1cpy.getBins();
        List<Bin> p1ChangedBins = new ArrayList<>();
        List<Integer> p1SticksFromChanged = new ArrayList<>();
        for (Bin bin : p1Bins) {
            boolean found = false;
            List<Integer> foundSticks = new ArrayList<>();
            for (Integer stick : p2SticksFromChosen) {
                if (bin.remove(stick)) {
                    foundSticks.add(stick);
                    found = true;
                }
            }
            if (found) {
                foundSticks.forEach(p2SticksFromChosen::remove);
                p1SticksFromChanged.addAll(bin.getSticks());
                p1ChangedBins.add(bin);
            }
        }
        p1Bins.removeAll(p1ChangedBins);
        p1cpy.addBins(p2ChosenBins);

        return new Solution(p1cpy, p1SticksFromChanged);
    }

}

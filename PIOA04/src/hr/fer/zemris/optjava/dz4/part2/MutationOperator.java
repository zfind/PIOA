package hr.fer.zemris.optjava.dz4.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 06.11.16..
 */
public class MutationOperator {

    private int mtParam;
    private Random random;

    public MutationOperator(int mtParam, Random random) {
        this.mtParam = mtParam;
        this.random = random;
    }

    public Solution mutate(Solution solution) {
        int binsToRemove = random.nextInt(Math.min(mtParam, solution.size()));
        List<Integer> sticksFromRemoved = new ArrayList<>();
        for (int i = 0; i < binsToRemove; i++) {
            int binIndex = random.nextInt(solution.size());
            Bin removed = solution.remove(binIndex);
            sticksFromRemoved.addAll(removed.getSticks());
        }
        return new Solution(solution, sticksFromRemoved);
    }

}

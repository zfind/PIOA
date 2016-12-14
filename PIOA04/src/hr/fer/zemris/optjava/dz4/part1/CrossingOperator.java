package hr.fer.zemris.optjava.dz4.part1;

import java.util.Random;

/**
 * Created by zac on 02.11.16..
 */
public class CrossingOperator {
    private final Random random;
    private final double alpha;

    public CrossingOperator(Random random, double alpha) {
        this.random = random;
        this.alpha = alpha;
    }

    Solution cross(Solution p1, Solution p2) {
        Solution ch = p1.newLikeThis();

        for (int i = 0; i < ch.values.length; i++) {
            double cmin = Math.min(p1.values[i], p2.values[i]);
            double cmax = Math.max(p1.values[i], p2.values[i]);

            /*  BLX-alpha   */
//            double I = cmax - cmin;
//            double range = cmax - cmin + 2 * alpha * I;
//            ch.values[i] = cmin - I*alpha + random.nextDouble() * range;

            /*  Aritmeticko krizanje */
            double lambda = random.nextDouble();
            ch.values[i] = lambda * cmin + (1 - lambda) * cmax;
        }

        return ch;
    }

}

package test;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class Test1 {

    public static void main(String[] args) {
        IRNG rng = RNG.getRNG();

        for (int i = 0; i < 20; i++) {
            System.out.println(rng.nextInt(-5, 5));
        }
    }

}
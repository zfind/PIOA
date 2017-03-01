package hr.fer.zemris.optjava.rng.rngimpl;

import ec.util.MersenneTwisterFast;
import hr.fer.zemris.optjava.rng.IRNG;


/**
 * Created by zac on 22.01.17..
 */
public class RNGMersenneTwisterImpl implements IRNG {

    private MersenneTwisterFast random;

    public RNGMersenneTwisterImpl() {
        random = new MersenneTwisterFast();
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public double nextDouble(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    @Override
    public float nextFloat() {
        return random.nextFloat();
    }

    @Override
    public float nextFloat(float min, float max) {
        return random.nextFloat() * (max - min) + min;
    }

    @Override
    public int nextInt() {
        return random.nextInt();
    }

    @Override
    public int nextInt(int min, int max) {
//        return (int) (random.nextDouble() * (max - min) + min);
        return random.nextInt(max - min) + min;
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public double nextGaussian() {
        return random.nextGaussian();
    }
}

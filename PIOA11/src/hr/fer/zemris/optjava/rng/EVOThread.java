package hr.fer.zemris.optjava.rng;

import hr.fer.zemris.optjava.rng.rngimpl.RNGMersenneTwisterImpl;

/**
 * Created by zac on 23.01.17..
 */
public class EVOThread extends Thread implements IRNGProvider {

    private IRNG rng = new RNGMersenneTwisterImpl();

    public EVOThread() {
    }

    public EVOThread(Runnable target) {
        super(target);
    }

    public EVOThread(String name) {
        super(name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public EVOThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public IRNG getRNG() {
        return rng;
    }

}

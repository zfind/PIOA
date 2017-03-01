package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;

/**
 * Created by zac on 23.01.17..
 */
public class ThreadBoundRNGProvider implements IRNGProvider {

    @Override
    public IRNG getRNG() {
        IRNGProvider rngProvider = (IRNGProvider) Thread.currentThread();
        return rngProvider.getRNG();
    }

}

package hr.fer.zemris.optjava.rng.provimpl;

import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.IRNGProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

/**
 * Created by zac on 22.01.17..
 */
public class ThreadLocalRNGProvider implements IRNGProvider {

    private ThreadLocal<IRNG> rngProvider;

    public ThreadLocalRNGProvider() {
        rngProvider = new ThreadLocal<>();
    }

    @Override
    public IRNG getRNG() {
        IRNG rng = rngProvider.get();
        if (rng != null) {
            return rng;
        }
        System.out.println("DBG: Stvaram novi generator");
        rng = new RNGRandomImpl();
        rngProvider.set(rng);
        return rng;
    }

}

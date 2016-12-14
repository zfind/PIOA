package hr.fer.zemris.optjava.dz7.net;

import java.util.Iterator;
import java.util.List;

/**
 * Created by zac on 12.12.16..
 */
public class Dataset implements Iterable<Sample> {
    private final List<Sample> samples;

    public Dataset(String filename) {
        this.samples = Parser.parse(filename);
    }

    public int size() {
        return samples.size();
    }

    public int getOutputDimension() {
        return samples.get(0).output.length;
    }

    public Sample getSample(int index) {
        return samples.get(index);
    }

    public double[] getInput(int index) {
        return samples.get(index).input;
    }

    public double[] getOutput(int index) {
        return samples.get(index).output;
    }

    @Override
    public Iterator<Sample> iterator() {
        return samples.iterator();
    }
}

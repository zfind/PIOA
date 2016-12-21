package hr.fer.zemris.optjava.dz8.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zac on 12.12.16..
 */
public class Dataset implements Iterable<Sample> {
    private final List<Sample> samples;

    public Dataset(String filename, int l, int size) {
        samples = new ArrayList<>();

        List<Integer> tmp = Parser.parse(filename);
        int min = Collections.min(tmp);
        int max = Collections.max(tmp);
        int range = max - min;

        double[] normalized = new double[size];
        for (int i = 0; i < size; i++) {
            normalized[i] = -1. + 2. * (tmp.get(i) - min) / range;
        }

        for (int i = 0; i < size - l; i++) {
            double[] input = new double[l];
            for (int j = i, currIndex = 0; j < i + l; j++) {
                input[currIndex++] = normalized[j];
            }
            double[] output = new double[1];
            output[0] = normalized[i + l];
            samples.add(new Sample(input, output));
        }
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

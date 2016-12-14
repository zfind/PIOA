package hr.fer.zemris.trisat;

import java.util.Arrays;
import java.util.Random;

/**
 * Stores bit vectors which are used as assignments
 * @author zac
 */
public class BitVector {
    protected boolean[] arr;

    public BitVector(Random rand, int numberOfBits) {
        arr = new boolean[numberOfBits];
        for (int i=0; i < numberOfBits; i++) {
            arr[i] = rand.nextBoolean();
        }
    }

    public BitVector(boolean ... bits) {
        arr = Arrays.copyOf(bits, bits.length);
    }

    public BitVector(int n) {
        arr = new boolean[n];
    }

    // vraca vrijednost index-te varijable
    public boolean get(int index) {
        return arr[index];
    }

    // vraca broj varijabli koje predstavlja
    public int getSize() {
        return arr.length;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (boolean b : arr) {
            stringBuilder.append(b ? "1" : "0");
        }
        return stringBuilder.toString();
    }

    // vraca promjenjivu kopiju trenutnog rjesenja
    public MutableBitVector copy() {
        return new MutableBitVector(arr);
    }

}
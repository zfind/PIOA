package hr.fer.zemris.trisat;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by zac on 09.10.16..
 * Generator of neighbourhood of given bit vector
 */
public class BitVectorNGenerator implements Iterable<MutableBitVector> {
    private BitVector assignment;

    public BitVectorNGenerator(BitVector assignment) {
        this.assignment = assignment;
    }

    // vraca iterator koji na svaki next() racuna sljedeceg susjeda
    @Override
    public Iterator<MutableBitVector> iterator() {
        return new Iterator<MutableBitVector>() {
            int current = 0;
            @Override
            public boolean hasNext() {
                return assignment.getSize() > current;
            }

            @Override
            public MutableBitVector next() {
                if (hasNext()) {
                    MutableBitVector next = assignment.copy();
                    next.flip(current++);
                    return next;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    // vraca kompletno susjedstvo kao jedno polje
    public MutableBitVector[] createNeighbourhood() {
        MutableBitVector[] mutableBitVectors = new MutableBitVector[assignment.getSize()];
        int i = 0;
        for (MutableBitVector mutableBitVector : this) {
            mutableBitVectors[i++] = mutableBitVector;
        }
        return mutableBitVectors;
    }

}

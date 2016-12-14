package hr.fer.zemris.trisat;

/**
 * Created by zac on 09.10.16..
 * Stores bit vector which can be changed
 */
public class MutableBitVector extends BitVector {

    public MutableBitVector(boolean ... bits) {
        super(bits);
    }

    public MutableBitVector(int n) {
        super(n);
    }

    // zapisuje predanu vrijednost u zadanu varijablu
    public void set(int index, boolean value) {
        arr[index] = value;
    }

    // invertira i-ti bit
    public void flip(int index) {
        arr[index] ^= true;
    }

}

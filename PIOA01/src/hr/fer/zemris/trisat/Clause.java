package hr.fer.zemris.trisat;

import java.util.Arrays;

/**
 * Created by zac on 09.10.16..
 * Stores formula clauses in conjunctive normal form
 */
public class Clause {
    private int[] indexes;

    public Clause(int[] indexes) {
        this.indexes = Arrays.copyOf(indexes, indexes.length);
    }

    // vraca broj literala koji cine klauzulu
    public int getSize() {
        return indexes.length;
    }

    // vraca indeks varijable koja je index-ti clan ove klauzule
    public int getLiteral(int index) {
        return indexes[index];
    }

    // vraca true ako predana dodjela zadovoljava ovu klauzulu
    public boolean isSatisfied(BitVector assignment) {
        boolean result = false;
        for (int index : indexes) {
            if (index >= 0){
                result = result || assignment.get(Math.abs(index) -1);
            } else {
                result = result || !assignment.get(Math.abs(index) -1);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index : indexes) {
            if (index < 0) {
                stringBuilder.append("~");
            }
            stringBuilder.append("x");
            stringBuilder.append(Math.abs(index));
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}

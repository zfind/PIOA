package hr.fer.zemris.trisat;

/**
 * Created by zac on 09.10.16..
 * Class for storing formula, provides clauses
 */
public class SATFormula {
    private Clause[] clauses;
    private int numberOfVariables;

    public SATFormula(int numberOfVariables, Clause[] clauses) {
        this.numberOfVariables = numberOfVariables;
        this.clauses = clauses;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfClauses() {
        return clauses.length;
    }

    public Clause getClause(int index) {
        return clauses[index];
    }

    public boolean isSatisfied(BitVector assignment) {
        boolean result = true;
        for (Clause clause : clauses) {
            result = result && clause.isSatisfied(assignment);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Clause clause : clauses) {
            stringBuilder.append("(");
            stringBuilder.append(clause.toString());
            stringBuilder.append(")\n");
        }
        return stringBuilder.toString();
    }

}

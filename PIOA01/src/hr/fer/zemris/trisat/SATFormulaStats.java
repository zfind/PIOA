package hr.fer.zemris.trisat;

/**
 * Created by zac on 09.10.16..
 */
public class SATFormulaStats {
    private static final double percentageConstantUp = 0.01;
    private static final double percentageConstanrDown = 0.1;
    private static final double percentageUnitAmount = 50;
    private int numberOfSatisfied = 0;
    private boolean isSatisfied = false;
    private double percentageBonus = 0.;
    private double[] post;
    private SATFormula formula;

    public SATFormulaStats(SATFormula formula) {
        this.formula = formula;
        this.post = new double[formula.getNumberOfClauses()];
    }

    // analizira se predano rjesenje i pamte svi relevantni pokazatelji
    public void setAssignment(BitVector assignment, boolean updatePercentages) {
        numberOfSatisfied = 0;
        isSatisfied = false;
        percentageBonus = 0.;

        int numberOfClauses = formula.getNumberOfClauses();
        for (int i =0; i < numberOfClauses; i++) {
            Clause clause = formula.getClause(i);
            boolean clauseSatisfied = clause.isSatisfied(assignment);
            if (clauseSatisfied) {
                numberOfSatisfied++;
            }
            if (updatePercentages) {
                if (clauseSatisfied) {
                    post[i] += (1. - post[i]) * percentageConstantUp;
                } else {
                    post[i] += (0. - post[i]) * percentageConstanrDown;
                }
            }
        }

        if (numberOfSatisfied == numberOfClauses) {
            isSatisfied = true;
        }

        for (int i=0; i< numberOfClauses; i++) {
            Clause clause = formula.getClause(i);
            if (clause.isSatisfied(assignment)) {
                percentageBonus += percentageUnitAmount * (1. - post[i]);
            } else {
                percentageBonus -= percentageUnitAmount * (1. - post[i]);
            }
        }
    }

    // vraca temeljem onoga sto je setAssignment zapamtio: broj klauzula koje su zadovoljene
    public int getNumberOfSatisfied() {
        return numberOfSatisfied;
    }

    // vraca temeljem onoga sto je setAssignment zapamtio
    public boolean isSatisfied() {
        return isSatisfied;
    }

    // vraca temeljem onoga sto je setAssignment zapamtio: suma korekcija klauzula
    public double getPercentageBonus() {
        return percentageBonus;
    }

    // vraca temeljem onoga sto je setAssignment zapamtio: procjena postotka za klauzulu
    public double getPercentage(int index) {
        return post[index];
    }

}

package hr.fer.zemris.trisat;

/**
 * Created by zac on 09.10.16..
 * Algorithm interface
 */
public interface Algorithm {

	/**
	 * Solves given 3-SAT problem.
	 * Results are printed to stdout
	 * @param formula given 3-SAT formula
	 */
    public void solve(SATFormula formula);
}

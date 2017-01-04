package hr.fer.zemris.optjava.dz9;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zac on 02.01.17..
 */
public class Solution {

    public double[] x;
    public double[] z;
    public double fitness;
    public List<Solution> S;
    public int eta;

    public Solution(double[] x, double[] z) {
        this.x = x;
        this.z = z;
        this.S = new ArrayList<Solution>();
        this.eta = 0;
    }

}
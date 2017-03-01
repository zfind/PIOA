package hr.fer.zemris.optjava.dz10;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zac on 02.01.17..
 */
public class Solution {

    public final double[] x;
    public final double[] z;
    public int front;
    public double crowdDistance;
    public List<Solution> S;
    public int eta;

    public Solution(double[] x, double[] z) {
        this.x = x;
        this.z = z;
        this.S = new ArrayList<>();
        this.eta = 0;
    }

}
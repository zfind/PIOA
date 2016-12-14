package hr.fer.zemris.optjava.dz3;

/**
 * Created by zac on 23.10.16..
 */
public interface INeighborhood<T extends SingleObjectiveSolution> {

    T randomNeighbor(T t);

}

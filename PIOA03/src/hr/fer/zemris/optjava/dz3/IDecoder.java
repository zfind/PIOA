package hr.fer.zemris.optjava.dz3;

import java.text.DecimalFormat;

/**
 * Created by zac on 23.10.16..
 */
public interface IDecoder<T extends SingleObjectiveSolution> {

    double[] decode(T t);

    void decode(T t, double[] doubles);

    default String toString(T object) {
        double[] decoded = decode(object);

        DecimalFormat df = new DecimalFormat("#.##");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');

        int last = decoded.length-1;
        for (int i = 0; i<last; i++) {
            stringBuilder.append(df.format(decoded[i]));
            stringBuilder.append(" ");
        }
        stringBuilder.append(df.format(decoded[last]));
        stringBuilder.append(')');

        return stringBuilder.toString();
    }

}

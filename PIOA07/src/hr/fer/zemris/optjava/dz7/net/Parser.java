package hr.fer.zemris.optjava.dz7.net;

/**
 * Created by zac on 12.12.16..
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Parser {

    public static List<Sample> parse(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;

        List<Sample> dataset = new ArrayList<>();

        try {

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                //System.out.println(line);
                if (line.startsWith("(")) {
                    String[] sample = line.split(":");

                    // input
                    String tmp = sample[0].substring(1, sample[0].length() - 1);
                    String[] xx = tmp.split(",");
                    double[] input = new double[xx.length];
                    for (int i = 0; i < xx.length; i++) {
                        input[i] = Double.parseDouble(xx[i]);
                    }

                    // output
                    tmp = sample[1].substring(1, sample[1].length() - 1);
                    xx = tmp.split(",");
                    double[] output = new double[xx.length];
                    for (int i = 0; i < xx.length; i++) {
                        output[i] = Double.parseDouble(xx[i]);
                    }

                    dataset.add(new Sample(input, output));
                } else {
                    continue;
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    public static void main(String[] args) {
        List<Sample> dataset = Parser.parse("data/07-iris-formatirano.data");
        for (int i = 0; i < 10; i++) {
            System.out.println(dataset.get(i).toString());
        }
    }

}
package hr.fer.zemris.optjava.dz8.net;

/**
 * Created by zac on 12.12.16..
 */

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Parser {

    public static List<Integer> parse(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Integer> dataset = new ArrayList<>();

        try {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                //System.out.println(line);
                if (!line.isEmpty()) {
                    int input = Integer.parseInt(line);
                    dataset.add(input);
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;
    }

    public static void main(String[] args) {
        Dataset dataset = new Dataset("data/08-Laser-generated-data.txt", 3, 10);
        for (Sample sample : dataset) {
            System.out.println(sample.toString());
        }
    }

}
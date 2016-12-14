package hr.fer.zemris.optjava.dz5.part2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zac on 14.11.16..
 */
public class Parser {

    public static double[][][] parse(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;

        int N = 0;
        List<String[]> distanceLines = new ArrayList<>();
        List<String[]> costLines = new ArrayList<>();

        try {

            line = reader.readLine();
            line = line.trim();
            N = Integer.parseInt(line);
            line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                //System.out.println(line);
                if (line.equals("")) {
                    break; //empty
                } else {
                    String[] xx = line.split("( )+");
                    distanceLines.add(xx);
                }
            }

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) {
                    break;
                } else {
                    String[] xx = line.split("( )+");
                    costLines.add(xx);
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        double[][] distanceLookup = new double[N][N];
        double[][] costLookup = new double[N][N];

        int currentRow = 0;
        for (String[] xx : distanceLines) {
            for (int i = 0; i < N; i++) {
                distanceLookup[currentRow][i] = Integer.parseInt(xx[i]);
            }
            currentRow++;
        }

        currentRow = 0;
        for (String[] xx : costLines) {
            for (int i = 0; i < N; i++) {
                costLookup[currentRow][i] = Integer.parseInt(xx[i]);
            }
            currentRow++;
        }

        return new double[][][]{distanceLookup, costLookup};
    }

}
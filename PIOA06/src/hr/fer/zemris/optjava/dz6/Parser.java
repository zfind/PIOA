package hr.fer.zemris.optjava.dz6;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Parser {

    public static double[][] parse(String path) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;

        List<String[]> distanceLines = new ArrayList<>();

        try {
            boolean coordinates = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                //System.out.println(line);
                if (line.startsWith("NODE_COORD_SECTION") || line.startsWith("DISPLAY_DATA_SECTION") || line.startsWith("EOF")) {
                    coordinates = true;
                } else if (coordinates) {
                    String[] xx = line.split("( )+");
                    distanceLines.add(xx);
                } else {
                    continue;
                }
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        int N = distanceLines.size();

        City[] cities = new City[N];
        for (int i = 0; i < N; i++) {
            cities[i] = new City(Double.parseDouble(distanceLines.get(i)[1]),
                    Double.parseDouble(distanceLines.get(i)[2]));
        }

        double[][] distances = new double[N][N];

        for (int i = 0; i < N; i++) {
            City a = cities[i];
            distances[i][i] = 0;
            for (int j = i + 1; j < N; j++) {
                City b = cities[j];
                double dist = Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
                distances[i][j] = dist;
                distances[j][i] = dist;
            }
        }

        return distances;
    }

    public static void main(String[] args) {
        double[][] distances = Parser.parse("data/bayg29.tsp");
        DecimalFormat df = new DecimalFormat("#.##");
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances.length; j++) {
                System.out.print(df.format(distances[i][j]) + " ");
            }
            System.out.println();
        }
    }

}
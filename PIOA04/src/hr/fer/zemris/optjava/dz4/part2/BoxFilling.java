package hr.fer.zemris.optjava.dz4.part2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by zac on 05.11.16..
 */
public class BoxFilling {

    private ISelection selectionBest;
    private ISelection selectionWorst;
    private CrossingOperator crOp;
    private MutationOperator mtOp;
    private int maxIter;
    private Population population;
    private int epsilon;
    private boolean p;

    public BoxFilling(ISelection selectionBest, ISelection selectionWorst,
                      CrossingOperator crOp, MutationOperator mtOp,
                      int maxIter, Population population, int epsilon, boolean p) {
        this.selectionBest = selectionBest;
        this.selectionWorst = selectionWorst;
        this.crOp = crOp;
        this.mtOp = mtOp;
        this.maxIter = maxIter;
        this.population = population;
        this.epsilon = epsilon;
        this.p = p;
    }

    public static void main(String[] args) {
        if (!(args.length == 10)) {
            System.out.println("Error. Krivi argumenti");
            System.out.println("potrebno: datoteka vel_populacije n m p max_iteracija uvjet_zaustavljanja max_visina param_krizanja param_mutacije ");
            System.out.println("primjer: kutije/problem-20-50-5.dat 50 3 3 true 100 50 20 0.3 5");
            System.exit(-1);
        }
        String path = args[0];
        Integer[] tmp = Parser.parse(path);
        List<Integer> sticks = new ArrayList<>(Arrays.asList(tmp));
        int populationSize = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int m = Integer.parseInt(args[3]);
        boolean p = Boolean.parseBoolean(args[4]);
        int maxIter = Integer.parseInt(args[5]);
        int epsilon = Integer.parseInt(args[6]);
        int maxHeight = Integer.parseInt(args[7]);
        double crossingParam = Double.parseDouble(args[8]);
        int mutationParam = Integer.parseInt(args[9]);

        Random random = new Random();

        Population population = new Population(sticks, populationSize, maxHeight, random);
        ISelection selectionN = new Tournament(n, false, random);
        ISelection selectionM = new Tournament(m, true, random);
        CrossingOperator crOp = new CrossingOperator(crossingParam, random);
        MutationOperator mtOp = new MutationOperator(mutationParam, random);

        BoxFilling algorithm = new BoxFilling(selectionN, selectionM,
                crOp, mtOp, maxIter, population, epsilon, p);

        Solution solution = algorithm.run();

        System.out.println(solution.toString());

        System.out.println("Velicina: " + solution.size());

    }


    public Solution run() {
        Solution best = population.getBest();
        int bestSize = best.size();
        System.out.println("0:\t" + bestSize);
        for (int i = 1; i <= maxIter; i++) {
            Solution current = population.getBest();
            int currentSize = current.size();
            if (currentSize < bestSize) {
                bestSize = currentSize;
                System.out.println(i + ":\t" + bestSize);
            }
            if (currentSize <= epsilon) {
                System.out.println("\nUvjet zadovoljen.\nPronadeno rjesenje: ");
                return current;
            }
            Solution p1 = selectionBest.select(population);
            Solution p2 = selectionBest.select(population);
            Solution ch = crOp.cross(p1, p2);
            ch = mtOp.mutate(ch);
            Solution bad = selectionWorst.select(population);
            if (p) {
                if (ch.getFitness() > bad.getFitness()) {
                    bad.swapWith(ch);
                }
            } else {
                bad.swapWith(ch);
            }
        }
        System.out.println("\nMaksimalni broj iteracija.\nPronadeno rjesenje: ");
        return population.getBest();
    }


    private static class Parser {
        static Integer[] parse(String path) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String line;
            Integer[] array = null;

            try {
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    //System.out.println(line);
                    if (line.startsWith("#")) {
                        continue; //comment
                    } else if (line.startsWith("[")) {
                        line = line.substring(1, line.length() - 1);
                        String[] tmp = line.split(",( )+");
                        array = new Integer[tmp.length];
                        for (int i = 0; i < array.length; i++) {
                            array[i] = Integer.parseInt(tmp[i]);
                        }
                    } else {
                        System.out.println("Unknown content. " + line);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (array == null) {
                System.out.println("Greska u ulaznoj datoteci");
                System.exit(-1);
            }
            return array;
        }
    }
}

package hr.fer.zemris.trisat;

import java.io.*;

/**
 * Created by zac on 10.10.16..
 * Parser of formula file
 */
public class Parser {

	/**
	 * Parses formula file
	 * @param path path to file
	 * @return formula from file
	 */
    public static SATFormula parseFormula(String path) {
        int numberOfVariables = 0;
        int numberOfClauses = 0;
        Clause[] clauses = null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line;
        int currentClause = 0;
        try {
            while (!((line = reader.readLine().trim()).startsWith("%"))) {
                //System.out.println(line);
                if (line.startsWith("c")) {
                    continue; //comment
                } else if (line.startsWith("p")) {
                    String[] xx = line.split("( )+");
                    numberOfVariables = Integer.parseInt(xx[2]);
                    numberOfClauses = Integer.parseInt(xx[3]);
                    clauses = new Clause[numberOfClauses];
                } else {
                    String[] xx = line.split("( )+");
                    int[] indexes = new int[xx.length-1]; // last is 0
                    for (int i=0; i< indexes.length; i++) {
                        indexes[i] = Integer.parseInt(xx[i]);
                    }
                    Clause clause = new Clause(indexes);
                    clauses[currentClause++] = clause;
                }
            }

            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return new SATFormula(numberOfVariables, clauses);

    }

    public static void main(String[] args) {
        String path = "/home/zac/Projekti/IdeaProjects/PIOA/PIOA1/files/uf50-01000.cnf";
        SATFormula satFormula = Parser.parseFormula(path);

        Algorithm alg = new Algorithm3();
        alg.solve(satFormula);
    }

}

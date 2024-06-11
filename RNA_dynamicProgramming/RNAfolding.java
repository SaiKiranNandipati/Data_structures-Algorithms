
/**
 * created on October 25th, 2023
 * implemented by Sai Kiran Nandipati
 
 */

/**
 * Copyright SAI KIRAN NANDIPATI October 25th, 2023. 
 *
 * This code is the property of Sai Kiran Nandipati. No part of this code
 * may be copied, modified, or distributed without the express written permission
 * of the copyright owner.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * RNAFolding class implements an algorithm for finding the optimal secondary
 * structure of RNA sequences using dynamic programming.
 */
public class RNAfolding {
	private static Integer[][][] dpArray = null;
	private static List<List<Integer>> basePairs = null;

	/**
	 * Creates a list of base pairs based on the calculated dynamic programming
	 * array.
	 *
	 * @param i The starting index.
	 * @param j The ending index.
	 */

	private static void createBasePairs(int i, int j) {
		if (i >= 0 && j >= 0) {
			Integer t = dpArray[i][j][1];
			if (t != null) {
				if (t == j - 1) {
					createBasePairs(i, j - 1);
				} else {
					// t and j are 2 pairs
					List<Integer> pair = new ArrayList<>(List.of(t, j));
					basePairs.add(pair);
					createBasePairs(i, t - 1);
					createBasePairs(t + 1, j - 1);
				}
			}
		}
	}

	/**
	 * Folds an RNA sequence to find the optimal secondary structure and prints the
	 * results.
	 *
	 * @param RNA The RNA sequence to fold.
	 */
	public static void foldRNA(String RNA) {
		int length = RNA.length();
		// save the value and previuos iteration point for printing result
		dpArray = new Integer[length][length][2];
		// Will store the result here
		basePairs = new ArrayList<>();

		// Initialize 0 for small substrings
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (i >= j - 4)
					dpArray[i][j][0] = 0;
			}
		}

		for (int k = 5; k < length; k++) {
			for (int i = 0; i < length - k; i++) {

				int j = i + k;
				dpArray[i][j][0] = 0;
				dpArray[i][j][1] = null;

				// Default value is opt(i, j-1)
				if (dpArray[i][j - 1][0] != 0) {
					dpArray[i][j][0] = dpArray[i][j - 1][0];
					dpArray[i][j][1] = j - 1;
				}

				// find maximum value between all t's
				for (int t = i; t < j - 4; t++) {
					// base condition ii

					String pairString = String.format("%c%c", RNA.charAt(t), RNA.charAt(j));

					if (pairString.equals("AU") || pairString.equals("UA") || pairString.equals("CG")
							|| pairString.equals("GC")) {
						int tFolding = 1;
						if (t - 1 >= 0 && dpArray[i][t - 1][0] != null)
							tFolding += dpArray[i][t - 1][0];
						if (t + 1 < length && dpArray[t + 1][j - 1][0] != null)
							tFolding += dpArray[t + 1][j - 1][0];
						if (dpArray[i][j][0] < tFolding) {
							dpArray[i][j][0] = tFolding;
							dpArray[i][j][1] = t;
						}

					}
				}

			}
		}

		// print results
		createBasePairs(0, length - 1);
		Collections.sort(basePairs, (list1, list2) -> list1.get(0).compareTo(list2.get(0)));

		for (List<Integer> pair : basePairs) {
			System.out.println(RNA.charAt(pair.get(0)) + "-" + RNA.charAt(pair.get(1)) + " (" + (pair.get(0) + 1) + ","
					+ (pair.get(1) + 1) + ")");
		}
		System.out.println("Total number of base pairs:" + basePairs.size());

	}

	/**
	 * The main method for reading RNA sequences from a file and finding their
	 * optimal
	 * secondary structures.
	 *
	 * @param args Command-line arguments, where the first argument is the filename.
	 * @throws IOException if there is an error reading the input file.
	 */

	public static void main(String[] args) throws IOException {
		String fileName = args[0];
		System.out.println("Folding RNA in file " + fileName);
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		String rnaName = "";
		while ((line = br.readLine()) != null) {
			if (line.startsWith("**"))
				rnaName = line.substring(2, line.length() - 1);
			if (line.length() > 0 && !line.startsWith("**")) {
				System.out.println("\n** " + rnaName + ", length=" + line.length() + ", Optimal secondary structure:");
				foldRNA(line);
			}
		}

		System.out.println("Assignment 5 by SAI KIRAN NANDIPATI");
	}
}

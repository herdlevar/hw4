/* 
 * Jared Herdlevar, Demy Loulias, Gabrielle Glynn
 * TCSS 343 Project/HW4: Trading Post Problem
 * Fall 2015: Due December 8, 2015
 * 
 * This program implements brute force, divide and conquer,
 * and dynamic programming to find the cheapest solution as
 * defined by the problem statement. 
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.HashSet;

public class tcss343 {
	
	static int[] sequenceDC;	
	static int[] sequenceDP;
	static Set<Integer> sequenceBrute;
	static int[] sequenceFake;
	static int[] vDC;
	static List<ArrayList<Integer>> posts;
	static int n;
	static int brute;

	public static void main(String[] args) throws IOException {
		// createTestFiles();
		
		// Read the input file
		Scanner scan = new Scanner(System.in);
		Scanner scan2 = new Scanner("");
		posts  = new ArrayList<ArrayList<Integer>>();
		int offset = 0;
		while (scan.hasNextLine()) { // read each line
			scan2 = new Scanner(scan.nextLine());
			posts.add(new ArrayList<Integer>());
			for (int j = 0; j < offset; j++) {
				scan2.next();
				posts.get(offset).add(null);
			}
			while (scan2.hasNextInt()) {
				posts.get(offset).add(scan2.nextInt());
			}
			offset++;
		}
		scan.close();
		scan2.close();
		
		n = posts.get(0).size(); // find "n"		
		// printSets(bruteIndex); // uncomment to check the indexes for brute force
		
		// initialize the sequence to recover path taken for each method
		sequenceDP = new int[n];
		sequenceDC = new int[n];
		sequenceFake = new int[n];
		vDC = new int[n]; // used for divide and conquer with memory
		
		// run all the tests
		dynamicProgramming();		
		System.out.println("Brute:");
		System.out.println(brute(powerSet(n)));	
		for (Integer i : sequenceBrute) {
			System.out.print((i+1) + " ");
		}
		System.out.println("\n\nDivide and Conquer:");
		System.out.println(divideConquer(n));
		sequence(sequenceDC);
		System.out.println("\nDynamic Programming:");
		System.out.println(dynamicProgramming());
		sequence(sequenceDP);
		System.out.println("\nDivide and Conquer with \"memory\":");
		divideConquerFake(n);
		System.out.println(vDC[n-1]);	
		sequence(sequenceFake);
	}

	// This function solves the problem using dynamic programming	
	static int dynamicProgramming() {
		int[] v = new int[n]; 		// this array holds the minimum value for each "n"
		v[0] = 0; 			// costs 0 to go nowhere
		v[1] = posts.get(0).get(1);     // only 1 cost to go from post 1 to 2
		sequenceDP[0] = -1;  		// represents where you came from
		sequenceDP[1] = 0;		// came from post 1. (using 0 for indexing purposes)
		for (int i = 2; i < n; i++) { 	// Start looking at post 3
			v[i] = posts.get(0).get(i); 	// get the cost to get here from post 1
			for (int j = 1; j < i; j++) {   // Now check all other cominations
				v[i] = Math.min(v[i], posts.get(j).get(i) + v[j]);   // store the min cost to get here
				if (v[i] == posts.get(j).get(i) + v[j]) { 		// if min changes update the sequence for how
					sequenceDP[i] = j;				// you got here
				}
			}
		}
		return v[n-1]; // return the min value to get to the end
	} 
	
	// this function solves the problem using divide and conquer
	static int divideConquer(int n) {
		if (n == 1) { // terminating case
			sequenceDC[0] = -1; // where you came from (-1 to represent you are at the start)
			return 0; 		// return 0 cause it costs nothing to get to the start
		}
		int v = posts.get(0).get(n-1); // see how much it costs to get to post n from the start
		int temp = v;			// used to see if better solution found
		for (int i = 1; i < n-1; i++) { // check all other cases
			v = Math.min(v, posts.get(i).get(n-1) + divideConquer(i+1)); // take minimun path
			if (temp != v) sequenceDC[n-1] = i; // if solution updated, update the sequence for 
			temp = v;				// how you got here
		}		
		return v; // return the minimum
	}
	
	// This solves the problem using divide and conquer but remembers the solution to the overlapping sub problems
	static void divideConquerFake(int n) {
		if (n == 2) {	// here it stops when n = 2 cause why not
			sequenceFake[0] = -1; // where you came from to get to the start
			sequenceFake[1] = 0;  // where you came from to get to post 2
			vDC[0] = 0;		// the cost to get to the start
			vDC[1] = posts.get(0).get(1); // the cost to get to the second post
			return; // here the sub problem solutions are stored in array vDC
		}
		divideConquerFake(n-1); // call the smaller sub problem
		vDC[n-1] = posts.get(0).get(n-1); // get cost to get to "n" from the start
		sequenceFake[n-1] = 0;
		int temp = vDC[n-1];
		for (int i = 1; i < n; i++) { // check all other paths
			vDC[n-1] = Math.min(vDC[n-1], posts.get(i).get(n-1) + vDC[i]);
			if (temp != vDC[n-1]) sequenceFake[n-1] = i;
			temp = vDC[n-1];
		}
		return;
	}
	
	// This function brute forces the solution but only considers the set of posts that include the start and finish post.
	static int brute(Set<Set<Integer>> i) {
		int v = Integer.MAX_VALUE; // max this value for first call to Math.min
		int last = 0;
		int temp = 0;
		for (Set<Integer> set : i) { // look through each sub set 
			for (Integer post : set) { // go through all posts of the set
				temp += posts.get(last).get(post);
				last = post;		
			}
			v = Math.min(v, temp);
			if (temp == v) sequenceBrute = set;
			temp = 0;
			last = 0;
		}
		return v;
	}
	
	// This function prints the order taken to get to the end
	static void sequence(int[] seq) {
		List<Integer> solution = new ArrayList<Integer>();
		solution.add(0, n);
		for (int i = n - 1; i > 0; i = seq[i]) {
			solution.add(0,seq[i] + 1);
		}
		Iterator<Integer> it = solution.iterator();
		while (it.hasNext()) {
			System.out.print(it.next() + " ");
		}
		System.out.println();
	}
	
	// This method generates the random test files.
	static void createTestFiles() throws IOException {
		int[] testN = {100, 200, 400, 600, 800};
		int offset = 0;
		PrintWriter out;
		Random rand = new Random();
		for (int i = 0; i < testN.length; i++) {
			offset = 0;
			out = new PrintWriter(new FileWriter("test" + testN[i] + ".txt"));
			for (int m = 0; m < testN[i]; m++) {
				for (int k = 0; k < offset; k++) {
					// printf the NAs here
					out.print("NA\t");
				}
				out.print("0\t");
				for (int j = offset + 1; j < testN[i]; j++) {
					out.print(Math.abs(rand.nextInt() % 100) + 1 + "\t");
				}
				out.println();
				offset++;
			}
			out.close();
		}
	}

	// This creates the set of sets for the brute force method to check
	static Set<Set<Integer>> powerSet(int n) {
		SortedSet<Integer> stops = new TreeSet<Integer>();
		for (int i = 1; i < n-1; i++) {
			stops.add(i);
		}
		Set<Set<Integer>> index = makeSet(stops);
		for (Set<Integer> sets : index) {
			sets.add(0);
			sets.add(n-1);
		}
		return index;
	}
	
	// helper function for the creating of the sets
	static Set<Set<Integer>> makeSet(Set<Integer> theSet) {
		Set<Set<Integer>> sets = new HashSet<Set<Integer>>();
		if (theSet.isEmpty()) {
			sets.add(new TreeSet<Integer>());
			return sets;
		}
		List<Integer> list = new ArrayList<Integer>(theSet);
		Integer first = list.get(0);
		Set<Integer> rest = new TreeSet<Integer>(list.subList(1, list.size()));
		for (Set<Integer> set : makeSet(rest)) {
			Set<Integer> newSet = new TreeSet<Integer>();
			newSet.add(first);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	// this method is used to test the set making methods.
	static void printSets(Set<Set<Integer>> theSets) {
		for (Set<Integer> set : theSets) {
			for (Integer i : set) {
				System.out.print(i + " ");	
			}
			System.out.println();
		}
	}
}

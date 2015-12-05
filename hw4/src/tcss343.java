

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
		createTestFiles();
		brute = 0;
		Scanner scan = new Scanner(System.in);
		Scanner scan2 = new Scanner("");
		posts  = new ArrayList<ArrayList<Integer>>();
		int offset = 0;
		while (scan.hasNextLine()) {
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
		
		n = posts.get(0).size();		
		Set<Set<Integer>> bruteIndex = powerSet(n);
		printSets(bruteIndex);
		sequenceDP = new int[n];
		sequenceDC = new int[n];
		sequenceFake = new int[n];
		vDC = new int[n];
		
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
	
	static int dynamicProgramming() {
		int[] v = new int[n];
		v[0] = 0;
		v[1] = posts.get(0).get(1);
		sequenceDP[0] = -1;
		sequenceDP[1] = 0;
		for (int i = 2; i < n; i++) {
			v[i] = posts.get(0).get(i);
			for (int j = 1; j < i; j++) {
				v[i] = Math.min(v[i], posts.get(j).get(i) + v[j]);
				if (v[i] == posts.get(j).get(i) + v[j]) {
					sequenceDP[i] = j;
				}
			}
		}
		return v[n-1];
	} 
	
	static int divideConquer(int n) {
		if (n == 1) {
			sequenceDC[0] = -1;
			return 0;
		}
		int v = posts.get(0).get(n-1);
		int temp = v;
		for (int i = 1; i < n-1; i++) {
			v = Math.min(v, posts.get(i).get(n-1) + divideConquer(i+1));
			if (temp != v) sequenceDC[n-1] = i;
			temp = v;
		}		
		return v;
	}
	
	static void divideConquerFake(int n) {
		if (n == 2) {
			sequenceFake[0] = -1;
			sequenceFake[1] = 0;
			vDC[0] = 0;
			vDC[1] = posts.get(0).get(1);
			return;
		}
		divideConquerFake(n-1);
		vDC[n-1] = posts.get(0).get(n-1);
		sequenceFake[n-1] = 0;
		int temp = vDC[n-1];
		for (int i = 1; i < n; i++) {
			vDC[n-1] = Math.min(vDC[n-1], posts.get(i).get(n-1) + vDC[i]);
			if (temp != vDC[n-1]) sequenceFake[n-1] = i;
			temp = vDC[n-1];
		}
		return;
	}
	
	static int brute(Set<Set<Integer>> i) {
		int v = Integer.MAX_VALUE;
		int last = 0;
		int temp = 0;
		for (Set<Integer> set : i) {
			for (Integer post : set) {
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

	static void printSets(Set<Set<Integer>> theSets) {
		for (Set<Integer> set : theSets) {
			for (Integer i : set) {
				System.out.print(i + " ");	
			}
			System.out.println();
		}
	}
}

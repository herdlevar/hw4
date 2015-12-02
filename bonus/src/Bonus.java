import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Bonus {
	
	static int n;
	static int m;
	static List<int[]> stones;
	static boolean[][] field;

	public static void main(String[] args) {
		Scanner scan = null;
		stones = new ArrayList<int[]>();
		for (int i = 0; i < args.length; i++) {
			scan = new Scanner(args[i]);
			if (i == 0) {
				n = scan.nextInt();
			} else if (i == 1) {
				m = scan.nextInt();
			} else {
				stones.add(0, new int[2]);
				stones.get(0)[0] = scan.nextInt();
				i++;
				scan.close();
				scan = new Scanner(args[i]);
				stones.get(0)[1] = scan.nextInt();
			}
		}
		scan.close();
		field = createField();
		printField();
		int[] temp = bruteForce(field);
		System.out.println(temp[2]);
		System.out.println(temp[0] + ", " + temp[1]);
	}
	
	static boolean[][] createField() {
		boolean[][] field = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				field[i][j] = false;
			}
		}
		Iterator<int[]> it = stones.iterator();
		int[] temp;
		while (it.hasNext()) {
			temp = it.next();
			field[temp[0]][temp[1]] = true;
		}
		return field;
	}

	static void printField() {
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (field[i][j]) {
					System.out.print("X ");
				} else {
					System.out.print("O ");
				}
			}
			System.out.println();
		}
	}
	
	static public int[] bruteForce(boolean A[][]) {
		int topCornerAndSize[] = {0,0,0};

		for(int x = 0; x < n; x++)
		{
			for(int y = 0; y < n; y++)
			{
				for(int testSize = 0; testSize < Math.min(n - x, n - y); testSize++)
				{
					if(canDrawSquare(A, x, y, testSize) && testSize + 1 > topCornerAndSize[2])
					{
						topCornerAndSize[0] = x;
						topCornerAndSize[1] = y;
						topCornerAndSize[2] = testSize + 1;
					} else {
						break;
					}
				}
			}
		}

		return topCornerAndSize;
	}
	
	static public boolean canDrawSquare(boolean A[][], int x, int y, int s) {
		for(int i = x; i < (s + x); i++)
		{
			if(A[i][y+s]) {
				return false;
			}
		}
		
		for(int j = y; j < (s + j)-1; j++)
		{
			if(A[x+s][j]) {
				return false;
			}
		}
	
		return true;
	}
	
}

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class challenge {
	
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
		int topCornerAndSize[] = new int[3];
		int arraySize = A[0].length;

		for(int x = 0; x < arraySize; x++)
		{
			for(int y = 0; y < arraySize; y++)
			{
				for(int testSize = 1; testSize <= Math.min(arraySize - x, arraySize - y); testSize++)
				{
					if(canDrawSquare(A, x, y, testSize))
					{
						if(testSize > topCornerAndSize[2])
						{
							topCornerAndSize[0] = x;
							topCornerAndSize[1] = y;
							topCornerAndSize[2] = testSize;
						}
					}
					else
					{
						break;
					}
				
				}
			}
		}

		return topCornerAndSize;
	}
	
	static public boolean canDrawSquare(boolean A[][], int x, int y, int s) {
		if(A[x][y] == true)
			return false;
		for(int i = x; i < (s + x); i++)
		{
			for(int j = y; j < (s + y); j++)
			{
				if(A[i][j] == true)
					return false;
			}
		}
	
		return true;
	}
	
}
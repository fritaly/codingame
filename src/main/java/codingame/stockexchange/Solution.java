package codingame.stockexchange;

import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        in.nextLine();
        
        int[] values = new int[n];
        int[] maximums = new int[n];
        int[] minimums = new int[n];
        
        int i = 0;
        
        int max = Integer.MIN_VALUE;
        
        for (String value : in.nextLine().split(" ")) {
			values[i] = Integer.parseInt(value);

			if (i > 0) {
				max = Math.max(max, values[i - 1]);
			}
			
			maximums[i++] = max;
		}
        
        int min = Integer.MAX_VALUE;
        
        for (int j = values.length - 1; j >= 0; j--) {
        	minimums[j] = Math.min(min, values[j]);
		}
        
        int result = Integer.MAX_VALUE;
        
        for (int j = 1; j < minimums.length - 1; j++) {
			result = Math.min(result, minimums[j] - maximums[j]);
		}
        
        if (result > 0) {
        	result = 0;
        }
        
        System.out.println(result);
    }
}
package codingame.horseracing;

import java.util.Arrays;
import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        
        int[] array = new int[n];
        
        for (int i = 0; i < n; i++) {
            array[i] = in.nextInt();
        }
        
        Arrays.sort(array);

        int min = Integer.MAX_VALUE;
        
        for (int i = 0; i < array.length - 1; i++) {
			min = Math.min(min, array[i+1] - array[i]);
		}
        
        System.out.println(min);
    }
}
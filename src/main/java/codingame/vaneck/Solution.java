package codingame.vaneck;

import java.util.Arrays;
import java.util.Scanner;

/**
 * <p>See <a href="https://www.codingame.com/ide/puzzle/van-ecks-sequence">Van Eck's sequence</a>.</p>
 */
public class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int A1 = in.nextInt();
        int N = in.nextInt();

        int[] array = new int[1_000_000];

        Arrays.fill(array, 0);

        int current = A1;

        for (int i=2; i<=N ; i++) {
            int lastSeenAt = array[current];

            array[current] = i-1;

            if (lastSeenAt == 0) {
                // Never seen this term before
                current = 0;
            } else {
                current = i - lastSeenAt - 1;
            }

            // System.out.println("Term " + i + ": " + current);
        }

        System.out.println(current);
    }
}

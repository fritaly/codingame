/**
 * Copyright 2021, Francois Ritaly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codingame.sum_of_spirals_diagonals;

import java.util.*;

class Solution {

    public static void main(String args[]) {
        int n = new Scanner(System.in).nextInt();
        final int end = n * n;

        long sum = 0;
        int current = 1, turns = 0;

        while (current <= end) {
            System.err.println("=== ");
            System.err.printf("Sum: %d + %d = %d%n", sum, current, sum + current);

            sum += current;
            current += (n - 1);

            if (n == 1) {
                break;
            }

            if (++turns % 4 == 0) {
                // End of layer
                n -= 2;
            }
        }

        System.out.println(sum);
    }
}
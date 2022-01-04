/**
 * Copyright 2015-2022, Francois Ritaly
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
package codingame.next_growing_number;

import java.util.Arrays;
import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        long n = new Scanner(System.in).nextLong();

        System.err.println(n);

        n++;

        final char[] array = Long.toString(n).toCharArray();

        for (int i = 0; i < array.length - 1; i++) {
            if (array[i+1] < array[i]) {
                array[i+1] = array[i];

                Arrays.fill(array, i+2, array.length, '0');
            }
        }

        System.out.println(new String(array));
    }
}
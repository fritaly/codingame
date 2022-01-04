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
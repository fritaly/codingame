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
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
package codingame.temperatures;

import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // the number of temperatures to analyse
        in.nextLine();
        String temps = in.nextLine(); // the n temperatures expressed as integers ranging from -273 to 5526

        Integer result = null;
        
        if (n > 0) {
        	for (String value : temps.split(" ")) {
    			int temperature = Integer.parseInt(value);
    			
    			if ((result == null) || (Math.abs(result) > Math.abs(temperature))) {
    				result = temperature;
    			} else if (Math.abs(result) == Math.abs(temperature)) {
    				if ((result < 0) && (temperature > 0)) {
    					result = temperature;
    				}
    			}
    		}
        }
        
        System.out.println((result == null) ? 0 : result);
    }
}
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
package codingame.thedescent;

import java.util.Scanner;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int spaceX = in.nextInt();
            int spaceY = in.nextInt();
            
            int[] heights = new int[8];
            int maxHeight = Integer.MIN_VALUE;
            
            for (int i = 0; i < 8; i++) {
            	// represents the height of one mountain, from 9 to 0. Mountain heights are provided from left to right.
            	heights[i] = in.nextInt();
            	
            	maxHeight = Math.max(maxHeight, heights[i]);
            }
            
            // either:  FIRE (ship is firing its phase cannons) or HOLD (ship is not firing).
            if (heights[spaceX] == maxHeight) {
            	System.out.println("FIRE");
            } else {
            	System.out.println("HOLD"); 
            }
        }
    }
}

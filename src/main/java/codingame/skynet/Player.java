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
package codingame.skynet;

import java.util.Scanner;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int road = in.nextInt(); // the length of the road before the gap.
        int gap = in.nextInt(); // the length of the gap.
        int platform = in.nextInt(); // the length of the landing platform.
        int minSpeed = gap + 1;
        
        System.err.println(String.format("Road: %d, Gap: %d, Platform: %d", road, gap, platform));
        
        // game loop
        while (true) {
            int speed = in.nextInt(); // the motorbike's speed.
            int coordX = in.nextInt(); // the position on the road of the motorbike.
            
            if (coordX < road) {
            	// Before the gap
            	if (speed > minSpeed) {
            		System.out.println("SLOW");
            	} else if (speed < minSpeed) {
                	System.out.println("SPEED");
                } else if (coordX + speed < road) {
                	System.out.println("WAIT");
                } else if (coordX + speed >= road) {
                	System.out.println("JUMP");
                }
            } else if (coordX >= road + gap) {
            	// After the gap
            	System.out.println("SLOW");
            }
            
            // A single line containing one of 4 keywords: SPEED, SLOW, JUMP, WAIT.
            
            coordX += speed;
        }
    }
}
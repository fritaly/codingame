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
package codingame.onboarding;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * CodinGame planet is being attacked by slimy insectoid aliens.
 * <---
 * Hint:To protect the planet, you can implement the pseudo-code provided in the statement, below the player.
 **/
class Player {
	
	private static class Enemy implements Comparable<Enemy> {
		final String name;
		
		final int distance;
		
		Enemy(String name, int distance) {
			this.name = name;
			this.distance = distance;
		}
		
		@Override
		public int compareTo(Enemy o) {
			return this.distance - o.distance;
		}
	}

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        final Set<Enemy> enemies = new TreeSet<Enemy>();
        
        // game loop
        while (true) {
        	enemies.clear();
        	enemies.add(new Enemy(in.next(), in.nextInt()));
        	enemies.add(new Enemy(in.next(), in.nextInt()));
        	
//        	System.err.println(enemies);

        	System.out.println(enemies.iterator().next().name);
        }
    }
}

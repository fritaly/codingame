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

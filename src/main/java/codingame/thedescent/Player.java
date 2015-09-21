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

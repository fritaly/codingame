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
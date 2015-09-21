package codingame.powerofthor;

import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 * ---
 * Hint: You can use the debug stream to print initialTX and initialTY, if Thor seems not follow your orders.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int lightX = in.nextInt(); // the X position of the light of power
        int lightY = in.nextInt(); // the Y position of the light of power
        int thorX = in.nextInt(); // Thor's starting X position
        int thorY = in.nextInt(); // Thor's starting Y position

        // game loop
        while (true) {
            int remainingTurns = in.nextInt();

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            
            String direction = "";
            
            if (thorY > lightY) {
            	direction = "N";
            	
            	thorY--;
            } else if (thorY < lightY) {
            	direction = "S";
            	
            	thorY++;
            }
            
            if (thorX > lightX) {
            	direction += "W";
            	
            	thorX--;
            } else if (thorX < lightX) {
            	direction += "E";
            	
            	thorX++;
            }

            // A single line providing the move to be made: N NE E SE S SW W or NW
            System.out.println(direction); 
        }
    }
}
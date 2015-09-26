package codingame.heatdetector;

import java.util.Scanner;

class Player {
	
	private static class Area {
		final int x1, x2, y1, y2;
		
		public Area(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		
		boolean contains(int x, int y) {
			return (x1 <= x) && (x <= x2) && (y1 <= y) && (y <= y2); 
		}
		
		boolean overlaps(Area area) {
			return contains(x1, y1) || contains(x1, y2) || contains(x2, y1) || contains(x2, y2);
		}
		
		Area intersection(Area area) {
			if (!overlaps(area)) {
				return null;
			}
			
			return new Area(Math.max(this.x1, area.x1), Math.max(this.y1, area.y1), 
					Math.min(this.x2, area.x2), Math.min(this.y2, area.y2));
		}
		
		Area getArea(int x, int y, String direction, int width, int height) {
			if ("U".equals(direction)) {
				return intersection(new Area(x1, 0, x2, y - 1));
			} else if ("D".equals(direction)) {
				return intersection(new Area(x1, y + 1, x2, height - 1));
			} else if ("L".equals(direction)) {
				return intersection(new Area(0, y1, x - 1, y2));
			} else if ("R".equals(direction)) {
				return intersection(new Area(x + 1, y1, width - 1, y2));
			} else if ("UR".equals(direction)) {
				return intersection(new Area(x + 1, 0, width - 1, y - 1));
			} else if ("UL".equals(direction)) {
				return intersection(new Area(0, 0, x - 1, y - 1));
			} else if ("DR".equals(direction)) {
				return intersection(new Area(x + 1, y + 1, width - 1, height - 1));
			} else if ("DL".equals(direction)) {
				return intersection(new Area(0, y + 1, x - 1, height - 1));
			}
			
			throw new IllegalArgumentException("Unexpected direction: " + direction);
		}
		
		int getCenterX() {
			return x1 + (x2 - x1) / 2;
		}
		
		int getCenterY() {
			return y1 + (y2 - y1) / 2;
		}
	}

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // width of the building.
        int height = in.nextInt(); // height of the building.
        int turns = in.nextInt(); // maximum number of turns before game over.
        int batmanX = in.nextInt();
        int batmanY = in.nextInt();
        
        Area area = new Area(0, 0, width - 1, height - 1);

        // game loop
        while (true) {
            String direction = in.next(); // the direction of the bombs from batman's current location (U, UR, R, DR, D, DL, L or UL)

            // Compute the new area
            area = area.getArea(batmanX, batmanY, direction, width, height);
            
            batmanX = area.getCenterX();
            batmanY = area.getCenterY();

            // the location of the next window Batman should jump to.
            System.out.println(String.format("%d %d", batmanX, batmanY)); 
        }
    }
}
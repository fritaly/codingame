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
package codingame.apu;
import java.util.Scanner;

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // the number of cells on the X axis
        in.nextLine();
        int height = in.nextInt(); // the number of cells on the Y axis
        in.nextLine();
        
        final char[][] array = new char[height][width];
        
        for (int y = 0; y < height; y++) {
            String line = in.nextLine(); // width characters, each either 0 or .

            for (int x = 0; x < line.length(); x++) {
				array[y][x] = line.charAt(x);
			}
        }

        for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				char c = array[y][x];

				if (c == '0') {
					int x2 = -1, y2 = -1, x3 = -1, y3 = -1;
					
					for (int i = x + 1; i < width; i++) {
						if (array[y][i] == '0') {
							x2 = i;
							y2 = y;
							break;
						}
					}
					for (int i = y + 1; i < height; i++) {
						if (array[i][x] == '0') {
							x3 = x;
							y3 = i;
							break;
						}
					}
					
					System.out.println(String.format("%d %d %d %d %d %d", x, y, x2, y2, x3, y3));					
				}
			}
		}
    }
}
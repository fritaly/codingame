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
package codingame.wheres_wally;

import java.util.*;

class Solution {

    private static class Array {

        final int width, height;
        final char[][] array;

        Array(Scanner scanner) {
            this.width = scanner.nextInt();
            this.height = scanner.nextInt();
            this.array = new char[height][width];

            System.err.println(width + " " + height);

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            for (int y = 0; y < height; y++) {
                final String line = scanner.nextLine();

                System.err.println(line);

                array[y] = line.toCharArray();
            }
        }

        boolean matches(Array pattern, int px, int py) {
            if (px + pattern.width > width) {
                return false;
            }
            if (py + pattern.height > height) {
                return false;
            }

            for (int y = 0; y < pattern.height; y++) {
                for (int x = 0; x < pattern.width; x++) {
                    if (pattern.array[y][x] == ' ') {
                        // Match
                    } else if (pattern.array[y][x] == array[py + y][px + x]) {
                        // Match
                    } else {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private static boolean match(char[][] wally, char[][] map, int startX, int startY) {
        System.err.println("=== Testing (" + startX + "," + startY + ") ===");

        for (int y = 0; y < wally.length ; y++) {
            for (int x = 0; x < wally[0].length ; x++) {
                final char wallyChar = wally[y][x];
                final char mapChar = map[startY + y][startX + x];

                System.err.printf("wally[%d][%d]='%s' = map[%d][%d]='%s' %n", y, x, wallyChar, startY + y, startX + x, mapChar);

                if ((wallyChar == ' ') && (mapChar != '.')) {
                    System.err.println("Returning false");
                    return false;
                }
                if (wallyChar != mapChar) {
                    System.err.println("Returning false");
                    return false;
                }
            }
        }

        System.err.println("Returning true");
        return true;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final Array wally = new Array(scanner);
        final Array grid = new Array(scanner);

        for (int y = 0; y < grid.height - wally.height + 1; y++) {
            for (int x = 0; x < grid.width - wally.width + 1; x++) {
                if (grid.matches(wally, x, y)) {
                    System.out.println(x + " " + y);
                    return;
                }
            }
        }
    }
}
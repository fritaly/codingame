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
package codingame.number_of_paths_between_2_points;

import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt(), width = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] map = new char[height][];

        for (int y = 0; y < height; y++) {
            map[y] = scanner.nextLine().toCharArray();
        }

        final int[][] grid = new int[height][];

        for (int y = 0; y < height; y++) {
            grid[y] = new int[width];
        }

        grid[0][0] = 1;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x == 0 && y == 0) {
                    continue;
                }

                grid[y][x] = 0;

                if ((y > 0) && (map[y - 1][x] == '0')) {
                    grid[y][x] += grid[y - 1][x]; // from top
                }
                if ((x > 0) && (map[y][x - 1] == '0')) {
                    grid[y][x] += grid[y][x - 1]; // from left
                }
            }
        }

        System.out.println(grid[height - 1][width - 1]);
    }
}
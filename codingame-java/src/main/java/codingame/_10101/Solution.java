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
package codingame._10101;

import java.util.*;
import java.util.stream.IntStream;

class Solution {

    private static boolean fullRow(char[][] grid, int y) {
        return IntStream.range(0, grid[y].length).mapToObj(x -> grid[y][x]).allMatch(c -> c == '#');
    }

    private static boolean fullColumn(char[][] grid, int x) {
        return IntStream.range(0, grid.length).mapToObj(y -> grid[y][x]).allMatch(c -> c == '#');
    }

    private static int score(char[][] grid) {
        int score = 0;
        score += IntStream.range(0, grid.length).map(y -> fullRow(grid, y) ? 1 : 0).sum();
        score += IntStream.range(0, grid[0].length).map(x -> fullColumn(grid, x) ? 1 : 0).sum();

        return score;
    }

    private static char[] clone(char[] source) {
        final char[] result = new char[source.length];

        System.arraycopy(source, 0, result, 0, source.length);

        return result;
    }

    private static char[][] clone(char[][] source) {
        final char[][] result = new char[source.length][];

        for (int y = 0; y < source.length; y++) {
            result[y] = clone(source[y]);
        }

        return result;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt(), height = scanner.nextInt();

        System.err.printf("%d %d%n", width, height);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] grid = new char[height][];

        for (int y = 0; y < height; y++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            grid[y] = line.toCharArray();
        }

        int bestScore = 0;
        char[][] solution = null;

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                if (grid[y][x] == '.' && grid[y][x+1] == '.' && grid[y+1][x] == '.' && grid[y+1][x+1] == '.') {
                    final char[][] clone = clone(grid);
                    clone[y][x] = '#';
                    clone[y][x+1] = '#';
                    clone[y+1][x] = '#';
                    clone[y+1][x+1] = '#';

                    final int score = score(clone);

                    if (score > bestScore) {
                        bestScore = score;
                        solution = clone;
                    }
                }
            }
        }

        if (solution != null) {
            for (int y = 0; y < height; y++) {
                System.err.println(new String(solution[y]));
            }

            System.out.println(bestScore);
        } else {
            System.out.println("0");
        }
    }
}
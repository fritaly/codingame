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
package codingame.tetris;

import java.util.Scanner;

class Solution {

    private static boolean copy(final char[][] grid, final char[][] shape, int sx, int sy) {
        for (int y = 0; y < shape.length; y++) {
            for (int x = 0; x < shape[y].length; x++) {
                if ((grid[sy + y][sx + x] == '*') && (shape[y][x] == '*')) {
                    return false;
                }

                if (shape[y][x] == '*') {
                    grid[sy + y][sx + x] = shape[y][x];
                }
            }
        }

        return true;
    }

    private static int count(final char[][] grid) {
        int count = 0;

        for (int y = 0; y < grid.length; y++) {
            if (!new String(grid[y]).contains(".")) {
                count++;
            }
        }

        return count;
    }

    private static char[] clone(char[] source) {
        final char[] clone = new char[source.length];

        System.arraycopy(source, 0, clone, 0, source.length);

        return clone;
    }

    private static char[][] clone(final char[][] source) {
        final char[][] clone = new char[source.length][];

        for (int y = 0; y < source.length; y++) {
            clone[y] = clone(source[y]);
        }

        return clone;
    }

    private static void dump(char[][] grid) {
        System.err.println("=== Dump ===");

        for (int y = 0; y < grid.length; y++) {
            System.err.println(new String(grid[y]));
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int shapeWidth = scanner.nextInt(), shapeHeight = scanner.nextInt();

        System.err.printf("%d %d%n", shapeWidth, shapeHeight);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] shape = new char[shapeHeight][];

        for (int y = 0; y < shapeHeight; y++) {
            shape[y] = scanner.nextLine().toCharArray();
        }

        dump(shape);

        final int width = scanner.nextInt(), height = scanner.nextInt();

        System.err.printf("%d %d%n", width, height);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] grid = new char[height][];

        for (int y = 0; y < height; y++) {
            grid[y] = scanner.nextLine().toCharArray();
        }

        dump(grid);

        int bestScore = 0, solutionX = 0, solutionY = 0;

        for (int y = height - shapeHeight; y >= 0; y--) {
            for (int x = 0; x < width - shapeWidth + 1; x++) {
                final char[][] clone = clone(grid);

                if (copy(clone, shape, x, y)) {
                    dump(clone);

                    final int score = count(clone);

                    if (score > bestScore) {
                        bestScore = score;
                        solutionX = x;
                        solutionY = y;
                    }
                }
            }
        }

        System.out.printf("%d %d%n", solutionX, height - solutionY - 1);
        System.out.println(bestScore);
    }
}
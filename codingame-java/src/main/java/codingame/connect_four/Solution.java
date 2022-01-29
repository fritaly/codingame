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
package codingame.connect_four;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

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

    private static int findY(char[][] grid, int x) {
        for (int y = 5; y >= 0 ; y--) {
            if (grid[y][x] == '.') {
                return y;
            }
        }

        // The column is full
        return -1;
    }

    private static boolean checkVertically(char[][] grid, char c, int x) {
        return IntStream.range(0, 6)
                .mapToObj(y -> Character.toString(grid[y][x]))
                .collect(Collectors.joining())
                .contains((c == '1' ? "1111" : "2222"));
    }

    private static boolean checkVertically(char[][] grid, char c) {
        return IntStream.range(0, 7).anyMatch(x -> checkVertically(grid, c, x));
    }

    private static boolean checkHorizontally(char[][] grid, char c, int y) {
        return IntStream.range(0, 7)
                .mapToObj(x -> Character.toString(grid[y][x]))
                .collect(Collectors.joining())
                .contains((c == '1' ? "1111" : "2222"));
    }

    private static boolean checkHorizontally(char[][] grid, char c) {
        return IntStream.range(0, 6).anyMatch(y -> checkHorizontally(grid, c, y));
    }

    private static String collectDiagonally1(char[][] grid, int x, int y) {
        return IntStream.range(0, 6)
                .filter(n -> (x + n < 7) && (y + n < 6))
                .mapToObj(n -> Character.toString(grid[y+n][x+n]))
                .collect(Collectors.joining());
    }

    private static String collectDiagonally2(char[][] grid, int x, int y) {
        return IntStream.range(0, 6)
                .filter(n -> (x + n < 7) && (y - n >= 0))
                .mapToObj(n -> Character.toString(grid[y-n][x+n]))
                .collect(Collectors.joining());
    }

    private static boolean checkDiagonally1(char[][] grid, char c) {
        final String pattern = (c == '1') ? "1111" : "2222";

        for (int y = 5; y >= 0; y--) {
            if (collectDiagonally1(grid, 0, y).contains(pattern)) {
                return true;
            }
        }
        for (int x = 0; x < 7; x++) {
            if (collectDiagonally1(grid, x, 0).contains(pattern)) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkDiagonally2(char[][] grid, char c) {
        final String pattern = (c == '1') ? "1111" : "2222";

        for (int y = 0; y < 6; y++) {
            if (collectDiagonally2(grid, 0, y).contains(pattern)) {
                return true;
            }
        }
        for (int x = 0; x < 7; x++) {
            if (collectDiagonally2(grid, x, 5).contains(pattern)) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkDiagonally(char[][] grid, char c) {
        return checkDiagonally1(grid, c) || checkDiagonally2(grid, c);
    }

    private static boolean check(char[][] grid, char c) {
        return checkVertically(grid, c) || checkHorizontally(grid, c) || checkDiagonally(grid, c);
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final char[][] grid = new char[6][];

        for (int y = 0; y < 6; y++) {
            final String line = scanner.next();

            System.err.println(line);

            grid[y] = line.toCharArray();
        }

        findSolutions(grid, '1');
        findSolutions(grid, '2');
    }

    private static void findSolutions(char[][] grid, char c) {
        final List<String> solutions = new ArrayList<>();

        for (int x = 0; x < 7; x++) {
            // Try to drop a token in each column
            final int y = findY(grid, x);

            if (y != -1) {
                final char[][] clone = clone(grid);
                clone[y][x] = c;

                if (check(clone, c)) {
                    solutions.add("" + x);
                }
            }
        }

        if (solutions.isEmpty()) {
            System.out.println("NONE");
        } else {
            System.out.println(String.join(" ", solutions));
        }
    }
}
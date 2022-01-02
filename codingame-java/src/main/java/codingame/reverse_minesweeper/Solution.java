/**
 * Copyright 2021, Francois Ritaly
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
package codingame.reverse_minesweeper;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private static class Coords {
        final int x, y;

        private Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Coords up() {
            return new Coords(x, y - 1);
        }

        Coords down() {
            return new Coords(x, y + 1);
        }

        Coords left() {
            return new Coords(x - 1, y);
        }

        Coords right() {
            return new Coords(x + 1, y);
        }

        List<Coords> neighbors() {
            return Arrays.asList(up().left(), up(), up().right(), left(), right(), down().left(), down(), down().right());
        }

        boolean isValid(int width, int height) {
            return ((0 <= x) && (x < width)) && ((0 <= y) && (y < height));
        }
    }

    private static char count(char[][] grid, int x, int y, int width, int height) {
        final int mines = (int) new Coords(x, y).neighbors().stream()
                .filter(p -> p.isValid(width, height))
                .map(p -> grid[p.y][p.x])
                .filter(c -> c == 'x')
                .count();

        return (mines == 0) ? '.' : (char) ('0' + mines);
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt();
        final int height = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] grid = new char[height][width];

        for (int y = 0; y < height; y++) {
            grid[y] = scanner.nextLine().toCharArray();
        }

        final char[][] output = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                output[y][x] = (grid[y][x] == 'x') ? '.' : count(grid, x, y, width, height);
            }
        }

        System.out.println(Arrays.asList(output)
                .stream()
                .map(r -> new String(r))
                .collect(Collectors.joining("\n")));
    }
}
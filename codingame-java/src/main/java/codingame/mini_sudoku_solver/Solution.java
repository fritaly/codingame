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
package codingame.mini_sudoku_solver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static final Set<Character> DIGITS = new LinkedHashSet(Arrays.asList('1', '2', '3', '4'));

    private static boolean isValid(Set<Character> set) {
        if (set == null) {
            return false;
        }
        if (set.size() != 4) {
            return false;
        }

        return set.contains('1') && set.contains('2') && set.contains('3') && set.contains('4');
    }

    private static class Grid {
        final char[][] grid = new char[4][4];

        Grid(Scanner scanner) {
            for (int y = 0; y < 4; y++) {
                grid[y] = scanner.nextLine().toCharArray();
            }
        }

        Grid(Grid template) {
            for (int n = 0; n < 16; n++) {
                this.grid[n / 4][n % 4] = template.grid[n / 4][n % 4];
            }
        }

        Set<Character> getCandidates(final int x, final int y) {
            final Set<Character> set = new HashSet<>(DIGITS);
            set.removeAll(digitsInColumn(x));
            set.removeAll(digitsOnRow(y));
            set.removeAll(digitsInSquare(x, y));

            return set;
        }

        Set<Character> digitsInSquare(final int x, final int y) {
            final Set<Character> set = new HashSet<>();

            for (int _y = (y / 2) * 2; _y < (y / 2) * 2 + 2; _y++) {
                for (int _x = (x / 2) * 2; _x < (x / 2) * 2 + 2; _x++) {
                    set.add(grid[_y][_x]);
                }
            }

            set.remove('0');

            return set;
        }

        Set<Character> digitsOnRow(int y) {
            final Set<Character> set = new HashSet<>();

            for (int x = 0; x < 4; x++) {
                set.add(grid[y][x]);
            }

            set.remove('0');

            return set;
        }

        Set<Character> digitsInColumn(int x) {
            final Set<Character> set = new HashSet<>();

            for (int y = 0; y < 4; y++) {
                set.add(grid[y][x]);
            }

            set.remove('0');

            return set;
        }

        boolean validate() {
            for (int y = 0; y < 4; y++) {
                if (!isValid(digitsOnRow(y))) {
                    return false;
                }
            }
            for (int x = 0; x < 4; x++) {
                if (!isValid(digitsInColumn(x))) {
                    return false;
                }
            }

            return isValid(digitsInSquare(0, 0)) && isValid(digitsInSquare(0, 2))
                    && isValid(digitsInSquare(2, 0)) && isValid(digitsInSquare(2, 2));
        }

        Grid solve() {
            boolean update;

            do {
                update = false;

                for (int y = 0; (y < 4); y++) {
                    for (int x = 0; (x < 4); x++) {
                        if (grid[y][x] == '0') {
                            final Set<Character> set = getCandidates(x, y);

                            if (set.size() == 0) {
                                // The grid isn't valid
                                return null;
                            } else if (set.size() == 1) {
                                // Only one possibility, set the digit
                                grid[y][x] = set.iterator().next();

                                System.err.println("=== Added '" + grid[y][x] + "' in (" + x + "," + y + ") ===");
                                System.err.println(this);

                                update = true;
                            }
                        }
                    }
                }
            } while (update);

            for (int y = 0; (y < 4); y++) {
                for (int x = 0; (x < 4); x++) {
                    if (grid[y][x] == '0') {
                        final Set<Character> set = getCandidates(x, y);

                        for (Character digit : set) {
                            // Fork the grid
                            final Grid clone = new Grid(this);
                            clone.grid[y][x] = digit;

                            final Grid result = clone.solve();

                            if (result != null) {
                                return result;
                            }
                        }
                    }
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return IntStream.range(0, 4)
                    .mapToObj(y -> new String(grid[y]))
                    .collect(Collectors.joining("\n"));
        }
    }

    public static void main(String args[]) {
        final Grid grid = new Grid(new Scanner(System.in));

        System.err.println(grid);

        grid.solve();

        System.out.println(grid);
    }
}
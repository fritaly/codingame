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
package codingame.sudoku_solver;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static final Set<Character> DIGITS = new LinkedHashSet(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9'));

    private static boolean isValid(Set<Character> set) {
        if (set == null) {
            return false;
        }
        if (set.size() != 9) {
            return false;
        }
        
        return DIGITS.stream().allMatch(d -> set.contains(d));
    }

    private static class Grid {
        final char[][] grid = new char[9][9];

        Grid(Scanner scanner) {
            for (int y = 0; y < 9; y++) {
                grid[y] = scanner.nextLine().toCharArray();
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

            for (int _y = (y / 3) * 3; _y < (y / 3) * 3 + 3; _y++) {
                for (int _x = (x / 3) * 3; _x < (x / 3) * 3 + 3; _x++) {
                    set.add(grid[_y][_x]);
                }
            }

            set.remove('0');

            return set;
        }

        Set<Character> digitsOnRow(int y) {
            final Set<Character> set = new HashSet<>();

            for (int x = 0; x < 9; x++) {
                set.add(grid[y][x]);
            }

            set.remove('0');

            return set;
        }

        Set<Character> digitsInColumn(int x) {
            final Set<Character> set = new HashSet<>();

            for (int y = 0; y < 9; y++) {
                set.add(grid[y][x]);
            }

            set.remove('0');

            return set;
        }

        public String save() {
            return IntStream.range(0, 9).mapToObj(y -> new String(grid[y])).collect(Collectors.joining());
        }

        public void restore(String backup) {
            for (int n = 0; n < backup.length(); n++) {
                grid[n / 9][n % 9] = backup.charAt(n);
            }
        }

        boolean validate() {
            for (int y = 0; y < 9; y++) {
                if (!isValid(digitsOnRow(y))) {
                    return false;
                }
            }
            for (int x = 0; x < 9; x++) {
                if (!isValid(digitsInColumn(x))) {
                    return false;
                }
            }

            for (int x = 0; x < 9; x += 3) {
                for (int y = 0; y < 9; y += 3) {
                    if (!isValid(digitsInSquare(x, y))) {
                        return false;
                    }
                }
            }

            return true;
        }

        Grid solve() {
            boolean update;

            do {
                update = false;

                for (int y = 0; y < 9; y++) {
                    for (int x = 0; x < 9; x++) {
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

            if (validate()) {
                return this;
            }

            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    if (grid[y][x] == '0') {
                        final Set<Character> set = getCandidates(x, y);

                        System.err.printf("Testing candidates (%s) in (%d,%d) ...%n", set, x, y);

                        for (Character digit : set) {
                            // Save the grid state
                            final String backup = save();

                            // Test the digit
                            grid[y][x] = digit;

                            System.err.printf("Testing %s in (%d,%d) ...%n", digit, x, y);

                            final Grid result = solve();

                            if (result != null) {
                                return result;
                            }

                            System.err.printf("Test (%s in (%d,%d)) failed%n", digit, x, y);

                            // Restore the grid state
                            restore(backup);
                        }

                        return null;
                    }
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return IntStream.range(0, 9)
                    .mapToObj(y -> new String(grid[y]))
                    .collect(Collectors.joining("\n"));
        }
    }

    public static void main(String args[]) {
        Grid grid = new Grid(new Scanner(System.in));

        System.err.println(grid);

        grid = grid.solve();

        System.out.println(grid);
    }
}
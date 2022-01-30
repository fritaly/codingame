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
package codingame.a_mountain_of_a_mole_hill;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isValid() {
            return (0 <= x) && (x < 16) && (0 <= y) && (y < 16);
        }

        List<Position> neighbors() {
            return Arrays.asList(new Position(x, y - 1),
                    new Position(x - 1, y),
                    new Position(x + 1, y),
                    new Position(x, y + 1));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final char[][] map = new char[16][];

        for (int y = 0; y < 16; y++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            map[y] = line.toCharArray();
        }

        final LinkedList<Position> queue = new LinkedList<>();

        // Scan the map edges and identify the positions outside the gardens
        for (int n = 0; n < 16; n++) {
            if (map[0][n] == '.' || map[0][n] == 'o') {
                queue.add(new Position(n, 0));
            }
            if (map[15][n] == '.' || map[15][n] == 'o') {
                queue.add(new Position(n, 15));
            }
            if (map[n][0] == '.' || map[n][0] == 'o') {
                queue.add(new Position(0, n));
            }
            if (map[n][15] == '.' || map[n][15] == 'o') {
                queue.add(new Position(15, n));
            }
        }

        // Record all the outside positions in '.'
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                if (map[y][x] == '.') {
                    queue.add(new Position(x, y));
                }
            }
        }

        System.err.printf("Queue: %s%n", queue);

        // Use a diffusion algorithm to identify all the outside positions and erase the outside hills
        final Set<Position> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            final Position position = queue.removeFirst();

            if (visited.add(position)) {
                // New position, not visited yet
                final char c = map[position.y][position.x];

                if (c == 'o') {
                    // Erase the hill
                    map[position.y][position.x] = '.';
                } else if (c == '.') {
                    // Do nothing
                }

                queue.addAll(position.neighbors()
                        .stream()
                        .filter(p -> p.isValid() && !visited.contains(p) && (map[p.y][p.x] != '|' && map[p.y][p.x] != '+' && map[p.y][p.x] != '-'))
                        .collect(Collectors.toList()));
            }
        }

        // Scan the map to find "nested" hills and erase them (cf "Include and infiltrate")
        for (int y = 0; y < 16; y++) {
            boolean inside = false;

            for (int x = 0; x < 16; x++) {
                if (map[y][x] == '|') {
                    inside = !inside;
                } else if (map[y][x] == '+' || map[y][x] == '-') {
                    break;
                } else if (map[y][x] == 'o') {
                    if (!inside) {
                        map[y][x] = '.';
                    }
                }
            }
        }

        for (int y = 0; y < 16; y++) {
            System.err.println(new String(map[y]));
        }

        int count = 0;

        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                if (map[y][x] == 'o') {
                    count++;
                }
            }
        }

        System.out.println(count);
    }
}
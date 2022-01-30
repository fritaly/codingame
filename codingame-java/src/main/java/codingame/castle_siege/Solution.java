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
package codingame.castle_siege;

import java.util.*;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        double distanceTo(Position target) {
            final int dx = this.x - target.x, dy = this.y - target.y;

            return Math.sqrt(dx * dx + dy * dy);
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

    private static class Tower {
        final Position position;

        private Tower(Position position) {
            this.position = position;
        }

        List<Position> findEnemies(char[][] map) {
            final List<Position> enemies = new ArrayList<>();

            for (int dy = -2; dy <= 2; dy++) {
                for (int dx = -2; dx <= 2; dx++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }

                    final int x = position.x + dx, y = position.y + dy;

                    if ((0 <= x) && (x < map[0].length) && (0 <= y) && (y < map.length)) {
                        if (Character.isDigit(map[y][x])) {
                            enemies.add(new Position(x, y));
                        }
                    }
                }
            }

            return enemies;
        }
    }

    private static void dump(char[][] map) {
        for (int y = 0; y < map.length; y++) {
            System.err.println(new String(map[y]));
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt(), height = scanner.nextInt();

        System.err.printf("%d %d%n", width, height);

        final char[][] map = new char[height][];

        for (int y = 0; y < height; y++) {
            map[y] = scanner.next().toCharArray();
        }

        dump(map);

        // Locate the towers
        final List<Tower> towers = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (map[y][x] == 'T') {
                    towers.add(new Tower(new Position(x, y)));
                }
            }
        }

        int turn = 0;
        boolean lost = false;

        while (!lost) {
            turn++;

            System.err.println("=== Turn " + turn + " ===");
            System.err.println("Before:");
            dump(map);

            // The towers attack
            for (Tower tower : towers) {
                // Find the enemies this tower can attack
                final List<Position> enemies = tower.findEnemies(map);

                if (!enemies.isEmpty()) {
                    // Sort the enemies
                    Collections.sort(enemies, (a, b) -> {
                        // The priority is the enemy the closest to the northern edge (low y)
                        if (a.y < b.y) {
                            return +1;
                        } else if (a.y > b.y) {
                            return -1;
                        }

                        // The second priority is the enemy the closest to the tower
                        final double distanceA = a.distanceTo(tower.position), distanceB = b.distanceTo(tower.position);

                        if (distanceA < distanceB) {
                            return +1;
                        } else if (distanceA > distanceB) {
                            return -1;
                        }

                        // The third priority is the enemy the most eastern
                        if (a.x < b.x) {
                            return +1;
                        } else if (a.x > b.x) {
                            return -1;
                        }

                        return 0;
                    });

                    // Shoot the enemy, decrease its health
                    final Position enemy = enemies.iterator().next();

                    map[enemy.y][enemy.x]--;

                    if (map[enemy.y][enemy.x] == '0') {
                        // Remove the enemy
                        map[enemy.y][enemy.x] = '.';
                    }
                }
            } // loop on towers

            // The enemies move
            boolean enemyFound = false;

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if ((map[y][x] != 'T') && (map[y][x] != '.')) {
                        if (y == 0) {
                            lost = true;
                            map[y][x] = '.';
                        } else if (map[y-1][x] == 'T') {
                            // The enemy dies on the tower
                            enemyFound = true;
                            map[y][x] = '.';
                        } else {
                            // The enemy moves towards north
                            map[y-1][x] = map[y][x];
                            map[y][x] = '.';

                            enemyFound = true;
                        }
                    }
                }
            }

            if (!enemyFound) {
                break;
            }


            System.err.println("After: ");
            dump(map);
        }

        System.out.printf("%s %d", lost ? "LOSE" : "WIN", turn);
    }
}
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
package codingame.dungeon_3d;

import java.util.*;
import java.io.*;
import java.math.*;

class Solution {

    private static class Position {
        final int x, y, z;

        private Position(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int distance(Position target) {
            // Manhattan distance
            return Math.abs(this.x - target.x) + Math.abs(this.y - target.y) + Math.abs(this.z - target.z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y && z == position.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    private static class PriorityPosition implements Comparable<PriorityPosition> {
        final int priority;
        final Position position;

        private PriorityPosition(int priority, Position position) {
            this.priority = priority;
            this.position = position;
        }

        @Override
        public int compareTo(PriorityPosition other) {
            return Integer.compare(this.priority, other.priority);
        }
    }

    private static List<Position> findPath(char[][][] dungeon, Position origin, Position destination) {
        final int height = dungeon[0].length, width = dungeon[0][0].length;

        final PriorityQueue<PriorityPosition> queue = new PriorityQueue<>();
        queue.add(new PriorityPosition(0, origin));

        final Map<Position, Position> cameFrom = new HashMap<>();
        cameFrom.put(origin, null);

        final Map<Position, Integer> costSoFar = new HashMap<>();
        costSoFar.put(origin, 0);

        while (!queue.isEmpty()) {
            final PriorityPosition current = queue.remove();

            if (current.equals(destination)) {
                break;
            }

            final int x = current.position.x, y = current.position.y, z = current.position.z;

            // Identify the neighbor positions
            final List<Position> neighbors = new ArrayList<>();

            if ((x > 0) && (dungeon[z][y][x-1] != '#')) {
                neighbors.add(new Position(x - 1, y, z)); // left
            }
            if ((y > 0) && (dungeon[z][y-1][x] != '#')) {
                neighbors.add(new Position(x, y - 1, z)); // up
            }
            if ((z > 0) && (dungeon[z-1][y][x] != '#')) {
                neighbors.add(new Position(x, y, z - 1)); // down
            }
            if ((x < width - 1) && (dungeon[z][y][x+1] != '#')) {
                neighbors.add(new Position(x + 1, y, z)); // right
            }
            if ((y < height - 1) && (dungeon[z][y+1][x] != '#')) {
                neighbors.add(new Position(x, y + 1, z)); // down
            }
            if ((z < dungeon.length - 1) && (dungeon[z+1][y][x] != '#')) {
                neighbors.add(new Position(x, y, z + 1)); // up
            }

            for (Position next : neighbors) {
                final int newCost = costSoFar.get(current.position) + 1;

                if (!costSoFar.containsKey(next) || (newCost < costSoFar.get(next))) {
                    costSoFar.put(next, newCost);
                    final int priority = newCost + next.distance(destination);
                    queue.add(new PriorityPosition(priority, next));
                    cameFrom.put(next, current.position);
                }
            }
        }

        Position current = destination;

        final List<Position> path = new ArrayList<>();

        while (!current.equals(origin)) {
            path.add(current);
            current = cameFrom.get(current);

            if (current == null) {
                return Collections.emptyList();
            }
        }

        Collections.reverse(path);

        return path;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int levelCount = scanner.nextInt(), height = scanner.nextInt(), width = scanner.nextInt(), ln = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][][] dungeon = new char[levelCount][][];

        for (int z = 0; z < levelCount; z++) {
            scanner.nextLine();

            final char[][] level = new char[height][];

            for (int y = 0; y < height; y++) {
                level[y] = scanner.nextLine().toCharArray();
            }

            dungeon[z] = level;
        }

        Position start = null, destination = null;

        for (int z = 0; z < dungeon.length; z++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (dungeon[z][y][x] == 'A') {
                        start = new Position(x, y, z);
                    } else if (dungeon[z][y][x] == 'S') {
                        destination = new Position(x, y, z);
                    }
                }
            }
        }

        final List<Position> path = findPath(dungeon, start, destination);

        System.out.println(path.isEmpty() ? "NO PATH" : path.size());
    }
}
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
package codingame.paper_labyrinth;

import java.util.*;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int distance(Position target) {
            // Manhattan distance
            return Math.abs(this.x - target.x) + Math.abs(this.y - target.y);
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

    private static List<Position> findPath(int[][] labyrinth, Position origin, Position destination) {
        final int height = labyrinth.length, width = labyrinth[0].length;

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

            final int x = current.position.x, y = current.position.y;

            // Identify the neighbor positions
            final List<Position> neighbors = new ArrayList<>();

            if ((y > 0) && ((labyrinth[y][x] & 4) == 0)) {
                neighbors.add(new Position(x, y - 1)); // top
            }
            if ((x > 0) && ((labyrinth[y][x] & 2) == 0)) {
                neighbors.add(new Position(x - 1, y)); // left
            }
            if ((x < width - 1) && ((labyrinth[y][x] & 8) == 0)) {
                neighbors.add(new Position(x + 1, y)); // right
            }
            if ((y < height - 1) && ((labyrinth[y][x] & 1) == 0)) {
                neighbors.add(new Position(x, y + 1)); // bottom
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
        }

        path.add(origin);

        Collections.reverse(path);

        return path;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final Position start = new Position(scanner.nextInt(), scanner.nextInt());
        final Position rabbit = new Position(scanner.nextInt(), scanner.nextInt());

        System.err.printf("%d %d%n", start.x, start.y);
        System.err.printf("%d %d%n", rabbit.x, rabbit.y);

        final int width = scanner.nextInt(), height = scanner.nextInt();

        final int[][] labyrinth = new int[height][];

        for (int y = 0; y < height; y++) {
            labyrinth[y] = new int[width];

            final String line = scanner.next();

            System.err.println(line);

            final char[] chars = line.toCharArray();

            for (int x = 0; x < chars.length; x++) {
                labyrinth[y][x] = Integer.parseInt("" + chars[x], 16);
            }
        }

        System.out.printf("%d %d", findPath(labyrinth, start, rabbit).size() - 1, findPath(labyrinth, rabbit, start).size() - 1);
    }
}
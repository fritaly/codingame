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
package codingame.squares_order;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private static class Point {
        final int x, y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
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

    private static class Positions {
        final List<Point> points = new ArrayList<>();
        final char id;

        private Positions(char id) {
            this.id = id;
        }
    }

    private static class Square {
        final char id;
        final Point topLeft, bottomRight;

        private Square(char id, Point topLeft, Point bottomRight) {
            this.id = id;
            this.topLeft = topLeft;
            this.bottomRight = bottomRight;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Square.class.getSimpleName() + "[", "]")
                    .add("id=" + id)
                    .add(topLeft.toString())
                    .add(bottomRight.toString())
                    .toString();
        }

        int size() {
            return bottomRight.y - topLeft.y + 1;
        }

        Set<Point> allPoints() {
            final Set<Point> set = new LinkedHashSet<>();

            for (int x = topLeft.x; x <= bottomRight.x; x++) {
                set.add(new Point(x, topLeft.y));
                set.add(new Point(x, bottomRight.y));
            }
            for (int y = topLeft.y; y <= bottomRight.y; y++) {
                set.add(new Point(topLeft.x, y));
                set.add(new Point(bottomRight.x, y));
            }

            return set;
        }

        Set<Point> missingPoints(char[][] grid) {
            final Set<Point> result = new LinkedHashSet<>();

            for (Point point : allPoints()) {
                if (grid[point.y][point.x] != id) {
                    result.add(point);
                }
            }

            return result;
        }

        boolean isPossible(char[][] grid, Positions positions) {
            // Check the grid to see whether this square is possible
            int matches = 0;

            for (Point point : allPoints()) {
                final char c = grid[point.y][point.x];

                if (c == '.') {
                    return false;
                }
                if (c == id) {
                    matches++;
                } else {
                    // Point occulted by another square
                }
            }

            // The square id must have matched all the positions
            return matches == positions.points.size();
        }
    }

    // A pair where the left element is lesser than the right one
    private static class Pair {
        final char left, right;

        private Pair(char left, char right) {
            this.left = left;
            this.right = right;
        }

        boolean matches(char a, char b) {
            return (left == a && right == b) || (left == b && right == a);
        }

        int compare(char a, char b) {
            if (left == a && right == b) {
                return -1;
            }
            if (left == b && right == a) {
                return +1;
            }

            // Not supposed to happen
            throw new IllegalStateException();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return left == pair.left && right == pair.right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }

        @Override
        public String toString() {
            return String.format("%s < %s", left, right);
        }
    }

    private static List<Pair> filter(Set<Pair> pairs, char left) {
        return pairs.stream().filter(p -> p.left == left).collect(Collectors.toList());
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt();
        final int width = scanner.nextInt();
        final int nb = scanner.nextInt();

        System.err.printf("%d %d %d%n", height, width, nb);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] grid = new char[height][width];

        for (int y = 0; y < height; y++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            grid[y] = line.toCharArray();
        }

        final Map<Character, Positions> positionsById = new LinkedHashMap<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final char c = grid[y][x];

                if (c != '.') {
                    if (!positionsById.containsKey(c)) {
                        positionsById.put(c, new Positions(c));
                    }

                    positionsById.get(c).points.add(new Point(x, y));
                }
            }
        }

        final Map<Character, Square> solutions = new LinkedHashMap<>();

        // Find all the possible squares matching the corresponding digit positions
        for (int squareId = 1; squareId <= nb; squareId++) {
            for (int x1 = 0; x1 < width; x1++) {
                for (int y1 = 0; y1 < height; y1++) {
                    for (int size = 1; (x1 + size < width) && (y1 + size < height) ; size++) {
                        final Point p1 = new Point(x1, y1);
                        final Point p2 = new Point(x1 + size, y1 + size);

                        final Square candidate = new Square(Integer.toString(squareId).charAt(0), p1, p2);

                        if (candidate.isPossible(grid, positionsById.get(candidate.id))) {
                            System.err.println("Square #" + candidate.id + " is possible in [" + p1 + ", " + p2 + "]");

                            solutions.put(candidate.id, candidate);
                        }
                    }
                }
            }
        }

        final Set<Pair> relationships = new LinkedHashSet<>();

        // Use the collision points to infer overlapping relationships between the different squares
        for (Square square : solutions.values()) {
            for (Point point : square.missingPoints(grid)) {
                if (grid[point.y][point.x] != square.id) {
                    relationships.add(new Pair(square.id, grid[point.y][point.x]));
                }
            }
        }

        // Generate additional relationships by combining them: "1 < 2" + "2 < 3" -> "1 < 3"
        for (char c = '1'; c <= '1' + nb; c++) {
            for (Pair pair : filter(relationships, c)) {
                for (Pair pair1 : filter(relationships, pair.right)) {
                    relationships.add(new Pair(c, pair1.right));
                }
            }
        }

        final List<Square> list = new ArrayList<>(solutions.values());

        // Sort the squares based on the overlapping relationships
        Collections.sort(list, (o1, o2) -> {
            final Optional<Pair> optionalRelation = relationships.stream().filter(r -> r.matches(o1.id, o2.id)).findFirst();

            if (!optionalRelation.isPresent()) {
                System.err.println("Comparing " + o1.id + " & " + o2.id + " -> 0");

                return 0;
            }

            final int result = optionalRelation.get().compare(o1.id, o2.id);

            System.err.println("Comparing " + o1.id + " & " + o2.id + " -> " + result);

            return result;
        });

        System.err.println(list);

        for (Square square : list) {
            System.out.println(square.id + " " + square.size());
        }
    }
}
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
package codingame.surface;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class Point {
        final int x, y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point withX(int x) {
            return new Point(x, this.y);
        }

        Point withY(int y) {
            return new Point(this.x, y);
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

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt();
        final int height = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] map = new char[height][width];

        for (int y = 0; y < height; y++) {
            map[y] = scanner.nextLine().toCharArray();
        }

        final int N = scanner.nextInt();

        final List<Point> points = IntStream.range(0, N)
                .mapToObj(i -> new Point(scanner.nextInt(), scanner.nextInt()))
                .collect(Collectors.toList());

        for (Point point : points) {
            final LinkedList<Point> queue = new LinkedList<>(Collections.singletonList(point));
            final Set<Point> visited = new HashSet<>();

            int surface = 0;

            while (!queue.isEmpty()) {
                final Point p = queue.removeFirst();

                if (map[p.y][p.x] == 'O') {
                    surface++;
                    visited.add(p);
                    if (p.x - 1 >= 0) {
                        final Point p2 = p.withX(p.x - 1);
                        if (visited.add(p2)) {
                            queue.add(p2);
                        }
                    }
                    if (p.x + 1 < width) {
                        final Point p2 = p.withX(p.x + 1);
                        if (visited.add(p2)) {
                            queue.add(p2);
                        }
                    }
                    if (p.y - 1 >= 0) {
                        final Point p2 = p.withY(p.y - 1);
                        if (visited.add(p2)) {
                            queue.add(p2);
                        }
                    }
                    if (p.y + 1 < height) {
                        final Point p2 = p.withY(p.y + 1);
                        if (visited.add(p2)) {
                            queue.add(p2);
                        }
                    }
                }
            }

            System.out.println(surface);
        }
    }
}
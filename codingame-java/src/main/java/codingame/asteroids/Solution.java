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
package codingame.asteroids;

import java.io.PrintStream;
import java.util.*;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }

    private static class Star {
        final char id;
        Position p1, p2;

        private Star(char id) {
            this.id = id;
        }

        Position extrapolate(int t1, int t2, int t3) {
            final int dx = p2.x - p1.x, dy = p2.y - p1.y;
            final int dt = t2 - t1;
            final double vx = ((double) dx) / dt, vy = ((double) dy) / dt;

            return new Position(p1.x + (int) Math.floor((t3 - t1) * vx), p1.y + (int) Math.floor((t3 - t1) * vy));
        }
    }

    private static void dump(char[][] grid, PrintStream printStream) {
        for (char[] row : grid) {
            printStream.println(new String(row));
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt();
        final int height = scanner.nextInt();
        final int t1 = scanner.nextInt(), t2 = scanner.nextInt(), t3 = scanner.nextInt();

        System.err.printf("%d %d %d %d %d%n", width, height, t1, t2, t3);

        final char[][] picture1 = new char[height][width], picture2 = new char[height][width];

        final Map<Character, Star> stars = new TreeMap<>();

        for (int y = 0; y < height; y++) {
            picture1[y] = scanner.next().toCharArray();
            picture2[y] = scanner.next().toCharArray();
        }

        dump(picture1, System.err);
        System.err.println();
        dump(picture2, System.err);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final char char1 = picture1[y][x], char2 = picture2[y][x];

                if (char1 != '.') {
                    stars.putIfAbsent(char1, new Star(char1));
                    stars.get(char1).p1 = new Position(x, y);
                }
                if (char2 != '.') {
                    stars.putIfAbsent(char2, new Star(char2));
                    stars.get(char2).p2 = new Position(x, y);
                }
            }
        }

        final char[][] picture3 = new char[height][width];

        for (int y = 0; y < height; y++) {
            Arrays.fill(picture3[y], '.');
        }

        final List<Star> listOfStars = new ArrayList<>(stars.values());

        Collections.sort(listOfStars, (a, b) -> Character.compare(b.id, a.id));

        for (Star star : listOfStars) {
            final Position p = star.extrapolate(t1, t2, t3);

            if ((0 <= p.x) && (p.x < width) && (0 <= p.y) && (p.y < height)) {
                picture3[p.y][p.x] = star.id;
            }
        }

        System.err.println();
        for (char[] row : picture3) {
            System.out.println(new String(row));
        }
    }
}
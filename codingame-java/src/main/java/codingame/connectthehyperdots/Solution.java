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
package codingame.connectthehyperdots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Solution {

    private static class Point {

        final String letter;

        final int[] coords;

        private Point(String letter, int[] coords) {
            this.letter = letter;
            this.coords = coords;
        }

        private double distance(Point point) {
            final int sum = IntStream.range(0, this.coords.length)
                    .map(n -> this.coords[n] - point.coords[n])
                    .map(n -> n * n)
                    .sum();

            return Math.pow(sum, (1.0f / coords.length));
        }

        private boolean sameOrthant(Point point) {
            int same = 0, zero = 0, dimension = coords.length;

            for (int i = 0; i < coords.length; i++) {
                final float s1 = Math.signum(this.coords[i]);
                final float s2 = Math.signum(point.coords[i]);

                if (s1 == s2) {
                    same++;
                } else if (s1 == 0 || s2 == 0) {
                    zero++;
                }
            }

            return (same + zero == dimension);
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final int count = scanner.nextInt();
        final int n = scanner.nextInt();

        System.err.printf("%d %d%n", count, n);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<Point> points = new ArrayList<>(n);

        for (int i = 0; i < count; i++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            final List<String> list = new ArrayList<>(Arrays.asList(line.split(" ")));

            points.add(new Point(list.remove(0), list.stream().mapToInt(s -> Integer.parseInt(s)).toArray()));
        }

        final int[] zeroes = new int[n];

        Arrays.fill(zeroes, 0);

        Point current = new Point("", zeroes);

        String result = "";

        while (!points.isEmpty()) {
            Point nextPoint = null;
            double minDistance = Double.MAX_VALUE;

            for (Point point : points) {
                final double distance = current.distance(point);

                if (distance < minDistance) {
                    minDistance = distance;
                    nextPoint = point;
                }
            }

            points.remove(nextPoint);

            if (!current.sameOrthant(nextPoint)) {
                result += " ";
            }

            result = result + nextPoint.letter;

            current = nextPoint;
        }

        System.out.println(result.trim());
    }
}
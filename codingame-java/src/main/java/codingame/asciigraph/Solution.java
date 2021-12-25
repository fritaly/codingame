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
package codingame.asciigraph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Solution {

    private static class Point {
        final int x, y;

        private Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();

        System.err.println(n);

        final List<Point> points = IntStream.range(0, n)
                .mapToObj(i -> new Point(scanner.nextInt(), scanner.nextInt()))
                .collect(toList());

        final int minX = Math.min(0, points.stream().mapToInt(p -> p.x).min().orElse(0)) - 1;
        final int minY = Math.min(0, points.stream().mapToInt(p -> p.y).min().orElse(0)) - 1;
        final int maxX = Math.max(0, points.stream().mapToInt(p -> p.x).max().orElse(0)) + 1;
        final int maxY = Math.max(0, points.stream().mapToInt(p -> p.y).max().orElse(0)) + 1;

        for (int y = maxY; y >= minY ; y--) {
            for (int x = minX; x <= maxX ; x++) {
                final int x1 = x, y1 = y;

                final Optional<Point> result = points.stream().filter(p -> (p.x == x1) && (p.y == y1)).findFirst();

                if (result.isPresent()) {
                    points.remove(points);

                    System.out.print("*");

                    continue;
                }

                if (x == 0 && y == 0) {
                    System.out.print("+");
                } else if (x == 0) {
                    System.out.print("|");
                } else if (y == 0) {
                    System.out.print("-");
                } else {
                    System.out.print(".");
                }
            }

            System.out.println("");
        }
    }
}
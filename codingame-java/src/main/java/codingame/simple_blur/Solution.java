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
package codingame.simple_blur;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Solution {

    private static class Color {
        final int r, g, b;

        private Color(Scanner scanner) {
            this.r = scanner.nextInt();
            this.g = scanner.nextInt();
            this.b = scanner.nextInt();
        }

        private Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        @Override
        public String toString() {
            return String.format("(%d, %d, %d)", r, g, b);
        }
    }

    private static Color average(List<Color> colors) {
        double r = 0, g = 0, b = 0;

        for (Color color : colors) {
            r += color.r;
            g += color.g;
            b += color.b;
        }

        return new Color((int) Math.floor(r / colors.size()),
                (int) Math.floor(g / colors.size()),
                (int) Math.floor(b / colors.size()));
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt(), width = scanner.nextInt();

        final Color[][] image = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image[y][x] = new Color(scanner);
            }
        }

        final Color[][] output = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final List<Color> pixels = new ArrayList<>();
                pixels.add(image[y][x]);

                if (y > 0) {
                    pixels.add(image[y - 1][x]);
                }
                if (y < height - 1) {
                    pixels.add(image[y + 1][x]);
                }
                if (x > 0) {
                    pixels.add(image[y][x - 1]);
                }
                if (x < width - 1) {
                    pixels.add(image[y][x + 1]);
                }

                output[y][x] = average(pixels);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                System.out.printf("%d %d %d%n", output[y][x].r, output[y][x].g, output[y][x].b);
            }
        }
    }
}
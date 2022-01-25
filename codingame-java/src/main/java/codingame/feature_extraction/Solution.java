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
package codingame.feature_extraction;

import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class Array {

        final int width, height;
        final int[][] array;

        Array(Scanner scanner) {
            this.height = scanner.nextInt();
            this.width = scanner.nextInt();

            System.err.printf("%d %d%n", width, height);

            this.array = new int[height][width];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    array[y][x] = scanner.nextInt();

                    System.err.printf("%d ", array[y][x]);
                }

                System.err.println();
            }
        }

        int compute(Array kernel, int px, int py) {
            int result = 0;

            for (int y = 0; y < kernel.height; y++) {
                for (int x = 0; x < kernel.width; x++) {
                    result += array[py + y][px + x] * kernel.array[y][x];
                }
            }

            return result;
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final Array image = new Array(scanner);
        final Array kernel = new Array(scanner);

        final int[][] output = new int[image.height - kernel.height + 1][image.width - kernel.width + 1];

        for (int y = 0; y < image.height - kernel.height + 1; y++) {
            for (int x = 0; x < image.width - kernel.width + 1; x++) {
                output[y][x] = image.compute(kernel, x, y);
            }
        }

        for (int y = 0; y < output.length; y++) {
            System.out.println(toString(output[y]));
        }
    }

    private static String toString(int[] array) {
        return IntStream.of(array).mapToObj(i -> Integer.toString(i)).collect(Collectors.joining(" "));
    }
}
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
package codingame.the_hungry_duck_part2;

import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt(), height = scanner.nextInt();

        final int[][] food = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                food[y][x] = scanner.nextInt();
            }
        }

        final int[][] output = new int[height][width];

        for (int x = 0; x < width; x++) {
            if (x == 0) {
                output[0][x] = food[0][x];
            } else {
                output[0][x] = food[0][x] + output[0][x-1];
            }
        }

        for (int y = 0; y < height; y++) {
            if (y == 0) {
                output[y][0] = food[y][0];
            } else {
                output[y][0] = food[y][0] + output[y-1][0];
            }
        }

        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width; x++) {
                final int top = food[y][x] + output[y-1][x];
                final int left = food[y][x] + output[y][x-1];

                output[y][x] = Math.max(left, top);
            }
        }

        System.out.println(output[height - 1][width - 1]);
    }
}
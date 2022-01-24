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
package codingame.magic_square;

import java.util.*;
import java.util.stream.IntStream;

class Solution {

    private static int sumColumn(int[][] array, int x) {
        return IntStream.range(0, array.length).map(y -> array[y][x]).sum();
    }

    private static int sumRow(int[][] array, int y) {
        return IntStream.range(0, array.length).map(x -> array[y][x]).sum();
    }

    private static int sumDiag1(int[][] array) {
        return IntStream.range(0, array.length).map(n -> array[n][n]).sum();
    }

    private static int sumDiag2(int[][] array) {
        return IntStream.range(0, array.length).map(n -> array[array.length - 1 - n][n]).sum();
    }

    private static boolean contains(int[][] array, int value) {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array.length; x++) {
                if (array[y][x] == value) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isMagic(int[][] array) {
        // The array contains the values [1 .. n]
        final int n = array.length * array.length;

        // Expected sum per column / row / diagonal
        final int expectedSum = (n * (n + 1)) / 2 / array.length;

        for (int p = 0; p < array.length; p++) {
            if (sumRow(array, p) != expectedSum) {
                return false;
            }
            if (sumColumn(array, p) != expectedSum) {
                return false;
            }
        }

        if (sumDiag1(array) != expectedSum) {
            return false;
        }
        if (sumDiag2(array) != expectedSum) {
            return false;
        }

        if (!IntStream.range(1, n + 1).allMatch(i -> contains(array, i))) {
            return false;
        }

        return true;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();

        final int[][] array = new int[n][n];

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                array[y][x] = scanner.nextInt();
            }
        }

        System.out.println(isMagic(array) ? "MAGIC" : "MUGGLE");
    }
}
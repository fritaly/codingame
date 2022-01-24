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
package codingame.cards_castle;

import java.util.Scanner;

class Solution {

    private static boolean isStable(char[][] array, int x, int y) {
        final char currentChar = array[y][x];

        if (currentChar == '.') {
            return false;
        }
        if (currentChar == '/') {
            if (x > 0) {
                final char leftChar = array[y][x-1];

                if (leftChar == '/') {
                    // "//" is unstable
                    return false;
                }
            }
            if (x < array[y].length - 1) {
                final char rightChar = array[y][x+1];

                if ((rightChar == '/') || (rightChar == '.')) {
                    // "//" and "/." are unstable
                    return false;
                }
            }

            if (y < array.length - 1) {
                // Check the character below
                final char belowChar = array[y+1][x];

                if (belowChar != '\\') {
                    return false;
                }

                if (!isStable(array, x, y+1)) {
                    return false;
                }
            }

            return true;
        }

        if (currentChar == '\\') {
            if (x > 0) {
                final char leftChar = array[y][x-1];

                if ((leftChar == '\\') || (leftChar == '.')) {
                    // "\\" and ".\" are unstable
                    return false;
                }
            }
            if (x < array[y].length - 1) {
                final char rightChar = array[y][x+1];

                if ((rightChar == '\\')) {
                    // "\\" is unstable
                    return false;
                }
            }

            if (y < array.length - 1) {
                // Check the character below
                final char belowChar = array[y+1][x];

                if (belowChar != '/') {
                    return false;
                }

                if (!isStable(array, x, y+1)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt();

        System.err.println(height);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] array = new char[height][];

        for (int y = 0; y < height; y++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            array[y] = line.toCharArray();
        }

        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                if ((array[y][x] != '.') && !isStable(array, x, y)) {
                    System.out.println("UNSTABLE");

                    return;
                }
            }
        }

        System.out.println("STABLE");
    }
}
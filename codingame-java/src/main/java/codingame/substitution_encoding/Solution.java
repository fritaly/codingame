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
package codingame.substitution_encoding;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private static String encode(char[][] table, char c) {
        for (int y = 0; y < table.length; y++) {
            for (int x = 0; x < table[y].length; x++) {
                if (table[y][x] == c) {
                    return "" + y + x;
                }
            }
        }

        return null;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int rows = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] table = new char[rows][];

        for (int y = 0; y < rows; y++) {
            table[y] = scanner.nextLine().replaceAll(" ", "").toCharArray();
        }

        final String message = scanner.nextLine();

        System.out.println(message.chars().mapToObj(c -> encode(table, (char) c)).collect(Collectors.joining()));;
    }
}
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
package codingame.shadow_casting;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private static void fill(char[][] source, char[][] output, int d) {
        for (int y = 0; y < source.length; y++) {
            for (int x = 0; x < source[y].length; x++) {
                if (source[y][x] != ' ') {
                    output[y+d][x+d] = (d == 2) ? '`' : (d == 1) ? '-' : source[y][x];
                }
            }
        }
    }

    private static String trimRight(String s) {
        int index = s.length() - 1;

        while (s.charAt(index) == ' ') {
            index--;
        }

        return s.substring(0, index + 1);
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int N = scanner.nextInt();

        System.err.println(N);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] grid = new char[N][];

        int width = 0;

        for (int y = 0; y < N; y++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            grid[y] = line.toCharArray();

            width = Math.max(width, grid[y].length);
        }

        final char[][] output = new char[N+2][];

        for (int y = 0; y < N + 2; y++) {
            output[y] = new char[width + 2];

            Arrays.fill(output[y], ' ');
        }

        for (int i = 2; i >= 0; i--) {
            fill(grid, output,  i);
        }

        System.out.println(Arrays.asList(output).stream().map(a -> trimRight(new String(a))).collect(Collectors.joining("\n")));
    }
}
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

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static String padRight(String s, int length) {
        return (s.length() >= length) ? s : padRight(s + " ", length);
    }

    private static String trimRight(String s) {
        return s.endsWith(" ") ? trimRight(s.substring(0, s.length() - 1)) : s;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt();

        System.err.println(height);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<String> lines = IntStream.range(0, height)
                .mapToObj(n -> scanner.nextLine())
                .collect(Collectors.toList());

        final int width = lines.stream().mapToInt(s -> s.length()).max().orElse(0);

        final char[][] grid = new char[height + 2][width + 2];

        for (int y = 0; y < height; y++) {
            final String line = lines.get(y);

            System.err.println(line);

            grid[y] = padRight(line, width + 2).toCharArray();
        }

        grid[height] = padRight("", width + 2).toCharArray();
        grid[height + 1] = padRight("", width + 2).toCharArray();

        for (int y = 0; y < height + 2; y++) {
            for (int x = 0; x < width + 2; x++) {
                final char c = grid[y][x];

                if ((y == height + 1) || (x == width + 1)) {
                    continue;
                }

                if (grid[y+1][x+1] == ' ') {
                    if (c == '#') {
                        grid[y+1][x+1] = '-';
                    } else if (c == '-') {
                        grid[y+1][x+1] = '`';
                    }
                }
            }
        }

        for (int y = 0; y < height + 2; y++) {
            System.out.println(trimRight(new String(grid[y])));
        }
    }
}
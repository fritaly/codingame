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
package codingame.object_insertion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static boolean match(char[][] map, char[][] object, int offsetX, int offsetY, boolean copy) {
        for (int y = 0; y < object.length; y++) {
            for (int x = 0; x < object[0].length; x++) {
                final char mapChar = map[offsetY + y][offsetX + x];
                final char objectChar = object[y][x];

                if (objectChar == '.') {
                    continue;
                }

                if (mapChar != '.') {
                    return false;
                }

                if (copy) {
                    map[offsetY + y][offsetX + x] = objectChar;
                }
            }
        }

        return true;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int objectHeight = scanner.nextInt(), objectWidth = scanner.nextInt();

        System.err.printf("%d %d%n", objectHeight, objectWidth);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] object = new char[objectHeight][objectWidth];

        for (int y = 0; y < objectHeight; y++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            object[y] = line.toCharArray();
        }

        final int height = scanner.nextInt(), width = scanner.nextInt();

        System.err.printf("%d %d%n", height, width);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] map = new char[height][width];

        for (int y = 0; y < height; y++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            map[y] = line.toCharArray();
        }

        final List<Position> list = new ArrayList<>();

        for (int y = 0; y <= height - objectHeight; y++) {
            for (int x = 0; x <= width - objectWidth; x++) {
                if (match(map, object, x, y, false)) {
                    list.add(new Position(x, y));
                }
            }
        }

        System.out.println(list.size());

        if (list.size() == 1) {
            final Position p = list.get(0);

            match(map, object, p.x, p.y, true);

            System.out.println(Arrays.asList(map)
                    .stream()
                    .map(a -> new String(a))
                    .collect(Collectors.joining("\n")));
        }
    }
}
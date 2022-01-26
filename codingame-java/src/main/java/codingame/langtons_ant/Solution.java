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
package codingame.langtons_ant;

import java.util.Scanner;

class Solution {

    private enum Direction {
        N, S, W, E;

        Direction left() {
            switch (this) {
                case E:
                    return N;
                case S:
                    return E;
                case W:
                    return S;
                case N:
                    return W;
            }

            throw new RuntimeException();
        }

        Direction right() {
            switch (this) {
                case E:
                    return S;
                case S:
                    return W;
                case W:
                    return N;
                case N:
                    return E;
            }

            throw new RuntimeException();
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt(), height = scanner.nextInt();
        int x = scanner.nextInt(), y = scanner.nextInt();

        Direction direction = Direction.valueOf(scanner.next());

        int turns = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] grid = new char[height][width];

        for (int i = 0; i < height; i++) {
            grid[i] = scanner.nextLine().toCharArray();
        }

        for (int i = 0; i < turns; i++) {
            direction = (grid[y][x] == '#') ? direction.right() : direction.left();
            grid[y][x] = (grid[y][x] == '#') ? '.' : '#';
            switch (direction) {
                case E:
                    x++;
                    break;
                case N:
                    y--;
                    break;
                case W:
                    x--;
                    break;
                case S:
                    y++;
                    break;
            }
        }

        for (int i = 0; i < height; i++) {
            System.out.println(new String(grid[i]));
        }
    }
}
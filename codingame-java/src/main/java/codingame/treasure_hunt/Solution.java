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
package codingame.treasure_hunt;

import java.util.*;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static int search(char[][] map, Position p) {
        final List<Position> candidates = new ArrayList<>();

        if ((p.y > 0) && (map[p.y - 1][p.x] != '#')) {
            candidates.add(new Position(p.x, p.y - 1)); // Up
        }
        if ((p.x > 0) && (map[p.y][p.x - 1] != '#')) {
            candidates.add(new Position(p.x - 1, p.y)); // Left
        }
        if ((p.y < map.length - 1) && (map[p.y + 1][p.x] != '#')) {
            candidates.add(new Position(p.x, p.y + 1)); // Down
        }
        if ((p.x < map[0].length - 1) && (map[p.y][p.x + 1] != '#')) {
            candidates.add(new Position(p.x + 1, p.y)); // Right
        }

        int maxGold = 0;

        for (Position candidate : candidates) {
            int gold = 0;

            if (Character.isDigit(map[candidate.y][candidate.x])) {
                gold += map[candidate.y][candidate.x] - '0';
            }

            // Save the character for later
            final char backup = map[candidate.y][candidate.x];

            // Ensure the position can't be traversed again
            map[candidate.y][candidate.x] = '#';

            gold += search(map, candidate);

            // Restore the initial map state
            map[candidate.y][candidate.x] = backup;

            maxGold = Math.max(gold, maxGold);
        }

        return maxGold;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt(), width = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] map = new char[height][];

        Position start = null;

        for (int y = 0; y < height; y++) {
            map[y] = scanner.nextLine().toCharArray();

            for (int x = 0; x < map[y].length; x++) {
                if (map[y][x] == 'X') {
                    start = new Position(x, y);
                }
            }
        }

        map[start.y][start.x] = '#';

        System.out.println(search(map, start));
    }
}
/*
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
package codingame

class DungeonMap {

    final char[][] map

    final int width, height

    DungeonMap(Scanner scanner, int width, int height) {
        map = new char[height][width]
        this.width = width
        this.height = height

        for (int y = 0; y < height; y++) {
            def row = scanner.nextLine()

            for (int x = 0; x < width; x++) {
                map[y][x] = row.charAt(x)
            }

            System.err.println("${row}")
        }
    }

    int walk(int x, int y) {
        def steps = 0

        while (true) {
            if ((y < 0) || (y >= height)) {
                // Invalid position
                return -1
            }
            if ((x < 0) || (x >= width)) {
                // Invalid position
                return -1
            }

            def c = map[y][x]

            switch (c) {
                case 'T':
                    return steps
                case '>':
                    map[y][x] = 'X' // Mark the position as visited
                    x++
                    break
                case '<':
                    map[y][x] = 'X' // Mark the position as visited
                    x--
                    break
                case 'v':
                    map[y][x] = 'X' // Mark the position as visited
                    y++
                    break
                case '^':
                    map[y][x] = 'X' // Mark the position as visited
                    y--
                    break
                case 'X': // Position already visited
                case '#':
                case '.':
                    return -1
            }

            steps++
        }
    }
}

def input = new Scanner(System.in)

def width = input.nextInt()
def height  = input.nextInt()
def startY = input.nextInt()
def startX = input.nextInt()

def mapCount = input.nextInt()

input.nextLine()

def minSteps = Integer.MAX_VALUE, solution = -1

for (i = 0; i < mapCount; ++i) {
    def map = new DungeonMap(input, width, height)

    def steps = map.walk(startX, startY)

    if (steps != -1) {
        if (steps < minSteps) {
            minSteps = steps
            solution = i
        }
    }
}

println "${(solution == -1) ? 'TRAP' : solution}"
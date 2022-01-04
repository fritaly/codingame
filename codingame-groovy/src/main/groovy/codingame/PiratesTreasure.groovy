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

def input = new Scanner(System.in)

def width = input.nextInt()
def height = input.nextInt()

System.err.println("${width}")
System.err.println("${height}")

def map = new int[height][width]

for (y = 0; y < height; y++) {
    for (x = 0; x < width; x++) {
        def v = input.nextInt()

        map[y][x] = v
    }

    System.err.println("${map[y].join(' ')}")
}

def found = false

for (y = 0; !found && (y < height); y++) {
    for (x = 0; !found && (x < width); x++) {
        def score = 0

        for (dy = -1; dy <= 1; dy++) {
            for (dx = -1; dx <= 1; dx++) {
                if ((dx == 0) && (dy == 0)) {
                    continue
                }

                def y2 = y + dy
                def x2 = x + dx

                if ((y2 < 0) || (y2 > height - 1)) {
                    continue
                }
                if ((x2 < 0) || (x2 > width - 1)) {
                    continue
                }

                score += map[y2][x2]
            }
        }

        def topOrBottom = (y == 0) || (y == height - 1)
        def leftOrRight = (x == 0) || (x == width - 1)

        if (topOrBottom && leftOrRight && (score == 3)) {
            println "${x} ${y}"

            found = true
        } else if ((topOrBottom || leftOrRight) && (score == 5)) {
            println "${x} ${y}"

            found = true
        } else if (score == 8) {
            println "${x} ${y}"

            found = true
        }
    }
}
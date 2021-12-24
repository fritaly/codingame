/*
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
package codingame.sudokuvalidator


class Grid {
    int[][] array = new int[9][9]

    Grid(Scanner input) {
        for (def x = 0; x < 9; x++) {
            for (def y = 0; y < 9; y++) {
                def n = input.nextInt()

                array[y][x] = n
            }
        }
    }

    boolean test() {
        def template = [ 1, 2, 3, 4, 5, 6, 7, 8, 9 ].toSet()

        for (int y = 0; y < array.length; y++) {
            def set = new TreeSet(template)

            for (int x = 0; x < array.length; x++) {
                set.remove(array[y][x])
            }

            if (!set.empty) {
                return false
            }
        }

        for (int x = 0; x < array.length; x++) {
            def set = new TreeSet(template)

            for (int y = 0; y < array.length; y++) {
                set.remove(array[y][x])
            }

            if (!set.empty) {
                return false
            }
        }

        for (x in [ 1, 4, 7 ]) {
            for (y in [ 1, 4, 7 ]) {
                def set = new TreeSet(template)

                for (xn in [ -1, 0, +1]) {
                    for (yn in [ -1, 0, +1]) {
                        set.remove(array[y + yn][x + xn])
                    }
                }

                if (!set.empty) {
                    return false
                }
            }
        }


        true
    }
}

def input = new Scanner(System.in)

def grid = new Grid(input)

println Boolean.toString(grid.test())
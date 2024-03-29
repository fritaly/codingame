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

private void dump(char[][] array, PrintStream stream) {
    stream.println array.collect { row -> new String(row) }.join('\n')
}

private void gravity(char[][] array) {
    // Let the #s fall
    for (int x=0; x < array[0].length; x++) {
        def dots = 0, hashes = 0
        for (int y=0; y < array.length; y++) {
            if (array[y][x] == '.') {
                dots++
            }
            if (array[y][x] == '#') {
                hashes++
            }
        }
        for (int y=0; y < array.length; y++) {
            if (y < dots) {
                array[y][x] = '.'
            } else {
                array[y][x] = '#'
            }
        }
    }
}

def input = new Scanner(System.in)

def width = input.nextInt()
def height = input.nextInt()

def array = new char[height][width]

for (i = 0; i < height; ++i) {
    def line = input.next()

    for (j = 0; j < width; j++) {
        array[i][j] = line.charAt(j)
    }
}

dump(array, System.err)

gravity(array)

dump(array, System.out)
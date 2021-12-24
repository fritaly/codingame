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
package codingame

class Letter {
    char character
    boolean crossed

    Letter(char character) {
        this.character = character
    }
}

class Grid {
    Letter[][] array

    Grid(int height, int width) {
        this.array = new Letter[height][width]
    }

    int getHeight() {
        array.length
    }

    int getWidth() {
        array[0].length
    }

    void dump(boolean letter) {
        array.each { row ->
            System.err.println row.collect { letter ? "${it.character}" : (it.crossed ? 'X' : '.') }.join('')
        }
    }

    boolean containsHorizontally(int x, int y, String word) {
        if (x + word.length() > width) {
            return false
        }

        for (int n = 0; n < word.length(); n++) {
            if (array[y][x + n].character != word.charAt(n)) {
                return false;
            }
        }

        for (int n = 0; n < word.length(); n++) {
            array[y][x + n].crossed = true
        }

        true
    }

    boolean containsVertically(int x, int y, String word) {
        if (y + word.length() > height) {
            return false
        }

        for (int n = 0; n < word.length(); n++) {
            if (array[y + n][x].character != word.charAt(n)) {
                return false;
            }
        }

        for (int n = 0; n < word.length(); n++) {
            array[y + n][x].crossed = true
        }

        true
    }

    boolean containsDiagonally1(int x, int y, String word) {
        if (x + word.length() > width) {
            return false
        }
        if (y + word.length() > height) {
            return false
        }

        for (int n = 0; n < word.length(); n++) {
            if (array[y + n][x + n].character != word.charAt(n)) {
                return false;
            }
        }

        for (int n = 0; n < word.length(); n++) {
            array[y + n][x + n].crossed = true
        }

        true
    }

    boolean containsDiagonally2(int x, int y, String word) {
        if (x + word.length() > width) {
            return false
        }
        if (y - word.length() < 0) {
            return false
        }

        for (int n = 0; n < word.length(); n++) {
            if (array[y - n][x + n].character != word.charAt(n)) {
                return false;
            }
        }

        for (int n = 0; n < word.length(); n++) {
            array[y - n][x + n].crossed = true
        }

        true
    }
}

def input = new Scanner(System.in)
def n = input.nextInt()

System.err.println("${n}")

def words = [] as Set<String>

for (i = 0; i < n; ++i) {
    def word = input.next()

    System.err.println("${word}")

    words << word
}

def height = input.nextInt()
def width = input.nextInt()

def grid = new Grid(height, width)

System.err.println("${height} ${width}")

for (i = 0; i < height; ++i) {
    def line = input.next()

    System.err.println("${line}")

    line.toCharArray().eachWithIndex{ char c, int j ->
        grid.array[i][j] = new Letter(c)
    }
}

grid.dump(true)

System.err.println("Words: ${words}")

// Add reverse words to the list of words to search
words.addAll(words.collect { it.reverse() })

System.err.println("Words (with reverse): ${words}")

// Search words horizontally
for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
        // Clone the list to avoid ConcurrentModificationExceptions
        new ArrayList<>(words).each { word ->
            if (grid.containsHorizontally(x, y, word) || grid.containsVertically(x, y, word)
                    || grid.containsDiagonally1(x, y, word) || grid.containsDiagonally2(x, y, word)) {

                // Each word is found only once in the grid, remove it when found
                words.remove(word)
            }
        }
    }
}

grid.dump(false)

for (int y = 0; y < height; y++) {
    for (int x = 0; x < width; x++) {
        if (!grid.array[y][x].crossed) {
            print "${grid.array[y][x].character}"
        }
    }
}

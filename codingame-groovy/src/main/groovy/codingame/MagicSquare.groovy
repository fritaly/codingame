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

static void dump(int[][] array) {
    array.each {
        System.err.println it.join('\t')
    }
}

static int row(int[][] array, int y) {
    array[y].sum()
}

static int column(int[][] array, int x) {
    def sum = 0

    for (int y = 0; y < array.length; y++) {
        sum += array[y][x]
    }

    sum
}

static int diag1(int[][] array) {
    def sum = 0

    for (int n = 0; n < array.length; n++) {
        sum += array[n][n]
    }

    sum
}

static int diag2(int[][] array) {
    def sum = 0

    for (int n = 0; n < array.length; n++) {
        sum += array[array.length - n - 1][n]
    }

    sum
}

def input = new Scanner(System.in)

def n = input.nextInt()

def array = new int[n][n]
def integers = [] as Set<Integer>

for (i = 0; i < n; ++i) {
    for (j = 0; j < n; ++j) {
        def c = input.nextInt()

        array[i][j] = c
        integers << c
    }
}

dump(array)

if (integers.size() == n * n) {
    def sums = [] as Set<Integer>

    for (int y = 0; y < array.length ; y++) {
        sums << row(array, y)
    }
    for (int x = 0; x < array.length ; x++) {
        sums << column(array, x)
    }
    sums << diag1(array) << diag2(array)

    println "${sums.size() == 1 ? 'MAGIC' : 'MUGGLE'}"
} else {
    println 'MUGGLE'
}
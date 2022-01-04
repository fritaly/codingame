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

class Range {
    final double start, end

    // Average is in range [0, 1]
    Range(double average) {
        this.start = average - 0.1 // -10%
        this.end = average + 0.1 // +10%
    }

    boolean contains(double value) {
        (start <= value) && (value <= end)
    }
}

def input = new Scanner(System.in)

def N = input.nextInt()

input.nextLine()

// Array used for counting the number of transactions starting with '0', '1', '2', etc
def counts = new int[10]

(0..9).each { index ->
    counts[index = 0]
}

// 1: 30.1%
// 2: 17.6%
// 3: 12.5%
// 4: 9.7%
// 5: 7.9%
// 6: 6.7%
// 7: 5.8%
// 8: 5.1%
// 9: 4.6%
def ranges = [ 0.301, 0.176, 0.125, 0.097, 0.079, 0.067, 0.058, 0.051, 0.046 ].collect { new Range(it) }

for (i = 0; i < N; ++i) {
    def transaction = input.nextLine()

    // System.err.println("${transaction}")

    // Find the position of the first digit in the transaction
    def index = 0

    while (!Character.isDigit(transaction.charAt(index))) {
        index++
    }

    counts[transaction.charAt(index) - '0'.charAt(0)]++
}

System.err.println("Total: ${N}")
System.err.println("${counts}")

def fraudulent = !(1..9).every { i ->
    // Ratio of transactions starting with the ith digit
    def ratio = ((double) counts[i]) / N

    ranges[i - 1].contains(ratio)
}

println fraudulent
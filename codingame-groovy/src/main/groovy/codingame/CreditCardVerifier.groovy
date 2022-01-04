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

def n = input.nextInt()

input.nextLine()

for (i = 0; i < n; ++i) {
    def card = input.nextLine().replace(' ', '') // Remove blanks

    // System.err.println("${card}")

    def list = []

    card.reverse().toCharArray().eachWithIndex{ char c, int i ->
        if (i % 2 == 1) {
            def value = 2 * (c - '0'.charAt(0))

            list << ((value > 9) ? (value - 9) : value)
        }
    }

    def sum = list.sum(0)

    list = []

    card.reverse().toCharArray().eachWithIndex{ char c, int i ->
        if (i % 2 == 0) {
            list << (c - '0'.charAt(0))
        }
    }

    def sum2 = list.sum()

    println "${(sum + sum2) % 10 == 0 ? 'YES' : 'NO'}"
}
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

input = new Scanner(System.in);

N = input.nextInt()
input.nextLine()

def invalids = []

for (i = 0; i < N; ++i) {
    def isbn = input.nextLine()

    System.err.println("=== ${isbn} ===")

    if (isbn.matches('[0-9]{9}[0-9X]')) {
        def value = 0

        isbn.toCharArray()[0..-2].eachWithIndex { c, index ->
            value += (Integer.parseInt(Character.toString(c)) * (10 - index))
        }

        value = (value % 11)

        System.err.println("Value: ${value}")

        def expected = (value == 0) ? '0' : (value == 1) ? 'X' : "${11 - value}"

        System.err.println("Expected: ${expected}")

        if (!isbn.endsWith(expected)) {
            invalids << isbn
        }

    } else if (isbn.matches('[0-9]{13}')) {
        def weight = 1, value = 0

        isbn.toCharArray()[0..-2].eachWithIndex{ c, index ->
            value += (Integer.parseInt(Character.toString(c)) * weight)

            weight = (weight == 1) ? 3 : 1
        }

        value = value % 10

        System.err.println("Value: ${value}")

        def expected = (value == 0) ? '0' : "${10 - value}"

        System.err.println("Expected: ${expected}")

        if (!isbn.endsWith(expected)) {
            invalids << isbn
        }
    } else {
        invalids << isbn
    }
}

println "${invalids.size()} invalid:"

invalids.each {
    println it
}
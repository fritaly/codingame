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

def n = input.nextInt() // number of mappings

def map = [:]

for (i = 0; i < n; ++i) {
    def binary = input.next()
    def character = input.nextInt() as char

    map[binary] = "${character}"
}

System.err.println("Map: ${map}")

def encoded = input.next()

String decode(String input, int index, Map<String, String> mappings, String result) {
    if (input == '') {
        return result
    }

    for (entry in mappings.entrySet()) {
        def key = entry.key
        def character = entry.value

        if (input.startsWith(key)) {
            def decoded = decode(input.substring(key.length()), index + key.length(), mappings, result + character)

            if (decoded) {
                return decoded
            }
        }
    }

    "DECODE FAIL AT INDEX ${index}"
}

println decode(encoded, 0, map, "")
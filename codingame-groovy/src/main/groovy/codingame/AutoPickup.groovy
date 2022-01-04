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

def input = new Scanner(System.in);
def n = input.nextInt()
def packet = input.next()

def output = new StringBuilder()

// [instruction id (3 bits)][packet length (4 bits)][packet info (packet length bits)]

while (packet) {
    def instruction = packet.substring(0, 3)

    // Skip the instruction id
    packet = packet.substring(3)

    def lengthAsText = packet.substring(0, 4)
    def length = Integer.parseInt(lengthAsText, 2)

    // Skip the packet length
    packet = packet.substring(4)

    def id = packet.substring(0, length)

    packet = packet.substring(length)

    if (instruction == '101') {
        output << '001' << lengthAsText << id
    }
}

println output.toString()
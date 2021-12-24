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

enum Month {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
}

def input = new Scanner(System.in)

def N = input.nextInt()
input.nextLine()

int decode(String s) {
    def result = 0

    while (s) {
        def temp = s.substring(0, 3)
        s = s.substring(3)

        result = (result * 12) + Month.valueOf(temp).ordinal()
    }

    result
}

String encode(int value) {
    def result = ''

    while (value > 0) {
        def temp = value % 12

        value = ((value - temp) / 12)

        result = Month.values()[temp].name() + result
    }

    result
}

def sum = 0

for (i = 0; i < N; ++i) {
    sum += decode(input.nextLine())
}

println encode(sum)
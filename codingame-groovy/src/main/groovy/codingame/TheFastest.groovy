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

def input = new Scanner(System.in)

def N = input.nextInt()

def times = []

for (i = 0; i < N; ++i) {
    times << input.next()
}

def solution = times.min { time ->
    def array = time.split(':')

    (Integer.parseInt(array[0]) * 3600) + (Integer.parseInt(array[1]) * 60) + Integer.parseInt(array[2])
}

println solution
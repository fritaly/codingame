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

class Pattern {

    String pattern
    int tempo

    Pattern(String pattern, int tempo) {
        this.pattern = pattern
        this.tempo = tempo
    }

    boolean matches(int n) {
        (n % tempo == 0)
    }

    String merge(String other) {
        [ 0, 1, 2, 3 ].collect { "${(pattern.charAt(it) == 'X') || (other.charAt(it) == 'X') ? 'X' : '0'}" }.join('')
    }
}

def input = new Scanner(System.in)

def L = input.nextInt()
def N = input.nextInt()

def patterns = [] as List<Pattern>

for (i = 0; i < N; ++i) {
    def pattern = input.next()
    def tempo = input.nextInt()

    patterns << new Pattern(pattern, tempo)
}

for (i = L; i >= 1; i--) {
    def result = '0000'

    patterns.findAll { it.matches(i) }.each { result = it.merge(result) }

    println result
}
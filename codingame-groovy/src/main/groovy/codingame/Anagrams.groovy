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

private static String step4(String phrase) {
    // 4) Count the number of letters in each word, and reverse that list of numbers, re-applying the revised word
    // lengths to the letter sequence
    def lengths = phrase.split(' ').collect { it.length() }.reverse()

    phrase = phrase.replace(' ', '')

    def start = 0

    lengths.collect { length ->
        def word = phrase.substring(start, start + length)

        start += length

        word
    }.join(' ')
}

private static String step3(String phrase) {
    // 3) Find every 4th letter of the alphabet in the phrase (D, H, L, etc.) and shift their positions one to the left,
    // with the first letter wrapped around to the last position
    def letters = new LinkedList<Character>()

    phrase.toCharArray().each { c ->
        if ((c == 'D') || (c == 'H') || (c == 'L') || (c == 'P') || (c == 'T') || (c == 'X')) {
            letters << c
        }
    }

    if (letters) {
        // Shift the last letter to the right
        letters.addFirst(letters.removeLast())
    }

    phrase.toCharArray().collect { c ->
        if ((c == 'D') || (c == 'H') || (c == 'L') || (c == 'P') || (c == 'T') || (c == 'X')) {
            letters.removeFirst()
        } else {
            c
        }
    }.join('')
}

private static String step2(String phrase) {
    // Find every 3rd letter of the alphabet in the phrase (C, F, I, etc.) and shift their positions one to the right,
    // with the last letter wrapped around to the first position.
    def letters = new LinkedList<Character>()

    phrase.toCharArray().each { c ->
        if ((c == 'C') || (c == 'F') || (c == 'I') || (c == 'L') || (c == 'O') || (c == 'R') || (c == 'U') || (c == 'X')) {
            letters << c
        }
    }

    if (letters) {
        // Shift the first letter to the left
        letters.addLast(letters.removeFirst())
    }

    phrase.toCharArray().collect { c ->
        if ((c == 'C') || (c == 'F') || (c == 'I') || (c == 'L') || (c == 'O') || (c == 'R') || (c == 'U') || (c == 'X')) {
            letters.removeFirst()
        } else {
            c
        }
    }.join('')
}

private static String step1(String phrase) {
    // Find every 2nd letter of the alphabet in the phrase (B, D, F, etc.) and reverse their order within the phrase.
    def letters = new LinkedList<Character>()

    phrase.toCharArray().each { c ->
        if ((c == 'B') || (c == 'D') || (c == 'F') || (c == 'H') || (c == 'J') || (c == 'L') || (c == 'N') || (c == 'P') || (c == 'R') || (c == 'T') || (c == 'V') || (c == 'X') || (c == 'Z')) {
            letters << c
        }
    }

    // Reverse the letters
    letters = letters.reverse() as LinkedList<Character>

    phrase.toCharArray().collect { c ->
        if ((c == 'B') || (c == 'D') || (c == 'F') || (c == 'H') || (c == 'J') || (c == 'L') || (c == 'N') || (c == 'P') || (c == 'R') || (c == 'T') || (c == 'V') || (c == 'X') || (c == 'Z')) {
            letters.removeFirst()
        } else {
            c
        }
    }.join('')
}

def input = new Scanner(System.in)

def phrase = input.nextLine()

System.err.println("Phase: ${phrase}")

phrase = step4(phrase)

System.err.println("Phase: ${phrase}")

phrase = step3(phrase)

System.err.println("Phase: ${phrase}")

phrase = step2(phrase)

System.err.println("Phase: ${phrase}")

phrase = step1(phrase)

println phrase
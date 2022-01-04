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

enum Numeral {
    I(1),
    V(5),
    X(10),
    L(50),
    C(100),
    D(500),
    M(1000)

    int value

    Numeral(int value) {
        this.value = value
    }
}

class RomanNumber {
    String representation

    RomanNumber(String string) {
        this.representation = string
    }

    String toString() {
        representation;
    }

    static String toRoman(int value) {
        assert (value > 0)

        def buffer = new StringBuilder()

        while (value > 0) {
            if (value >= 1000) {
                buffer << 'M'

                value -= 1000
            } else if (value >= 900) {
                buffer << 'CM'

                value -= 900
            } else if (value >= 500) {
                buffer << 'D'

                value -= 500
            } else if (value >= 400) {
                buffer << 'CD'

                value -= 400
            } else if (value >= 100) {
                buffer << 'C'

                value -= 100
            } else if (value >= 90) {
                buffer << 'XC'

                value -= 90
            } else if (value >= 50) {
                buffer << 'L'

                value -= 50
            } else if (value >= 40) {
                buffer << 'XL'

                value -= 40
            } else if (value >= 10) {
                buffer << 'X'

                value -= 10
            } else if (value == 9) {
                buffer << 'IX'

                value -= 9
            } else if (value >= 5) {
                buffer << 'V'

                value -= 5
            } else if (value == 4) {
                buffer << 'IV'

                value -= 4
            } else if (value == 3) {
                buffer << 'III'

                value -= 3
            } else if (value == 2) {
                buffer << 'II'

                value -= 2
            } else if (value == 1) {
                buffer << 'I'

                value -= 1
            }
        }

        buffer.toString()
    }

    int getValue() {
        def value = 0

        Numeral previousNumeral = null

        for (char c : representation.toCharArray()) {
            def numeral = Numeral.valueOf(Character.toString(c))

            if (!previousNumeral || (numeral.compareTo(previousNumeral) <= 0)) {
                value += numeral.value
            } else {
                value -= previousNumeral.value
                value += (numeral.value - previousNumeral.value)
            }

            previousNumeral = numeral
        }

        value
    }
}

def input = new Scanner(System.in)

def rom1 = new RomanNumber(input.next())
def rom2 = new RomanNumber(input.next())

println RomanNumber.toRoman(rom1.value + rom2.value)
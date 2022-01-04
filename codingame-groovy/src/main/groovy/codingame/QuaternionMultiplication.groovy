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

class Quaternion {

    private final java.util.regex.Pattern pattern = java.util.regex.Pattern.compile('(?<coeff>[0-9]*)(?<type>i|j|k)?')

    int a, b, c, d

    Quaternion(int a, int b, int c, int d) {
        this.a = a
        this.b = b
        this.c = c
        this.d = d
    }

    Quaternion(String string) {
        assert string

        while (string) {
            def negative = false

            if (string.startsWith('-')) {
                negative = true

                string = string.substring(1)
            } else if (string.startsWith('+')) {
                negative = false

                string = string.substring(1)
            }

            def strings = string.split('[+-]')

            def matcher = pattern.matcher(strings[0])

            if (!matcher.matches()) {
                throw new RuntimeException("Error when matching '${strings[0]}' from '${strings}'")
            }

            def coeff = matcher.group('coeff') // can be null
            def type = matcher.group('type') // can be null

            if (type == null) {
                d = Integer.parseInt(coeff)

                if (negative) {
                    d = -d
                }
            } else if (type == 'i') {
                a = (coeff ? Integer.parseInt(coeff) : 1)

                if (negative) {
                    a = -a
                }
            } else if (type == 'j') {
                b = (coeff ? Integer.parseInt(coeff) : 1)

                if (negative) {
                    b = -b
                }
            } else if (type == 'k') {
                c = (coeff ? Integer.parseInt(coeff) : 1)

                if (negative) {
                    c = -c
                }
            } else {
                throw new RuntimeException("Unexpected type: '${type}'")
            }

            string = string.substring(strings[0].length())
        }
    }

    Quaternion multiply(Quaternion other) {
        // (ai+bj+ck+d)(a'i+b'j+c'k+d') = ix(ad'+bc'-cb'+da') + jx(-ac'+bd'+ca'+db') + kx(ab'-ba'+cd'+dc') + (-aa'-bb'-cc'+dd')

        def a2 = other.a, b2 = other.b, c2 = other.c, d2 = other.d

        new Quaternion(a*d2+b*c2-c*b2+d*a2, -a*c2+b*d2+c*a2+d*b2, a*b2-b*a2+c*d2+d*c2, -a*a2-b*b2-c*c2+d*d2)
    }

    String format() {
        // "${a}i+${b}j+${c}k+${d}"

        def parts = [] as List<String>

        if (a == -1) {
            parts << "-i"
        } else if (a < 0) {
            parts << "${a}i"
        } else if (a == 0) {
        } else if (a == 1) {
            parts << 'i'
        } else {
            parts << "${a}i"
        }

        if (b == -1) {
            parts << "-j"
        } else if (b < 0) {
            parts << "${b}j"
        } else if (b == 0) {
        } else if (b == 1) {
            if (parts) {
                parts << '+'
            }

            parts << 'j'
        } else {
            if (parts) {
                parts << '+'
            }

            parts << "${b}j"
        }

        if (c == -1) {
            parts << "-k"
        } else if (c < 0) {
            parts << "${c}k"
        } else if (c == 0) {
        } else if (c == 1) {
            if (parts) {
                parts << '+'
            }

            parts << 'k'
        } else {
            if (parts) {
                parts << '+'
            }

            parts << "${c}k"
        }

        if (d < 0) {
            parts << "${d}"
        } else if (d == 0) {
        } else {
            if (parts) {
                parts << '+'
            }

            parts << "${d}"
        }

        parts.join('')
    }

    @Override
    String toString() {
        "Quaternion[${format()}]"
    }
}

def input = new Scanner(System.in)

def expression = input.next()

def quaternions = [] as List<Quaternion>

while (expression.startsWith('(')) {
    def substring = expression.substring(1, expression.indexOf(')'))

    System.err.println("Substring: ${substring}")

    quaternions << new Quaternion(substring)

    expression = expression.substring(expression.indexOf(')') + 1)
}

System.err.println("${quaternions}")

def result = quaternions.remove(0)

while (quaternions) {
    result = result.multiply(quaternions.remove(0))
}

println result.format()
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

class Square {
    final int size

    Square(int size) {
        this.size = size
    }

    boolean contains(int x, int y) {
        ((-size / 2 <= x) && (x <= size / 2)) && ((-size / 2 <= y) && (y <= size / 2))
    }
}

class Circle {
    final int diameter

    Circle(int diameter) {
        this.diameter = diameter
    }

    boolean contains(int x, int y) {
        Math.sqrt((x * x) + (y * y)) <= (diameter / 2)
    }
}

class Line {

    // y = a.x + b
    final int a, b

    Line(int a, int b) {
        this.a = a
        this.b = b
    }

    int y(int x) {
        a * x + b
    }

    boolean under(int x, int y) {
        this.y(x) >= y
    }

    boolean above(int x, int y) {
        this.y(x) <= y
    }
}

// A(-size, 0), B(0, size), C(size, 0) D(0, -size)
class Diamond {
    final int size
    final Line ab, bc, cd, da

    Diamond(int size) {
        this.size = size

        // AB: y = x + size
        // CD: y = x - size
        // BC: y = -x + size
        // DA: y = -x - size
        this.ab = new Line(1, size >> 1)
        this.bc = new Line(-1, size >> 1)
        this.cd = new Line(1, -size >> 1)
        this.da = new Line(-1, -size >> 1)
    }

    boolean contains(int x, int y) {
        ab.under(x, y) && bc.under(x, y) && cd.above(x, y) && da.above(x, y)
    }
}

class Competitor {
    final String name

    int score = 0

    Competitor(String name) {
        this.name = name
    }
}

input = new Scanner(System.in);

SIZE = input.nextInt()

System.err.println "Size: ${SIZE}"

def square = new Square(SIZE)
def circle = new Circle(SIZE)
def diamond = new Diamond(SIZE)

N = input.nextInt()
input.nextLine()

def competitors = []

for (i = 0; i < N; ++i) {
    competitors << new Competitor(input.nextLine())
}

T = input.nextInt()

for (i = 0; i < T; ++i) {
    throwName = input.next()
    x = input.nextInt()
    y = input.nextInt()

    System.err.println "${throwName}: (${x}, ${y})"

    def points = 0

    if (square.contains(x, y)) {
        points = 5

        System.err.println "In square"

        if (circle.contains(x, y)) {
            points = 10

            System.err.println "In circle"

            if (diamond.contains(x, y)) {
                points = 15

                System.err.println "In diamond"
            }
        }
    }

    System.err.println "=> ${points} points"

    competitors.find { it.name == throwName }.score += points
}

def scores = competitors.collect { it.score }.unique().sort { a, b -> b <=> a }

scores.each { score ->
    competitors.findAll { it.score == score }.each {
        println "${it.name} ${it.score}"
    }
}
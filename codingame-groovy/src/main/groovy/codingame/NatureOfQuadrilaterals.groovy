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

class Point {
    final String name
    final int x, y

    Point(Scanner scanner) {
        this.name = scanner.next()
        this.x = scanner.nextInt()
        this.y = scanner.nextInt()
    }

    String dump() {
        "${name} ${x} ${y}"
    }

    @Override
    String toString() {
        "${name}(${x}, ${y})"
    }
}

class Quadrilateral {
    final Point a, b, c, d

    Quadrilateral(Point a, Point b, Point c, Point d) {
        this.a = a
        this.b = b
        this.c = c
        this.d = d
    }

    String name() {
        "${a.name}${b.name}${c.name}${d.name}"
    }
}

class Vector2D {
    final Point start, end

    Vector2D(Point start, Point end) {
        this.start = start
        this.end = end
    }

    int deltaX() {
        end.x - start.x
    }

    int deltaY() {
        end.y - start.y
    }

    int lengthSquare() {
        (deltaX() * deltaX()) + (deltaY() * deltaY())
    }

    boolean orthogonalTo(Vector2D that) {
        if (slope() == Double.POSITIVE_INFINITY) {
            return that.slope() == 0
        }
        if (slope() == 0) {
            return that.slope() == Double.POSITIVE_INFINITY
        }

        (this.deltaX() * that.deltaX()) + (this.deltaY() * that.deltaY()) == 0
    }

    double slope() {
        if (deltaX() == 0) {
            return Double.POSITIVE_INFINITY
        }

        deltaY() / deltaX()
    }

    boolean parallelTo(Vector2D that) {
        Double.compare(this.slope(), that.slope()) == 0
    }

    boolean sameLength(Vector2D that) {
        this.lengthSquare() == that.lengthSquare()
    }

    @Override
    String toString() {
        "${start.name}${end.name}(${deltaX()}, ${deltaY()})"
    }
}

def scanner = new Scanner(System.in)

def n = scanner.nextInt()

System.err.println("${n}")

for (i = 0; i < n; ++i) {
    def A = new Point(scanner)
    def B = new Point(scanner)
    def C = new Point(scanner)
    def D = new Point(scanner)

    System.err.println("${A.dump()} ${B.dump()} ${C.dump()} ${D.dump()}")

    def quadrilateral = new Quadrilateral(A, B, C, D)

    System.err.println "${A}, ${B}, ${C}, ${D}"

    def AB = new Vector2D(A, B)
    def BC = new Vector2D(B, C)
    def CD = new Vector2D(C, D)
    def DA = new Vector2D(D, A)

    System.err.println "${AB}, ${BC}, ${CD}, ${DA}"

    if (AB.orthogonalTo(BC) && BC.orthogonalTo(CD) && CD.orthogonalTo(DA)) {
        if (AB.sameLength(BC) && BC.sameLength(CD) && CD.sameLength(DA)) {
            println "${quadrilateral.name()} is a square."
        } else {
            println "${quadrilateral.name()} is a rectangle."
        }

        continue
    }

    if (AB.sameLength(BC) && BC.sameLength(CD) && CD.sameLength(DA)) {
        println "${quadrilateral.name()} is a rhombus."

        continue
    }

    if (AB.sameLength(CD) && BC.sameLength(DA)) {
        if (AB.parallelTo(CD) && BC.parallelTo(DA)) {
            println "${quadrilateral.name()} is a parallelogram."

            continue
        }
    }

    println "${quadrilateral.name()} is a quadrilateral."
}
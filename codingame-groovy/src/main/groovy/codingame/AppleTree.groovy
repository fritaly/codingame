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

class Apple {
    int x, y, z, radius

    Apple(Scanner scanner) {
        this.x = scanner.nextInt()
        this.y = scanner.nextInt()
        this.z = scanner.nextInt()
        this.radius = scanner.nextInt()
    }

    boolean collides(Apple other) {
        def deltaX = this.x - other.x
        def deltaY = this.y - other.y
        def deltaZ = this.z - other.z

        def distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)

        distance <= (this.radius + other.radius)
    }

    @Override
    String toString() {
        "Apple[coordinates: (${x}, ${y}, ${z}), radius: ${radius}]"
    }
}

def input = new Scanner(System.in)

def N = input.nextInt()
def index = input.nextInt()

def apples = [] as List<Apple>

for (i = 0; i < N; ++i) {
    apples << new Apple(input)
}

System.err.println("${apples}")

def fallingApples = [] as List<Apple>
fallingApples << apples.remove(index)

while (fallingApples) {
    def apple = fallingApples.remove(0)

    // Find all the apples below the falling one
    def candidates = apples.findAll { it.z <= apple.z }.sort { a,b -> b.z - a.z }

    System.err.println("Candidates: ${candidates}")

    // Let the apple fall and check the collisions along the way
    for (candidate in candidates) {
        apple.z = candidate.z

        if (apple.collides(candidate)) {
            System.err.println("${apple} collides with ${candidate}")

            apples.remove(candidate)
            fallingApples << candidate
        }
    }
}

println apples.size()
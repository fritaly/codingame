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

enum MoveDirection {
    NORTH('^'), EAST('>'), SOUTH('V'), WEST('<')

    final String symbol

    MoveDirection(String symbol) {
        this.symbol = symbol
    }

    Position move(Position origin) {
        switch (this) {
            case NORTH:
                return new Position(origin.x, origin.y-1)
            case SOUTH:
                return new Position(origin.x, origin.y+1)
            case WEST:
                return new Position(origin.x-1, origin.y)
            case EAST:
                return new Position(origin.x+1, origin.y)
        }
    }

    MoveDirection nextClockwise() {
        switch (this) {
            case NORTH:
                return EAST
            case SOUTH:
                return WEST
            case WEST:
                return NORTH
            case EAST:
                return SOUTH
        }
    }
}

class Position {
    final int x, y

    Position(int x, int y) {
        this.x = x
        this.y = y
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof Position) {
            return (this.x == obj.x) && (this.y == obj.y);
        }

        false
    }

    @Override
    String toString() {
        "(${x}, ${y})"
    }
}

class PositionDirection {
    final Position position
    final MoveDirection direction

    PositionDirection(Position position, MoveDirection direction) {
        this.position = position
        this.direction = direction
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof PositionDirection) {
            return (this.position == obj.position) && (this.direction == obj.direction);
        }

        false
    }
}

class Robot {
    final char[][] map

    // History of the previous (positions, directions) visited by the robot
    final List<PositionDirection> history = []

    Robot(char[][] map) {
        this.map = map
    }

    MoveDirection direction = MoveDirection.NORTH

    Position position

    private char tileAt(Position position) {
        map[position.y][position.x]
    }

    void move(long remainingMoves) {
        while (remainingMoves > 0) {
            // Element facing the robot ?
            def targetPosition = direction.move(position)

            def target = tileAt(targetPosition)

            if ((target == '.') || (target == 'X')) {
                // Mark the position visited with 'X'
                map[position.y][position.x] = 'X'

                // Record the position / direction in the history
                history << new PositionDirection(position, direction)

                // Move the robot
                position = targetPosition

                // Decrement the number of remaining moves
                remainingMoves--
            } else if (target == '#') {
                // Obstacle in front of the robot, turn right

                // Mark the position visited with 'X'
                map[position.y][position.x] = 'X'

                // Record the position / direction in the history (before changing the direction)
                history << new PositionDirection(position, direction)

                while (target == '#') {
                    direction = direction.nextClockwise()
                    target = tileAt(direction.move(position))
                }

                position = direction.move(position)

                // Decrement the number of remaining moves
                remainingMoves--
            } else {
                throw new IllegalStateException("Unexpected tile: ${target}")
            }

            // After moving, check if the robot is in a loop
            if (tileAt(position) == 'X') {
                // We already visited this position, the robot is in a loop ! Find the position of the current position
                // in the history of positions visited by the robot
                def current = new PositionDirection(position, direction)

                def index = history.indexOf(current)

                if (index != -1) {
                    System.err.println("Loop detected !")
                    System.err.println("- Current position: ${position}")
                    System.err.println("- Direction: ${direction}")
                    System.err.println("Current position / position found in history at index #${index}")
                    System.err.println("Size of history: ${history.size()}")
                    System.err.println("Remaining moves: ${remainingMoves}")

                    // Infer the size of the loop, that is the number of moves before the robot returns to its current
                    // position
                    def loopSize = history.size() - index

                    // Ignore all the extra loops and infer the number of remaining moves
                    remainingMoves = remainingMoves % loopSize

                    // Determine the final position based on the history and the number of remaining moves
                    position = history.get((index + remainingMoves) as int).position

                    System.err.println("Final position: ${position}")

                    remainingMoves = 0
                }
            }
        }
    }

    void render() {
        println ""
        println "==="
        println ""

        map.eachWithIndex { row, index ->
            def text = new String(row)

            if (index == position.y) {
                println text.substring(0, position.x) + direction.symbol + text.substring(position.x+1)
            } else {
                println text
            }
        }
    }
}

def input = new Scanner(System.in)

def width = input.nextInt()
def height = input.nextInt()
def n = input.nextLong()

System.err.println("${width} ${height}")
System.err.println("${n}")

input.nextLine()

def map = new char[height][width]

def robot = new Robot(map)

for (y = 0; y < height; y++) {
    def line = input.nextLine()

    System.err.println("${line}")

    for (x = 0; x < width; x++) {
        def character = line.charAt(x)

        map[y][x] = character

        if (character == 'O') {
            robot.position = new Position(x, y)

            System.err.println("Start position: ${robot.position}")

            // Replace 'O' by '.' (empty area)
            map[y][x] = '.'
        }
    }
}

// Move the robot
robot.move(n)

println "${robot.position.x} ${robot.position.y}"

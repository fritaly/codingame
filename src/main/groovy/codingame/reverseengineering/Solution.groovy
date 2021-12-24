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
package codingame.reverseengineering

enum Direction {
    NORTH('C'), SOUTH('D'), WEST('E'), EAST('A')

    String id

    Direction(String id) {
        this.id = id
    }

    Direction opposite() {
        switch (this) {
            case SOUTH:
                return NORTH
            case EAST:
                return WEST
            case WEST:
                return EAST
            case NORTH:
                return SOUTH
        }
    }

    static List<Direction> randomValues() {
        def source = [ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST ]

        // Shuffle the directions to make the behavior less predictable
        Collections.shuffle(source)

        source
    }
}

class Position {
    int x, y

    Position(int x, int y) {
        this.x = x
        this.y = y
    }

    int distanceTo(Position other) {
        Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
    }

    Position towards(Direction direction, int width, int height) {
        // The maze can contain some "shortcuts" that's why we need to know the width / height of the maze to support
        // the use of shortcuts
        switch (direction) {
            case Direction.NORTH:
                return new Position(x, (y - 1 + height) % height)
            case Direction.SOUTH:
                return new Position(x, (y + 1) % height)
            case Direction.WEST:
                return new Position((x - 1 + width) % width, y)
            case Direction.EAST:
                return new Position((x + 1) % width, y)
            default:
                throw new RuntimeException("Unexpected direction: ${direction}")
        }
    }

    @Override
    String toString() {
        "(${x}, ${y})"
    }

    @Override
    int hashCode() {
        Objects.hash(x, y)
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof Position) {
            return (this.x == obj.x) && (this.y == obj.y)
        }

        false
    }
}

class Maze {
    char[][] grid

    Position[] previousPositions
    Position[] positions

    Maze(int width, int height, int entityCount) {
        grid = new char[height][width]

        grid.each { row ->
            // Use ' ' to render the positions which have never been visited
            Arrays.fill(row, ' ' as char)
        }

        positions = new Position[entityCount]
        previousPositions = new Position[entityCount]
    }

    int getEntityCount() {
        positions.length
    }

    int getWidth() {
        grid[0].length
    }

    int getHeight() {
        grid.length
    }

    Position playerPosition() {
        positions[-1]
    }

    boolean isGhost(Position position) {
        def c = charAt(position)

        if ((c == '.') || (c == '#') || (c == ' ') || (c == "${entityCount}")) {
            return false
        }

        true
    }

    boolean moved(int index) {
        def previousPosition = previousPositions[index]
        def position = positions[index]

        if (!previousPosition || !position) {
            return false
        }

        // The entity moved if its current position is different from the previous one
        previousPosition != position
    }

    void setPosition(int index, Position p) {
        // Backup the previous position
        previousPositions[index] = positions[index]
        positions[index] = p

        // Update the grid
        def previousPosition = previousPositions[index]

        if (previousPosition) {
            setChar(previousPosition, '.' as char)
        }

        setChar(p, "${index}".charAt(0))
    }

    Position getPosition(int index) {
        positions[index]
    }

    Position getPositionTowards(Direction direction) {
        playerPosition().towards(direction, width, height)
    }

    void setChar(Position position, char c) {
        grid[position.y][position.x] = c
    }

    char charAt(Position position) {
        grid[position.y][position.x]
    }

    void dump() {
        grid.each { row ->
            System.err.println("${new String(row)}")
        }
    }
}

/**
 * Contains information about the 4 nearby cells.
 */
class Cells {

    String north, east, south, west

    Cells(Scanner scanner) {
        this.north = scanner.nextLine()
        this.east = scanner.nextLine()
        this.south = scanner.nextLine()
        this.west = scanner.nextLine()
    }

    boolean isWall(Direction direction) {
        getCell(direction) == '#'
    }

    String getCell(Direction direction) {
        switch (direction) {
            case Direction.NORTH:
                return north
            case Direction.EAST:
                return east
            case Direction.SOUTH:
                return south
            case Direction.WEST:
                return west
        }
    }
}

def input = new Scanner(System.in)

def height = input.nextInt()
def width = input.nextInt()
def entityCount = input.nextInt()
input.nextLine()

def maze = new Maze(width, height, entityCount)

Direction previousMoveDirection = null

def visitedPositions = new HashSet()

while (true) {
    // The logging of the following 4 properties indicate that there are indicators about the surrounding positions.
    // They indicate nearby walls !
    def cells = new Cells(input)

    for (i = 0; i < entityCount; ++i) {
        def x = input.nextInt()
        def y = input.nextInt()
        def position = new Position(x, y)

        maze.setPosition(i, position)

        if (i == entityCount - 1) {
            // Record all the positions already visited
            visitedPositions << position

            if (cells.north == '#') {
                maze.setChar(maze.getPositionTowards(Direction.NORTH), '#' as char)
            }
            if (cells.east == '#') {
                maze.setChar(maze.getPositionTowards(Direction.EAST), '#' as char)
            }
            if (cells.south == '#') {
                maze.setChar(maze.getPositionTowards(Direction.SOUTH), '#' as char)
            }
            if (cells.west == '#') {
                maze.setChar(maze.getPositionTowards(Direction.WEST), '#' as char)
            }
        }
    }

    input.nextLine()

    maze.dump()

    def moveDirection = null

    // Find if there are (moving) ghosts within 5 cells
    def nearGhosts = []

    for (int i = 0; i < entityCount - 1; i++) {
        if (maze.moved(i)) {
            // Ignore the ghosts which are static
            def distance = maze.getPosition(i).distanceTo(maze.playerPosition())

            if (distance <= 5) {
                nearGhosts << i
            }
        }
    }

    if (nearGhosts) {
        // Mode danger: try to maximize the distance with the ghosts nearby
        System.err.println("Detected ${nearGhosts.size()} ghost(s) nearby: ${nearGhosts}")

        def bestScore = Double.MAX_VALUE, selection = null

        for (direction in Direction.values()) {
            def targetPosition = maze.getPositionTowards(direction)

            def elementType = maze.charAt(targetPosition)

            if (elementType == '#') {
                // There is a wall in that direction, skip it
                System.err.println("Ignoring ${direction} ${targetPosition} because of a wall")
                continue
            }
            if (maze.isGhost(targetPosition)) {
                System.err.println("Ignoring ${direction} ${targetPosition} because of a ghost")
                continue
            }

            System.err.println("Evaluating ${direction} ...")

            double score = 0d

            for (i in nearGhosts) {
                def distance = targetPosition.distanceTo(maze.getPosition(i))

                System.err.println("Ghost ${i} - distance = ${distance}")

                if (distance == 0) {
                    score = Double.MAX_VALUE
                    break
                }

                score += (1 / distance)
            }

            System.err.println("${direction} -> score = ${score}")

            if (score < bestScore) {
                bestScore = score
                selection = direction

                System.err.println("New best solution: ${bestScore} (${direction})")
            }
        }

        moveDirection = selection
    } else {
        // Mode Patrol: try each direction. The player can move in 4 directions
        def candidates = [] as List<Direction>

        for (candidate in Direction.randomValues()) {
            if (!cells.isWall(candidate)) {
                def targetPosition = maze.getPositionTowards(candidate)

                if (!maze.isGhost(targetPosition)) {
                    if (!visitedPositions.contains(targetPosition)) {
                        // Favor positions never visited before
                        candidates.add(0, candidate)
                    } else {
                        candidates.add(candidate)
                    }
                } else {
                    // There is a ghost on the target position, ignore the direction
                }
            } else {
                // The cell is a wall, ignore the direction
            }
        }

        if (previousMoveDirection) {
            // Backtrack as the last option !
            candidates.remove(previousMoveDirection.opposite())
            candidates.add(previousMoveDirection.opposite())
        }

        for (candidate in candidates) {
            // Find what's on the cell in that direction
            def targetPosition = maze.getPositionTowards(candidate)

            def elementType = maze.charAt(targetPosition)

            if (elementType == '#') {
                // There is a wall in that direction, skip it
                System.err.println("Ignoring ${candidate} ${targetPosition} because it's a wall")
                continue
            }
            if (maze.isGhost(targetPosition)) {
                System.err.println("Ignoring ${candidate} ${targetPosition} because it's occupied by a ghost (${elementType})")
                continue
            }

            // The player can move in that direction
            moveDirection = candidate
            break
        }
    }

    if (!moveDirection) {
        // No move direction found because we are surrounded by ghosts, print "B" which means "Don't move"
        println 'B'
    } else {
        println moveDirection.id
    }

    // Save the positions for the next turn
    previousMoveDirection = moveDirection
}
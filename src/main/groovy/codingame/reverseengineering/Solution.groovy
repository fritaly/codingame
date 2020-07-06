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

    int getWidth() {
        grid[0].length
    }

    int getHeight() {
        grid.length
    }

    Position playerPosition() {
        positions[-1]
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

        setChar(p, "${index+1}".charAt(0))
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

    void setChar(int x, int y, char c) {
        grid[y][x] = c
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

    // Array recording who moved during the previous turn
    def moved = new boolean[entityCount]

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
        if (!moved[i]) {
            // The ghost is still, ignore it
            continue
        }

        def distance = maze.getPosition(i).distanceTo(maze.playerPosition())

        if (distance <= 5) {
            nearGhosts << i
        }
    }

    if (nearGhosts) {
        // Mode danger: try to maximize the distance with the ghosts nearby
        System.err.println("Detected ${nearGhosts.size()} ghost(s) nearby")

        def bestScore = 0, selection = null

        for (direction in Direction.values()) {
            def targetPosition = maze.getPositionTowards(direction)

            def elementType = maze.charAt(targetPosition)

            if (elementType == '#') {
                // There is a wall in that direction, skip it
                System.err.println("Ignoring ${direction} ${targetPosition} because it's a wall")
                continue
            }
            if ((elementType == '1') || (elementType == '2') || (elementType == '3') || (elementType == '4')) {
                System.err.println("Ignoring ${direction} ${targetPosition} because it's occupied by a ghost (${elementType})")
                continue
            }

            def score = 0

            for (i in nearGhosts) {
                score += targetPosition.distanceTo(maze.getPosition(i))
            }

            System.err.println("${direction} -> ${score}")

            if (score >= bestScore) {
                bestScore = score
                selection = direction

                System.err.println("New best solution: ${bestScore} (${direction})")
            }
        }

        moveDirection = selection
    } else {
        // Mode Patrol: try each direction. The player can move in 4 directions
        def candidates = [] as List<Direction>

        for (candidate in [ Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST ]) {
            if (cells.getCell(candidate) == '_') {
                def targetPosition = maze.getPositionTowards(candidate)

                if (!visitedPositions.contains(targetPosition)) {
                    // Favor positions never visited before
                    candidates.add(0, candidate)
                } else {
                    candidates.add(candidate)
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
            if ((elementType == '1') || (elementType == '2') || (elementType == '3') || (elementType == '4')) {
                System.err.println("Ignoring ${candidate} ${targetPosition} because it's occupied by a ghost (${elementType})")
                continue
            }

            // The player can move in that direction
            moveDirection = candidate
            break
        }
    }

    println moveDirection.id

    // Save the positions for the next turn
    previousMoveDirection = moveDirection
}
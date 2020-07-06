package codingame.reverseengineering

int manhattanDistance(Position p1, Position p2) {
    Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y)
}

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

    // Tells whether the position is valid
    boolean isValid(char[][] grid) {
        (0 <= y) && (y < grid.length) && (0 <= x) && (x < grid[0].length)
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

void dump(char[][] array) {
    array.each { row ->
        System.err.println("${new String(row)}")
    }
}

def input = new Scanner(System.in)

def height = input.nextInt()
def width = input.nextInt()
def entityCount = input.nextInt()
input.nextLine()

def grid = new char[height][width]

grid.each { row ->
    // Use ' ' to render the positions which have never been used
    Arrays.fill(row, ' ' as char)
}

// Array storing the previous position of each entity
def previousPositions = null

Direction previousMoveDirection = null

while (true) {
    // The logging of the following 4 properties indicate that there are indicators about the surrounding positions.
    // They indicate nearby walls !
    def northWall = input.nextLine()
    def eastWall = input.nextLine()
    def southWall = input.nextLine()
    def westWall = input.nextLine()

    System.err.println("${northWall} ${eastWall} ${southWall} ${westWall}")

    def positions = new Position[5]

    // Array recording who moved during the previous turn
    def moved = new boolean[5]

    for (i = 0; i < entityCount; ++i) {
        def x = input.nextInt()
        def y = input.nextInt()
        def position = new Position(x, y)

        positions[i] = position

        if (i == 4) {
            def northPosition = position.towards(Direction.NORTH, width, height)
            def eastPosition = position.towards(Direction.EAST, width, height)
            def southPosition = position.towards(Direction.SOUTH, width, height)
            def westPosition = position.towards(Direction.WEST, width, height)

            if (northPosition.isValid(grid)) {
                grid[northPosition.y][northPosition.x] = ((northWall == '#') ? '#' : '.') as char
            }
            if (eastPosition.isValid(grid)) {
                grid[eastPosition.y][eastPosition.x] = ((eastWall == '#') ? '#' : '.') as char
            }
            if (southPosition.isValid(grid)) {
                grid[southPosition.y][southPosition.x] = ((southWall == '#') ? '#' : '.') as char
            }
            if (westPosition.isValid(grid)) {
                grid[westPosition.y][westPosition.x] = ((westWall == '#') ? '#' : '.') as char
            }
        }

        if (previousPositions) {
            def previousPosition = previousPositions[i]

            // Erase the previous position from the map with a '.' to highlight the positions which are "traversable"
            grid[previousPosition.y][previousPosition.x] = '.'

            moved[i] = (previousPosition != position)
        }

        grid[y][x] = "${i+1}".charAt(0)
    }

    input.nextLine()

    dump(grid)

    def moveDirection = null

    // Find if there are (moving) ghosts within 5 cells
    def nearGhosts = []

    for (int i = 0; i < 4; i++) {
        if (!moved[i]) {
            // The ghost is still, ignore it
            continue
        }

        def distance = manhattanDistance(positions[i], positions[4])

        if (distance <= 5) {
            nearGhosts << i
        }
    }

    if (nearGhosts) {
        // Mode danger: try to maximize the distance with the ghosts nearby
        System.err.println("Detected ${nearGhosts.size()} ghost(s) nearby")

        def bestScore = 0, selection = null

        for (direction in Direction.values()) {
            def targetPosition = positions[4].towards(direction, width, height)

            def elementType = grid[targetPosition.y][targetPosition.x]

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
                score += manhattanDistance(targetPosition, positions[i])
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
        def candidates = [ Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH ]

        Collections.shuffle(candidates)

        if (previousMoveDirection) {
            // Keep going in the same direction
            candidates.remove(previousMoveDirection)
            candidates.add(0, previousMoveDirection)

            // Backtrack as a last option
            candidates.remove(previousMoveDirection.opposite())
            candidates.add(previousMoveDirection.opposite())
        }

        for (candidate in candidates) {
            // Find what's on the cell in that direction
            def targetPosition = positions[4].towards(candidate, width, height)

            def elementType = grid[targetPosition.y][targetPosition.x]

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
    previousPositions = positions
    previousMoveDirection = moveDirection
}
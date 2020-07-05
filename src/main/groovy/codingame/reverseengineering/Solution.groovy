package codingame.reverseengineering

enum Direction {
    NORTH('C'), SOUTH('D'), WEST('E'), EAST('A')

    String id

    Direction(String id) {
        this.id = id
    }
}

class Position {
    int x, y

    Position(int x, int y) {
        this.x = x
        this.y = y
    }

    Position towards(Direction direction) {
        switch (direction) {
            case Direction.NORTH:
                return new Position(x, y - 1)
            case Direction.SOUTH:
                return new Position(x, y + 1)
            case Direction.WEST:
                return new Position(x - 1, y)
            case Direction.EAST:
                return new Position(x + 1, y)
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
    Arrays.fill(row, '.' as char)
}

// Array storing the previous position of each entity
def previousPositions = null

// Set containing the positions already visited
def visitedPositions = new HashSet<Position>()

Direction previousMoveDirection = null

while (true) {
    firstInput = input.nextLine()
    secondInput = input.nextLine()
    thirdInput = input.nextLine()
    fourthInput = input.nextLine()

    def positions = new Position[5]

    for (i = 0; i < entityCount; ++i) {
        def x = input.nextInt()
        def y = input.nextInt()
        def position = new Position(x, y)

        positions[i] = position

        if (previousPositions) {
            def previousPosition = previousPositions[i]

            // Erase the previous position from the map
            grid[previousPosition.y][previousPosition.x] = '.'

            if (i == 4) {
                // Record the player's position in the history
                visitedPositions << position

                if (previousPosition == position) {
                    // The position didn't change, the player couldn't move. Mark the wall on the map. Position of the
                    // wall ?
                   def targetPosition = position.towards(previousMoveDirection)

                    grid[targetPosition.y][targetPosition.x] = 'X' as char
                }
            }
        }

        grid[y][x] = "${i+1}".charAt(0)
    }

    input.nextLine()

    // The player can move in 4 directions
    def candidates = [ Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH ]

    def moveDirection = null

    // Try each direction
    for (candidate in candidates) {
        // Find what's on the cell in that direction
        def targetPosition = positions[4].towards(candidate)

        if (grid[targetPosition.y][targetPosition.x] == 'X') {
            // There is a wall in that direction, skip it
            continue
        }
        if (visitedPositions.contains(targetPosition)) {
            // Ignore positions already visited by the player
            continue
        }

        // The player can move in that direction
        moveDirection = candidate
        break
    }

    println moveDirection.id

    dump(grid)

    // Save the positions for the next turn
    previousPositions = positions
    previousMoveDirection = moveDirection
}
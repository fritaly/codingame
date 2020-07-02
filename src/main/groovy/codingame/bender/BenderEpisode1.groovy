package codingame.bender

enum State {
    NORMAL, BREAKING

    State nextState() {
        (this == NORMAL) ? BREAKING : NORMAL
    }

    boolean canBreakObstacles() {
        (this == BREAKING)
    }
}

enum Direction {
    SOUTH, WEST, NORTH, EAST
}

class Position {
    final int x, y

    Position(int x, int y) {
        this.x = x
        this.y = y
    }

    Position next(Direction direction) {
        switch (direction) {
            case Direction.SOUTH:
                return new Position(x, y+1)
            case Direction.NORTH:
                return new Position(x, y-1)
            case Direction.WEST:
                return new Position(x-1, y)
            case Direction.EAST:
                return new Position(x+1, y)
            default:
                throw new RuntimeException("Unexpected direction: ${direction}")
        }
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof Position) {
            return (this.x == obj.x) && (this.y == obj.y)
        }

        false
    }

    @Override
    String toString() {
        "[${x}, ${y}]"
    }
}

class Bender {

    Direction direction = Direction.SOUTH

    List<Direction> directionPriorities = [Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST ]

    Position position

    State state = State.NORMAL

    char[][] grid

    boolean dead = false

    char elementAt(Position p) {
        grid[p.y][p.x]
    }

    char elementTowards(Direction d) {
        elementAt(position.next(d))
    }

    boolean canTraverse(char element) {
        if (element == '#') {
            return false
        }
        if (element == 'X') {
            return state.canBreakObstacles()
        }

        true
    }

    Position getTeleportDestination(Position origin) {
        assert origin

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                def c = grid[y][x]

                if ((c == 'T') && ((origin.x != x) || (origin.y != y))) {
                    return new Position(x, y)
                }
            }
        }

        // Never supposed to happen
        throw new IllegalStateException("Unable to find the destination of teleport at ${origin}")
    }

    void move() {
        System.err.println("====")
        System.err.println("Moving (direction: ${direction.name()}) ...")

        def nextElement = elementTowards(direction)

        System.err.println("Next element: '${nextElement}'")

        if (!canTraverse(nextElement)) {
            // Obstacle, change direction
            System.err.println("Next element is an obstacle, testing other directions ...")

            for (d in directionPriorities) {
                System.err.println("Testing direction ${d.name()} ...")

                def element2 = elementTowards(d)

                System.err.println("Target element: '${element2}'")

                if (canTraverse(element2)) {
                    // Change direction
                    System.err.println("Bender can traverse the element, direction changed to ${d}")
                    direction = d
                    break
                }
            }
        }

        // Change position
        def oldPosition = position
        position = position.next(direction)

        // Print the name of the move direction
        println "${direction.name()}"

        System.err.println("Position changed: ${oldPosition} -> ${position}")

        def currentElement = elementAt(position)

        System.err.println("New element type: '${currentElement}'")

        if (currentElement == '$') {
            // Bender dies
            dead = true

            System.err.println("Bender dies !")
        } else if (currentElement == 'S') {
            direction = Direction.SOUTH

            System.err.println("Direction changed to ${direction}")
        } else if (currentElement == 'E') {
            direction = Direction.EAST

            System.err.println("Direction changed to ${direction}")
        } else if (currentElement == 'W') {
            direction = Direction.WEST

            System.err.println("Direction changed to ${direction}")
        } else if (currentElement == 'N') {
            direction = Direction.NORTH

            System.err.println("Direction changed to ${direction}")
        } else if (currentElement == 'I') {
            directionPriorities = directionPriorities.reverse()

            System.err.println("Reversing direction priorities ...")
        } else if (currentElement == 'B') {
            state = state.nextState()

            System.err.println("State changed to ${state.name()}")
        } else if (currentElement == 'X') {
            if (state.canBreakObstacles()) {
                // Break the obstacle
                grid[position.y][position.x] = ' '

                System.err.println("Obstacle broken !")
            } else {
                // Never supposed to happen
                throw new IllegalStateException()
            }
        } else if (currentElement == 'T') {
            // Teleport, find the teleport's destination
            position = getTeleportDestination(position)

            System.err.println("Teleport ! Position changed to ${position}")
        }
    }

    void dump() {
        grid.eachWithIndex { row, y ->
            if (position.y == y) {
                // Render Bender with a '*'
                System.err.println("${new String(row, 0, position.x)}*${new String(row, position.x + 1, row.length - position.x - 1)}")
            } else {
                System.err.println("${new String(row)}")
            }
        }
    }
}

def input = new Scanner(System.in)

def height = input.nextInt()
def width = input.nextInt()
input.nextLine()

System.err.println("${height} ${width}")

def grid = new char[height][width]

def bender = new Bender()
bender.grid = grid

for (def y = 0; y < height; ++y) {
    def row = input.nextLine()

    System.err.println("${row}")

    row.toCharArray().eachWithIndex{ char c, int x ->
        grid[y][x] = c

        if (c == '@') {
            // Set the start position
            bender.position = new Position(x, y)
        }
    }
}

while (!bender.dead) {
    bender.move()
    bender.dump()
}
package codingame.reverseengineering

class Position {
    int x, y

    Position(int x, int y) {
        this.x = x
        this.y = y
    }

    @Override
    String toString() {
        "(${x}, ${y})"
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
        }

        grid[y][x] = "${i+1}".charAt(0)
    }

    input.nextLine()

    println 'A'

    dump(grid)

    // Save the positions for the next turn
    previousPositions = positions
}
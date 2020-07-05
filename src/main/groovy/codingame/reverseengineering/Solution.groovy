package codingame.reverseengineering

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

while (true) {
    firstInput = input.nextLine()
    secondInput = input.nextLine()
    thirdInput = input.nextLine()
    fourthInput = input.nextLine()

    for (i = 0; i < entityCount; ++i) {
        def x = input.nextInt()
        def y = input.nextInt()

        grid[y][x] = "${i+1}".charAt(0)
    }

    input.nextLine()

    println 'A'

    dump(grid)
}
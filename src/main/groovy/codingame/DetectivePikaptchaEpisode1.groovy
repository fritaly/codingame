package codingame

private static void dump(char[][] grid) {
    grid.each { row ->
        System.err.println("${new String(row)}")
    }
}

def input = new Scanner(System.in)

def width = input.nextInt()
def height = input.nextInt()

def grid = new char[height][width]

for (y = 0; y < height; y++) {
    def line = input.next()

    line.toCharArray().eachWithIndex{ char c, int x ->
        grid[y][x] = c
    }
}

char zero = '0'
char wall = '#'

for (y = 0; y < height; y++) {
    for (x = 0; x < width; x++) {
        if (grid[y][x] == zero) {
            def count = 0

            if (y > 0) {
                if (grid[y - 1][x] != wall) {
                    count++
                }
            }
            if (y < height - 1) {
                if (grid[y + 1][x] != wall) {
                    count++
                }
            }
            if (x > 0) {
                if (grid[y][x - 1] != wall) {
                    count++
                }
            }
            if (x < width - 1) {
                if (grid[y][x + 1] != wall) {
                    count++
                }
            }

            grid[y][x] = "${count}".charAt(0)
        }
    }
}

dump(grid)

grid.each { row ->
    println new String(row)
}
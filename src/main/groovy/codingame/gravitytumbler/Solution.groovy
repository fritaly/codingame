package codingame.gravitytumbler

private void dump(char[][] array, PrintStream stream) {
    stream.println array.collect { row -> new String(row) }.join('\n')
}

private char[][] rotate(char[][] input) {
    def inputHeight = input.length
    def inputWidth = input[0].length

    char[][] output = new char[inputWidth][inputHeight]

    for (int y=0; y < inputHeight; y++) {
        for (int x=0; x < inputWidth; x++) {
            def c = input[y][x]

            output[inputWidth - x - 1][y] = c
        }
    }

    output
}

private void gravity(char[][] array) {
    // Let the #s fall
    for (int x=0; x < array[0].length; x++) {
        def dots = 0, hashes = 0
        for (int y=0; y < array.length; y++) {
            if (array[y][x] == '.') {
                dots++
            }
            if (array[y][x] == '#') {
                hashes++
            }
        }
        for (int y=0; y < array.length; y++) {
            if (y < dots) {
                array[y][x] = '.'
            } else {
                array[y][x] = '#'
            }
        }
    }
}

input = new Scanner(System.in)

def width = input.nextInt()
def height = input.nextInt()
def count = input.nextInt()
input.nextLine()

char[][] array = new char[height][width]

for (i = 0; i < height; ++i) {
    def raster = input.nextLine()

    for (j = 0; j < width; j++) {
        array[i][j] = raster.charAt(j)
    }
}

count.times {
    array = rotate(array)

    gravity(array)
}

dump(array, System.out)
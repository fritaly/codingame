package codingame

private void dump(char[][] array, PrintStream stream) {
    stream.println array.collect { row -> new String(row) }.join('\n')
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

def input = new Scanner(System.in)

def width = input.nextInt()
def height = input.nextInt()

def array = new char[height][width]

for (i = 0; i < height; ++i) {
    def line = input.next()

    for (j = 0; j < width; j++) {
        array[i][j] = line.charAt(j)
    }
}

dump(array, System.err)

gravity(array)

dump(array, System.out)
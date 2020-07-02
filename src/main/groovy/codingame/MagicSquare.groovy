package codingame

static void dump(int[][] array) {
    array.each {
        System.err.println it.join('\t')
    }
}

static int row(int[][] array, int y) {
    array[y].sum()
}

static int column(int[][] array, int x) {
    def sum = 0

    for (int y = 0; y < array.length; y++) {
        sum += array[y][x]
    }

    sum
}

static int diag1(int[][] array) {
    def sum = 0

    for (int n = 0; n < array.length; n++) {
        sum += array[n][n]
    }

    sum
}

static int diag2(int[][] array) {
    def sum = 0

    for (int n = 0; n < array.length; n++) {
        sum += array[array.length - n - 1][n]
    }

    sum
}

def input = new Scanner(System.in)

def n = input.nextInt()

def array = new int[n][n]
def integers = [] as Set<Integer>

for (i = 0; i < n; ++i) {
    for (j = 0; j < n; ++j) {
        def c = input.nextInt()

        array[i][j] = c
        integers << c
    }
}

dump(array)

if (integers.size() == n * n) {
    def sums = [] as Set<Integer>

    for (int y = 0; y < array.length ; y++) {
        sums << row(array, y)
    }
    for (int x = 0; x < array.length ; x++) {
        sums << column(array, x)
    }
    sums << diag1(array) << diag2(array)

    println "${sums.size() == 1 ? 'MAGIC' : 'MUGGLE'}"
} else {
    println 'MUGGLE'
}
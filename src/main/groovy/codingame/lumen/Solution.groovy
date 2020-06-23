package codingame.lumen

input = new Scanner(System.in);

N = input.nextInt()
L = input.nextInt()
input.nextLine()

array = new String[N][N]

for (i = 0; i < N; ++i) {
    LINE = input.nextLine()
    chunks = LINE.split(' ')

    chunks.eachWithIndex { chunk, n ->
        if (chunk == 'X') {
            chunk = '0'
        }

        array[i][n] = chunk
    }
}

System.err.println array

for (x = 0; x < N; ++x) {
    for (y = 0; y < N; ++y) {
        if (array[x][y] == 'C') {
            // Directly replace the candle
            array[x][y] = 'L'

            for (distance = 0; distance < L; distance++) {
                for (x2 = x - distance; x2 <= x + distance; ++x2) {
                    for (y2 = y - distance; y2 <= y + distance; ++y2) {
                        if ((0 <= x2) && (x2 < N)) {
                            if ((0 <= y2) && (y2 < N)) {
                                if (array[x2][y2] != 'C') {
                                    // Don't replace nearby candles !
                                    array[x2][y2] = 'L'
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

System.err.println array

result = 0

for (x = 0; x < N; ++x) {
    for (y = 0; y < N; ++y) {
        if (array[x][y] == '0') {
            result++
        }
    }
}

println result
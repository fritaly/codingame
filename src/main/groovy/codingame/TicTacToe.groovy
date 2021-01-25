package codingame

def input = new Scanner(System.in)

int row(char[][] board, int y) {
    (0 .. 2).collect { board[y][it] == 'O' ? 1 : 0 }.sum()
}

int column(char[][] board, int x) {
    (0 .. 2).collect { board[it][x] == 'O' ? 1 : 0 }.sum()
}

int diag1(char[][] board) {
    (0 .. 2).collect { board[it][it] == 'O' ? 1 : 0 }.sum()
}

boolean onDiag1(int x, int y) {
    (x == y)
}

int diag2(char[][] board) {
    (0 .. 2).collect {board[it][2 - it] == 'O' ? 1 : 0 }.sum()
}

boolean onDiag2(int x, int y) {
    (x == 2 - y)
}

def board = new char[3][3]

for (y = 0; y < 3; y++) {
    def line = input.nextLine()

    System.err.println("${line}")

    for (x = 0; x < 3; x++) {
        board[y][x] = line.charAt(x)
    }
}

def found = false

for (y = 0; !found && (y < 3); y++) {
    for (x = 0; !found && (x < 3); x++) {
        if (board[y][x] == '.') {
            if ((row(board, y) == 2) || (column(board, x) == 2) || (onDiag1(x, y) && diag1(board) == 2) || (onDiag2(x, y) && diag2(board) == 2)) {
                board[y][x] = 'O'.charAt(0)

                found = true
                break
            }
        }
    }
}

if (!found) {
    println "false"
} else {
    for (y = 0; y < 3; y++) {
        println "${board[y]}"
    }
}
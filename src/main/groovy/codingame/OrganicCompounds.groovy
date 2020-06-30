package codingame

def input = new Scanner(System.in)

def N = input.nextInt()
input.nextLine()

def lines = [] as List<String>

for (i = 0; i < N; i++) {
    def line = input.nextLine()

    lines << line

    System.err.println("${line}")
}

def maxLength = lines.collect { it.length() }.max()

System.err.println "Max length: ${maxLength}"

String[][] array = new String[lines.size()][maxLength / 3]

lines.eachWithIndex{ line, y ->
    def x = 0

    while (line.length() > 0) {
        // Expected values: "CH1", "CH2", "CH3", "(1)" or "(2)"
        array[y][x++] = line.substring(0, 3)

        line = line.substring(3)
    }
}

array.each {
    System.err.println "${it}"
}

def valid = true

for (int y = 0; y < array.length; y += 2) {
    for (int x = 0; x < array[y].length; x += 2) {
        def string = array[y][x]

        if (!string?.trim()) {
            continue
        }

        def left = (x > 0) ? array[y][x-1] : null
        def right = (x+1 < array[y].length) ? array[y][x+1] : null
        def top = (y-1 >= 0) ? array[y-1][x] : null
        def bottom = (y+1 < lines.size()) ? array[y+1][x] : null

        System.err.println("(${y},${x}) ${string}: Left: ${left}, Right: ${right}, Top: ${top}, Bottom: ${bottom}")

        def count = Integer.parseInt("${string.charAt(string.length() - 1)}")

        if (left?.trim()) {
            count += Integer.parseInt("${left.charAt(1)}")
        }
        if (right?.trim()) {
            count += Integer.parseInt("${right.charAt(1)}")
        }
        if (top?.trim()) {
            count += Integer.parseInt("${top.charAt(1)}")
        }
        if (bottom?.trim()) {
            count += Integer.parseInt("${bottom.charAt(1)}")
        }

        System.err.println("Count: ${count}")

        if (count != 4) {
            valid = false

            break
        }
    }

    if (!valid) {
        break
    }
}

println "${valid ? 'VALID' : 'INVALID'}"
package codingame.sudokuvalidator


class Grid {
    int[][] array = new int[9][9]

    Grid(Scanner input) {
        for (def x = 0; x < 9; x++) {
            for (def y = 0; y < 9; y++) {
                def n = input.nextInt()

                array[y][x] = n
            }
        }
    }

    boolean test() {
        def template = [ 1, 2, 3, 4, 5, 6, 7, 8, 9 ].toSet()

        for (int y = 0; y < array.length; y++) {
            def set = new TreeSet(template)

            for (int x = 0; x < array.length; x++) {
                set.remove(array[y][x])
            }

            if (!set.empty) {
                return false
            }
        }

        for (int x = 0; x < array.length; x++) {
            def set = new TreeSet(template)

            for (int y = 0; y < array.length; y++) {
                set.remove(array[y][x])
            }

            if (!set.empty) {
                return false
            }
        }

        for (x in [ 1, 4, 7 ]) {
            for (y in [ 1, 4, 7 ]) {
                def set = new TreeSet(template)

                for (xn in [ -1, 0, +1]) {
                    for (yn in [ -1, 0, +1]) {
                        set.remove(array[y + yn][x + xn])
                    }
                }

                if (!set.empty) {
                    return false
                }
            }
        }


        true
    }
}

def input = new Scanner(System.in)

def grid = new Grid(input)

println Boolean.toString(grid.test())
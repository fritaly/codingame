package codingame

def input = new Scanner(System.in)

def n = input.nextInt()

input.nextLine()

for (i = 0; i < n; ++i) {
    def card = input.nextLine().replace(' ', '') // Remove blanks

    // System.err.println("${card}")

    def list = []

    card.reverse().toCharArray().eachWithIndex{ char c, int i ->
        if (i % 2 == 1) {
            def value = 2 * (c - '0'.charAt(0))

            list << ((value > 9) ? (value - 9) : value)
        }
    }

    def sum = list.sum(0)

    list = []

    card.reverse().toCharArray().eachWithIndex{ char c, int i ->
        if (i % 2 == 0) {
            list << (c - '0'.charAt(0))
        }
    }

    def sum2 = list.sum()

    println "${(sum + sum2) % 10 == 0 ? 'YES' : 'NO'}"
}
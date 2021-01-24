package codingame

class Combination implements Comparable<Combination> {
    int n, x, y

    Combination(int n, int x, int y) {
        this.n = n
        this.x = x
        this.y = y
    }

    @Override
    int compareTo(Combination other) {
        // Sort the combinations by x in descending order.
        other.x <=> this.x
    }

    @Override
    String toString() {
        "1/${n} = 1/${x} + 1/${y}"
    }
}

def n = new Scanner(System.in).nextInt()

def combinations = []

// 1/n = 1/x + 1/y --> (x-n)(y-n) = n^2
// 1) We want (x - n) > 0 --> x > n --> x must start at n+1 (lower bound)
// 2) We want (x - n) <= n^2 --> x <= n * (n + 1)
for (int x = n+1; x <= (n * (n + 1)); x++) {
    def x_minus_n = x - n

    def x_times_n = (x * n) as double

    // 1/n = 1/x + 1/y --> y = (x * n) / (x - n). Infer y from x and n
    def y = x_times_n / x_minus_n

    if ((Math.floor(y) == y) && (x >= y)) {
        combinations << new Combination(n, x, y as int)
    }
}

combinations.sort().each {
    println it
}
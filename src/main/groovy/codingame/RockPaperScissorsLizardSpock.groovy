package codingame

class Player1 {
    int index
    Sign sign

    List<Integer> opponents = []

    Player1(int index, Sign sign) {
        this.index = index
        this.sign = sign
    }

    int compare(Player1 other) {
        this.sign.compare(other.sign)
    }

    @Override
    String toString() {
        "Player1[index: ${index}, sign: ${sign}]"
    }
}

enum Sign {
    R,
    P,
    C,
    L,
    S

    int compare(Sign other) {
        if (this == other) {
            return 0
        }
        switch (this) {
            case R:
                if ((other == L) || (other == C)) {
                    return +1
                }

                return -1

            case P:
                if ((other == R) || (other == S)) {
                    return +1
                }

                return -1

            case C:
                if ((other == P) || (other == L)) {
                    return +1
                }

                return -1

            case L:
                if ((other == S) || (other == P)) {
                    return +1
                }

                return -1

            case S:
                if ((other == C) || (other == R)) {
                    return +1
                }

                return -1

            default:
                throw new RuntimeException("Unsupported sign: ${this}")
        }
    }
}

def input = new Scanner(System.in)

def N = input.nextInt()

def players = [] as List<Player1>

for (i = 0; i < N; ++i) {
    def player = input.nextInt()
    def sign = input.next()

    players << new Player1(player, Sign.valueOf(sign))
}

while (players.size() > 1) {
    def buffer = [] as List<Player1>

    while (players.size() >= 2) {
        def first = players.remove(0)
        def second = players.remove(0)

        def result = first.compare(second)

        if (result == 0) {
            // In case of a tie, the player with the lowest number wins (it's scandalous but it's the rule)
            if (first.index < second.index) {
                buffer << first

                first.opponents << second.index
            } else {
                buffer << second

                second.opponents << first.index
            }
        } else if (result == +1) {
            buffer << first

            first.opponents << second.index
        } else {
            buffer << second

            second.opponents << first.index
        }
    }

    players.addAll(buffer)
    buffer.clear()
}

println players[0].index
println players[0].opponents.join(' ')
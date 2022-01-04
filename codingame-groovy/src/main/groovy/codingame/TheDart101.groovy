/*
 * Copyright 2015-2022, Francois Ritaly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codingame

class Player {

    String name

    List<String> shoots = new LinkedList<>()

    int score = 0

    Player(String name) {
        this.name = name
    }

    private static int parsePoints(String string) {
        if (string == 'X') {
            // Each time you miss the target, your total is decreased by 20 points.
            // If you miss twice consecutively in the same round, your total is decreased by another 10 points.
            // If you miss three times in the same round, your whole total is reset at 0.
            // In other words X.X means -40 but XX. and .XX means -50 in the same round.
            return -20
        }
        if (string.contains('*')) {
            def left = string.substring(0, string.indexOf('*'))
            def right = string.substring(string.indexOf('*') + 1)

            return Integer.parseInt(left) * Integer.parseInt(right)
        }

        Integer.parseInt(string)
    }

    boolean play() {
        if (!shoots) {
            throw new IllegalStateException("No more shoots !")
        }

        System.err.println("${name} is playing ...")

        def previousScore = score

        def misses = 0

        for (int i = 0; (i < 3) && (score < 101); i++) {
            // Evaluate the string. Examples: "10", "3*18"
            def points = parsePoints(shoots.removeFirst())

            if (points == -20) {
                misses++

                if (misses == 2) {
                    // Second miss in a row, 10 extra points
                    points = -30
                }
                if (misses == 3) {
                    // If you miss three times in the same round, your whole total is reset at 0.
                    System.err.println("3 misses in a row, score reset to 0")

                    score = 0

                    return false
                }
            } else {
                misses = 0
            }

            System.err.println("${name} scored ${points} point(s)")

            if (score + points > 101) {
                // If you exceed the score of 101 after a shoot, you revert to your total before your current round
                // and your round ends here
                score = previousScore

                System.err.println("The score exceeds 101 (${score + points}), the round ends prematurely. Score reset to ${score}")

                return false
            }

            score += points

            if (score < 0) {
                score = 0
            }

            System.err.println("New score: ${score}")
        }

        System.err.println("${name}'s finishes his round with a score of ${score}")

        return (score == 101)
    }

    @Override
    String toString() {
        "Player[name: '${name}', shoots: ${shoots}]"
    }
}

input = new Scanner(System.in);

def N = input.nextInt()
input.nextLine()

def players = [] as List<Player>

for (i = 0; i < N; i++) {
    players << new Player(input.nextLine())
}

for (i = 0; i < N; i++) {
    def shoots = input.nextLine()

    players[i].shoots.addAll(Arrays.asList(shoots.split(' ')))
}

System.err.println(players)

while (players.every { it.shoots }) {
    players.each { player ->
        if (player.play()) {
            println player.name
        }
    }
}

/*
 * Copyright 2021, Francois Ritaly
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

class Player2 {
    String name
    String killer

    // Sort the players by name
    Set<Player2> killed = new TreeSet<>({ Player2 a, Player2 b -> a.name <=> b.name })

    Player2(String name) {
        this.name = name
    }
}

def input = new Scanner(System.in)

def tributes = input.nextInt()
input.nextLine()

System.err.println("${tributes}")

// Sort the players by name
def players = [:] as TreeMap<String, Player2>

for (i = 0; i < tributes; ++i) {
    def name = input.nextLine()

    System.err.println("${name}")

    players.put(name, new Player2(name))
}

def turns = input.nextInt()
input.nextLine()

for (i = 0; i < turns; ++i) {
    def info = input.nextLine()

    System.err.println "${info}"

    def strings = info.split('[ ,]')

    def killer = strings[0]

    for (killed in strings[2 .. -1]) {
        // Skip blank lines
        if (killed) {
            players[killer].killed << players[killed]
            players[killed].killer = killer
        }
    }
}

players.eachWithIndex { name, player, n ->
    if (n > 0) {
        println ""
    }

    println "Name: ${player.name}"
    println "Killed: ${player.killed.collect { it.name }.join(', ') ?: 'None' }"
    println "Killer: ${player.killer ?: 'Winner'}"
}
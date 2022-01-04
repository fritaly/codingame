/**
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
package codingame.turing_machine;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class State {
        final String name;
        final List<Action> actions;
        final String value;

        State(String string) {
            // Example: "A:1 R B;1 R HALT"
            final String[] array = string.split(":");

            this.value = string;
            this.name = array[0];
            this.actions = Arrays.asList(array[1].split(";"))
                    .stream().map(s -> new Action(s))
                    .collect(Collectors.toList());
        }
    }

    private static class Action {
        final String symbol, direction, nextState;

        Action(String string) {
            final String[] array = string.split(" ");

            this.symbol = array[0];
            this.direction = array[1];
            this.nextState = array[2];
        }

        boolean moveLeft() {
            return "L".equals(direction);
        }

        @Override
        public String toString() {
            return String.format("%s %s %s", symbol, direction, nextState);
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int numberOfSymbols = scanner.nextInt(), tapeLength = scanner.nextInt(), initialPosition = scanner.nextInt();

        System.err.printf("%d %d %d%n", numberOfSymbols, tapeLength, initialPosition);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final String initialState = scanner.nextLine();

        System.err.println(initialState);

        final int N = scanner.nextInt();

        System.err.println(N);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<State> states = IntStream.range(0, N)
                .mapToObj(i -> new State(scanner.nextLine()))
                .collect(Collectors.toList());

        // Possible states indexed by name
        final Map<String, State> stateMap = new LinkedHashMap<>();

        states.forEach(it -> {
            stateMap.put(it.name, it);

            System.err.println(it.value);
        });

        // Initialization
        int head = initialPosition;
        final char[] tape = createTape(tapeLength);
        State currentState = stateMap.get(initialState);
        int count = 0;

        while (true) {
            System.err.println("===");
            System.err.println("Head index: " + head);
            System.err.println("Tape: " + new String(tape));
            System.err.println("Current state: " + currentState.name);
            System.err.println("Read from tape: " + tape[head] + " (" + (tape[head] - '0') + ")");
            System.err.println("Action to perform: " + currentState.actions.get(tape[head] - '0'));

            final Action action = currentState.actions.get(tape[head] - '0');

            tape[head] = action.symbol.charAt(0);

            count++;

            if (action.moveLeft()) {
                if (--head < 0) {
                    break;
                }
            } else {
                if (++head >= tapeLength) {
                    break;
                }
            }

            if ("HALT".equals(action.nextState)) {
                break;
            }

            currentState = stateMap.get(action.nextState);
        }

        System.out.println(count);
        System.out.println(head);
        System.out.println(new String(tape));
    }

    private static char[] createTape(int tapeLength) {
        final char[] tape = new char[tapeLength];
        Arrays.fill(tape, '0');
        return tape;
    }
}
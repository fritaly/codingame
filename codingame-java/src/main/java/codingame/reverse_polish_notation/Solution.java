/**
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
package codingame.reverse_polish_notation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int N = scanner.nextInt();

        System.err.println(N);

        final List<String> queue = IntStream.range(0, N)
                .mapToObj(i -> scanner.next())
                .collect(Collectors.toList());

        System.err.println(queue.stream().collect(Collectors.joining(" ")));

        final Stack<Integer> stack = new Stack<>();

        try {
            while (!queue.isEmpty()) {
                final String token = queue.remove(0);

                if (token.matches("[+-]?\\d+")) {
                    stack.push(Integer.parseInt(token));
                } else if ("ADD".equals(token)) {
                    stack.push(stack.pop() + stack.pop());
                } else if ("SUB".equals(token)) {
                    final int first = stack.pop(), second = stack.pop();

                    stack.push(second - first);
                } else if ("MUL".equals(token)) {
                    stack.push(stack.pop() * stack.pop());
                } else if ("DIV".equals(token)) {
                    final int first = stack.pop(), second = stack.pop();

                    stack.push(second / first);
                } else if ("MOD".equals(token)) {
                    final int first = stack.pop(), second = stack.pop();

                    stack.push(second % first);
                } else if ("POP".equals(token)) {
                    stack.pop();
                } else if ("DUP".equals(token)) {
                    stack.push(stack.peek());
                } else if ("SWP".equals(token)) {
                    final int first = stack.pop(), second = stack.pop();

                    stack.push(first);
                    stack.push(second);
                } else if ("ROL".equals(token)) {
                    final int n = stack.pop();

                    if (stack.size() < n) {
                        throw new RuntimeException();
                    }

                    stack.push(stack.remove(stack.size() - n));
                } else {
                    throw new RuntimeException("Unexpected token: " + token);
                }
            }

            System.out.println(stack.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(" ")));
        } catch (EmptyStackException | ArithmeticException e) {
            if (!stack.isEmpty()) {
                System.out.print(stack.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(" ")) + " ");
            }

            System.out.println("ERROR");
        }
    }
}
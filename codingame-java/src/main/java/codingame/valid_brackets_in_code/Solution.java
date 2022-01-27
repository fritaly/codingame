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
package codingame.valid_brackets_in_code;

import java.util.Scanner;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int n = scanner.nextInt();

        System.err.println(n);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final String text = IntStream.range(0, n).mapToObj(i -> {
            final String line = scanner.nextLine();

            System.err.println(line);

            return line;
        }).collect(Collectors.joining());

        final Stack<Character> stack = new Stack<>();
        boolean inString = false;
        int index = 0, brackets = 0;
        boolean valid = true;

        while ((index < text.length()) && valid) {
            final char c = text.charAt(index);

            if (inString) {
                if (c == '"') {
                    inString = false;

                    index++;
                } else if (c == '\\') {
                    // Skip the next character
                    index += 2;
                } else {
                    index++;
                }
            } else {
                if (c == '(' || c == '{' || c == '[') {
                    stack.push(c);

                    brackets++;
                } else if (c == ')' || c == '}' || c == ']') {
                    if (stack.isEmpty()) {
                        valid = false;
                    } else {
                        final Character popped = stack.pop();

                        if (c == ')' && popped != '(') {
                            valid = false;
                        } else if (c == '}' && popped != '{') {
                            valid = false;
                        } else if (c == ']' && popped != '[') {
                            valid = false;
                        }

                        brackets++;
                    }
                } else if (c == '"') {
                    inString = true;
                }

                index++;
            }
        }

        if (brackets == 0) {
            System.out.println("No brackets");
        } else {
            System.out.println(valid && stack.isEmpty() ? "Valid" : "Invalid");
        }
    }
}
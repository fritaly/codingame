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
package codingame.tedscompiler;

import java.util.Scanner;
import java.util.Stack;

class Solution {

    public static void main(String args[]) {
        final String line = new Scanner(System.in).nextLine();
        final Stack<Character> stack = new Stack<>();
        int result = 0;

        for (int i = 0; i < line.length(); i++) {
            final char c = line.charAt(i);

            if (c == '<') {
                stack.push(c);
            } else if (c == '>') {
                if (stack.isEmpty() || stack.pop() != '<') {
                    break;
                }
                if (stack.isEmpty()) {
                    result = i + 1;
                }
            } else {
                throw new IllegalStateException("Unexpected char: " + c);
            }
        }

        System.out.println(result);
    }
}
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
package codingame.caesar_is_the_chief;

import java.util.*;

class Solution {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static String decode(String input, int offset) {
        final StringBuilder buffer = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == ' ') {
                buffer.append(c);
            } else {
                buffer.append(ALPHABET.charAt((ALPHABET.indexOf(c) + offset) % 26));
            }
        }

        return buffer.toString();
    }

    public static void main(String args[]) {
        final String text = new Scanner(System.in).nextLine();

        System.err.println(text);

        for (int n = 0; n < 26; n++) {
            final String plain = decode(text, n);

            System.err.println(plain);

            if (plain.startsWith("CHIEF ") || plain.contains(" CHIEF ") || plain.endsWith(" CHIEF") || plain.equals("CHIEF")) {
                System.out.println(plain);
                return;
            }
        }

        System.out.println("WRONG MESSAGE");
    }
}
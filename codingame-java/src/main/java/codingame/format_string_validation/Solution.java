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
package codingame.format_string_validation;

import java.util.*;
import java.util.regex.Pattern;

class Solution {

    private static Pattern createPattern(String format) {
        final StringBuilder buffer = new StringBuilder().append("^");

        for (char c : format.toCharArray()) {
            switch (c) {
                case '?':
                    // Match one character
                    buffer.append(".");
                    break;
                case '~':
                    // Match zero or more characters
                    buffer.append(".*");
                    break;

                case '(':
                case ')':
                case '[':
                case ']':
                case '{':
                case '}':
                case '.':
                case '^':
                case '$':
                case '\\':
                case '/':
                case '*':
                case '+':
                case '"':
                case '|':
                    // Escape the regex meta-characters
                    buffer.append("\\").append(c);
                    break;

                default:
                    buffer.append(c);
                    break;
            }
        }

        buffer.append("$");

        System.err.println("Regexp: " + buffer);

        return Pattern.compile(buffer.toString());
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine();
        String format = scanner.nextLine();

        System.err.println(text);
        System.err.println(format);

        System.out.println(createPattern(format).matcher(text).matches() ? "MATCH" : "FAIL");
    }
}
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
package codingame.elementary_cellular_automaton;

import java.util.*;
import java.io.*;
import java.math.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static String padLeft(String s, int length) {
        if (s.length() == length) {
            return s;
        }

        return IntStream.range(0, length - s.length()).mapToObj(i -> "0").collect(Collectors.joining()) + s;
    }

    private static Map<String, Character> decode(int rule) {
        final String binary = padLeft(Integer.toBinaryString(rule), 8)
                .replace('0', '.')
                .replace('1', '@');

        final Map<String, Character> map = new LinkedHashMap<>();
        map.put("@@@", binary.charAt(0));
        map.put("@@.", binary.charAt(1));
        map.put("@.@", binary.charAt(2));
        map.put("@..", binary.charAt(3));
        map.put(".@@", binary.charAt(4));
        map.put(".@.", binary.charAt(5));
        map.put("..@", binary.charAt(6));
        map.put("...", binary.charAt(7));

        return map;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int R = scanner.nextInt(), N = scanner.nextInt();

        System.err.printf("%d %d%n", R, N);

        final Map<String, Character> rules = decode(R);

        final String startPattern = scanner.next();

        System.err.println(startPattern);

        final int L = startPattern.length();
        char[] array = startPattern.toCharArray();

        for (int i = 0; i < N; i++) {
            System.out.println(new String(array));

            final char[] output = new char[L];

            for (int j = 0; j < array.length; j++) {
                output[j] = rules.get("" + array[(j + L - 1) % L] + array[j] + array[(j + 1) % L]);
            }

            array = output;
        }
    }
}
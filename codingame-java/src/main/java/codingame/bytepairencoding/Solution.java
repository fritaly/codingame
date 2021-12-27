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
package codingame.bytepairencoding;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static Map<String, Integer> analyze(String input) {
        Map<String, LinkedList<Integer>> indices = new LinkedHashMap<>();

        for (int index = 0; index < input.length() - 1; index++) {
            final String string = input.substring(index, index + 2);

            indices.putIfAbsent(string, new LinkedList<>());

            final LinkedList<Integer> list = indices.get(string);

            // Note: we only count (and replace) non-overlapping repetitions
            if (list.isEmpty() || list.getLast() + 1 < index) {
                list.add(index);
            }
        }

        System.err.println("Analysis: " + indices);

        final Map<String, Integer> result = new LinkedHashMap<>();
        indices.entrySet().forEach(e -> result.put(e.getKey(), e.getValue().size()));

        return result;
    }

    private static String findStringToEncode(Map<String, Integer> map) {
        final int maxCount = map.values().stream().max(Comparator.comparingInt(a -> a)).get();

        if (maxCount == 1) {
            // No repeated string
            return null;
        }

        return map.entrySet().stream().filter(e -> e.getValue() == maxCount).findFirst().get().getKey();
    }

    private static class NonTerminals implements Iterator<Character> {
        private char current = 'Z';

        @Override
        public boolean hasNext() {
            return (current >= 'A');
        }

        @Override
        public Character next() {
            return current--;
        }
    }

    private static String encode(String input, Map<String, Character> rules) {
        final StringBuilder buffer = new StringBuilder();

        int start = 0, end = start + 2;

        while (end <= input.length()) {
            final String s = input.substring(start, end);

            if (rules.containsKey(s)) {
                buffer.append(rules.get(s));

                start += 2;
                end = start + 2;
            } else {
                buffer.append(s.substring(0, 1));

                start++;
                end++;
            }
        }

        if (start < input.length()) {
            // Process the remaining input
            buffer.append(input.substring(start));
        }

        return buffer.toString();
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();
        final int m = scanner.nextInt();

        scanner.nextLine();

        System.err.printf("%d %d%n", n, m);

        final String input = IntStream.range(0, n)
                .mapToObj(i -> scanner.nextLine())
                .collect(Collectors.joining());

        System.err.println(input);

        String current = input;

        final NonTerminals nonTerminals = new NonTerminals();
        final Map<String, Character> rules = new LinkedHashMap<>();

        while (nonTerminals.hasNext()) {
            System.err.println("Current: " + current);

            final Map<String, Integer> counts = analyze(current);

            System.err.println("Count: " + counts);

            final String stringToEncode = findStringToEncode(counts);

            System.err.println("String to encode: " + stringToEncode);

            if (stringToEncode == null) {
                break;
            }

            // Update the rules
            rules.put(stringToEncode, nonTerminals.next());

            System.err.println("Rules: " + rules);

            current = encode(current, rules);

            System.err.println("New current: " + current);
        }

        System.out.println(current);

        rules.entrySet().forEach(entry ->
            System.out.println(entry.getValue() + " = " + entry.getKey())
        );
    }
}
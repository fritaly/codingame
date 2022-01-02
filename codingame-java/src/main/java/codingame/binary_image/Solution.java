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
package codingame.binary_image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Solution {

    private static String decode(String string) {
        return decode(Arrays.stream(string.split(" "))
                .map(s -> Integer.parseInt(s))
                .collect(Collectors.toList()));
    }

    private static String decode(List<Integer> list) {
        final StringBuilder buffer = new StringBuilder();

        boolean black = false;

        if (list.get(0) == 0) {
            black = true;
            list.remove(0);
        }

        while (!list.isEmpty()) {
            final Integer n = list.remove(0);

            final char[] chars = new char[n];

            Arrays.fill(chars, black ? 'O' : '.');

            buffer.append(chars);

            black = !black;
        }

        return buffer.toString();
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt();

        System.err.println(height);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<String> decoded = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            final String line = scanner.nextLine();

            System.err.println(line);

            decoded.add(decode(line));
        }

        if (decoded.stream().mapToInt(s -> s.length()).distinct().count() > 1) {
            System.out.println("INVALID");
        } else {
            for (String s : decoded) {
                System.out.println(s);
            }
        }
    }
}
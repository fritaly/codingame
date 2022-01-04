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
package codingame.detective_geek;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static final Map<String, String> map = new LinkedHashMap<>();

    static {
        map.put("jan", "0");
        map.put("feb", "1");
        map.put("mar", "2");
        map.put("apr", "3");
        map.put("may", "4");
        map.put("jun", "5");
        map.put("jul", "6");
        map.put("aug", "7");
        map.put("sep", "8");
        map.put("oct", "9");
        map.put("nov", "a");
        map.put("dec", "b");
    }

    private static String decode(final String word) {
        return IntStream.range(0, word.length() / 3)
                .mapToObj(i -> word.substring(3 * i, 3 * (i + 1)))
                .map(s -> map.get(s))
                .collect(Collectors.joining());
    }

    private static String decodeWord(String word) {
        final String base12 = decode(word);
        final int base10 = base10(base12);
        final String c = Character.toString((char) base10);

        System.err.printf("%s -> %s -> %d -> %s%n", word, base12, base10, c);

        return c;
    }

    private static int base10(String base12) {
        return Integer.parseInt(base12, 12);
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final String time = scanner.nextLine(), address = scanner.nextLine();

        System.err.println(time);
        System.err.println(address);

        final int time2 = Integer.parseInt(time.replaceAll("#", "1").replaceAll("\\*", "0"), 2);

        System.out.printf("%02d:%02d%n", time2 / 100, time2 % 100);

        System.out.println(Arrays.asList(address.split(" "))
                .stream()
                .map(s -> decodeWord(s))
                .collect(Collectors.joining()));
    }
}
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
package codingame.ipv6_shortener;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static String trimLeft(String s) {
        while (s.startsWith("0") && s.length() > 1) {
            s = s.substring(1);
        }

        return s;
    }

    private static String compact(String s) {
        for (int i = 7; i > 1; i--) {
            final String str = IntStream.range(0, i).mapToObj(n -> "0").collect(Collectors.joining(":"));

            System.err.println(str);

            if (s.startsWith(str + ":")) {
                return s.replaceFirst(str + ":", "::");
            } else if (s.endsWith(":" + str)) {
                return s.replaceFirst(":" + str, "::");
            } else if (s.contains(":" + str + ":")) {
                return s.replaceFirst(":" + str + ":", "::");
            }
        }

        return s;
    }

    public static void main(String args[]) {
        final String ip = new Scanner(System.in).nextLine();

        System.err.println(ip);

        final String s = Arrays.asList(ip.split(":"))
                .stream()
                .map(str -> trimLeft(str))
                .collect(Collectors.joining(":"));

        System.out.println(compact(s));
    }
}
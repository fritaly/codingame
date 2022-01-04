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
package codingame.queneaunumbers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static LinkedList<String> permutate(LinkedList<String> list) {
        final LinkedList<String> result = new LinkedList<>();

        boolean tail = true;

        while (!list.isEmpty()) {
            result.add(tail ? list.removeLast() : list.removeFirst());

            tail = !tail;
        }

        return result;
    }

    private static String encode(List<String> list) {
        return list.stream().collect(Collectors.joining(","));
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final int n = scanner.nextInt();

        LinkedList<String> list = new LinkedList<>(IntStream.range(1, n + 1)
                .mapToObj(i -> "" + i)
                .collect(Collectors.toList()));

        final LinkedList<String> memory = new LinkedList<>();

        final String reference = encode(list);

        System.err.println(list);

        for (int i = 0; i < n; i++) {
            list = permutate(list);

            System.err.println(list);

            memory.add(encode(list));
        }

        if (reference.equals(memory.getLast())) {
            for (String s : memory) {
                System.out.println(s);
            }
        } else {
            System.out.println("IMPOSSIBLE");
        }
    }
}
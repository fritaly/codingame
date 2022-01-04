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
package codingame.fax_machine;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt(), height = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final char[][] array = new char[height][width];

        final int[] values = Arrays.asList(scanner.nextLine().split(" "))
                .stream().mapToInt(s -> Integer.parseInt(s))
                .toArray();

        int index = 0;
        boolean black = true;

        for (int value : values) {
            for (int i = 0; i < value; i++) {
                array[index / width][index % width] = black ? '*' : ' ';
                index++;
            }

            black = !black;
        }

        System.out.println(Arrays.asList(array)
                .stream()
                .map(a -> "|" + new String(a) + "|")
                .collect(Collectors.joining("\n")));;
    }
}
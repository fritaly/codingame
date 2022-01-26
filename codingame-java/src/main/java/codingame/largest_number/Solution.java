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
package codingame.largest_number;

import java.util.*;

class Solution {

    private static List<Integer> removeDigit(int number) {
        final String s = Integer.toString(number);

        final List<Integer> result = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {
            if (i == 0) {
                result.add(Integer.parseInt(s.substring(i + 1)));
            } else {
                result.add(Integer.parseInt(s.substring(0, i) + s.substring(i + 1)));
            }
        }

        return result;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int number = scanner.nextInt(), D = scanner.nextInt();

        int solution = 0;

        if (number % D == 0) {
            solution = number;
        }

        if (solution == 0) {
            for (Integer n : removeDigit(number)) {
                if (n % D == 0) {
                    solution = Math.max(solution, n);
                } else {
                    for (Integer p : removeDigit(n)) {
                        if (p % D == 0) {
                            solution = Math.max(solution, p);
                        }
                    }
                }
            }
        }

        System.out.println(solution);
    }
}
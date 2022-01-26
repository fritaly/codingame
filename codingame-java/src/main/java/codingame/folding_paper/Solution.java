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
package codingame.folding_paper;

import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final String order = scanner.nextLine(), side = scanner.nextLine();

        System.err.println(order);
        System.err.println(side);

        int up = 1, left = 1, right = 1, down = 1;

        for (char c : order.toCharArray()) {
            switch (c) {
                case 'U':
                    left *= 2;
                    right *= 2;
                    down += up;
                    up = 1;
                    break;
                case 'L':
                    up *= 2;
                    right += left;
                    down *= 2;
                    left = 1;
                    break;
                case 'R':
                    up *= 2;
                    left += right;
                    down *= 2;
                    right = 1;
                    break;
                case 'D':
                    up += down;
                    left *= 2;
                    right *= 2;
                    down = 1;
                    break;
                default:
                    throw new RuntimeException("Unexpected order: " + c);
            }
        }

        switch (side) {
            case "U":
                System.out.println(up);
                break;
            case "L":
                System.out.println(left);
                break;
            case "R":
                System.out.println(right);
                break;
            case "D":
                System.out.println(down);
                break;
        }
    }
}
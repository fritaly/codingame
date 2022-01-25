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
package codingame.one_billion_d_world;

import java.util.*;

class Solution {

    private static class Component {
        final long cardinality, value;

        private Component(long cardinality, long value) {
            this.cardinality = cardinality;
            this.value = value;
        }

        Component withCardinality(long n) {
            return new Component(n, value);
        }
    }

    private static class Vector {
        final List<Component> components = new ArrayList<>();

        private Vector(Scanner scanner) {
            final String line = scanner.nextLine();

            System.err.println(line);

            final String[] array = line.split(" ");

            for (int i = 0; i < array.length; i += 2) {
                components.add(new Component(Long.parseLong(array[i]), Long.parseLong(array[i+1])));
            }
        }

        long dot(Vector other) {
            long result = 0;

            final LinkedList<Component> queue1 = new LinkedList<>(this.components), queue2 = new LinkedList<>(other.components);

            while (!queue1.isEmpty() && !queue2.isEmpty()) {
                final Component component1 = queue1.removeFirst();
                final Component component2 = queue2.removeFirst();

                final long cardinality = Math.min(component1.cardinality, component2.cardinality);

                result += (cardinality * component1.value * component2.value);

                if (component1.cardinality > cardinality) {
                    queue1.addFirst(component1.withCardinality(component1.cardinality - cardinality));
                }
                if (component2.cardinality > cardinality) {
                    queue2.addFirst(component2.withCardinality(component2.cardinality - cardinality));
                }
            }

            return result;
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final Vector a = new Vector(scanner), b = new Vector(scanner);

        System.out.println(a.dot(b));
    }
}
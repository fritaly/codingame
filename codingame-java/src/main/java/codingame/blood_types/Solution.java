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
package codingame.blood_types;

import java.util.*;

class Solution {

    private enum Group {
        A, B, O;

        boolean a() {
            return equals(A);
        }

        boolean b() {
            return equals(B);
        }

        boolean o() {
            return equals(O);
        }
    }

    private enum BloodType {
        A, B, AB, O;

        List<Groups> groups() {
            switch (this) {
                case A:
                    return Arrays.asList(new Groups(Group.A, Group.A), new Groups(Group.A, Group.O));
                case B:
                    return Arrays.asList(new Groups(Group.B, Group.B), new Groups(Group.B, Group.O));
                case AB:
                    return Arrays.asList(new Groups(Group.A, Group.B));
                case O:
                    return Arrays.asList(new Groups(Group.O, Group.O));
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private static class Groups {
        final Group first, second;

        private Groups(Group first, Group second) {
            this.first = first;
            this.second = second;
        }

        BloodType bloodType() {
            if (first.a() && second.a()) {
                return BloodType.A;
            }
            if ((first.a() && second.o()) || (first.o() && second.a())) {
                return BloodType.A;
            }
            if (first.b() && second.b()) {
                return BloodType.B;
            }
            if ((first.b() && second.o()) || (first.o() && second.b())) {
                return BloodType.B;
            }
            if (first.o() && second.o()) {
                return BloodType.O;
            }
            if ((first.a() && second.b()) || (first.b() && second.a())) {
                return BloodType.AB;
            }

            throw new RuntimeException("");
        }
    }

    private enum Rhesus {
        PLUS, MINUS;

        boolean plus() {
            return equals(PLUS);
        }

        boolean minus() {
            return equals(MINUS);
        }

        char symbol() {
            return plus() ? '+' : '-';
        }

        public static Rhesus fromSymbol(char c) {
            switch (c) {
                case '+':
                    return PLUS;
                case '-':
                    return MINUS;
                default:
                    throw new IllegalArgumentException("Unexpected value: " + c);
            }
        }

        List<RhesusPair> rhesuses() {
            switch (this) {
                case MINUS:
                    return Arrays.asList(new RhesusPair(MINUS, MINUS));
                case PLUS:
                    return Arrays.asList(new RhesusPair(PLUS, PLUS), new RhesusPair(PLUS, MINUS));
                default:
                    throw new UnsupportedOperationException();
            }
        }
    }

    private static class RhesusPair {
        final Rhesus first, second;

        private RhesusPair(Rhesus first, Rhesus second) {
            this.first = first;
            this.second = second;
        }

        Rhesus rhesus() {
            return (first.plus() || second.plus()) ? Rhesus.PLUS : Rhesus.MINUS;
        }
    }

    private static class Blood {
        final BloodType type;
        final Rhesus rhesus;

        public static List<Blood> all() {
            return Arrays.asList(
                    Blood.parse("A+"), Blood.parse("A-"),
                    Blood.parse("B+"), Blood.parse("B-"),
                    Blood.parse("AB+"), Blood.parse("AB-"),
                    Blood.parse("O+"), Blood.parse("O-")
            );
        }

        private static Blood parse(String s) {
            return new Blood(BloodType.valueOf(s.substring(0, s.length() - 1)), Rhesus.fromSymbol(s.charAt(s.length() - 1)));
        }

        private Blood(BloodType type, Rhesus rhesus) {
            this.type = type;
            this.rhesus = rhesus;
        }

        Set<Blood> mix(Blood other) {
            final HashSet<Blood> result = new HashSet<>();

            for (Groups groups1 : this.type.groups()) {
                for (Groups groups2 : other.type.groups()) {

                    final Set<Rhesus> rhesuses = new TreeSet<>();

                    for (RhesusPair rhesus1 : this.rhesus.rhesuses()) {
                        for (RhesusPair rhesus2 : other.rhesus.rhesuses()) {
                            rhesuses.add(new RhesusPair(rhesus1.first, rhesus2.first).rhesus());
                            rhesuses.add(new RhesusPair(rhesus1.first, rhesus2.second).rhesus());
                            rhesuses.add(new RhesusPair(rhesus1.second, rhesus2.first).rhesus());
                            rhesuses.add(new RhesusPair(rhesus1.second, rhesus2.second).rhesus());
                        }
                    }

                    for (Rhesus rhesus : rhesuses) {
                        result.add(new Blood(new Groups(groups1.first, groups2.first).bloodType(), rhesus));
                        result.add(new Blood(new Groups(groups1.first, groups2.second).bloodType(), rhesus));
                        result.add(new Blood(new Groups(groups1.second, groups2.first).bloodType(), rhesus));
                        result.add(new Blood(new Groups(groups1.second, groups2.second).bloodType(), rhesus));
                    }
                }
            }

            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Blood blood = (Blood) o;
            return type == blood.type && rhesus == blood.rhesus;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, rhesus);
        }

        @Override
        public String toString() {
            return type.name() + rhesus.symbol();
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int N = scanner.nextInt();

        for (int i = 0; i < N; i++) {
            final String parent1 = scanner.next(), parent2 = scanner.next(), child = scanner.next();

            final Set<String> result = new TreeSet<>();

            for (Blood blood1 : Blood.all()) {
                for (Blood blood2 : Blood.all()) {
                    for (Blood blood : blood1.mix(blood2)) {
                        if ("?".equals(parent1) && parent2.equals(blood2.toString()) && child.equals(blood.toString())) {
                            result.add(blood1.toString());
                        }
                        if (parent1.equals(blood1.toString()) && "?".equals(parent2) && child.equals(blood.toString())) {
                            result.add(blood2.toString());
                        }
                        if (parent1.equals(blood1.toString()) && parent2.equals(blood2.toString()) && "?".equals(child)) {
                            result.add(blood.toString());
                        }
                    }
                }
            }

            if (result.isEmpty()) {
                System.out.println("impossible");
            } else {
                System.out.println(String.join(" ", result));
            }
        }
    }
}
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
package codingame.music_scores;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private static class Note {
        final char[][] array;
        final IntRange rangeX, rangeY;
        final Color color;

        private Note(char[][] array, IntRange rangeX, IntRange rangeY, Color color) {
            this.array = array;
            this.rangeX = rangeX;
            this.rangeY = rangeY;
            this.color = color;
        }

        String asText(List<Staff> staffs) {
            char pitch = ' ';

            for (Staff staff : staffs) {
                System.err.printf("Comparing %s to %s: %d ...%n", rangeY, staff, rangeY.compareTo(staff.rangeY));

                if (rangeY.compareTo(staff.rangeY) < 0) {
                    pitch = staff.note;
                    if (pitch == 'G') {
                        pitch = 'A';
                    } else {
                        pitch++;
                    }
                    break;
                } else if (rangeY.compareTo(staff.rangeY) == 0) {
                    pitch = staff.note;
                    break;
                } else {
                    pitch = staff.note;
                    if (pitch == 'G') {
                        pitch = 'A';
                    } else {
                        pitch++;
                    }
                }
            }

            return String.format("%s%s", pitch, color.symbol());
        }
    }

    private enum Color {
        WHITE, BLACK;

        char symbol() {
            return equals(WHITE) ? 'H' : 'Q';
        }
    }

    private static class Staff {
        final IntRange rangeY;
        char note;

        private Staff(IntRange rangeY) {
            this.rangeY = rangeY;
        }

        @Override
        public String toString() {
            return String.format("%s=%s", note, rangeY);
        }
    }

    private static class IntRange implements Comparable<IntRange> {
        // Min and max and included
        final int min, max;

        private IntRange(int min, int max) {
            this.min = min;
            this.max = max;
        }

        int span() {
            return max - min + 1;
        }

        @Override
        public int compareTo(IntRange that) {
            return (this.max < that.min) ? -1 : (this.min > that.max) ? +1 : 0;
        }

        @Override
        public String toString() {
            return String.format("[%d,%d]", min, max);
        }
    }

    private static void dump(char[][] array) {
        for (int y = 0; y < array.length; y++) {
            System.err.println(new String(array[y]));
        }
    }

    private static char[][] decode(String image, int width, int height) {
        final char[][] array = new char[height][width];

        for (int y = 0; y < array.length; y++) {
            Arrays.fill(array[y], '.');
        }

        final String[] strings = image.split(" ");

        System.err.println("Array length: " + strings.length);

        int index = 0;

        for (int i = 0; i < strings.length; i += 2) {
            final int length = Integer.parseInt(strings[i+1]);

            if ("B".equals(strings[i])) {
                for (int n = index; n < index + length; n++) {
                    array[n / width][n % width] = '*';
                }
            }

            index += length;
        }

        return array;
    }

    private static int minX(char[][] array) {
        for (int x = 0; x < array[0].length; x++) {
            for (int y = 0; y < array.length; y++) {
                if (array[y][x] != '.') {
                    return x;
                }
            }
        }

        return -1;
    }

    private static int maxX(char[][] array) {
        for (int x = array[0].length - 1; x >= 0 ; x--) {
            for (int y = 0; y < array.length; y++) {
                if (array[y][x] != '.') {
                    return x;
                }
            }
        }

        return -1;
    }

    private static List<IntRange> findStaffs(int x, char[][] array) {
        final List<IntRange> result = new ArrayList<>();

        int startY = -1;

        for (int y = 0; y < array.length; y++) {
            if (array[y][x] == '*') {
                if (startY == -1) {
                    startY = y;
                }
            } else {
                if (startY != -1) {
                    result.add(new IntRange(startY, y - 1));

                    startY = -1;
                }
            }
        }

        return result;
    }

    private static void eraseStaffs(char[][] array, List<Staff> staffs, IntRange rangeX) {
        for (int x = rangeX.min; x <= rangeX.max ; x++) {
            for (Staff staff : staffs) {
                final IntRange rangeY = staff.rangeY;

                if ((array[rangeY.min - 1][x] == '.') || (array[rangeY.max + 1][x] == '.')) {
                    for (int y = rangeY.min; y <= rangeY.max; y++) {
                        array[y][x] = '.';
                    }
                }
            }

        }
    }

    private static List<IntRange> findNotes(char[][] array, IntRange rangeX) {
        final List<IntRange> result = new ArrayList<>();

        int startX = -1;

        for (int x = rangeX.min; x <= rangeX.max; x++) {
            boolean empty = true;

            for (int y = 0; y < array.length; y++) {
                if (array[y][x] != '.') {
                    empty = false;
                    break;
                }
            }

            if (empty) {
                if (startX != -1) {
                    result.add(new IntRange(startX, x - 1));
                    startX = -1;
                }
            } else {
                if (startX == -1) {
                    startX = x;
                }
            }
        }

        return result;
    }

    private static Note identifyNote(char[][] array, IntRange rangeX) {
        // For each row, count the number of '*'
        int maxWidth = 0, startY = -1, endY = -1, maxCount = 0;

        for (int y = 0; y < array.length; y++) {
            int count = 0, minX = -1, maxX = -1;
            for (int x = rangeX.min; x <= rangeX.max; x++) {
                if (array[y][x] == '*') {
                    count++;
                    if (minX == -1) {
                        minX = x;
                    }
                    maxX = x;
                }
            }

            final int width = maxX - minX + 1;

            if (width > maxWidth) {
                maxWidth = width;
                startY = endY = y;
                maxCount = count;
            } else if (width == maxWidth) {
                endY = y;
            }
        }

        System.err.printf("Max width: %d between [%d,%d] - max count = %d%n", maxWidth, startY, endY, maxCount);

        return new Note(array, rangeX, new IntRange(startY, endY), (maxCount == rangeX.span() ? Color.BLACK : Color.WHITE));
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int width = scanner.nextInt(), height = scanner.nextInt();

        System.err.printf("%d %d%n", width, height);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final String image = scanner.nextLine();

        System.err.println(image);

        final char[][] array = decode(image, width, height);

        dump(array);

        // Identify the min and max x values
        final IntRange rangeX = new IntRange(minX(array), maxX(array));

        System.err.printf("Range X: %s%n", rangeX);

        // Locate the staffs
        final List<Staff> staffs = findStaffs(rangeX.min, array)
                .stream()
                .map(s -> new Staff(s))
                .collect(Collectors.toList());

        // Assign notes to the staffs found
        staffs.get(0).note = 'F';
        staffs.get(1).note = 'D';
        staffs.get(2).note = 'B';
        staffs.get(3).note = 'G';
        staffs.get(4).note = 'E';

        // Add an extra staff for C
        final Staff staff3 = staffs.get(3), staff4 = staffs.get(4);
        final int delta = staff4.rangeY.min - staff3.rangeY.min;
        staffs.add(new Staff(new IntRange(staff4.rangeY.min + delta, staff4.rangeY.max + delta)));
        staffs.get(5).note = 'C';

        System.err.printf("Staffs: %s%n", staffs);

        // Erase the staffs
        eraseStaffs(array, staffs, rangeX);

        dump(array);

        // Find the x-range associated to each note
        final List<IntRange> noteRanges = findNotes(array, rangeX);

        System.err.printf("Notes: %s%n", noteRanges);

        System.out.println(noteRanges.stream()
                .map(it -> identifyNote(array, it).asText(staffs))
                .collect(Collectors.joining(" ")));;
    }
}
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
package codingame.is_the_king_in_check_part_1;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean exists() {
            return ((0 <= x) && (x < 8)) && ((0 <= y) && (y < 8));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position position = (Position) o;
            return x == position.x && y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }

    private enum Piece {
        ROOK('R'),
        KNIGHT('N'),
        BISHOP('B'),
        QUEEN('Q');

        private final char id;

        Piece(char id) {
            this.id = id;
        }

        static Piece byId(char id) {
            return Arrays.asList(values()).stream().filter(p -> p.id == id).findFirst().orElse(null);
        }

        Stream<Position> accessiblePositions(Position from) {
            if (this == ROOK) {
                final Stream<Position> s1 = IntStream.range(0, 8).mapToObj(x -> new Position(x, from.y));
                final Stream<Position> s2 = IntStream.range(0, 8).mapToObj(y -> new Position(from.x, y));

                return Stream.concat(s1, s2).filter(p -> !p.equals(from));
            }
            if (this == BISHOP) {
                final Stream<Position> s1 = IntStream.range(1, 8).mapToObj(n -> new Position(from.x - n, from.y - n));
                final Stream<Position> s2 = IntStream.range(1, 8).mapToObj(n -> new Position(from.x - n, from.y + n));
                final Stream<Position> s3 = IntStream.range(1, 8).mapToObj(n -> new Position(from.x + n, from.y - n));
                final Stream<Position> s4 = IntStream.range(1, 8).mapToObj(n -> new Position(from.x + n, from.y + n));

                return Stream.of(s1, s2, s3, s4).flatMap(s -> s).filter(p -> p.exists());
            }
            if (this == QUEEN) {
                return Stream.concat(ROOK.accessiblePositions(from), BISHOP.accessiblePositions(from));
            }
            if (this == KNIGHT) {
                return Stream.of(new Position(from.x + 1, from.y - 2),
                        new Position(from.x + 1, from.y + 2),
                        new Position(from.x + 2, from.y - 1),
                        new Position(from.x + 2, from.y + 1),
                        new Position(from.x - 1, from.y - 2),
                        new Position(from.x - 1, from.y + 2),
                        new Position(from.x - 2, from.y - 1),
                        new Position(from.x - 2, from.y + 1))
                        .filter(p -> p.exists());
            }

            throw new UnsupportedOperationException();
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        final char[][] grid = new char[8][8];

        for (int y = 0; y < 8; y++) {
            grid[y] = scanner.nextLine().replaceAll(" ", "").toCharArray();
        }

        System.err.println(Arrays.asList(grid).stream().map(a -> new String(a)).collect(Collectors.joining("\n")));

        Position kingPosition = null, enemyPosition = null;
        Piece enemyPiece = null;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (grid[y][x] == 'K') {
                    kingPosition = new Position(x, y);
                } else if (grid[y][x] != '_') {
                    enemyPosition = new Position(x, y);
                    enemyPiece = Piece.byId(grid[y][x]);
                }
            }
        }

        System.err.printf("King: %s, Enemy: %s in %s%n", kingPosition, enemyPiece, enemyPosition);

        final List<Position> accessiblePositions = enemyPiece.accessiblePositions(enemyPosition).collect(Collectors.toList());

        System.err.println("Accessible positions: " + accessiblePositions);

        System.out.println(accessiblePositions.contains(kingPosition) ? "Check" : "No Check");
    }
}
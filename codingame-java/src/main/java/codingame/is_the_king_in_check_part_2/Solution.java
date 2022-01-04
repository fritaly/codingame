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
package codingame.is_the_king_in_check_part_2;

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

    private static class Piece {
        final Position position;
        final PieceType type;

        private Piece(Position position, PieceType type) {
            this.position = position;
            this.type = type;
        }

        List<Position> accessiblePositions(Position block) {
            final List<Position> result = type.accessiblePositions(position, block);

            System.err.printf("Accessible positions for %s in %s: %s%n", type, position, result);

            return result;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Piece.class.getSimpleName() + "[", "]")
                    .add("position=" + position)
                    .add("type=" + type)
                    .toString();
        }
    }

    private enum PieceType {
        ROOK('R'),
        KNIGHT('N'),
        BISHOP('B'),
        QUEEN('Q');

        private final char id;

        PieceType(char id) {
            this.id = id;
        }

        static PieceType byId(char id) {
            return Arrays.asList(values()).stream().filter(p -> p.id == id).findFirst().orElse(null);
        }

        List<Position> accessiblePositions(Position from, Position block) {
            if (this == ROOK) {
                final List<Position> result = new ArrayList<>();

                for (int x = from.x; x >= 0; x--) {
                    final Position position = new Position(x, from.y);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }
                for (int x = from.x; x < 8; x++) {
                    final Position position = new Position(x, from.y);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }
                for (int y = from.y; y >= 0; y--) {
                    final Position position = new Position(from.x, y);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }
                for (int y = from.y; y < 8; y++) {
                    final Position position = new Position(from.x, y);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }

                return result;
            }
            if (this == BISHOP) {
                final List<Position> result = new ArrayList<>();

                for (int n = 0; n < 8; n++) {
                    final Position position = new Position(from.x - n, from.y - n);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }
                for (int n = 0; n < 8; n++) {
                    final Position position = new Position(from.x - n, from.y + n);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }
                for (int n = 0; n < 8; n++) {
                    final Position position = new Position(from.x + n, from.y - n);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }
                for (int n = 0; n < 8; n++) {
                    final Position position = new Position(from.x + n, from.y + n);

                    if (position.equals(block)) {
                        break;
                    }

                    result.add(position);
                }

                return result.stream().filter(p -> p.exists()).collect(Collectors.toList());
            }
            if (this == QUEEN) {
                final List<Position> result = new ArrayList<>();
                result.addAll(ROOK.accessiblePositions(from, block));
                result.addAll(BISHOP.accessiblePositions(from, block));

                return result;
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
                        .filter(p -> p.exists())
                        .collect(Collectors.toList());
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

        Position kingPosition = null;
        final List<Piece> enemyPieces = new ArrayList<>();

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (grid[y][x] == 'k') {
                    kingPosition = new Position(x, y);
                } else if (grid[y][x] != '_') {
                    enemyPieces.add(new Piece(new Position(x, y), PieceType.byId(grid[y][x])));
                }
            }
        }

        System.err.printf("King: %s, Enemy pieces: %s%n", kingPosition, enemyPieces);

        final Piece first = enemyPieces.get(0), second = enemyPieces.get(1);

        if (first.accessiblePositions(second.position).contains(kingPosition) || second.accessiblePositions(first.position).contains(kingPosition)) {
            System.out.println("Check");
        } else {
            System.out.println("No Check");
        }
    }
}
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
package codingame.chess_moves_on_fen_position;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        static Position parse(String value) {
            // Example: "e2"
            final char c1 = value.charAt(0), c2 = value.charAt(1);

            return new Position(c1 - 'a', '8' - c2);
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }
    }

    private static class Move {
        final String id;
        final Position start, end;

        private Move(String id, Position start, Position end) {
            this.id = id;
            this.start = start;
            this.end = end;
        }

        boolean isPawnPromotion() {
            return id.length() > 4;
        }

        Piece getPromotionPiece() {
            return Piece.fromChar(id.charAt(4));
        }

        int deltaX() {
            return end.x - start.x;
        }

        int deltaY() {
            return end.y - start.y;
        }

        static Move parse(String move) {
            final Position start = Position.parse(move.substring(0, 2));
            final Position end = Position.parse(move.substring(2));

            return new Move(move, start, end);
        }

        @Override
        public String toString() {
            return String.format("%s (%s -> %s)", id, start, end);
        }
    }

    private enum PieceType {
        ROOK('r'),
        KNIGHT('n'),
        BISHOP('b'),
        QUEEN('q'),
        KING('k'),
        PAWN('p');

        private final char id;

        PieceType(char id) {
            this.id = id;
        }

        static PieceType fromId(char id) {
            for (PieceType value : values()) {
                if (value.id == id) {
                    return value;
                }
            }

            return null;
        }
    }

    private enum Color {
        WHITE, BLACK;
    }

    private static class Piece {
        final PieceType type;
        final Color color;

        private Piece(PieceType type, Color color) {
            this.type = type;
            this.color = color;
        }

        boolean isKing() {
            return type == PieceType.KING;
        }

        boolean isPawn() {
            return type == PieceType.PAWN;
        }

        static Piece fromChar(char c) {
            if (c == '.') {
                return null;
            }

            final PieceType type = PieceType.fromId(Character.toLowerCase(c));
            final Color color = Character.isLowerCase(c) ? Color.BLACK : Color.WHITE;

            return new Piece(type, color);
        }

        char getId() {
            return (color == Color.WHITE) ? Character.toUpperCase(type.id) : Character.toLowerCase(type.id);
        }
    }
    
    private static class Board {
        final Piece[][] grid = new Piece[8][8];

        Board(String fen) throws IOException {
            final String[] rows = fen.split("/");

            for (int y = 0; y < rows.length; y++) {
                int x = 0;

                try (Reader reader = new StringReader(rows[y])) {
                    while (true) {
                        final int c = reader.read();

                        if (c == -1) {
                            break;
                        }

                        if (Character.isDigit(c)) {
                            x += Integer.parseInt("" + (char) c);
                        } else {
                            grid[y][x++] = Piece.fromChar((char) c);
                        }
                    }
                }
            }
        }

        String toFEN() {
            final StringBuilder buffer = new StringBuilder();

            for (int y = 0; y < 8; y++) {
                final Piece[] row = grid[y];

                if (y > 0) {
                    buffer.append("/");
                }

                for (int x = 0; x < 8; x++) {
                    final Piece piece = row[x];

                    buffer.append((piece != null) ? piece.getId() : '.');
                }
            }

            return buffer.toString()
                    .replaceAll("\\.{8}", "8")
                    .replaceAll("\\.{7}", "7")
                    .replaceAll("\\.{6}", "6")
                    .replaceAll("\\.{5}", "5")
                    .replaceAll("\\.{4}", "4")
                    .replaceAll("\\.{3}", "3")
                    .replaceAll("\\.{2}", "2")
                    .replaceAll("\\.{1}", "1");
        }

        Piece getPiece(Position p) {
            return grid[p.y][p.x];
        }

        Piece removePiece(Position p) {
            final Piece piece = grid[p.y][p.x];

            grid[p.y][p.x] = null;

            return piece;
        }

        void setPiece(Position p, Piece piece) {
            grid[p.y][p.x] = piece;
        }

        void perform(Move move) {
            final Piece piece = removePiece(move.start);

            if (piece.isKing() && "e1g1".equals(move.id)) {
                // The white king is castling on the right !
                setPiece(move.end, piece);

                // Also move the rook
                perform(Move.parse("h1f1"));
            } else if (piece.isKing() && "e1c1".equals(move.id)) {
                // The white king is castling on the left !
                setPiece(move.end, piece);

                // Also move the rook
                perform(Move.parse("a1d1"));
            } else if (piece.isKing() && "e8g8".equals(move.id)) {
                // The black king is castling on the right !
                setPiece(move.end, piece);

                // Also move the rook
                perform(Move.parse("h8f8"));
            } else if (piece.isKing() && "e8c8".equals(move.id)) {
                // The black king is castling on the left !
                setPiece(move.end, piece);

                // Also move the rook
                perform(Move.parse("a8d8"));
            } else if (piece.isPawn() && (Math.abs(move.deltaY()) == 1) && (Math.abs(move.deltaX()) == 1)) {
                // Finish the regular move
                setPiece(move.end, piece);

                final Position target = new Position(move.end.x, move.start.y);

                final Piece targetPiece = getPiece(target);

                if ((targetPiece != null) && targetPiece.isPawn()) {
                    // En passant
                    removePiece(target);
                }
            } else {
                // Regular move
                setPiece(move.end, piece);
            }

            // Pawn promotion
            if (piece.isPawn() && move.isPawnPromotion()) {
                setPiece(move.end, move.getPromotionPiece());
            }
        }

        @Override
        public String toString() {
            final StringBuilder buffer = new StringBuilder(72);

            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    final Piece piece = grid[y][x];

                    buffer.append(piece != null ? piece.getId() : ".");
                }

                buffer.append("\n");
            }

            return buffer.toString();
        }
    }

    public static void main(String args[]) throws IOException {
        final Scanner scanner = new Scanner(System.in);
        final String B = scanner.nextLine();
        final int N = scanner.nextInt();

        System.err.println(B);
        System.err.println(N);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<String> moves = IntStream.range(0, N)
                .mapToObj(i -> scanner.nextLine())
                .collect(Collectors.toList());

        moves.forEach(it -> System.err.println(it));

        final Board board = new Board(B);
        System.err.println(board);

        while (!moves.isEmpty()) {
            final Move move = Move.parse(moves.remove(0));

            System.err.println(move);

            board.perform(move);

            System.err.println(board);
        }

        System.out.println(board.toFEN());
    }
}
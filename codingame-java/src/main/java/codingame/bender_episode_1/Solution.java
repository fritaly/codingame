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
package codingame.bender_episode_1;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private static class State {
        final Position position;
        final Direction direction;
        final boolean breaker;
        final boolean inverted;
        final String map;

        private State(Position position, Direction direction, boolean breaker, boolean inverted, String map) {
            this.position = position;
            this.direction = direction;
            this.breaker = breaker;
            this.inverted = inverted;
            this.map = map;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return breaker == state.breaker && inverted == state.inverted && position.equals(state.position) && direction == state.direction && map.equals(state.map);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, direction, breaker, inverted, map);
        }
    }

    private static class Position {
        final int x, y;

        private Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Position withX(int x) {
            return new Position(x, this.y);
        }

        Position withY(int y) {
            return new Position(this.x, y);
        }

        Position towards(Direction direction) {
            switch (direction) {
                case EAST:
                    return withX(x+1);
                case WEST:
                    return withX(x-1);
                case NORTH:
                    return withY(y-1);
                case SOUTH:
                    return withY(y+1);
            }

            throw new UnsupportedOperationException();
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

    private enum Direction {
        SOUTH,
        EAST,
        NORTH,
        WEST;

        static List<Direction> getDirections(boolean invert) {
            if (invert) {
                return Arrays.asList(WEST, NORTH, EAST, SOUTH);
            }

            return Arrays.asList(SOUTH, EAST, NORTH, WEST);
        }

        char symbol() {
            switch (this) {
                case WEST:
                    return '<';
                case EAST:
                    return '>';
                case SOUTH:
                    return 'v';
                case NORTH:
                    return '^';
            }

            throw new UnsupportedOperationException();
        }
    }

    private static class Cell {
        final char c;

        private Cell(char c) {
            this.c = c;
        }

        boolean isWall() {
            return (c == '#') || (c == 'X');
        }

        boolean isBreakableWall() {
            return (c == 'X');
        }

        boolean isDeathBooth() {
            return (c == '$');
        }

        boolean isInverter() {
            return (c == 'I');
        }

        boolean isTeleport() {
            return (c == 'T');
        }

        boolean isBeer() {
            return (c == 'B');
        }

        boolean isEmpty() {
            return (c == ' ');
        }

        boolean changesDirection() {
            return (c == 'S') || (c == 'E') || (c == 'N') || (c == 'W');
        }

        Direction getNewDirection() {
            switch (c) {
                case 'S':
                    return Direction.SOUTH;
                case 'N':
                    return Direction.NORTH;
                case 'W':
                    return Direction.WEST;
                case 'E':
                    return Direction.EAST;
            }

            throw new UnsupportedOperationException();
        }
    }

    private static class Map {
        final char[][] grid;

        private Position blenderPosition;

        // The initial direction is always SOUTH
        private Direction blenderDirection = Direction.SOUTH;

        private boolean inverted = false;

        private boolean breaker = false;

        private final List<Direction> moves = new ArrayList<>();

        private final Set<State> states = new LinkedHashSet<>();

        Map(int height, int width, Scanner scanner) {
            grid = new char[height][width];

            for (int y = 0; y < height; y++) {
                final String line = scanner.nextLine();

                System.err.println(line);

                grid[y] = line.toCharArray();
            }

            this.blenderPosition = find('@');

            // Remove Blender from the map
            setChar(blenderPosition, ' ');
        }

        String digest() {
            // Return a one-line string capturing the map's state
            return Arrays.asList(grid).stream()
                    .map(a -> new String(a))
                    .collect(Collectors.joining());
        }

        boolean saveState() {
            // Save the game state and return whether the state is a new one
            return this.states.add(new State(blenderPosition, blenderDirection, breaker, inverted, digest()));
        }

        int height() {
            return grid.length;
        }

        int width() {
            return grid[0].length;
        }

        Position getTeleportDestination(Position origin) {
            final List<Position> positions = getTeleports();

            if (!positions.remove(origin)) {
                throw new IllegalArgumentException(origin + " isn't a teleport cell");
            }

            return positions.iterator().next();
        }

        List<Position> getTeleports() {
            final List<Position> list = new ArrayList<>();

            for (int y = 0; y < height(); y++) {
                for (int x = 0; x < width(); x++) {
                    if (grid[y][x] == 'T') {
                        list.add(new Position(x, y));
                    }
                }
            }

            return list;
        }

        Position find(char c) {
            for (int y = 0; y < height(); y++) {
                for (int x = 0; x < width(); x++) {
                    if (grid[y][x] == c) {
                        return new Position(x, y);
                    }
                }
            }

            return null;
        }

        char charAt(Position p) {
            return grid[p.y][p.x];
        }

        void setChar(Position p, char c) {
            grid[p.y][p.x] = c;
        }

        Cell cellAt(Position p) {
            return new Cell(charAt(p));
        }

        boolean isValid(Position p) {
            return (0 <= p.x) && (p.x < width()) && (0 <= p.y) && (p.y < height());
        }

        public boolean move() {
            final List<Direction> queue = new ArrayList<>();
            queue.add(blenderDirection);
            queue.addAll(Direction.getDirections(inverted));

            System.err.println("====================");
            System.err.println("Inverted: " + inverted);
            System.err.println("Directions to test: " + queue);
            System.err.println("Breaker: " + breaker);

            while (!queue.isEmpty()) {
                blenderDirection = queue.remove(0);

                System.err.println("Current position: " + blenderPosition);
                System.err.println("Trying " + blenderDirection + " ...");

                final Position nextPosition = blenderPosition.towards(blenderDirection);

                System.err.println("Trying position " + nextPosition + " ...");

                if (!isValid(nextPosition)) {
                    System.err.println("Next position isn't valid. Skipping ...");

                    continue;
                }

                final Cell cell = cellAt(nextPosition);

                System.err.println("Next cell: " + cell.c);

                if (cell.isEmpty()) {
                    System.err.println("Moving to empty cell ...");

                    blenderPosition = nextPosition;

                    moves.add(blenderDirection);

                    return true;
                }
                if (cell.isDeathBooth()) {
                    System.err.println("Moving to death booth ...");

                    blenderPosition = nextPosition;

                    moves.add(blenderDirection);

                    return false;
                }
                if (cell.isBeer()) {
                    System.err.println("Moving to beer cell ...");

                    blenderPosition = nextPosition;
                    breaker = !breaker;

                    System.err.println("Breaker=" + breaker);

                    moves.add(blenderDirection);

                    return true;
                }
                if (cell.isInverter()) {
                    System.err.println("Moving to inverter cell ...");

                    blenderPosition = nextPosition;
                    inverted = !inverted;

                    System.err.println("Inverted=" + inverted);

                    moves.add(blenderDirection);

                    return true;
                }
                if (cell.isTeleport()) {
                    System.err.println("Moving to teleport cell ...");

                    blenderPosition = getTeleportDestination(nextPosition);

                    System.err.println("Teleported to " + blenderPosition);

                    moves.add(blenderDirection);

                    return true;
                }
                if (cell.changesDirection()) {
                    System.err.println("Moving to cell ...");

                    blenderPosition = nextPosition;

                    final Direction backup = blenderDirection;

                    blenderDirection = cell.getNewDirection();

                    System.err.println("Changed direction to " + blenderDirection);

                    moves.add(backup);

                    return true;
                }
                if (cell.isWall()) {
                    if (breaker && cell.isBreakableWall()) {
                        System.err.println("Breaking wall ...");

                        blenderPosition = nextPosition;
                        setChar(nextPosition, ' '); // Remove the wall

                        System.err.println("Removed wall in " + nextPosition);

                        moves.add(blenderDirection);

                        return true;
                    }

                    System.err.println("The wall in " + nextPosition + " can't be broken. Skipping ...");

                    // The wall can't be broken, try the next available direction
                }
            }

            throw new IllegalStateException();
        }

        @Override
        public String toString() {
            final char[][] clone = Solution.clone(grid);
            clone[blenderPosition.y][blenderPosition.x] = blenderDirection.symbol();

            return Arrays.asList(clone).stream()
                    .map(it -> new String(it))
                    .collect(Collectors.joining("\n"));
        }
    }

    private static char[] clone(char[] array) {
        return Arrays.copyOf(array, array.length);
    }

    private static char[][] clone(char[][] array) {
        final char[][] result = new char[array.length][];

        for (int i = 0; i < array.length; i++) {
            result[i] = clone(array[i]);
        }

        return result;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int height = scanner.nextInt();
        final int width = scanner.nextInt();

        System.err.printf("%d %d%n", height, width);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final Map map = new Map(height, width, scanner);
        map.saveState();

        while (map.move()) {
            if (!map.saveState()) {
                System.out.println("LOOP");
                return;
            }

            System.err.println();
            System.err.println(map);
        }

        map.moves.forEach(it -> System.out.println(it.name()));
    }
}
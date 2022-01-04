/*
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
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

class Position implements Comparable<Position> {
  final int x, y;

  const Position(this.x, this.y);

  @override
  int compareTo(Position that) {
    if (this.x == that.x) {
      return this.y.compareTo(that.y);
    }

    return this.x.compareTo(that.x);
  }

  @override
  bool operator ==(Object that) {
    if (that is Position) {
      return (this.x == that.x) && (this.y == that.y);
    }

    return false;
  }

  @override
  int get hashCode {
    var hash = 17;
    hash = 37 * hash + x.hashCode;
    hash = 37 * hash + y.hashCode;

    return hash;
  }

  Position up() => Position(x, y - 1);
  Position down() => Position(x, y + 1);
  Position left() => Position(x - 1, y);
  Position right() => Position(x + 1, y);

  @override
  String toString() {
    return "(${x},${y})";
  }
}

class Maze {
  final List<String> _grid;

  Maze(this._grid);

  int get width => _grid[0].length;
  int get height => _grid.length;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);

  bool isValid(Position position) => isValidX(position.x) && isValidY(position.y);

  bool isExit(Position position) {
    var char = charAt(position);
    var x = position.x, y = position.y;

    return ((x == 0) || (x == width - 1) || (y == 0) || y == height - 1) && (char == '.');
  }

  String charAt(Position position) {
   return _grid[position.y][position.x];
  }

  Set<Position> findExits(Position position, Set<Position> visited) {
    // trace("Visiting position ${position} (visited: ${visited}) ...");

    var visited2 = Set<Position>.from(visited);
    visited2.add(position);

    var exits = Set<Position>();

    if (isExit(position)) {
      exits.add(position);

      return exits;
    }

    var candidates = [ position.up(), position.left(), position.right(), position.down() ];

    for (var candidate in candidates) {
      if (!isValid(candidate)) {
        trace("${candidate} is not a valid position");
        continue;
      }
      if (visited2.contains(candidate)) {
        trace("${candidate} has already been visited");
        continue;
      }
      if ((charAt(candidate) == '#')) {
        trace("${candidate} is a wall");
        continue;
      }

      // Explore the candidate position
      exits.addAll(findExits(candidate, visited2));
    }

    return exits;
  }

  @override
  String toString() {
    return _grid.join('\n');
  }
}

void main() {
  var inputs = stdin.readLineSync().split(' ');

  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);

  inputs = stdin.readLineSync().split(' ');

  var startPosition = Position(int.parse(inputs[0]), int.parse(inputs[1]));

  var lines = <String>[];

  for (int i = 0; i < height; i++) {
    lines.add(stdin.readLineSync());
  }

  var exits = Maze(lines).findExits(startPosition, Set<Position>()).toList();
  exits.sort();

  print(exits.length);

  if (!exits.isEmpty) {
    print(exits.map((p) => "${p.x} ${p.y}").join('\n'));
  }
}
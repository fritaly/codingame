/*
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
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

class Position {
  final int x, y;

  Position(this.x, this.y);

  /// Returns a list with the 8 neighbor cells around this position
  List<Position> neighbors() {
    return [
      Position(x - 1, y - 1),
      Position(x - 1, y),
      Position(x - 1, y + 1),
      Position(x, y - 1),
      Position(x, y + 1),
      Position(x + 1, y - 1),
      Position(x + 1, y),
      Position(x + 1, y + 1),
    ];
  }

  @override
  bool operator ==(Object other) {
    if (other is Position) {
      return (this.x == other.x) && (this.y == other.y);
    }

    return false;
  }

  @override
  String toString() {
    return "(${x},${y})";
  }
}

class Grid {
  final List<List<String>> grid;

  /// The total number of mines to find in the grid
  final int totalMines;

  Grid(this.grid, this.totalMines);

  @override
  String toString() => grid.map((e) => e.join()).join('\n');

  int get height => grid.length;
  int get width => grid[0].length;

  String charAt(Position position) => grid[position.y][position.x];
  void setChar(Position position, String char) => grid[position.y][position.x] = char;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);
  bool exists(Position position) => isValidX(position.x) && isValidY(position.y);

  /// Tells whether all the mines have been found
  bool get solved => (mines().length == totalMines);

  /// Returns the remaining number of mines to find
  int get missingMines => totalMines - mines().length;

  List<Position> _findAll(bool predicate(String char)) {
    var result = <Position>[];

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        if (predicate(grid[y][x])) {
          result.add(Position(x, y));
        }
      }
    }

    return result;
  }

  /// Return all the positions with an hint (like '1') in the grid
  List<Position> hints() => _findAll((char) => int.tryParse(char) != null);

  /// Return all the positions with a '?' in the grid
  List<Position> candidates() => _findAll((char) => char == '?');

  /// Return all the positions with a mine in the grid
  List<Position> mines() => _findAll((char) => char == 'X');

  List<Position> neighborsOf(Position position) {
    return position.neighbors().where((p) => exists(p)).toList();
  }

  void dump() {
    print(grid.map((row) => row.join()).join('\n'));
  }

  void solve() {
    var allHints = hints();

    while (!solved) {
      trace("${this}");

      if (candidates().length == missingMines) {
        // The remaining '?' correspond to the missing mines
        candidates().forEach((p) { setChar(p, 'X'); });
        continue;
      }

      for (var hint in allHints) {
        // Number of mines around this position ?
        var count = int.parse(charAt(hint));

        // Number of mines already found around the position
        var existingMines = neighborsOf(hint).where((p) => charAt(p) == 'X').length;

        var missingMines = count - existingMines;

        if (count == existingMines) {
          // All the mines have already been found, replace the neighboring '?'
          // with '.'
          neighborsOf(hint).where((p) => charAt(p) == '?').forEach((p) {
            setChar(p, '.');
          });
        } else {
          // There are remaining mines around the position. Find the positions
          // where the mine could be
          var candidates = neighborsOf(hint).where((p) => charAt(p) == '?').toList();

          if (candidates.length == missingMines) {
            // There is a mine in all the candidate positions
            candidates.forEach((p) {
              setChar(p, 'X');
            });

            break;
          }
        }
      }
    }
  }
}

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var height = int.parse(inputs[0]);
  var width = int.parse(inputs[1]);
  var mineCount = int.parse(stdin.readLineSync());

  trace("${height} ${width}");
  trace("${mineCount}");

  var lines = <List<String>>[];

  for (var i = 0; i < height; i++) {
    lines.add(stdin.readLineSync().split(''));
  }

  var grid = Grid(lines, mineCount);

  trace("${grid}");

  grid.solve();

  trace("\n${grid}");

  var mines = grid.mines();
  mines.sort((a, b) {
    if (a.x == b.x) {
      return a.y.compareTo(b.y);
    }

    return a.x.compareTo(b.x);
  });

  for (var mine in mines) {
    print('${mine.x} ${mine.y}');
  }
}
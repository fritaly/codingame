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

  /// Tells whether the 2 positions are on the same row
  bool sameRow(Position other) => this.y == other.y;

  /// Tells whether the 2 positions are on the same column
  bool sameColumn(Position other) => this.x == other.x;

  /// Tells whether the 2 positions are in the same square
  bool sameSquare(Position other) {
    return ((this.x ~/ 3) == (other.x ~/3)) && ((this.y ~/ 3) == (other.y ~/3));
  }

  @override
  bool operator ==(Object other) {
    if (other is Position) {
      return (this.x == other.x) && (this.y == other.y);
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

  @override
  String toString() {
    return "(${x},${y})";
  }
}

const DIGITS = [ 1, 2, 3, 4, 5, 6, 7, 8, 9 ];

bool validate(List<int> list) {
  return (list.length == 9) && DIGITS.every((digit) => list.contains(digit));
}

Set<int> remove(Set<int> from, Iterable<int> iterable) {
  for (var n in iterable) {
    from.remove(n);
  }

  return from;
}

class Sudoku {
  final List<List<int>> grid;

  Sudoku(this.grid): assert (grid.length == 9), assert (grid[0].length == 9);

  /// Returns a Map containing the possible digits per position
  Map<Position, Set<int>> computeCandidatesPerPosition() {
    var map = Map<Position, Set<int>>();

    // Computes all the possible digits per empty cell
    for (var y = 0; y < 9; y++) {
      for (var x = 0; x < 9; x++) {
        if (grid[y][x] == 0) {
          // The cell is empty, find all the possible digits
          var candidates = Set<int>.from(DIGITS);
          remove(candidates, rowDigits(y) + columnDigits(x) + squareDigits(x ~/ 3, y ~/ 3));

          map[Position(x, y)] = candidates;
        }
      }
    }

    return map;
  }

  String dump() => grid.map((row) => row.join().replaceAll('0', '.')).join('\n');

  String render() {
    var lines = <String>[];
    lines.add("+---+---+---+");

    for (var i = 0; i < 3; i++) {
      lines.addAll(grid.sublist(i * 3, (i + 1) * 3).map((row) =>
        '|' + row.sublist(0, 3).join() + "|" + row.sublist(3, 6).join() + "|" + row.sublist(6, 9).join() + '|')
      .map((e) => e.replaceAll('0', '.')));

      lines.add("+---+---+---+");
    }

    return lines.join('\n');
  }

  /// Returns the digits on the nth row
  List<int> rowDigits(int n) {
    assert ((0 <= n) && (n < 9));

    return grid[n].where((e) => e != 0).toList();
  }

  /// Returns the digits in the nth column
  List<int> columnDigits(int n) {
    assert ((0 <= n) && (n < 9));

    return grid.map((row) => row[n]).where((e) => e != 0).toList();
  }

  /// Returns the positions in the square with the given coordinates
  List<Position> squarePositions(int x, int y) {
    assert ((0 <= x) && (x <= 2));
    assert ((0 <= y) && (y <= 2));

    // +---+---+---+
    // |0,0|1,0|2,0|
    // +---+---+---+
    // |0,1|1,1|2,1|
    // +---+---+---+
    // |0,2|1,2|2,2|
    // +---+---+---+

    var result = <Position>[];

    for (var py = y * 3; py < (y + 1) * 3; py++) {
      for (var px = x * 3; px < (x + 1) * 3; px++) {
        result.add(Position(px, py));
      }
    }

    return result;
  }

  /// Returns the digits in the square with the given coordinates
  List<int> squareDigits(int x, int y) {
    return squarePositions(x, y)
        .map((p) => grid[p.y][p.x])
        .where((d) => d != 0) // Filter out the zeroes representing empty cells
        .toList();
  }

  /// Tells whether the grid is valid
  bool isValid() {
    for (var n = 0; n < 9; n++) {
      if (!validate(rowDigits(n))) {
        trace("Row #${n+1} is not valid: ${rowDigits(n)}");

        return false;
      }

      if (!validate(columnDigits(n))) {
        trace("Column #${n+1} is not valid: ${columnDigits(n)}");

        return false;
      }

      if (!validate(squareDigits(n % 3, n ~/ 3))) {
        trace("Square (${n % 3}, ${n ~/ 3}) is not valid: ${squareDigits(n % 3, n ~/ 3)}");

        return false;
      }
    }

    return true;
  }

  List<List<int>> _cloneGrid() {
    return List.from(grid.map((row) => List<int>.from(row)));
  }

  Sudoku set(int x, int y, int digit) {
    var clone = _cloneGrid();
    clone[y][x] = digit;

    return Sudoku(clone);
  }

  bool canBe(int x, int y, int digit) {
    if (grid[y][x] == 0) {
      if (rowDigits(y).contains(digit)) {
        return false;
      }
      if (columnDigits(x).contains(digit)) {
        return false;
      }

      return true;
    }

    return false;
  }

  Sudoku solve(String stack) {
    var change;

    trace("\n${render()}\n");

    var candidatesPerPosition = Map<Position, Set<int>>();

    do {
      candidatesPerPosition = computeCandidatesPerPosition();

      change = false;

      // Find the positions where there is only one digit possible
      var result = candidatesPerPosition.entries.where((entry) => entry.value.length == 1).toList();

      if (!result.isEmpty) {
        var entry = result.first;
        var position = entry.key, digit = entry.value.first;

        trace("Adding ${digit} in ${position}");

        // Write the digit in the cell
        grid[position.y][position.x] = digit;

        // Update the map with the candidates per position by removing the digit
        // just added to the grid
        // candidatesPerPosition.entries
        //     .where((entry) => entry.key.sameColumn(position) || entry.key.sameRow(position) || entry.key.sameSquare(position))
        //     .forEach((entry) { entry.value.remove(digit); });

        change = true;
      }

      // Check each square and find the only possible position for each digit
      for (var y = 0; (y < 3) && !change; y++) {
        for (var x = 0; (x < 3) && !change; x++) {
          // The digits already present in the square
          var squaresDigits = squareDigits(x, y);
          var remainingDigits = remove(Set<int>.from(DIGITS), squaresDigits);
          var allPositions = squarePositions(x, y);

          // Find all the positions where this digit can be used
          for (var digit in remainingDigits) {
            var possibilities = allPositions
                .where((p) => (grid[p.y][p.x] == 0) && canBe(p.x, p.y, digit))
                .toList();

            if (possibilities.length == 1) {
              var position = possibilities.first;

              trace("Adding ${digit} in ${position} (only possibility)");

              grid[position.y][position.x] = digit;

              // Update the map with the candidates per position by removing the digit
              // just added to the grid
              // candidatesPerPosition.entries
              //     .where((entry) => entry.key.sameColumn(position) || entry.key.sameRow(position) || entry.key.sameSquare(position))
              //     .forEach((entry) { entry.value.remove(digit); });

              change = true;
            }
          }
        }
      }
    } while (change);

    trace("\n${render()}\n");

    // === //

    var entries = candidatesPerPosition.entries.toList();

    // Sort the empty cells by the number of candidates
    entries.sort((a, b) => a.value.length.compareTo(b.value.length));

    for (var entry in entries) {
      var position = entry.key, candidates = entry.value;
      final x = position.x, y = position.y;

      if (candidates.isEmpty) {
        // No candidate, the grid cannot be valid
        trace("\n** Backtracking because of (${x},${y}): no possible solution **\n");

        return null;
      }

      trace("Found ${candidates.length} candidate(s) for (${x},${y}): ${candidates}");

      for (var candidate in candidates) {
        trace("Testing ${candidate} in (${x},${y}) ...");

        // Fork the grid
        var derived = set(x, y, candidate);
        var result = derived.solve("");

        if (result != null) {
          return result;
        }
      }
    }

    if ((entries.isEmpty) && isValid()) {
      trace("Found a solution !");

      return this;
    }

    return null;
  }
}

void main() {
  var grid = List.filled(9, List.filled(9, 0));

  for (int y = 0; y < 9; y++) {
    var row = stdin.readLineSync().split('').map((e) => int.parse(e)).toList();

    trace("${row.join()}");

    grid[y] = row;
  }

  var sudoku = Sudoku(grid);

  print('${sudoku.solve("").dump()}');
}
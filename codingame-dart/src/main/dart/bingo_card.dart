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

class Coordinates {
  final int x, y;

  Coordinates(this.x, this.y);

  @override
  String toString() {
    return "(${x},${y})";
  }
}

class Card {
  final List<List<int>> grid;

  Card(this.grid);

  Coordinates find(int digit) {
    assert(digit != 0);

    for (int y = 0; y < grid.length; y++) {
      var row = grid[y];

      for (int x = 0; x < row.length; x++) {
        if (row[x] == digit) {
          return Coordinates(x, y);
        }
      }
    }

    return null;
  }

  int digit(int x, int y) {
    assert((0 <= x) && (x < 5));
    assert((0 <= y) && (y < 5));

    return grid[y][x];
  }

  void strike(int x, int y) {
    assert((0 <= x) && (x < 5));
    assert((0 <= y) && (y < 5));

    grid[y][x] = 0;
  }

  bool rowComplete(int y) {
    return grid[y].every((element) => element == 0);
  }

  bool columnComplete(int x) {
    return grid.every((row) => row[x] == 0);
  }

  bool diag1Complete() {
    return List.generate(5, (index) => index).map((n) => grid[n][n]).every((element) => element == 0);
  }

  bool diag2Complete() {
    return List.generate(5, (index) => index).map((n) => grid[4 - n][n]).every((element) => element == 0);
  }

  bool complete() {
    for (int y = 0; y < grid.length; y++) {
      if (!rowComplete(y)) {
        return false;
      }
    }

    return true;
  }

  void dump() {
    trace("${grid.map((row) => row.join('\t')).join('\n')}");
  }
}

List<int> parseInts(String string) {
  trace("${string}");

  return string.split(' ').map((e) => int.parse(e)).toList();
}

void main() {
  var n = int.parse(stdin.readLineSync());

  trace("${n}");

  var cards = <Card>[];

  for (int i = 0; i < n; i++) {
    cards.add(Card([
      parseInts(stdin.readLineSync()),
      parseInts(stdin.readLineSync()),
      parseInts(stdin.readLineSync()),
      parseInts(stdin.readLineSync()),
      parseInts(stdin.readLineSync())
    ]));
  }

  var calls = parseInts(stdin.readLineSync());

  trace("${calls.join(' ')}");

  var firstRow = -1, firstCard = -1;

  for (int n = 0; (n < calls.length) && (firstCard == -1); n++) {
    var message = "# Round #${n+1} #";
    var call = calls[n];

    trace("");
    trace("#" * message.length);
    trace(message);
    trace("#" * message.length);
    trace("");
    trace("Call: ${call}");

    for (Card card in cards) {
      var coordinates = card.find(call);

      if (coordinates != null) {
        card.strike(coordinates.x, coordinates.y);

        if (card.rowComplete(coordinates.y) || card.columnComplete(coordinates.x) || card.diag1Complete() || card.diag2Complete()) {
          if (firstRow == -1) {
            firstRow = n + 1;
          }

          if (card.complete()) {
            if (firstCard == -1) {
              firstCard = n + 1;
            }
          }
        }
      }

      if (true) {
        trace("");
        trace("### Card ###");
        trace("");

        card.dump();
      }
    }
  }

  print('${firstRow}');
  print('${firstCard}');
}

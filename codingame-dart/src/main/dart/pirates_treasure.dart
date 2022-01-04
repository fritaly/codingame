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

class Position {
  final int x, y;

  Position(this.x, this.y);

  @override
  bool operator ==(Object other) {
    if (other is Position) {
      return (this.x == other.x) && (this.y == other.y);
    }

    return false;
  }

  List<Position> neighbors() {
    return [
      Position(x - 1, y - 1),
      Position(x - 1, y),
      Position(x - 1, y + 1),
      Position(x, y - 1),
      Position(x, y + 1),
      Position(x + 1,y - 1),
      Position(x + 1, y),
      Position(x + 1, y + 1),
    ];
  }

  @override
  String toString() {
    return "(${x},${y})";
  }
}

class Grid {
  final List<String> rows;

  Grid(this.rows);

  int get width => rows[0].length;
  int get height => rows.length;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);
  bool exists(Position position) => isValidX(position.x) && isValidY(position.y);

  String charAt(Position position) => rows[position.y][position.x];

  @override
  String toString() {
    return "${rows.join('\n')}";
  }
}

void main() {
  var width = int.parse(stdin.readLineSync());
  var height = int.parse(stdin.readLineSync());

  trace("${width}\n${height}");

  var lines = <String>[];

  for (var i = 0; i < height; i++) {
    lines.add(stdin.readLineSync().split(' ').join(''));
  }

  var grid = Grid(lines);

  trace('${grid}');

  for(var y = 0; y < height; y++) {
    for(var x = 0; x < width; x++) {
      var position = Position(x, y);

      if (grid.charAt(position) == '0') {
        var positions = position.neighbors().where((p) => grid.exists(p)).toList();

        // trace("${positions.map((p) => "${p} => ${grid.charAt(p)}").join('\n')}");

        if (positions.every((p) => grid.charAt(p) == '1')) {
          print('${x} ${y}');

          return;
        }
      }
    }
  }
}
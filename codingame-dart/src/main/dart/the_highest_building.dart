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
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

String chomp(String string) {
  var index = string.length - 1;

  while (string[index] == ' ') {
    index--;
  }

  return string.substring(0, index + 1);
}

class Position {
  final int x, y;

  Position(this.x, this.y);

  Position left() => Position(x - 1, y);
  Position right() => Position(x + 1, y);
  Position down() => Position(x, y + 1);
  Position up() => Position(x, y - 1);

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

class Building {
  final int height, startX, endX;
  final List<String> rows;

  Building(this.height, this.startX, this.endX, this.rows);

  @override
  String toString() => rows.sublist(rows.length - height).map((row) => chomp(row.substring(startX, endX + 1))).join('\n');
}

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);

  trace("${width} ${height}");

  var rows = <String>[];

  for (int i = 0; i < height; i++) {
    rows.add(stdin.readLineSync());
  }

  trace("${rows.join('\n')}");

  var visited = Set<Position>();
  var buildings = <Building>[];

  for (var y = 0; (y < height) && buildings.isEmpty; y++) {
    for (var x = 0; x < width; x++) {
      var position = Position(x, y);

      if (visited.contains(position)) {
        continue;
      }

      if (rows[y][x] == '#') {
        var queue = [ position ];
        var positions = Set<Position>();

        while (!queue.isEmpty) {
          var current = queue.removeAt(0);

          if ((0 <= current.x) && (current.x < width) && (0 <= current.y) && (current.y < height)) {
            if (!positions.contains(current) && rows[current.y][current.x] == '#') {
              positions.add(current);
              visited.add(current);

              queue.addAll([ current.left(), current.right(), current.down(), current.up() ]);
            }
          }
        }

        var minX = positions.map((p) => p.x).reduce((value, element) => min(value, element));
        var maxX = positions.map((p) => p.x).reduce((value, element) => max(value, element));

        trace("Building found: height=${height - y} x in [${minX},${maxX}]");

        buildings.add(Building(height - y, minX, maxX, rows));

        visited.addAll(positions);
      }
    }
  }

  print(buildings.map((b) => b.toString()).join('\n\n'));
}
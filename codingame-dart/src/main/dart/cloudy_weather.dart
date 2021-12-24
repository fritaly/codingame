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

  /// Returns the Manhattan distance between the 2 positions
  int distance(Position that) {
    return (this.x - that.x).abs() + (this.y - that.y).abs();
  }

  List<Position> neighbors() {
    return [
      Position(x - 1, y),
      Position(x + 1, y),
      Position(x, y - 1),
      Position(x, y + 1)
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

class Cloud {
  final Position corner;
  final int width, height;

  Cloud(this.corner, this.width, this.height);

  bool contains(Position position) {
    var x = position.x, y = position.y;

    return ((corner.x <= x) && (x < corner.x + width))
        && ((corner.y <= y) && (y < corner.y + height));
  }

  @override
  String toString() => "Cloud[corner: ${corner}, width: ${width}, height: ${height}]";
}

List<Position> rebuildPath(Map<Position, Position> cameFrom, Position current) {
  var path = [ current ];

  while (cameFrom.containsKey(current)) {
    current = cameFrom[current];
    path.insert(0, current);
  }

  return path;
}

void main() {
  var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();
  var startPosition = Position(inputs[0], inputs[1]);

  trace("${inputs.join(' ')}");

  inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

  var destination = Position(inputs[0], inputs[1]);

  trace("${inputs.join(' ')}");

  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var clouds = <Cloud>[];

  for (var i = 0; i < N; i++) {
    inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    clouds.add(Cloud(Position(inputs[0], inputs[1]), inputs[2], inputs[3]));
  }

  var openSet = [ startPosition ];
  var cameFrom = Map<Position, Position>();

  var gScore = Map<Position, double>();
  gScore[startPosition] = 0;

  var fScore = Map<Position, double>();
  fScore[startPosition] = startPosition.distance(destination).toDouble();

  while (!openSet.isEmpty) {
    openSet.sort((a, b) => fScore[a].compareTo(fScore[b]));

    var current = openSet.removeAt(0);

    if (current == destination) {
      print(rebuildPath(cameFrom, current).length - 1);
      break;
    }

    for (var neighbor in current.neighbors()) {
      if (clouds.any((c) => c.contains(neighbor))) {
        continue;
      }

      gScore.putIfAbsent(current, () => double.maxFinite);
      gScore.putIfAbsent(neighbor, () => double.maxFinite);

      var tentative_gScore = gScore[current] + current.distance(neighbor);

      if (tentative_gScore < gScore[neighbor]) {
        cameFrom[neighbor] = current;
        gScore[neighbor] = tentative_gScore;
        fScore[neighbor] = gScore[neighbor] + neighbor.distance(destination);

        if (!openSet.contains(neighbor)) {
          openSet.add(neighbor);
        }
      }
    }
  }
}
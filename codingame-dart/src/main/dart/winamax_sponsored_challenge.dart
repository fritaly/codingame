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

class Direction {
  final String symbol, name;

  static const UP = Direction('^', 'UP');
  static const LEFT = Direction('<', 'LEFT');
  static const RIGHT = Direction('>', 'RIGHT');
  static const DOWN = Direction('v', 'DOWN');

  const Direction(this.symbol, this.name): assert ((symbol != null) && (symbol.length == 1));

  @override
  String toString() => "${name}";
}

// ================ //
// === Position === //
// ================ //

class Position {
  final int x, y;

  Position(this.x, this.y);

  bool isAligned(Position other) => (this.x == other.x) || (this.y == other.y);

  Direction directionTo(Position target) {
    // The 2 positions must be aligned
    assert (isAligned(target));

    if (this.x == target.x) {
      return (this.y > target.y) ? Direction.UP : Direction.DOWN;
    }

    return (this.x > target.x) ? Direction.LEFT : Direction.RIGHT;
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

  /// Returns all the positions at the given distance from this position
  List<Position> all(int n) => [ Position(x, y - n), Position(x - n, y), Position(x, y + n), Position(x + n, y) ];

  @override
  String toString() => "(${x}, ${y})";
}

// ============ //
// === Path === //
// ============ //

class Path {
  final List<Position> waypoints;

  Path(this.waypoints);

  Position get end => waypoints.last;

  List<Segment> get segments {
    var result = <Segment>[];

    var previous = null;

    for (var current in waypoints) {
      if (previous != null) {
        result.add(Segment(previous, current));
      }

      previous = current;
    }

    return result;
  }

  @override
  String toString() => "[${waypoints.join(' -> ')}]";
}

class Segment {
  final Position start, end;

  Segment(this.start, this.end): assert (start.isAligned(end));

  Direction get direction => start.directionTo(end);

  /// Returns a list of positions corresponding to the positions traversed by
  /// the segment
  List<Position> expand() {
    var steps = <Position>[];

    if (start.x == end.x) {
      // Aligned along X
      var minY = min(start.y, end.y), maxY = max(start.y, end.y);

      for (var current = minY; current <= maxY; current++) {
        steps.add(Position(start.x, current));
      }
    } else {
      // Aligned along Y
      var minX = min(start.x, end.x), maxX = max(start.x, end.x);

      for (var current = minX; current <= maxX; current++) {
        steps.add(Position(current, start.y));
      }
    }

    return steps;
  }

  int get length {
    if (start.x == end.x) {
      return (end.y - start.y).abs();
    }

    return (end.x - start.x).abs();
  }

  @override
  String toString() => "[${start} -> ${end}]";
}

// ============ //
// === Ball === //
// ============ //

class Ball implements Comparable<Ball> {
  final int shots;
  final Position position;

  Ball(this.shots, this.position);

  @override
  String toString() => "Ball[${shots}, ${position}]";

  @override
  int compareTo(Ball that) {
    // Order the balls by their number of remaining shots
    return this.shots.compareTo(that.shots);
  }
}

// ============ //
// === Grid === //
// ============ //

bool isArrow(String char) {
  switch (char) {
    case '^':
    case '<':
    case '>':
    case 'v':
      return true;
    default:
      return false;
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

  bool isHole(Position position) => (charAt(position) == 'H');
  bool isWater(Position position) => (charAt(position) == 'X');

  /// Updates the character at the given position
  void setChar(Position position, String char) {
    assert ((position != null) && exists(position));
    assert ((char != null) && (char.length == 1));

    var x = position.x, y = position.y;
    var row = rows[y];

    rows[y] = row.substring(0, x) + char + row.substring(x + 1);
  }

  /// Returns all the paths leading to a hole from the given start position and
  /// number of remaining shots
  List<Path> findPaths(Position startPosition, int shots) {
    assert (shots > 0);

    // Filter the invalid positions (out of the map), leading to water, crossing
    // another path
    var positions = startPosition.all(shots).where((p) => exists(p)
        && !isWater(p)
        && !Segment(startPosition, p).expand().any((e) => isArrow(charAt(e)))
    ).toList();

    if (positions.isEmpty) {
      return [];
    }

    if (shots == 1) {
      // Only return the positions leading to a hole
      return positions.where((p) => isHole(p)).map((p) => Path([ startPosition, p ])).toList(growable: false);
    }

    var paths = <Path>[];

    for (var position in positions) {
      if (isHole(position)) {
        paths.add(Path([ startPosition, position ]));
      } else {
        // Explore the next positions
        for (var subPath in findPaths(position, shots - 1)) {
          if (isHole(subPath.end)) {
            // Only return the paths leading to a hole
            paths.add(Path([ startPosition, ...subPath.waypoints ]));
          }
        }
      }
    }

    return paths;
  }

  List<Ball> getBalls() {
    var balls = <Ball>[];

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        var char = rows[y][x];

        if (isNumeric(char)) {
          balls.add(Ball(int.parse(char), Position(x, y)));
        }
      }
    }

    return balls;
  }

  @override
  String toString() {
    return "${rows.join('\n')}";
  }

  String render() {
    // Replace the characters '!' and 'X' respectively representing the holes
    // with a ball and the ponds
    return rows.map((r) => r.replaceAll('!', '.').replaceAll('X', '.')).join('\n');
  }

  /// Tells whether the grid is complete, that is when the grid no longer has
  /// balls
  bool isComplete() {
    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        var char = rows[y][x];

        if (isNumeric(char)) {
          // Found a ball, grid incomplete
          return false;
        }
      }
    }

    return true;
  }

  Grid drawSegment(Segment segment) {
    assert (segment != null);

    var clone = Grid(List.from(rows));
    var direction = segment.direction;

    trace("Drawing segment: ${segment} / direction=${direction}");

    for (var position in segment.expand()) {
      var char = charAt(position);

      if ((position == segment.end) && (char == 'H')) {
        // That's a hole, change the character to '!' to indicate the hole is
        // used
        clone.setChar(position, '!');
      } else if (isArrow(char)) {
        // There is an arrow, path isn't valid
        return null;
      } else {
        clone.setChar(position, direction.symbol);
      }
    }

    return clone;
  }

  Grid solve() {
    if (isComplete()) {
      return this;
    }

    // Find the balls and order them by their remaining shots
    var balls = getBalls();
    balls.sort();

    trace("Balls: ${balls}");

    for (var ball in balls) {
      trace("Processing ${ball} ...");

      // Find all the paths leading to a hole
      var paths = findPaths(ball.position, ball.shots);

      trace("Found paths: ${paths}");

      if (paths.isEmpty) {
        // This ball cannot reach a hole
        return null;
      }

      // Test each possibility
      for (var path in paths) {
        trace("=== Testing ${ball} with ${path} ... ===");

        var segments = path.segments;

        var fork = this, distance = ball.shots;

        for (var segment in segments) {
          trace("");
          trace("Before:\n\n${fork}\n");

          fork = fork.drawSegment(segment);

          if (fork == null) {
            break;
          }

          // Place the ball where it arrived
          fork.setChar(segment.end, "${--distance}");

          trace("After:\n\n${fork}\n");
        }

        if (fork == null) {
          trace("The path ${path} isn't valid");

          continue;
        }

        // Change 'H' into '!' to indicate the hole is used
        fork.setChar(path.end, '!');

        trace("After (2):\n\n${fork}\n");

        var solution = fork.solve();

        if (solution != null) {
          // Found a solution, return it
          return solution;
        }
      }
    }

    return null;
  }
}

bool isNumeric(String s) => (int.tryParse(s) != null);

void main() {
  var inputs = stdin.readLineSync().split(' ');

  trace("${inputs.join(' ')}");

  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);

  var lines = <String>[];

  for (int i = 0; i < height; i++) {
    lines.add(stdin.readLineSync());
  }

  trace("${lines.join('\n')}");

  var grid = Grid(lines);

  trace("${grid}");

  var solution = grid.solve();

  print("${solution.render()}");
}
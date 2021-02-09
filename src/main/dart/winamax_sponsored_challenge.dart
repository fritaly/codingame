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

// =================== //
// === Coordinates === //
// =================== //

class Coordinates {
  final int x, y;

  Coordinates(this.x, this.y);

  bool isAligned(Coordinates other) => (this.x == other.x) || (this.y == other.y);

  Direction directionTo(Coordinates target) {
    // The 2 positions must be aligned
    assert ((this.x == target.x) || (this.y == target.y));

    if (this.x == target.x) {
      return (this.y > target.y) ? Direction.UP : Direction.DOWN;
    }

    return (this.x > target.x) ? Direction.LEFT : Direction.RIGHT;
  }

  List<Coordinates> pathTo(Coordinates target) {
    // The 2 positions must be aligned
    assert ((this.x == target.x) || (this.y == target.y));

    var steps = <Coordinates>[];

    if (this.x == target.x) {
      // Aligned along X
      var minY = min(this.y, target.y), maxY = max(this.y, target.y);

      for (var current = minY; current <= maxY; current++) {
        steps.add(Coordinates(x, current));
      }
    } else {
      // Aligned along Y
      var minX = min(this.x, target.x), maxX = max(this.x, target.x);

      for (var current = minX; current <= maxX; current++) {
        steps.add(Coordinates(current, y));
      }
    }

    return steps;
  }

  @override
  bool operator ==(Object other) {
    if (other is Coordinates) {
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

  /// Returns the coordinates n steps upwards
  Coordinates up(int n) => Coordinates(x, y - n);

  /// Returns the coordinates n steps downwards
  Coordinates down(int n) => Coordinates(x, y + n);

  /// Returns the coordinates n steps leftwards
  Coordinates left(int n) => Coordinates(x - n, y);

  /// Returns the coordinates n steps rightwards
  Coordinates right(int n) => Coordinates(x + n, y);

  @override
  String toString() => "(${x}, ${y})";
}

// ============ //
// === Ball === //
// ============ //

class Ball implements Comparable<Ball> {
  final int shots;
  final Coordinates coordinates;

  Ball(this.shots, this.coordinates);

  @override
  String toString() => "Ball[${shots}, ${coordinates}]";

  @override
  int compareTo(Ball that) {
    // Order the balls by their number of remaining shots
    return this.shots.compareTo(that.shots);
  }
}

// ============ //
// === Hole === //
// ============ //

class Hole {
  final Coordinates coordinates;

  Hole(this.coordinates);

  @override
  String toString() => "Hole[${coordinates}]";
}

// ============ //
// === Grid === //
// ============ //

bool isArrow(String char) => [ '^', '<', 'v', '>' ].any((c) => (c == char));

class Grid {
  final List<String> rows;

  Grid(this.rows);

  int get width => rows[0].length;
  int get height => rows.length;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);
  bool exists(Coordinates coordinates) => isValidX(coordinates.x) && isValidY(coordinates.y);

  String charAt(Coordinates coordinates) => rows[coordinates.y][coordinates.x];

  /// Updates the character at the given coordinates
  void setChar(Coordinates coordinates, String char) {
    assert ((coordinates != null) && exists(coordinates));
    assert ((char != null) && (char.length == 1));

    var x = coordinates.x, y = coordinates.y;
    var row = rows[y];

    rows[y] = row.substring(0, x) + char + row.substring(x + 1);
  }

  bool isPathPossible(Coordinates source, Coordinates target) {
    assert ((source != null) && (target != null));

    // Resolve the path from the source to the target position
    var path = source.pathTo(target);

    // Check all the intermediary cells along the path
    for (var step in path) {
      var char = charAt(target);

      if (isArrow(char)) {
        // Not allowed to cross another path
        return false;
      }
      if (char == 'X') {
        // Only forbidden for the target cell
        if (step == target) {
          // The ball lands in the water
          return false;
        } else {
          // The ball flies over the water
        }
      }
      if (char == '!') {
        // TODO Can a ball fly over a hole ?
        return false;
      }
      if (isNumeric(char)) {
        if (step == target) {
          // Ball present on the target cell
          return false;
        }
      }
    }

    // The path is valid
    return true;
  }

  List<Ball> getBalls() {
    var balls = <Ball>[];

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        var char = rows[y][x];

        if (isNumeric(char)) {
          balls.add(Ball(int.parse(char), Coordinates(x, y)));
        }
      }
    }

    return balls;
  }

  List<Hole> getHoles() {
    var holes = <Hole>[];

    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        var char = rows[y][x];

        if (char == 'H') {
          holes.add(Hole(Coordinates(x, y)));
        }
      }
    }

    return holes;
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

  Grid drawPath(Coordinates source, Coordinates target) {
    assert ((source != null) && (target != null) && source.isAligned(target));

    var clone = Grid(List.from(rows));
    var path = source.pathTo(target);
    var direction = source.directionTo(target);

    trace("Drawing path: source=${source}, target=${target} -> path=${path} / direction=${direction}");

    for (var step in path) {
      var char = charAt(step);

      if ((step == target) && (char == 'H')) {
        // That's a hole, change the character to '!' to indicate the hole is
        // used
        clone.setChar(step, '!');
      } else {
        clone.setChar(step, direction.symbol);
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

      var coords = ball.coordinates, distance = ball.shots;

      // Find the positions the ball can reach
      var candidates = [ coords.up(distance), coords.down(distance), coords.left(distance), coords.right(distance) ];

      // Keep the valid positions (in the map) and those for which the path is
      // possible
      candidates = candidates.where((c) => exists(c) && isPathPossible(coords, c)).toList();

      trace("The ball ${ball} can go to ${candidates}");

      if (candidates.isEmpty) {
        // No possible solution for this grid
        return null;
      }

      // Recursively test each possibility
      for (var candidate in candidates) {
        trace("");
        trace("=== Trying ${ball} in ${candidate} ... ===");
        trace("");
        trace("Before:\n\n${this}\n");

        var child = drawPath(coords, candidate);

        trace("After:\n\n${child}\n");

        if (child.charAt(candidate) == '!') {
          // The ball fell into a hole
        } else {
          // Place the ball to the new location with a decreased distance
          child.setChar(candidate, "${distance - 1}");
        }

        trace("After (2):\n\n${child}\n");

        var solution = child.solve();

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
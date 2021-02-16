import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

class Position {
  final int x, y;

  Position(this.x, this.y);

  /// Returns the 8 target positions which can be reached from this position by
  /// the knight
  List<Position> all() {
    return [
      Position(x - 1, y - 2),
      Position(x + 1, y - 2),
      Position(x - 2, y - 1),
      Position(x + 2, y - 1),
      Position(x - 2, y + 1),
      Position(x + 2, y + 1),
      Position(x - 1, y + 2),
      Position(x + 1, y + 2)
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

class Board {
  final List<String> rows;

  Board(this.rows);

  int get width => rows[0].length;
  int get height => rows.length;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);
  bool exists(Position position) => isValidX(position.x) && isValidY(position.y);

  String charAt(Position position) => rows[position.y][position.x];

  Position _findPosition(String char) {
    for (var y = 0; y < height; y++) {
      for (var x = 0; x < width; x++) {
        if (rows[y][x] == char) {
          return Position(x, y);
        }
      }
    }

    throw "Start position not found !";
  }

  Position get startPosition => _findPosition('B');
  Position get endPosition => _findPosition('E');

  @override
  String toString() {
    return "${rows.join('\n')}";
  }
}

class Candidate {
  final int moves;
  final Position position;

  Candidate(this.moves, this.position);

  @override
  String toString() => "Candidate[${position} in ${moves} move(s)]";
}

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);
  var rows = <String>[];

  for (int i = 0; i < height; i++) {
    rows.add(stdin.readLineSync());
  }

  trace("${width} ${height}");
  trace("${rows.join('\n')}");

  var board = Board(rows);
  var endPosition = board.endPosition;

  var visited = Set<Position>(), queue = [ Candidate(0, board.startPosition) ];
  var found = false;

  trace("Positions: start = ${board.startPosition} / end = ${endPosition}");

  while (!found && !queue.isEmpty) {
    var current = queue.removeAt(0);

    trace("Current position: ${current} ...");

    visited.add(current.position);

    var candidates = current.position.all().where((p) => board.exists(p) && !visited.contains(p)).toList();

    for (var candidate in candidates) {
      if (candidate == endPosition) {
        trace("End position found");
        print(current.moves + 1);
        found = true;
        break;
      }

      var char = board.charAt(candidate);

      if (char == '#') {
        // Cannot move to this position
        trace("Move to ${candidate} is not possible");
        continue;
      }

      if (char == '.') {
        // Try this position
        trace("Move to ${candidate} is possible");
        queue.add(Candidate(current.moves + 1, candidate));
      }
    }
  }

  if (!found) {
    print('Impossible');
  }
}
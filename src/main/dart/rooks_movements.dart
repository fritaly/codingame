import 'dart:io';

import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

class Position {
  // Top left = a8, Top right = h8, Bottom left = a1, Bottom right = h1
  final int row;
  final String column;

  Position(this.column, this.row);

  factory Position.parse(String string) {
    return Position(string[0], int.parse(string[1]));
  }

  Position towards(Direction direction) {
    switch (direction) {
      case Direction.UP:
        return up();
      case Direction.LEFT:
        return left();
      case Direction.RIGHT:
        return right();
      case Direction.DOWN:
        return down();
      default:
        throw "Unexpected direction: ${direction}";
    }
  }

  Position up() {
    if (row == 8) {
      return null;
    }

    return Position(column, row + 1);
  }

  Position down() {
    if (row == 1) {
      return null;
    }

    return Position(column, row - 1);
  }

  Position left() {
    if (column == 'a') {
      return null;
    }

    return Position(String.fromCharCode(column.codeUnitAt(0) - 1), row);
  }

  Position right() {
    if (column == 'h') {
      return null;
    }

    return Position(String.fromCharCode(column.codeUnitAt(0) + 1), row);
  }

  @override
  bool operator ==(Object that) {
    if (that is Position) {
      return (this.row == that.row) && (this.column == that.column);
    }

    return false;
  }

  @override
  String toString() {
    return "${column}${row}";
  }
}

enum Color {
  WHITE, BLACK
}

class Piece {
  final Color color;
  final Position position;

  Piece(this.color, this.position);

  factory Piece.read(Stdin stdin) {
    var inputs = stdin.readLineSync().split(' ');
    var color = (inputs[0] == '0') ? Color.WHITE : Color.BLACK;
    var position = Position.parse(inputs[1]);

    return Piece(color, position);
  }

  @override
  String toString() {
    return "Piece[color: ${color}, position: ${position}]";
  }
}

enum Direction {
  UP, LEFT, RIGHT, DOWN
}

void main() {
  var initialPosition = Position.parse(stdin.readLineSync());
  var nbPieces = int.parse(stdin.readLineSync());

  var pieces = <Piece>[];

  for (int i = 0; i < nbPieces; i++) {
    pieces.add(Piece.read(stdin));
  }

  var moves = <String>[];

  // Check in the 4 directions
  for (var direction in [ Direction.UP, Direction.LEFT, Direction.RIGHT, Direction.DOWN ]) {
    var currentPosition = initialPosition;

    while (true) {
      currentPosition = currentPosition.towards(direction);

      if (currentPosition == null) {
        break;
      }

      var result = pieces.where((e) => e.position == currentPosition).toList();

      if (result.isEmpty) {
        moves.add("R${initialPosition}-${currentPosition}");
      } else {
        // Found one piece, check its color
        var piece = result.single;

        if (piece.color == Color.BLACK) {
          moves.add("R${initialPosition}x${currentPosition}");
        }

        break;
      }
    }
  }

  moves.sort();

  print(moves.join('\n'));
}
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

enum Color {
  WHITE, BLACK
}

class PieceType {
  final String char;

  const PieceType(this.char);

  static const KING = PieceType('K');
  static const QUEEN = PieceType('Q');
  static const ROOK = PieceType('R');
  static const BISHOP = PieceType('B');
  static const KNIGHT = PieceType('N');
  static const PAWN = PieceType('P');

  static PieceType from(String char) {
    switch (char.toUpperCase()) {
      case 'K':
        return PieceType.KING;
      case 'Q':
        return PieceType.QUEEN;
      case 'R':
        return PieceType.ROOK;
      case 'B':
        return PieceType.BISHOP;
      case 'N':
        return PieceType.KNIGHT;
      case 'P':
        return PieceType.PAWN;
      default:
        throw "Unable to parse piece '${char}'";
    }
  }
}

class Piece {
  final Color color;
  final PieceType type;

  const Piece(this.color, this.type);

  static const NONE = Piece(null, null);

  String asChar() {
    if (this == NONE) {
      return '.';
    }

    return (color == Color.WHITE) ? type.char.toUpperCase() : type.char.toLowerCase();
  }

  factory Piece.from(String char) {
    var color = (char == char.toUpperCase()) ? Color.WHITE : Color.BLACK;

    return Piece(color, PieceType.from(char.toUpperCase()));
  }
}

class Board {
  final List<List<String>> squares = List<List<String>>.generate(8, (index) => List<String>.generate(8, (index) => '.'));

  Piece pieceAt(int x, int y) => Piece.from(squares[y][x]);

  void setPiece(int x, int y, Piece piece) {
    squares[y][x] = piece.asChar();
  }

  Piece removePiece(int x, int y) {
    var piece = pieceAt(x, y);

    squares[y][x] = '.';

    return piece;
  }

  String asText() {
    return squares.map((row) => compress(row.join(''))).join('/');
  }

  @override
  String toString() {
    return squares.map((l) => l.join('')).join('\n');
  }
}

String compress(String row) {
  var result = "", chars = row.split('');

  for (var n = 0; n < 8; n++) {
    if (chars[n] == '.') {
      var count = 1;

      while ((n + 1 < 8) && (chars[n + 1] == '.')) {
        count++;
        n++;
      }

      result += "${count}";
    } else {
     result += chars[n];
    }
  }

  return result;
}

bool isNumeric(String s) => (int.tryParse(s) != null);

List<Piece> parseLine(String line) {
  var result = <Piece>[];

  for (var char in line.split('')) {
    if (isNumeric(char)) {
      for (var n = 0; n < int.parse(char); n++) {
        result.add(Piece.NONE);
      }
    } else {
      result.add(Piece.from(char));
    }
  }

  return result;
}

class Position {
  final int x, y;

  Position(this.x, this.y);

  factory Position.parse(String text) {
    // Example: "e2" -> (4, 6)
    var x = (text[0].codeUnitAt(0) - 'a'.codeUnitAt(0));
    var y = 8 - int.parse(text[1]);

    return Position(x, y);
  }
}

void main() {
  var board = Board();

  var B = stdin.readLineSync().split('/');

  trace("${B.join('/')}");

  for (var y = 0; y < 8; y++) {
    var pieces = parseLine(B[y]);

    for (var x = 0; x < 8; x++) {
      board.setPiece(x, y, pieces[x]);
    }
  }

  trace("${board}");
  trace("${board.asText()}");

  var N = int.parse(stdin.readLineSync());

  for (int i = 0; i < N; i++) {
    var change = stdin.readLineSync();

    trace("${change}");

    var startPosition = Position.parse(change.substring(0, 2));
    var endPosition = Position.parse(change.substring(2, 4));

    // TODO Handle "en passant" and "castling"
    var piece = board.removePiece(startPosition.x, startPosition.y);
    var targetPiece = board.setPiece(endPosition.x, endPosition.y, piece);

    // trace("${board}");
  }

  print(board.asText());
}
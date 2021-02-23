import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

class Side {
  final String name;

  static const TOP = Side('TOP');
  static const LEFT = Side('LEFT');
  static const RIGHT = Side('RIGHT');
  static const BOTTOM = Side('BOTTOM');
  static const IMPOSSIBLE = Side('IMPOSSIBLE');

  const Side(this.name);

  static List<Side> values() => [ TOP, LEFT, RIGHT, BOTTOM ];

  factory Side.valueOf(String name) => values().firstWhere((e) => e.name == name);

  Side opposite() {
    switch (this) {
      case TOP:
        return BOTTOM;
      case BOTTOM:
        return TOP;
      case LEFT:
        return RIGHT;
      case RIGHT:
        return LEFT;
      case IMPOSSIBLE:
        return IMPOSSIBLE;
      default:
        throw "Unexpected side: ${this}";
    }
  }

  @override
  String toString() {
    return "${name}";
  }
}

class RoomType {
  final int type;
  final Map<Side, Side> map;

  static const TYPE_0 = RoomType(0, { });
  static const TYPE_1 = RoomType(1, { Side.TOP:Side.BOTTOM, Side.LEFT:Side.BOTTOM, Side.RIGHT:Side.BOTTOM });
  static const TYPE_2 = RoomType(2, { Side.LEFT:Side.RIGHT, Side.RIGHT:Side.LEFT });
  static const TYPE_3 = RoomType(3, { Side.TOP:Side.BOTTOM });
  static const TYPE_4 = RoomType(4, { Side.TOP:Side.LEFT, Side.RIGHT:Side.BOTTOM, Side.LEFT:Side.IMPOSSIBLE });
  static const TYPE_5 = RoomType(5, { Side.TOP:Side.RIGHT, Side.LEFT:Side.BOTTOM, Side.RIGHT:Side.IMPOSSIBLE });
  static const TYPE_6 = RoomType(6, { Side.TOP:Side.IMPOSSIBLE, Side.LEFT:Side.RIGHT, Side.RIGHT:Side.LEFT });
  static const TYPE_7 = RoomType(7, { Side.TOP:Side.BOTTOM, Side.RIGHT:Side.BOTTOM });
  static const TYPE_8 = RoomType(8, { Side.LEFT:Side.BOTTOM, Side.RIGHT:Side.BOTTOM });
  static const TYPE_9 = RoomType(9, { Side.LEFT:Side.BOTTOM, Side.TOP:Side.BOTTOM });
  static const TYPE_10 = RoomType(10, { Side.TOP:Side.LEFT, Side.LEFT:Side.IMPOSSIBLE });
  static const TYPE_11 = RoomType(11, { Side.TOP:Side.RIGHT, Side.RIGHT:Side.IMPOSSIBLE });
  static const TYPE_12 = RoomType(12, { Side.RIGHT:Side.BOTTOM });
  static const TYPE_13 = RoomType(13, { Side.LEFT:Side.BOTTOM });

  static List<RoomType> values() => [ TYPE_0, TYPE_1, TYPE_2, TYPE_3, TYPE_4, TYPE_5, TYPE_6, TYPE_7, TYPE_8, TYPE_9, TYPE_10, TYPE_11, TYPE_12, TYPE_13 ];

  factory RoomType.valueOf(int type) => values().firstWhere((e) => e.type == type);

  const RoomType(this.type, this.map);
}

class Position {
  final int x, y;

  const Position(this.x, this.y);

  Position towards(Side side) {
    switch (side) {
      case Side.TOP:
        return Position(x, y - 1);
      case Side.LEFT:
        return Position(x - 1, y);
      case Side.RIGHT:
        return Position(x + 1, y);
      case Side.BOTTOM:
        return Position(x, y + 1);
      default:
        throw "Unexpected side: ${side}";
    }
  }

  @override
  bool operator ==(Object that) {
    if (that is Position) {
      return (this.x == that.x) && (this.y == that.y);
    }

    return false;
  }

  @override
  String toString() {
    return "(${x},${y})";
  }
}

class Grid {
  final List<List<int>> rooms;

  Grid(this.rooms);

  int get height => rooms.length;
  int get width => rooms[0].length;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);
  bool isValid(Position position) => isValidX(position.x) && isValidY(position.y);

  int typeAt(Position position) => rooms[position.y][position.x];

  RoomType roomAt(Position position) => RoomType.valueOf(typeAt(position));

  // RoomType roomAt(Position position) =>

  @override
  String toString() => rooms.join('\n');
}

void main() {
  var inputs = stdin.readLineSync().split(' ');

  trace("${inputs.join(' ')}");

  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);

  var rooms = <List<int>>[];

  for (var i = 0; i < height; i++) {
    // represents a line in the grid and contains W integers. Each integer represents one room of a given type.
    rooms.add(stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList(growable: false));
  }

  var grid = Grid(rooms);

  trace("${grid}");

  // the coordinate along the X axis of the exit (not useful for this first mission, but must be read).
  var EX = int.parse(stdin.readLineSync());

  while (true) {
    inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    var position = Position(int.parse(inputs[0]), int.parse(inputs[1]));
    var side = Side.valueOf(inputs[2]);

    // Current room ?
    var room = grid.roomAt(position);

    // Exit side ?
    side = room.map[side];

    if (side != Side.IMPOSSIBLE) {
      // Next position ?
      position = position.towards(side);
    }

    // One line containing the X Y coordinates of the room in which you believe
    // Indy will be on the next turn
    print('${position.x} ${position.y}');
  }
}
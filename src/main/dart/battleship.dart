import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

enum Direction {
  HORIZONTAL, VERTICAL
}

class Coordinates {
  final int x, y;

  Coordinates(this.x, this.y);

  @override
  bool operator ==(Object that) {
    if (that is Coordinates) {
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
  List<String> _lines;
  final List<Boat> _boats = [];

  Grid(List<String> lines) {
   this._lines = lines;

   // Automatically search the boats
   _searchBoats();
  }

  void shoot(Coordinates coordinates) {
    assert (coordinates != null);

    if (charAt(coordinates.x, coordinates.y) == '+') {
      // Identify the boat at these coordinates
      var target = boats.firstWhere((b) => b.allCoordinates().contains(coordinates));

      var initialHealth = target.health;

      // Update the grid
      setChar(coordinates.x, coordinates.y, '_');

      if (initialHealth > 0) {
        if (target.health == 0) {
          if (boats.where((b) => b.health == 0).length == 5) {
            print('TOUCHE COULE ${target.size} THEN LOSE');
          } else {
            print('TOUCHE COULE ${target.size}');
          }
        } else {
          print('TOUCHE');
        }
      }
    } else {
      print('MISSED');
    }
  }

  bool isValid() {
    var boats = _boats;

    // Check the number of boats
    if (boats.length != 5) {
      trace("Not the expected number of boats: ${boats.length}");

      return false;
    }

    // Check the boat sizes
    if (boats.where((b) => b.size == 5).length != 1) {
      trace("Not the expected number of 5-size boat");

      return false;
    }
    if (boats.where((b) => b.size == 4).length != 1) {
      trace("Not the expected number of 4-size boat");

      return false;
    }
    if (boats.where((b) => b.size == 3).length != 2) {
      trace("Not the expected number of 3-size boat");

      return false;
    }
    if (boats.where((b) => b.size == 2).length != 1) {
      trace("Not the expected number of 2-size boat");

      return false;
    }

    // Check that boats don't touch
    for (int i = 0; i < boats.length ; i++) {
      for (int j = i + 1; j < boats.length ; j++) {
        if (boats[i].touches(boats[j])) {
          return false;
        }
      }
    }

    return true;
  }

  List<Boat> get boats {
    return List.unmodifiable(_boats);
  }

  bool _isValid(int offset) {
    return (offset >= 0) && (offset < 10);
  }

  String charAt(int x, int y) {
    assert (_isValid(x) && _isValid(y));

    return _lines[y][x];
  }

  void setChar(int x, int y, String char) {
    assert (_isValid(x) && _isValid(y));
    assert ((char != null) && (char.length == 1));

    _lines[y] = _lines[y].substring(0, x) + char + _lines[y].substring(x + 1);
  }

  String _column(int x) {
    return _lines.map((line) => line[x]).join();
  }

  void _searchBoats() {
    // Search the boats in the grid
    var sizes = [ 5, 4, 3, 3, 2 ];

    sizes.forEach((size) {
      var boat = _searchBoat(size);

      if (boat != null) {
        _boats.add(boat);
      }
    });
  }

  int _indexOfBoat(int size, String string) {
    assert ((2 <= size) && (size <= 5));

    var currentSize = 0, startIndex = -1;

    for (int n = 0; n < 10; n++) {
      var char = string[n];

      if (char == '+' || char == '_') {
        if (currentSize == 0) {
          startIndex = n;
        }

        currentSize++;

        if (currentSize == size) {
          if (n < 9) {
            if (string[n+1] == '.') {
              return startIndex;
            }
          } else {
              return startIndex;
          }
        }
      } else {
        currentSize = 0;
        startIndex = -1;
      }
    }

    return -1;
  }

  Boat _searchBoat(int size) {
    assert ((2 <= size) && (size <= 5));

    // Search horizontally
    var direction = Direction.HORIZONTAL;

    for (int y = 0; y < 10; y++) {
      var index = _indexOfBoat(size, _lines[y]);

      if (index != -1) {
        var boat = Boat(Coordinates(index, y), size, direction, this);

        if (!_boats.contains(boat)) {
          return boat;
        }
      }
    }

    // Search vertically
    direction = Direction.VERTICAL;

    for (int x = 0; x < 10; x++) {
      var index = _indexOfBoat(size, _column(x));

      if (index != -1) {
        var boat = Boat(Coordinates(x, index), size, direction, this);

        if (!_boats.contains(boat)) {
          return boat;
        }
      }
    }

    return null;
  }
}

class Boat {
  final int size;
  final Direction direction;
  final Coordinates start;
  final Grid _grid;

  Boat(Coordinates this.start, this.size, this.direction, this._grid);

  Coordinates get end {
    switch (direction) {
      case Direction.HORIZONTAL:
        return Coordinates(start.x + size - 1, start.y);
      case Direction.VERTICAL:
        return Coordinates(start.x, start.y + size - 1);
    }
  }

  int get health {
    return allCoordinates().map((c) => _grid.charAt(c.x, c.y)).where((c) => c == '+').length;
  }

  operator ==(Object that) {
    if (that is Boat) {
      return (this.size == that.size) && (this.direction == that.direction) && (this.start == that.start);
    }

    return false;
  }

  List<Coordinates> allCoordinates() {
    var result = <Coordinates>[];

    for (int x = start.x; x <= end.x; x++) {
      for (int y = start.y; y <= end.y; y++) {
        result.add(Coordinates(x, y));
      }
    }

    return result;
  }

  bool touches(Boat that) {
    assert (that != null);

    for (Coordinates c1 in this.allCoordinates()) {
      for (Coordinates c2 in that.allCoordinates()) {
        if (((c1.x - c2.x).abs() <= 1) && ((c1.y - c2.y).abs() <= 1)) {
          trace("${c1} & ${c2} touch");

          return true;
        }
      }
    }

    return false;
  }

  @override
  String toString() {
    return "Boat[size: ${size}, direction: ${direction}, ${start}]";
  }
}

Coordinates parseCoordinates(String shot) {
  assert (shot != null);

  var char1 = shot[0], char2 = shot[1];

  // A1 => (0,0), A2 => (0,1)
  return Coordinates(char1.codeUnitAt(0) - 'A'.codeUnitAt(0), int.parse(char2) - 1);
}

void main() {
  var line = stdin.readLineSync();

  trace("${line}");

  var coordinates = parseCoordinates(line);

  var lines = List<String>();

  for (int i = 0; i < 10; i++) {
    lines.add(stdin.readLineSync());
  }

  trace("${lines.join('\n')}");
  trace("${coordinates}");

  var grid = new Grid(lines);

  trace("${grid._boats.map((e) => "${e.toString()}").join('\n')}");

  if (!grid.isValid()) {
    print('INVALID');
  } else {
    grid.shoot(coordinates);
  }
}
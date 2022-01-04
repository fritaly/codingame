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

enum Direction {
  UP, DOWN
}

/// Turns the given Iterator<T> into an Iterable<T>
Iterable<T> toIterable<T>(Iterator<T> iterator) {
  var result = List<T>();

  do {
    result.add(iterator.current);
  } while (iterator.moveNext());

  return result;
}

class Coordinates {
  final int x, y;

  Coordinates(this.x, this.y);

  bool operator ==(Object that) {
    if (that is Coordinates) {
      return (this.x == that.x) && (this.y == that.y);
    }

    return false;
  }

  Coordinates next(Direction direction) {
    switch (direction) {
      case Direction.UP:
        return Coordinates(x, y-1);
      case Direction.DOWN:
        return Coordinates(x, y+1);
    }
  }

  @override
  String toString() {
    return "(${x},${y})";
  }
}

class Grid {
  final List<String> grid;

  Grid(this.grid);

  int get width {
    return grid[0].length;
  }

  int get height {
    return grid.length;
  }

  /// Returns a "snake" iterator of coordinates
  Iterator<Coordinates> iterator() {
    return new SnakeIterator(this);
  }

  /// Returns a clone of this Grid.
  Grid clone() {
    return Grid(List<String>.from(grid));
  }

  /// Tells whether the given coordinates exists in this Grid.
  bool isValid(Coordinates coordinates) {
    return (0 <= coordinates.x) && (coordinates.x < width) && (0 <= coordinates.y) && (coordinates.y < height);
  }

  /// Returns the coordinates where the snake iterator starts.
  Coordinates get start {
    return Coordinates(0, height - 1);
  }

  /// Returns the coordinates where the snake iterator ends.
  Coordinates get end {
    if (width % 2 == 0) {
      return Coordinates(width - 1, height - 1);
    }

    return Coordinates(width - 1, 0);
  }

  String charAt(int x, int y) {
    assert ((0 <= x) && (x < width));
    assert ((0 <= y) && (y < height));

    return grid[y][x];
  }

  void setChar(int x, int y, String char) {
    grid[y] = grid[y].substring(0, x) + char + grid[y].substring(x+1);
  }

  /// Transposes this Grid into another Grid following the snake iteration logic.
  Grid transpose() {
    var coordinates = toIterable(iterator());
    var values = coordinates.map((coords) => charAt(coords.x, coords.y)).toList();

    trace("Before: ${values}");

    // Right shift the list of values by one
    values.insert(0, values.removeLast());

    trace("After: ${values}");

    // Generate the transposed grid
    var result = clone();

    for (Coordinates coords in coordinates) {
      result.setChar(coords.x, coords.y, values.removeAt(0));
    }

    return result;
  }

  void dump() {
    trace("${grid.join('\n')}");
  }
}

class SnakeIterator implements Iterator<Coordinates> {

  Grid _grid;
  Coordinates _current;
  Direction _direction = Direction.UP;

  SnakeIterator(Grid grid) {
    this._grid = grid;
    this._current = _grid.start;
  }

  @override
  Coordinates get current => _current;

  @override
  bool moveNext() {
    if (_current == _grid.end) {
      return false;
    }

    var coordinates = _current.next(_direction);

    if (!_grid.isValid(coordinates)) {
      // Move to the right cell
      coordinates = Coordinates(_current.x + 1, _current.y);

      // Flip the direction
      _direction = (_direction == Direction.UP) ? Direction.DOWN : Direction.UP;
    }

    _current = coordinates;

    return true;
  }
}

void trace(String message) {
  stderr.writeln("${message}");
}

void main() {
  var N = int.parse(stdin.readLineSync());
  var X = int.parse(stdin.readLineSync());

  trace("${N}");
  trace("${X}");

  var lines = <String>[];

  for (int i = 0; i < N; i++) {
    lines.add(stdin.readLineSync());
  }

  var grid = Grid(lines);
  grid.dump();

  var result = grid;

  for (int i = 0; i < X; i++) {
    result = result.transpose();
  }

  print('${result.grid.join('\n')}');
}
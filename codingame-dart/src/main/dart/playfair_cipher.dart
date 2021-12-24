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
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

List<String> split(String message) {
  assert (message.length % 2 == 0);

  var list = <String>[];

  while (message.length > 0) {
    list.add(message.substring(0, 2));

    message = message.substring(2);
  }

  return list;
}

class Position {
  final int x, y;

  const Position(this.x, this.y);

  Position right() => Position((x + 1) % 5, y);
  Position left() => Position((x + 4) % 5, y);
  Position up() => Position(x, (y + 4) % 5);
  Position down() => Position(x, (y + 1) % 5);

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
  final List<String> _lines;

  Grid(this._lines): assert(_lines.length == 5);

  String charAt(Position position) {
   return _lines[position.y][position.x];
  }

  bool contains(String char) => (find(char) != null);

  Position find(String char) {
    for (int y = 0; y < 5; y++) {
      for (int x = 0; x < 5; x++) {
        var position = Position(x, y);

        if (charAt(position) == char) {
          return position;
        }
      }
    }

    return null;
  }

  String encrypt(String plain) {
    var filtered = filter(plain);

    return (filtered.length % 2 == 0) ? split(filtered).map((s) => _encrypt(s)).join('') : "DUD";
  }

  String _encrypt(String plain) {
    assert (plain.length == 2);

    var position1 = find(plain[0]);
    var position2 = find(plain[1]);

    if ((position1 == null) || (position2 == null)) {
      return plain;
    }

    if (position1.y == position2.y) {
      return charAt(position1.right()) + charAt(position2.right());
    }
    if (position1.x == position2.x) {
      return charAt(position1.down()) + charAt(position2.down());
    }

    var minX = min(position1.x, position2.x), maxX = max(position1.x, position2.x);
    var minY = min(position1.y, position2.y), maxY = max(position1.y, position2.y);

    return charAt(Position(position2.x, (position2.y == minY) ? maxY : minY)) + charAt(Position(position1.x, (position1.y == minY) ? maxY : minY));
  }

  String decrypt(String ciphered) {
    var filtered = filter(ciphered);

    return (filtered.length % 2 == 0) ? split(filtered).map((s) => _decrypt(s)).join('') : "DUD";
  }

  String _decrypt(String ciphered) {
    assert (ciphered.length == 2);

    var position1 = find(ciphered[0]);
    var position2 = find(ciphered[1]);

    if ((position1 == null) && (position2 == null)) {
      return ciphered;
    }

    if (position1.y == position2.y) {
      return charAt(position1.left()) + charAt(position2.left());
    }
    if (position1.x == position2.x) {
      return charAt(position1.up()) + charAt(position2.up());
    }

    var minX = min(position1.x, position2.x), maxX = max(position1.x, position2.x);
    var minY = min(position1.y, position2.y), maxY = max(position1.y, position2.y);

    return charAt(Position(position2.x, (position2.y == minY) ? maxY : minY)) + charAt(Position(position1.x, (position1.y == minY) ? maxY : minY));
  }

  String filter(String string) {
    var buffer = StringBuffer();

    for (var char in string.split('')) {
      if (contains(char.toUpperCase())) {
        buffer.write(char.toUpperCase());
      }
    }

    return buffer.toString();
  }

  @override
  String toString() {
    return _lines.join('\n');
  }
}

void main() {
  var lines = <String>[];

  for (int i = 0; i < 5; i++) {
    var line = stdin.readLineSync();

    trace("${line}");

    lines.add(line.split(' ').join(''));
  }

  var grid = Grid(lines);

  var action = stdin.readLineSync();

  trace("${action}");

  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var messages = <String>[];

  for (int i = 0; i < N; i++) {
    var message = stdin.readLineSync();

    trace("${message}");

    messages.add(message);
  }

  for (var message in messages) {
    if (action == 'ENCRYPT') {
      print(grid.encrypt(message));
    } else {
      print(grid.decrypt(message));
    }
  }
}
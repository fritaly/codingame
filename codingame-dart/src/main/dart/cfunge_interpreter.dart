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
import 'dart:collection';
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

enum Direction {
  UP, LEFT, DOWN, RIGHT
}

class Program {
  final List<String> lines;
  final Queue<int> _stack = new Queue();

  Program(this.lines);

  String charAt(int x, int y) {
    return lines[y][x];
  }

  List<int> nextPosition(int x, int y, Direction direction) {
    switch (direction) {
      case Direction.UP:
        return [ x, y-1 ];
      case Direction.DOWN:
        return [ x, y+1 ];
      case Direction.LEFT:
        return [ x-1, y ];
      case Direction.RIGHT:
        return [ x+1, y ];
    }
  }

  void run() {
    int x = 0, y = 0;
    Direction direction = Direction.RIGHT;
    var stringMode = false;

    while (true) {
      var char = charAt(x, y);

      trace("(${x},${y}) = ${char}");

      if (stringMode) {
        if (char == '"') {
          stringMode = false;
        } else {
          _stack.addLast(char.codeUnitAt(0));
        }
      } else {
        if (int.tryParse(char) != null) {
          // The char is a digit
          var digit = int.parse(char);

          _stack.addLast(digit);
        } else if (char == '>') {
          direction = Direction.RIGHT;
        } else if (char == '<') {
          direction = Direction.LEFT;
        } else if (char == '^') {
          direction = Direction.UP;
        } else if (char == 'v') {
          direction = Direction.DOWN;
        } else if (char == 'E') {
          break;
        } else if (char == 'S') {
          // Skip the next character and continue with the subsequent character
          var next = nextPosition(x, y, direction);
          x = next[0];
          y = next[1];
        } else if (char == '+') {
          var first = _stack.removeLast();
          var second = _stack.removeLast();
          _stack.addLast(first + second);
        } else if (char == '-') {
          var first = _stack.removeLast();
          var second = _stack.removeLast();
          _stack.addLast(second - first);
        } else if (char == '*') {
          var first = _stack.removeLast();
          var second = _stack.removeLast();
          _stack.addLast(first * second);
        } else if (char == 'P') {
          // Pop the top value
          _stack.removeLast();
        } else if (char == 'X') {
          // Switch the order of the top two stack values
          var first = _stack.removeLast();
          var second = _stack.removeLast();
          _stack.addLast(first);
          _stack.addLast(second);
        } else if (char == 'D') {
          // Push a duplicate of the top value onto the stack
          _stack.addLast(_stack.last);
        } else if (char == '_') {
          // Pop the top value from the stack. If it is 0, continue to the right.
          // Otherwise, go left.
          if (_stack.removeLast() == 0) {
            direction = Direction.RIGHT;
          } else {
            direction = Direction.LEFT;
          }
        } else if (char == '|') {
          // Pop the top value from the stack. If it is 0, continue down.
          // Otherwise, go up.
          if (_stack.removeLast() == 0) {
            direction = Direction.DOWN;
          } else {
            direction = Direction.UP;
          }
        } else if (char == 'I') {
          stdout.write('${_stack.removeLast()}');
        } else if (char == 'C') {
          stdout.write('${String.fromCharCode(_stack.removeLast())}');
        } else if (char == '"') {
          stringMode = true;
        }
      }

      var next = nextPosition(x, y, direction);
      x = next[0];
      y = next[1];
    }
  }

  void dump() {
    trace(lines.join('\n'));
  }
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var lines = <String>[];

  for (int i = 0; i < N; i++) {
    lines.add(stdin.readLineSync());
  }

  var program = Program(lines);
  program.dump();
  program.run();
}
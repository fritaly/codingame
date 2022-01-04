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

void trace(String message) {
  stderr.writeln("${message}");
}

int find(String input, int index, String char1, [ String char2 ]) {
  while (index < input.length) {
    if (input[index] == char1) {
      return index;
    }
    if ((char2 != null) && (input[index] == char2)) {
      return index;
    }

    index++;
  }
  
  return -1;
}

class ParseResult<T> {
  final int index;
  final T result;

  ParseResult(this.index, this.result);
}

ParseResult<Set<int>> parseSet(String input, int index) {
  if (input[index] != '{') {
    throw "Unexpected char '${input[index]}' at index ${index}";
  }

  // Consume the '{'
  index++;

  var endIndex = find(input, index, '}');

  if (endIndex == -1) {
    throw "Unable to find the matching '}'";
  }

  var set = Set<int>.from(input.substring(index, endIndex).split(';').map((e) => int.parse(e)));

  return ParseResult(endIndex + 1, set);
}

ParseResult<Set<int>> parseInterval(String input, int index) {
  if ((input[index] != '[') && input[index] != ']') {
    throw "Unexpected char '${input[index]}' at index ${index}";
  }

  var openBracket = input[index];

  // Consume the '['
  index++;

  var endIndex = find(input, index, ']', '[');

  if (endIndex == -1) {
    throw "Unable to find the matching ']' or '['";
  }

  var closeBracket = input[endIndex];

  var substring = input.substring(index, endIndex);

  var bounds = substring.split(';').map((e) => int.parse(e)).toList();

  if (bounds.length != 2) {
    throw "Unable to parse '${substring}'";
  }

  var set = Set<int>();

  if (openBracket == ']') {
    // Exclude the start value
    bounds[0]++;
  }
  if (closeBracket == '[') {
    // Exclude the end value
    bounds[1]--;
  }

  for (int n = bounds[0]; n <= bounds[1]; n++) {
    set.add(n);
  }

  return ParseResult(endIndex + 1, set);
}

void main() {
  var input = stdin.readLineSync();

  trace("${input}");

  var stack = List();
  var index = 0;

  while (index < input.length) {
    var char = input[index];

    switch (char) {
      case '{':
        var result = parseSet(input, index);

        index = result.index;
        stack.add(result.result);
        break;

      case '[':
      case ']':
        var result = parseInterval(input, index);

        index = result.index;
        stack.add(result.result);
        break;

      case 'U':
      case 'I':
      case '-':
      case '(':
      case ')':
        stack.add(char);
        index++;
        break;

      default:
        throw "Unexpected character '${char}'";
    }

    // Try reducing the stack as much as possible
    while (stack.length >= 3) {
      var left = stack[stack.length - 3];
      var middle = stack[stack.length - 2];
      var right = stack[stack.length - 1];

      if ((middle is String) && (left is Set) && (right is Set)) {
        if (middle == 'U') {
          stack.removeLast();
          stack.removeLast();
          stack.removeLast();

          left.addAll(right);

          stack.add(left);
        } else if (middle == 'I') {
          stack.removeLast();
          stack.removeLast();
          stack.removeLast();

          left.retainAll(right);

          stack.add(left);
        } else if (middle == '-') {
          stack.removeLast();
          stack.removeLast();
          stack.removeLast();

          left.removeAll(right);

          stack.add(left);
        } else {
          throw "Unexpected state: ${stack}";
        }
      } else if ((left == '(') && (middle is Set) && (right == ')')) {
        stack.removeLast();
        stack.removeLast();
        stack.removeLast();

        stack.add(middle);
      } else {
        break;
      }
    }
  }

  if (stack.length != 1) {
    throw "Parsing failed";
  }

  var result = stack.removeLast();

  if (result.isEmpty) {
    print('EMPTY');
  } else {
    var list = result.toList();
    list.sort();

    print(list.join(' '));
  }
}
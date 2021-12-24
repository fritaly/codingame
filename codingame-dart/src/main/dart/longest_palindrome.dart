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

void trace(String message) {
  stderr.writeln("${message}");
}

String search(String string, int leftIndex, int rightIndex) {
  var offset = 1;

  while ((leftIndex - offset >= 0) && (rightIndex + offset < string.length)) {
    var left = string[leftIndex - offset], right = string[rightIndex + offset];

    if (left != right) {
      break;
    }

    offset++;
  }

  offset--;

  return string.substring(leftIndex - offset, rightIndex + offset + 1);
}

void main() {
  var string = stdin.readLineSync();

  trace("${string}");

  var solutions = [ '' ];

  for (var n = 0; n < string.length; n++) {
    var palindrome1 = search(string, n, n);

    trace("${palindrome1}");

    if (palindrome1.length > solutions[0].length) {
      solutions = [ palindrome1 ];
    } else if (palindrome1.length == solutions[0].length) {
      solutions.add(palindrome1);
    }

    if ((n + 1 < string.length) && (string[n] == string[n + 1])) {
      var palindrome2 = search(string, n, n + 1);

      trace("${palindrome2}");

      if (palindrome2.length > solutions[0].length) {
        solutions = [ palindrome2 ];
      } else if (palindrome2.length == solutions[0].length) {
        solutions.add(palindrome2);
      }
    }
  }

  print(solutions.join('\n'));
}
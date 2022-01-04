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

int computeHeight(int total) {
  var height = 0;

  while ((total >= height + 1)) {
    height++;
    total -= height;
  }

  return height;
}

String center(String string, int length) {
  if (string.length >= length) {
    return string;
  }

  var missing = length - string.length;
  var left = missing ~/ 2;
  var right = missing - left;

  return (' ' * left) + string + (' ' * right);
}

void main() {
  var glasses = int.parse(stdin.readLineSync());

  trace("${glasses}");

  var height = computeHeight(glasses);

  trace("${height}");

  var glass = [
    ' *** ',
    ' * * ',
    ' * * ',
    '*****'
  ];

  var width = (height * 5) + (height - 1);

  trace("${width}");

  for (var row = 1; row <= height; row++) {
    print('${glass.map((s) => List.generate(row, (row) => s).join(' '))
        .map((s) => center(s, width)).join('\n')}');
  }
}
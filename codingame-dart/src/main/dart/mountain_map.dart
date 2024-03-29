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
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

String chomp(String string) {
  var index = string.length - 1;

  while (string[index] == ' ') {
    index--;
  }

  return string.substring(0, index + 1);
}

void main() {
  var n = int.parse(stdin.readLineSync());
  var heights = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

  trace("${n}");
  trace("${heights.join(' ')}");

  var height = heights.reduce((value, element) => max(value, element));
  var width = heights.map((n) => 2 * n).reduce((value, element) => value + element);

  final List<List<String>> grid = List.generate(height, (index) => List.generate(width, (index) => " "));

  var x = 0;
  for (var h in heights) {
    for (var y = 0; y < h; y++) {
      grid[height - 1 - y][x++] = '/';
    }
    for (var y = h - 1; y >= 0; y--) {
      grid[height - 1 - y][x++] = '\\';
    }
  }

  print("${grid.map((e) => chomp(e.join())).join('\n')}");
}
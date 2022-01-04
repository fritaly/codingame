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

int find(List<List<int>> lake, int x, int y) {
  if ((x >= lake[0].length) || (y >= lake.length)) {
    return 0;
  }

  // Try on the right
  var right = find(lake, x + 1, y);

  // Try below
  var below = find(lake, x, y + 1);

  if (right > below) {
    return lake[y][x] + right;
  }

  return lake[y][x] + below;
}

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);

  trace("${width} ${height}");

  var lake = List<List<int>>(height);

  for (var i = 0; i < height; i++) {
    var foods = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    lake[i] = foods;
  }

  trace("${lake.map((list) => list.join(' ')).join('\n')}");

  print(find(lake, 0, 0));
}
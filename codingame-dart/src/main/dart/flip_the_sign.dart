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

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var height = int.parse(inputs[0]);
  var width = int.parse(inputs[1]);

  var grid = List.generate(height, (index) => List.generate(width, (index) => false));

  for (int y = 0; y < height; y++) {
    var chunks = stdin.readLineSync().split(' ');

    stderr.writeln('${chunks.join(' ')}');

    for (int x = 0; x < width; x++) {
      // Just store whether the integer is positive
      grid[y][x] = (int.parse(chunks[x]) > 0);
    }
  }

  // Sequence of signs to check
  var sequence = <bool>[];

  for (int y = 0; y < height; y++) {
    var chunks = stdin.readLineSync().split(' ');

    stderr.writeln('${chunks.join(' ')}');

    for (int x = 0; x < width; x++) {
      if (chunks[x] == 'X') {
        // Retrieve the sign for the cell (x,y) and add it to the sequence
        sequence.add(grid[y][x]);
      }
    }
  }

  stderr.writeln('Sequence: ${sequence}');

  var valid = true;

  if (!sequence.isEmpty) {
    var expected = sequence[0];

    for (bool b in sequence) {
      if (b != expected) {
        valid = false;
        break;
      }

      expected = !expected;
    }
  } else {
    // Print "true" when the sequence is empty
  }

  print(valid);
}

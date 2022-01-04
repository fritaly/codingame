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

void main() {
  var inputs = stdin.readLineSync().split(' ');

  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);
  var countX = int.parse(inputs[2]);
  var countY = int.parse(inputs[3]);

  inputs = stdin.readLineSync().split(' ');

  var x_values = <int>[ 0, width ];

  for (int i = 0; i < countX; i++) {
    x_values.add(int.parse(inputs[i]));
  }

  inputs = stdin.readLineSync().split(' ');

  var y_values = <int>[ 0, height ];

  for (int i = 0; i < countY; i++) {
    y_values.add(int.parse(inputs[i]));
  }

  // Sort the values
  x_values.sort();
  y_values.sort();

  // Compute all the possible widths and heights from the given x and y values
  var widths = <int>[];
  var heights = <int>[];

  for (int x0 = 0; x0 < x_values.length; x0++) {
    for (int x1 = x0 + 1; x1 < x_values.length; x1++) {
      widths.add(x_values[x1] - x_values[x0]);
    }
  }

  for (int y0 = 0; y0 < y_values.length; y0++) {
    for (int y1 = y0 + 1; y1 < y_values.length; y1++) {
      heights.add(y_values[y1] - y_values[y0]);
    }
  }

  var squares = 0;

  // Loop over all the widths
  widths.forEach((width) {
    // Find the height(s) matching the current width
    var matches = heights.where((element) => element == width).length;

    squares += matches;
  });

  print("${squares}");
}
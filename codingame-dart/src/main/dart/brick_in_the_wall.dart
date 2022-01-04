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

double work(int weight, int height) {
  assert (height >= 1);

  // W = ((L-1) * 6.5 / 100) * g * m
  return ((height - 1) * 6.5 / 100) * 10 * weight;
}

void main() {
  var bricksPerRow = int.parse(stdin.readLineSync());
  var numberOfBricks = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(' ');

  trace("${bricksPerRow}");
  trace("${numberOfBricks}");
  trace("${inputs.join(' ')}");

  var weights = inputs.map((s) => int.parse(s)).toList();

  // Sort the bricks from heaviest to lightest
  weights.sort((a, b) => b.compareTo(a));

  var totalWork = 0.0;

  var row = 1;
  var remainingSpace = bricksPerRow;

  while (!weights.isEmpty) {
    var weight = weights.removeAt(0);

    if (remainingSpace == 0) {
      row++;
      remainingSpace = bricksPerRow;
    }

    totalWork += work(weight, row);
    remainingSpace--;
  }

  print(totalWork.toStringAsFixed(3));
}
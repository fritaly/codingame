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
  var N = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(' ');

  stderr.writeln("${N}");
  stderr.writeln("${inputs.join(' ')}");

  var points = <int>[];

  for (int i = 0; i < N; i++) {
    var x = int.parse(inputs[i]);

    points.add(x);
  }

  var cost = 0;

  while (points.length > 1) {
    // Ensure that the cards are sorted by ascending health points
    points.sort();

    // Merge the 2 first cards into a new one
    var sum = points.removeAt(0) + points.removeAt(0);

    cost += sum;

    // Add the new card to the deck
    points.add(sum);
  }

  print(cost);
}
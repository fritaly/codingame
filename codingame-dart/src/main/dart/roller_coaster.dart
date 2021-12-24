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
import 'dart:collection';
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

void main() {
  var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();
  var availableSeats = inputs[0], maxRuns = inputs[1], groupCount = inputs[2];

  trace("${inputs.join(' ')}");

  var queue = Queue<int>();

  for (var i = 0; i < groupCount; i++) {
    queue.add(int.parse(stdin.readLineSync()));
  }

  trace("${queue.join('\n')}");

  var gain = 0;

  for (var i = 0; i < maxRuns; i++) {
    var remainingSeats = availableSeats;
    var current = List<int>();

    while (!queue.isEmpty && (remainingSeats >= queue.first)) {
      var groupSize = queue.removeFirst();

      current.add(groupSize);

      remainingSeats -= groupSize;
      gain += groupSize;
    }

    queue.addAll(current);

    // trace("Run with ${availableSeats - remainingSeats} people");
  }

  print(gain);
}
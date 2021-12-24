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

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var lengths = <int>[];

  for (int i = 0; i < N; i++) {
    lengths.add(int.parse(stdin.readLineSync()));
  }

  trace("${lengths.join('\n')}");

  var longestSequence = <int>[];

  for (var i = 0; i < lengths.length; i++) {
    for (var j = i + 1; j < lengths.length; j++) {
      var delta = lengths[j] - lengths[i], sequence = [ lengths[i] ];

      // Start the search at index j
      var current = lengths[i], index = i;

      while (true)  {
        current += delta;
        index = lengths.indexOf(current, index + 1);

        if (index == -1) {
          break;
        }

        sequence.add(current);
      }

      if (sequence.length > longestSequence.length) {
        longestSequence = sequence;

        trace("New longest sequence: ${longestSequence}");
      }
    }
  }

  trace("${longestSequence}");

  print(longestSequence.length);
}
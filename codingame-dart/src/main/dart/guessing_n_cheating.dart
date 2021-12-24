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
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

class Answer {
  final int n;
  final String hint;

  Answer(this.n, this.hint);
}

bool between(int n, int low, int high) {
  return (low <= n) && (n <= high);
}

void main() {
  var R = int.parse(stdin.readLineSync());

  trace("${R}");

  var answers = <Answer>[];

  for (var i = 0; i < R; i++) {
    var array = stdin.readLineSync().split(' ');

    trace("${array.join(' ')}");
  
    answers.add(Answer(int.parse(array[0]), '${array[1]} ${array[2]}'));
  }
  
  var round = -1, maxLower = 1, minUpper = 100;

  for (var i = 0; i < R; i++) {
    var answer = answers[i];

    if (answer.hint == 'too high') {
      // Adjust the upper bound
      minUpper = min(minUpper, answer.n - 1);
    } else if (answer.hint == 'too low') {
      // Adjust the lower bound
      maxLower = max(maxLower, answer.n + 1);
    } else if (answer.hint == 'right on') {
      // Check that the answer is in the possible range
      if (!between(answer.n, maxLower, minUpper)) {
        round = i+1;
        break;
      }
    }

    trace("new range: [${maxLower};${minUpper}]");

    if (maxLower > minUpper) {
      round = i+1;
      break;
    }
  }

  if (round == -1) {
    print('No evidence of cheating');
  } else {
    print('Alice cheated in round ${round}');
  }
}
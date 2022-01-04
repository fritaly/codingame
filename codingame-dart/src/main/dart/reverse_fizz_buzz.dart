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

bool isNumeric(String s) => (int.tryParse(s) != null);

class Range {
  /// The end bound is included
  final int start, end;

  Range(this.start, this.end);

  bool contains(int n) => (start <= n) && (n <= end);

  @override
  String toString() => "[${start}-${end}]";
}

/// Infers the numeric value behind the string at the given index in the list
int inferValue(List<dynamic> list, int index) {
  var value = list[index];

  if (value is int) {
    return value;
  }

  var range = Range(0, list.length - 1);

  // Infer the numeric value behind the string by searching for an integer
  // on the left and right sides
  var distance = 1;

  while (true) {
    var left = index - distance, right = index + distance;

    if (range.contains(left)) {
      var leftValue = list[left];

      if (leftValue is int) {
        return leftValue + distance;
      }
    } else if (range.contains(right)) {
      var rightValue = list[right];

      if (rightValue is int) {
        return rightValue - distance;
      }
    } else {
      break;
    }

    distance++;
  }

  throw "Unable to infer the value at index ${index}";
}

void main() {
  var n = int.parse(stdin.readLineSync());

  var values = [];
  var fizzIndices = <int>[], buzzIndices = <int>[], fizzBuzzIndices = <int>[];

  for (int i = 0; i < n; i++) {
    var line = stdin.readLineSync();

    if (isNumeric(line)) {
      values.add(int.parse(line));
    } else {
      values.add(line);

      if (line == 'Fizz') {
        fizzIndices.add(i);
      } else if (line == 'Buzz') {
        buzzIndices.add(i);
      } else if (line == 'FizzBuzz') {
        fizzBuzzIndices.add(i);
        fizzIndices.add(i);
        buzzIndices.add(i);
      }
    }
  }

  trace("${values.join('\n')}");

  trace("Fizz indices: ${fizzIndices}");
  trace("Buzz indices: ${buzzIndices}");
  trace("FizzBuzz indices: ${fizzBuzzIndices}");

  // Infer the difference between 2 consecutive Fizz / Buzz values
  var fizz = -1, buzz = -1;

  if (fizzIndices.length > 1) {
    fizz = fizzIndices[1] - fizzIndices[0];
  } else {
    fizz = inferValue(values, fizzIndices[0]);
  }
  if (buzzIndices.length > 1) {
    buzz = buzzIndices[1] - buzzIndices[0];
  } else {
    buzz = inferValue(values, buzzIndices[0]);
  }

  print('${fizz} ${buzz}');
}
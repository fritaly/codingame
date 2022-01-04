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

bool validate(String s) => s.replaceAll('Fizz', '').replaceAll('Buzz', '').isEmpty;

String transform(int n) {
  var buffer = StringBuffer();

  var times3 = "${n}".split('').where((c) => c == '3').length;
  var times5 = "${n}".split('').where((c) => c == '5').length;

  buffer.write('Fizz' * times3);

  var temp = n;
  while (temp % 3 == 0) {
    buffer.write('Fizz');
    temp = temp ~/ 3;
  }

  buffer.write('Buzz' * times5);

  temp = n;
  while (temp % 5 == 0) {
    buffer.write('Buzz');
    temp = temp ~/ 5;
  }

  return buffer.isEmpty ? "${n}" : buffer.toString();
}

void main() {
  var map = Map<String, List<int>>();

  for (var n = 1; n < 1000; n++) {
    var result = transform(n);

    trace("${n} -> ${result}");

    map.putIfAbsent(result, () => List<int>()).add(n);
  }

  var n = int.parse(stdin.readLineSync());

  trace("${n}");

  var rows = <String>[];

  for (int i = 0; i < n; i++) {
    rows.add(stdin.readLineSync());
  }

  trace("${rows.join('\n')}");

  for (int i = 0; i < n; i++) {
    var row = rows[i];

    if (int.tryParse(row) != null) {
      // The row is numeric
      if (transform(int.parse(row)) == row) {
        print(row);
      } else {
        // Invalid numeric value (3)
        print('ERROR');
      }
    } else if (!validate(row)) {
      // Invalid non-numeric value (POTATO)
      print('ERROR');
    } else {
      // Return the smallest integer associated to this string
      print(map[row][0]);
    }
  }
}
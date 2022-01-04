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

var base = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

String encode(int n) {
  var result = '';

  do {
    n--;
    var remainder = n % base.length;

    result = base[remainder] + result;

    n = (n - remainder) ~/ base.length;
  } while (n > 0);

  return result;
}

int decode(String string) {
  var result = 0;

  while (!string.isEmpty) {
    result = (result * base.length) + base.indexOf(string[0]) + 1;

    string = string.substring(1);
  }

  return result;
}

void main() {
  var n = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(' ');

  print(inputs.map((s) {
    if (int.tryParse(s) != null) {
      return encode(int.parse(s));
    }

    return decode(s);
  }).join(' '));
}
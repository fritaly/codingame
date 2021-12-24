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

String encode(int n, String base) {
  var result = '';

  do {
    var remainder = n % base.length;

    result = result + base[remainder];

    n = (n - remainder) ~/ base.length;
    n--;
  } while (n >= 0);

  return result;
}

void main() {
  var encoded = int.parse(stdin.readLineSync());
  var alphabet = stdin.readLineSync();

  trace("${encoded}");
  trace("${alphabet}");

  print(encode(encoded, alphabet));
}
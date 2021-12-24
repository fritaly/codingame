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

class Digit {
  final String top, center, bottom;
  final int digit;

  Digit(this.digit, this.top, this.center, this.bottom);

  bool matches(String s1, String s2, String s3) {
    return (this.top == s1) && (this.center == s2) && (this.bottom == s3);
  }
}

void main() {
  var line1 = stdin.readLineSync();
  var line2 = stdin.readLineSync();
  var line3 = stdin.readLineSync();

  var digits = [
    Digit(0, ' _ ', '| |', '|_|'),
    Digit(1, '   ', '  |', '  |'),
    Digit(2, ' _ ', ' _|', '|_ '),
    Digit(3, ' _ ', ' _|', ' _|'),
    Digit(4, '   ', '|_|', '  |'),
    Digit(5, ' _ ', '|_ ', ' _|'),
    Digit(6, ' _ ', '|_ ', '|_|'),
    Digit(7, ' _ ', '  |', '  |'),
    Digit(8, ' _ ', '|_|', '|_|'),
    Digit(9, ' _ ', '|_|', ' _|'),
  ];

  var count = line1.length / 3;

  var result = "";

  for (int i = 0 ; i < count; i++) {
    var s1 = line1.substring(i * 3, (i + 1) * 3);
    var s2 = line2.substring(i * 3, (i + 1) * 3);
    var s3 = line3.substring(i * 3, (i + 1) * 3);

    var match = digits.firstWhere((e) => e.matches(s1, s2, s3));

    result += "${match.digit}";
  }

  print(result);
}
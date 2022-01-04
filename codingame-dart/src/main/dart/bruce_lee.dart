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

bool isValid(String encoded) {
  var array = encoded.split(' ');

  var valid = true;

  for (int n = 0; n < array.length ; n++) {
    if (n % 2 == 0) {
      if (array[n] != '0' && array[n] != '00') {
        valid = false;
        break;
      }
    } else {
      if (!array[n].replaceAll('0', '').isEmpty) {
        valid = false;
        break;
      }
    }
  }

  return valid;
}

void main() {
  var encoded = stdin.readLineSync();

  stderr.writeln("${encoded}");

  if (!isValid(encoded)) {
    print('INVALID');
    return;
  }

  var array = encoded.split(' ');

  if (array.length % 2 != 0) {
    print('INVALID');
    return;
  }

  var buffer = StringBuffer();

  for (int n = 0; n < array.length ; n += 2) {
    var digit = (array[n] == '0' ? '1' : '0');

    for (int i = 0; i < array[n+1].length; i++) {
      buffer.write(digit);
    }
  }

  stderr.write("${buffer}");

  var binary = buffer.toString();

  if (buffer.length % 7 != 0) {
    print('INVALID');
    return;
  }

  var decoded = List.generate(buffer.length ~/ 7, (index) => index)
      .map((n) => binary.substring(n * 7, (n + 1) * 7))
      .map((s) => int.parse("0${s}", radix: 2))
      .map((n) => String.fromCharCode(n))
      .join('');

  print(decoded);
}
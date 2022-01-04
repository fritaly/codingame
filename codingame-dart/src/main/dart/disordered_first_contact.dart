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
import 'dart:math';

List<int> decompose(int n) {
  var result = <int>[], length = 1;

  while (n > 0) {
    result.add(min(n, length));
    n -= length;
    length++;
  }

  return result;
}

String encode(String message) {
  var lengths = decompose(message.length);

  var list = <String>[], tail = true;

  for (var length in lengths) {
    var string = message.substring(0, length);

    if (tail) {
      list.add(string);
    } else {
      list.insert(0, string);
    }

    tail = !tail;

    message = message.substring(length);
  }

  return list.join();
}

String decode(String message) {
  var lengths = decompose(message.length).reversed.toList();

  var result = "";
  var tail = (lengths.length % 2 == 1);

  for (var length in lengths) {
    var string;

    if (tail) {
      string = message.substring(message.length - length);
      result = string + result;
      message = message.substring(0, message.length - length);
    } else {
      string = message.substring(0, length);
      result = string + result;
      message = message.substring(length);
    }

    tail = !tail;
  }

  return result;
}

// See https://www.codingame.com/ide/puzzle/disordered-first-contact
void main() {
  var N = int.parse(stdin.readLineSync());
  var message = stdin.readLineSync();

  var result = message;

  if (N > 0) {
    for (int i = 0; i < N; i++) {
      result = decode(result);
    }
  } else {
    for (int i = 0; i < N.abs(); i++) {
      result = encode(result);
    }
  }

  print(result);
}
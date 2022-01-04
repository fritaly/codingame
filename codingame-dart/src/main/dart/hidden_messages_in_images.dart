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

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);

  var pixels = List<int>();

  for (int i = 0; i < height; i++) {
    // Extract the ASCII code associated to the characters
    pixels.addAll(stdin.readLineSync().split(' ').map((e) => int.parse(e)));
  }
  
  var binary = pixels.map((e) {
    // Convert the ASCII code to binary and extract the least significant bit
    var binary = e.toRadixString(2);
    return binary.substring(binary.length - 1);
  }).join('');

  var result = Iterable<int>.generate((binary.length / 8).toInt())
      .map((n) => binary.substring(n * 8, (n + 1) * 8)) // Extract the 8 bits for the nth byte
      .map((e) => String.fromCharCode(int.parse(e, radix: 2))) // Turn the 8 bits to a character
      .join(); // Aggregate all the characters decoded

  print('${result}');
}
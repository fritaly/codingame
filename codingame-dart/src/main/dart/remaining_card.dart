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

/// Returns the first power of 2 which is greater or equal to the given integer
int powerOf2(int n) {
  var result = 1;

  while (result < n) {
    result *= 2;
  }

  return result;
}

void main() {
  var n = int.parse(stdin.readLineSync());

  trace("${n}");

  var power2 = powerOf2(n);

  // Tests with different input values show a pattern: the result is always the
  // input value minus the difference between the input value and the first
  // power of 2 greater than n
  // Result = n - (powerOf2 - n) = 2 * n - powerOf2
  // Examples:
  // Input = 8 -> Output = 8 (n-0)
  // Input = 9 -> Output = 2 (n-7)
  // Input = 10 -> Output = 4 (n-6)
  // Input = 11 -> Output = 6 (n-5)
  // Input = 12 -> Output = 8 (n-4)
  // Input = 13 -> Output = 10 (n-3)
  // Input = 14 -> Output = 12 (n-2)
  // Input = 15 -> Output = 14 (n-1)
  // Input = 16 -> Output = 16 (n-0)
  // Input = 17 -> Output = 2 (n-15)
  print("${2 * n - power2}");
}
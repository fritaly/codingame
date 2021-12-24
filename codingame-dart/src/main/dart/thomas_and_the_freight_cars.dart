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

List<int> find(List<int> weights, List<int> cars, int index) {
  if (index >= weights.length) {
    return cars;
  }

  var current = weights[index];
  var head = !cars.isEmpty ? cars[0] : 0;
  var tail = !cars.isEmpty ? cars.last : 0;

  // trace("Testing cars=${cars}, current=${current} ...");

  var branch1 = cars, branch2 = cars, branch3 = cars;

  if (current > head) {
    // Try the current car as new head
    branch1 = find(weights, [ current ] + cars, index + 1);
  }
  if (current < tail) {
    // Try the current car as new tail
    branch2 = find(weights, cars + [ current ], index + 1);
  }

  // Try by ignoring the current car
  branch3 = find(weights, cars, index + 1);

  final int size1 = branch1.length, size2 = branch2.length, size3 = branch3.length;

  // trace("Branch 1: ${branch1}");
  // trace("Branch 2: ${branch2}");
  // trace("Branch 3: ${branch3}");

  if ((size1 > size2) && (size1 > size3)) {
    return branch1;
  }
  if ((size2 > size1) && (size2 > size3)) {
    return branch2;
  }

  return branch3;
}

void main() {
  var N = int.parse(stdin.readLineSync());
  var weights = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

  trace("${N}");
  trace("${weights.join(' ')}");

  print(find(weights, [], 0).length);
}
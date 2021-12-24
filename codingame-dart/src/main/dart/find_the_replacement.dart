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
  if (true) {
    stderr.writeln(message);
  }
}

void main() {
  var X = stdin.readLineSync();
  var Y = stdin.readLineSync();

  trace("${X}");
  trace("${Y}");

  var mappings = Map<String, Set<String>>();

  for (int i = 0; i < X.length ; i++) {
    var c1 = X[i];
    var c2 = Y[i];

    if (!mappings.containsKey(c1)) {
      mappings[c1] = Set<String>();
    }

    mappings[c1].add(c2);
  }

  trace("${mappings}");

  // Remove all the mappings where a letter maps to itself
  mappings.removeWhere((key, value) => (value.length == 1) && value.contains(key));

  trace("${mappings}");

  if (mappings.isEmpty) {
    print("NONE");
  } else if (mappings.entries.any((entry) => entry.value.length > 1)) {
    print("CAN'T");
  } else {
    mappings.entries.forEach((entry) { 
      print("${entry.key}->${entry.value.single}");
    });
  }
}
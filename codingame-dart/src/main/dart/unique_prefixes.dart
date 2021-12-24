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

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var words = List<String>();

  for (int i = 0; i < N; i++) {
    words.add(stdin.readLineSync());
  }

  trace("${words.join('\n')}");

  for (int i = 0; i < N; i++) {
    var word = words[i];

    var prefix = "";
    var matches = Set<String>.from(words);

    for (var j = 0; j < word.length; j++) {
      prefix += word[j];

      matches = matches.where((word) => word.startsWith(prefix)).toSet();

      if (matches.length == 1) {
        break;
      }
    }

    print(prefix);
  }
}
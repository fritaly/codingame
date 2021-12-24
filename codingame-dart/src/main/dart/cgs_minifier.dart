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

  var output = new StringBuffer();

  var mappings = Map<String, String>();

  for (int i = 0; i < N; i++) {
    var line = stdin.readLineSync();

    trace("${line}");

    var n=0;

    while (n < line.length) {
      var char = line[n];

      if (char == ' ') {
        // Skip the blank (outside of strings)
        n++;
      } else if (char == '\$') {
        // Detected the start of a variable name, find the closing $
        var endIndex = line.indexOf('\$', n+1);
        var name = line.substring(n, endIndex + 1);

        trace("Variable ${name} detected");

        if (!mappings.containsKey(name)) {
          mappings[name] = '\$${String.fromCharCode('a'.codeUnitAt(0) + mappings.length)}\$';
        }

        output.write('${mappings[name]}');

        n += name.length;
      } else if (char == '\'') {
        // Detected the start of a string, copy the string as is to the output
        do {
          output.write(line[n++]);
        } while (line[n] != '\'');

        output.write('\'');
        n++;
      } else {
        output.write(line[n++]);
      }
    }
  }

  print(output);
}
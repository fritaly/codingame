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

  var lines = <String>[];

  for (var i = 0; i < N; i++) {
    lines.add(stdin.readLineSync());
  }

  trace("${lines.join('\n')}");

  print('<table>');

  while (lines.length > 1) {
    var line1 = lines.removeAt(0);
    var cellCount = line1.split('').where((c) => c == '+').length - 1;
    var aggregatedValues = List<String>.generate(cellCount, (index) => '');

    while (!lines.isEmpty && lines[0].startsWith('|')) {
      var line = lines.removeAt(0);

      // Remove the first and last entries corresponding to empty strings
      var values = line.split('|').map((s) => s.trim()).toList();
      values.removeAt(0);
      values.removeLast();

      trace("${values}");

      for (var n = 0; n < values.length; n++) {
        if (!values[n].isEmpty) {
          if (aggregatedValues[n].isEmpty) {
            aggregatedValues[n] = values[n];
          } else {
            aggregatedValues[n] = aggregatedValues[n] + ' ' + values[n];
          }
        }
      }
    }

    print("<tr>${aggregatedValues.map((s) => "<td>${s}</td>").join()}</tr>");
  }

  lines.removeAt(0);

  print('</table>');

}
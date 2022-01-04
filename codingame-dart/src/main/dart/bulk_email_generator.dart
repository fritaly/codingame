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

bool isWellFormed(String string) {
  var depth = 0;

  for (var char in string.split('')) {
    if (char == '(') {
      depth++;
    } else if (char == ')') {
      depth--;
    }
  }

  return (depth == 0);
}

class Generator {
  final String id;
  final List<String> _values;

  Generator._(this.id, this._values);

  factory Generator.create(String text) {
    return Generator._(text, text.split('|'));
  }

  String next(int n) => _values[n % _values.length];
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var lines = <String>[];

  for (int i = 0; i < N; i++) {
    lines.add(stdin.readLineSync());
  }

  trace("${lines.join('\n')}");

  lines = mergeMultilines(lines);

  var generators = Map<String, Generator>();

  var count = 0;

  for (var line in lines) {
    var buffer = StringBuffer();

    var index = 0;

    while (index < line.length) {
      var char = line[index];

      if (char == '(') {
        var startIndex = index; // index of first character after '('

        var endIndex = line.indexOf(')', startIndex);

        if (endIndex != -1) {
          index++; // for the opening '('
          index = endIndex + 1; // for the closing ')'

          // Text identifying the generator
          var text = line.substring(startIndex + 1, index - 1);

          if (!generators.containsKey(text)) {
            generators[text] = Generator.create(text);
          }

          buffer.write(generators[text].next(count++));
        } else {
          // No closing ')' found, just write the character and continue
          buffer.write(char);

          index++;
        }
      } else {
        buffer.write(char);

        index++;
      }
    }

    print(buffer.toString());
  }
}

/// Merge the multi-line strings detected in the given list of strings and
/// returns the result
List<String> mergeMultilines(List<String> lines) {
  var result = <String>[];

  while (!lines.isEmpty) {
    var line = lines.removeAt(0);

    if (isWellFormed(line)) {
      result.add(line);
    } else {
      // Rebuild the multi-line string by merging the line with the one after in
      // the input list
      lines.insert(0, line + '\n' + lines.removeAt(0));
    }
  }
  return result;
}
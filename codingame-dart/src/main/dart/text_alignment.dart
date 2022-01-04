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

void trace(String message) {
  stderr.writeln("${message}");
}

String chomp(String string) {
  var index = string.length - 1;

  while (string[index] == ' ') {
    index--;
  }

  return string.substring(0, index + 1);
}

String center(String string, int length) {
  var missing = length - string.length;
  var left = (missing / 2).floor();
  // var right = missing - left;

  return (' ' * left) + string /* + (' ' * right) */;
}

void main() {
  var alignment = stdin.readLineSync();
  var N = int.parse(stdin.readLineSync());

  trace("${alignment}");
  trace("${N}");

  var lines = <String>[];

  for (int i = 0; i < N; i++) {
    lines.add(stdin.readLineSync());
  }

  trace("${lines.join('\n')}");

  var maxLength = lines.map((s) => s.length).reduce((value, element) => max(value, element));

  trace("Max length: ${maxLength}");

  switch (alignment) {
    case 'LEFT':
      print('${lines.map((s) => chomp(s)).join('\n')}');
      break;
    case 'RIGHT':
      print('${lines.map((s) => s.padLeft(maxLength)).join('\n')}');
      break;
    case 'CENTER':
      // Spread the remaining space evenly on both sides of the text. If the
      // number of remaining spaces is odd, leave the extra space on the right.
      // Don't actually append space characters to the end of the line, though.
      print('${lines.map((s) => center(s, maxLength)).join('\n')}');
      break;
    case 'JUSTIFY':
      // Spread the remaining space evenly between words of the line so that all
      // lines end on the same column, with the exception that lines with a single
      // word should remain left-aligned. When necessary, round the numbers of
      // spaces to their integer part. For instance if you need to spread 11
      // spaces between 5 words, the theoretical number of spaces between each
      // pair of words is 2.75, yielding a total number of used spaces of 2.75 -
      // 5.5 - 8.75 - 11. Round to 2 - 5 - 8 -11 and the effective number of
      // spaces in each block is 2 - 3 - 3 - 3.
      lines.forEach((line) {
        var words = line.split(' ');

        if (words.length == 1) {
          print('${words[0]}');
        } else {
          // Number of whitespaces missing on the line
          var missing = maxLength - line.length;

          // Average number of whitespaces to insert between 2 words
          var increment = (missing / (words.length - 1));
          var buffer = StringBuffer();

          // Counter of average whitespaces added
          var sum = 0.0;

          // Actual number of whitespaces added
          var added = 0;

          for (int i = 0; i < words.length; i++) {
            buffer.write(words[i]);

            if (i != words.length - 1) {
              buffer.write(' ');

              sum += increment;

              if (added < sum.floor()) {
                var delta = sum.floor() - added;

                buffer.write(' ' * delta);

                added += delta;
              }
            }
          }

          print('${buffer}');
        }
      });
      break;
  }
}
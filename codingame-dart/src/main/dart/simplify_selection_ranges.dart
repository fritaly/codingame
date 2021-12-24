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

class Range {
  final int start;
  int end;

  int get length => (end - start) + 1;

  Range(this.start, this.end): assert (start <= end);

  @override
  String toString() => (start == end) ? "${start}" : "${start}-${end}";
}

void main() {
  var line = stdin.readLineSync().replaceAll('[', '').replaceAll(']', '');

  trace("${line}");

  var list = line.split(',').map((e) => int.parse(e)).toList();
  list.sort();

  var ranges = <Range>[];

  while (!list.isEmpty) {
    var n = list.removeAt(0);

    if (ranges.length >= 2) {
      var second2last = ranges[ranges.length - 2];
      var last = ranges.last;

      if ((second2last.length == 1) && (last.length == 1) && (second2last.start + 1 == last.start) && (last.start + 1 == n)) {
        // Merge the 3 unit ranges into a new range
        ranges.removeLast();
        second2last.end = n;
      } else if ((last.length >= 3) && (last.end + 1 == n)) {
        // Extend the existing range
        last.end = n;
      } else {
        // Add a new unit range
        ranges.add(Range(n, n));
      }
    } else if (ranges.length >= 1) {
      var last = ranges.last;

      if ((last.length >= 3) && (last.end + 1 == n)) {
        // Extend the existing range
        last.end = n;
      } else {
        // Add a new unit range
        ranges.add(Range(n, n));
      }
    } else {
      // Add a new unit range
      ranges.add(Range(n, n));
    }
  }

  print(ranges.join(','));
}
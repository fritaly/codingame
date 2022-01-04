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

class Generator implements Iterator<String> {
  final String _initialValue;
  String _current;

  Generator(this._initialValue): assert (_initialValue != null);

  @override
  String get current => _current;

  @override
  bool moveNext() {
    if (_current == null) {
      _current = _initialValue;
    } else {
      var result = _current.split('')
          .map((c) => int.parse(c))
          .map((n) => n * n)
          .reduce((value, element) => value + element);

      _current = "${result}";
    }

    return true;
  }
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  for (int i = 0; i < N; i++) {
    var x = stdin.readLineSync();

    trace("${x}");

    var generator = Generator(x);
    var history = Set<String>();

    while (generator.moveNext()) {
      var current = generator.current;

      // trace("Found ${current}");

      if (current == '1') {
        print('${x} :)');
        break;
      }
      if (history.contains(current)) {
        print('${x} :(');
        break;
      }

      history.add(current);
    }
  }
}
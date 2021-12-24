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

class Fraction implements Comparable<Fraction> {
  final int n, p;

  Fraction(this.n, this.p);

  Fraction median(Fraction that) {
    return Fraction(this.n + that.n, this.p + that.p);
  }

  operator ==(Object that) {
    if (that is Fraction) {
      return compareTo(that) == 0;
    }

    throw "Unexpected object: ${that}";
  }

  @override
  int compareTo(Fraction that) {
    return (that.n * this.p) - (this.n * that.p);
  }

  @override
  String toString() {
    return "${n}/${p}";
  }
}

class Number {
  final Fraction leftSeed, rightSeed;

  Number(this.leftSeed, this.rightSeed);

  Fraction get value {
    return leftSeed.median(rightSeed);
  }

  Number get left {
    return Number(leftSeed, value);
  }

  Number get right {
    return Number(value, rightSeed);
  }

  String asText() {
    return "${value.n}/${value.p}";
  }

  @override
  String toString() {
    return "Number[${value}]";
  }
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var root = Number(Fraction(0,1), Fraction(1,0));

  for (int i = 0; i < N; i++) {
    var line = stdin.readLineSync();

    trace("${line}");

    if (line[0] == 'L' || line[0] == 'R') {
      var current = root;

      for (int n = 0; n < line.length; n++) {
        current = ((line[n]) == 'L') ? current.left : current.right;
      }

      print('${current.asText()}');
    } else {
      var n = int.parse(line.substring(0, line.indexOf('/')));
      var p = int.parse(line.substring(line.indexOf('/') + 1));

      var target = Fraction(n, p);

      var current = root;

      var buffer = StringBuffer();

      while (current.value.compareTo(target) != 0) {
        if (current.value.compareTo(target) < 0) {
          buffer.write('L');
          current = current.left;

          trace("L -> ${current.value}");
        } else {
          buffer.write('R');
          current = current.right;
          trace("R -> ${current.value}");
        }
      }

      print('${buffer}');
    }
  }
}
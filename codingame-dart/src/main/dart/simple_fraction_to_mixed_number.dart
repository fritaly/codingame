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

class Fraction {
  final int n, p;

  Fraction(this.n, this.p);

  bool get positive {
    return ((n >= 0) && (p >= 0)) || ((n < 0) && (p < 0));
  }

  Fraction reduce() {
    var factors1 = decompose(n);
    var factors2 = decompose(p);

    // trace("${n} = ${factors1.join(' x ')}");
    // trace("${p} = ${factors2.join(' x ')}");

    var removed = <int>[];

    factors1.forEach((n) {
      if (factors2.remove(n)) {
        removed.add(n);
      }
    });

    removed.forEach((n) {
      factors1.remove(n);
    });

    // Ensure the 2 lists cannot be empty
    if (factors1.isEmpty) {
      factors1.add(1);
    }
    if (factors2.isEmpty) {
      factors2.add(1);
    }

    return Fraction(factors1.reduce((value, element) => value * element),
        factors2.reduce((value, element) => value * element));
  }

  String format() {
    if (p == 0) {
      return 'DIVISION BY ZERO';
    }

    var sign = positive ? '' : '-';

    var integerValue = (n.abs() ~/ p.abs());
    var remainder = (n.abs() % p.abs());

    if (remainder == 0) {
      if (integerValue == 0) {
        // Don't print the sign
        return '0';
      }

      return "${sign}${integerValue}";
    }

    // Reduce the fraction part
    var fraction = Fraction(remainder, p.abs()).reduce();

    if (integerValue == 0) {
      return "${sign}${fraction}";
    }

    return "${sign}${integerValue} ${fraction}";
  }

  @override
  String toString() {
    return "${n}/${p}";
  }
}

/// Iterator returning the prime numbers 
class PrimeIterator implements Iterator<int> {
  int _current = 1;
  Set<int> previous = Set();

  @override
  int get current => _current;

  @override
  bool moveNext() {
    do {
      if (_current > 2) {
        _current += 2;
      } else {
        _current++;
      }
    } while (previous.any((n) => _current % n == 0));

    previous.add(_current);

    return true;
  }
}

List<int> decompose(int n) {
  List<int> result = [];

  var remainder = n;
  var limit = sqrt(n);

  var primes = new PrimeIterator();

  while (primes.moveNext() && (primes.current <= limit)) {
    var prime = primes.current;

    while (remainder % prime == 0) {
      result.add(prime);

      remainder = remainder ~/ prime;
    }
  }

  if (result.isEmpty || (remainder != 1)) {
    result.add(remainder);
  }

  // trace("${n} = ${result.join(' x ')}");

  return result;
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var fractions = <Fraction>[];

  for (int i = 0; i < N; i++) {
    var line = stdin.readLineSync();

    trace("${line}");
    
    var array = line.split('/');

    fractions.add(Fraction(int.parse(array[0]), int.parse(array[1])));
  }

  for (int i = 0; i < N; i++) {
    print(fractions[i].format());
  }
}
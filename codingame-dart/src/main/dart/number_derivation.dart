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

/// Tries to decompose the given integer into a product of integers. Returns a
/// list of 2 integers if successful otherwise returns a list with n (because it
/// is prime)
List<int> decompose(int n) {
  var remainder = n;
  var limit = sqrt(n);

  var primes = new PrimeIterator();

  while (primes.moveNext() && (primes.current <= limit)) {
    var prime = primes.current;

    if (remainder % prime == 0) {
      // Return as soon as we find a factor -> n = p x m
      return [ prime, remainder ~/ prime ];
    }
  }

  // n is prime
  return [ n ];
}

int derive(int n) {
  if (n == 1) {
    return 0;
  }

  var factors = decompose(n);

  if (factors.length == 1) {
    // If a number is prime p, p′=1
    return 1;
  }
  if (factors.length == 2) {
    // If a number is the product p×q, (p×q)′=p′×q + p×q′
    var p = factors[0], q = factors[1];

    return derive(p) * q + p * derive(q);
  }

  throw "Impossible !";
}

void main() {
  var n = int.parse(stdin.readLineSync());

  trace("${n}");

  print(derive(n));
}
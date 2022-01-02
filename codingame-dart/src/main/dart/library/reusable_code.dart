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
// ================= //
// === Direction === //
// ================= //

import 'dart:io';
import 'dart:math';

class Direction {
  final String name;

  static const UP = Direction('UP');
  static const LEFT = Direction('LEFT');
  static const RIGHT = Direction('RIGHT');
  static const DOWN = Direction('DOWN');

  const Direction(this.name);

  @override
  String toString() => "${name}";
}

// ================ //
// === Position === //
// ================ //

class Position {
  final int x, y;

  Position(this.x, this.y);

  bool isAligned(Position other) => (this.x == other.x) || (this.y == other.y);

  double distance(Position that) {
    var dx = this.x - that.x, dy = this.y - that.y;

    return sqrt(dx * dx + dy * dy);
  }

  @override
  bool operator ==(Object other) {
    if (other is Position) {
      return (this.x == other.x) && (this.y == other.y);
    }

    return false;
  }

  @override
  int get hashCode {
    var hash = 17;
    hash = 37 * hash + x.hashCode;
    hash = 37 * hash + y.hashCode;

    return hash;
  }

  /// Returns all the positions at the given distance from this position
  List<Position> all(int n) => [ Position(x, y - n), Position(x - n, y), Position(x, y + n), Position(x + n, y) ];

  @override
  String toString() => "(${x}, ${y})";
}

// ============= //
// === Point === //
// ============= //

class Point {
  final int x, y;

  Point(this.x, this.y);

  bool isAligned(Point other) => (this.x == other.x) || (this.y == other.y);

  double distance(Point that) {
    var dx = this.x - that.x, dy = this.y - that.y;

    return sqrt(dx * dx + dy * dy);
  }

  @override
  bool operator ==(Object other) {
    if (other is Point) {
      return (this.x == other.x) && (this.y == other.y);
    }

    return false;
  }

  @override
  int get hashCode {
    var hash = 17;
    hash = 37 * hash + x.hashCode;
    hash = 37 * hash + y.hashCode;

    return hash;
  }

  @override
  String toString() => "(${x}, ${y})";
}

// ============== //
// === Vector === //
// ============== //

class Vector {
  final int x, y;

  Vector(this.x, this.y);

  Vector operator *(int n) => Vector(x * n, y * n);
  Vector operator +(Vector that) => Vector(x + that.x, y + that.y);

  @override
  bool operator ==(Object other) {
    if (other is Vector) {
      return (this.x == other.x) && (this.y == other.y);
    }

    return false;
  }

  @override
  int get hashCode {
    var hash = 11;
    hash = 29 * hash + x.hashCode;
    hash = 29 * hash + y.hashCode;

    return hash;
  }

  double get length => sqrt(x * x + y * y);

  factory Vector.from(Point start, Point end) {
    return Vector(end.x - start.x, end.y - start.y);
  }

  @override
  String toString() {
    return "Vector(${x}, ${y})";
  }
}

// ============ //
// === Grid === //
// ============ //

class Grid {
  final List<String> rows;

  Grid(this.rows);

  int get width => rows[0].length;
  int get height => rows.length;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);
  bool exists(Position position) => isValidX(position.x) && isValidY(position.y);

  String charAt(Position position) => rows[position.y][position.x];

  @override
  String toString() {
    return "${rows.join('\n')}";
  }
}

// ===================== //
// === PrimeIterator === //
// ===================== //

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

// ===================== //
// === Miscellaneous === //
// ===================== //

bool isNumeric(String s) => (int.tryParse(s) != null);

void trace(String message) {
  stderr.writeln("${message}");
}

/// Tells whether the given integer is prime
bool isPrime(int n) {
  var iterator = new PrimeIterator();

  while (iterator.moveNext()) {
    var current = iterator.current;

    if (current == n) {
      return true;
    }
    if ((current > n) || (n % current == 0)) {
      return false;
    }
  }
}

/// Returns a list of integers representing the factors found upon decomposing n
List<int> decompose(int n) {
  List<int> factors = [];

  var remainder = n;
  var limit = sqrt(n);

  var primes = new PrimeIterator();

  while (primes.moveNext() && (primes.current <= limit)) {
    var prime = primes.current;

    while (remainder % prime == 0) {
      factors.add(prime);

      remainder = remainder ~/ prime;
    }
  }

  if (factors.isEmpty || (remainder != 1)) {
    factors.add(remainder);
  }

  // trace("${n} = ${result.join(' x ')}");

  return factors;
}
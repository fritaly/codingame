import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

class Solution implements Comparable<Solution> {
  final int n, x, y;

  Solution(this.n, this.x, this.y);

  @override
  int compareTo(Solution other) {
    // Sort the combinations by x in descending order
    return other.x.compareTo(this.x);
  }

  @override
  String toString() => "1/${n} = 1/${x} + 1/${y}";
}

class Product {
  int first, second;

  Product(int first, int second) {
    // Invariant: first >= second
    this.first = max(first, second);
    this.second = min(first, second);
  }

  @override
  bool operator ==(Object other) {
    if (other is Product) {
      return (this.first == other.first) && (this.second == other.second);
    }

    return false;
  }

  @override
  int get hashCode {
    var hash = 17;
    hash = 37 * hash + first.hashCode;
    hash = 37 * hash + second.hashCode;

    return hash;
  }

  @override
  String toString() => "${first} x ${second} = ${first * second}";
}

Set<Product> decompose(int number) {
  var products = Set<Product>();

  for (var n = 1; n <= sqrt(number); n++) {
    if (number % n == 0) {
      products.add(Product(n, number ~/ n));
    }
  }

  return products;
}

void main() {
  var n = int.parse(stdin.readLineSync());

  trace("${n}");

  var n2 = n * n, products = decompose(n2);

  trace("n * n = ${n2}");
  trace("${products.join('\n')}");

  // 1/n = 1/x + 1/y --> (x-n)(y-n) = n^2

  var solutions = <Solution>[];

  for (var product in products) {
    // product = first * second and n^2 = (x - n)(y - n)
    // -> first = x - n -> x = first + n
    // -> second = y - n -> y = second + n
    solutions.add(Solution(n, product.first + n, product.second + n));
  }

  solutions.sort();
  solutions.forEach((element) { print(element); });
}
import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

/// Iterator returning the prime numbers up to a given upper limit
class PrimeIterator implements Iterator<int> {
  int _index = 0;
  List<bool> _array;

  PrimeIterator(int max) {
    assert (max >= 2);
    this._array = List<bool>.generate(max, (index) => true);
  }

  @override
  int get current => _index + 1;

  @override
  bool moveNext() {
    if (_index + 1 >= _array.length) {
      return false;
    }

    do {
      _index++;
    } while ((_index < _array.length) && _array[_index] == false);

    if (_index + 1 >= _array.length) {
      return false;
    }

    var found = _array[_index];

    if (found) {
      var prime = _index + 1;

      for (var current = _index; current + prime < _array.length; current += prime) {
        _array[current + prime] = false;

      }
    }

    return found;
  }
}

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var height = int.parse(inputs[0]);
  var width = int.parse(inputs[1]);

  trace("${height} ${width}");

  var upperBound = pow(10, max(height, width));
  var primes = Set<int>(), iterator = new PrimeIterator(upperBound);

  while (iterator.moveNext() && (iterator.current < upperBound)) {
    primes.add(iterator.current);
  }

  trace("${primes}");

  List<List<int>> grid = List.generate(height, (index) => List<int>(width));

  for (int y = 0; y < height; y++) {
    var row = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    for (int x = 0; x < width; x++) {
      grid[y][x] = row[x];
    }
  }

  trace(grid.map((e) => e.join(' ')).join('\n'));

  var primesFound = Set<int>();

  for (var y = 0; y < height; y++) {
    for (var x = 0; x < width; x++) {
      var hvalue = 0;
      for (var dx = x; dx < width; dx++) {
        hvalue = hvalue * 10 + grid[y][dx];

        if (primes.contains(hvalue)) {
          primesFound.add(hvalue);
        }
      }

      var vvalue = 0;
      for (var dy = y; dy < height; dy++) {
        vvalue = vvalue * 10 + grid[dy][x];

        if (primes.contains(vvalue)) {
          primesFound.add(vvalue);
        }
      }
    }
  }

  trace("${primesFound}");
  print(primesFound.length);
}
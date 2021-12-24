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
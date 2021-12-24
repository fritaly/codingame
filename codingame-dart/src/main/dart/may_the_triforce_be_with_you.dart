import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

String center(String string, int length) {
  if (string.length >= length) {
    return string;
  }

  var missing = length - string.length;
  var left = missing ~/ 2;
  var right = missing - left;

  return (' ' * left) + string + (' ' * right);
}

String chomp(String string) {
  var index = string.length - 1;

  while (string[index] == ' ') {
    index--;
  }

  return string.substring(0, index + 1);
}

/// Returns a list containing the lines used for drawing a triforce of the given
/// size
List<String> triforce(int n) {
  assert (n > 0);

  var length = 1 + 2 * (n - 1);

  return List<int>.generate(n, (index) => index + 1)
      .map((n) => '*' * (1 + 2 * (n - 1)))
      .map((s) => center(s, length))
      .toList();
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var template = triforce(N);
  var width = 2 * template[0].length + 1;

  [ 1, 2 ].forEach((n) {
    for (var i = 0; i < template.length; i++) {
      var string = center(List<String>.generate(n, (index) => template[i]).join(' '), width);

      if ((n == 1) && (i == 0)) {
        string = '.' + string.substring(1);
      }

      print(chomp(string));
    }
  });
}
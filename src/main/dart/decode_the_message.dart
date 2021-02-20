import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

String encode(int n, String base) {
  var result = '';

  do {
    var remainder = n % base.length;

    result = result + base[remainder];

    n = (n - remainder) ~/ base.length;
    n--;
  } while (n >= 0);

  return result;
}

void main() {
  var encoded = int.parse(stdin.readLineSync());
  var alphabet = stdin.readLineSync();

  trace("${encoded}");
  trace("${alphabet}");

  print(encode(encoded, alphabet));
}
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

var base = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

String encode(int n) {
  var result = '';

  do {
    n--;
    var remainder = n % base.length;

    result = base[remainder] + result;

    n = (n - remainder) ~/ base.length;
  } while (n > 0);

  return result;
}

int decode(String string) {
  var result = 0;

  while (!string.isEmpty) {
    result = (result * base.length) + base.indexOf(string[0]) + 1;

    string = string.substring(1);
  }

  return result;
}

void main() {
  var n = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(' ');

  print(inputs.map((s) {
    if (int.tryParse(s) != null) {
      return encode(int.parse(s));
    }

    return decode(s);
  }).join(' '));
}
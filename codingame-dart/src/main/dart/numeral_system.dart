import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

String baseOf(int radix) => "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(0, radix);

int parse(String string, int radix) {
  var value = 0, base = baseOf(radix);

  for (var i = 0; i < string.length; i++) {
    value = (value) * radix + base.indexOf(string[i]);
  }

  trace("${string} in base ${radix} = ${value} in base 10");

  return value;
}

void main() {
  var array = stdin.readLineSync().split(RegExp('[+=]'));
  var x = array[0], y = array[1], z = array[2];

  trace("${x}+${y}=${z}");

  var base36 = baseOf(36);

  // Highest digit ?
  var minRadix = "${x}${y}${z}".split('').map((c) => base36.indexOf(c)).reduce((value, element) => max(value, element)) + 1;

  for (var radix = minRadix; radix <= 36; radix++) {
    if (parse(x, radix) + parse(y, radix) == parse(z, radix)) {
      print('${radix}');
      break;
    }
  }
}
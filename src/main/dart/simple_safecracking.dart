import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

int decode(String string) {
  // Because of the phrase used for inferring the transposition, the digits will
  // always be decoded the same way
  switch (string) {
    case 'one':
      return 1;
    case 't*o':
      return 2;
    case 'th*ee':
      return 3;
    case 'fo**':
      return 4;
    case 'fi*e':
      return 5;
    case 'si*':
      return 6;
    case 'se*en':
      return 7;
    case 'ei*ht':
      return 8;
    case 'nine':
      return 9;
    case '*e*o':
      return 0;
    default:
      throw "Unexpected value: ${string}";
  }
}

Map<String, String> getDecodingMap(String encoded) {
  var map = Map<String, String>(), message = 'The safe combination is';

  // Infer the transposition
  for (var n = 0; n < encoded.length; n++) {
    map[encoded[n]] = message[n];
  }

  // Don't transpose the following characters
  [ ' ', '-', ':' ].forEach((char) {
    map[char] = char;
  });

  return map;
}

void main() {
  var message = stdin.readLineSync();

  trace("${message}");

  var left = message.substring(0, message.indexOf(':'));
  var right = message.substring(message.indexOf(':') + 1).trim();

  var decodingMap = getDecodingMap(left);
  var decoded = right.split('').map((char) => decodingMap[char] ?? '*').join();

  trace("${decoded}");

  print(decoded.split('-').map((s) => decode(s)).join(''));
}
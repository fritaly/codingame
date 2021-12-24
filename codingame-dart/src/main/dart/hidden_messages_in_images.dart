import 'dart:io';

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var width = int.parse(inputs[0]);
  var height = int.parse(inputs[1]);

  var pixels = List<int>();

  for (int i = 0; i < height; i++) {
    // Extract the ASCII code associated to the characters
    pixels.addAll(stdin.readLineSync().split(' ').map((e) => int.parse(e)));
  }
  
  var binary = pixels.map((e) {
    // Convert the ASCII code to binary and extract the least significant bit
    var binary = e.toRadixString(2);
    return binary.substring(binary.length - 1);
  }).join('');

  var result = Iterable<int>.generate((binary.length / 8).toInt())
      .map((n) => binary.substring(n * 8, (n + 1) * 8)) // Extract the 8 bits for the nth byte
      .map((e) => String.fromCharCode(int.parse(e, radix: 2))) // Turn the 8 bits to a character
      .join(); // Aggregate all the characters decoded

  print('${result}');
}
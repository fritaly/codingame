import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

String search(String string, int leftIndex, int rightIndex) {
  var offset = 1;

  while ((leftIndex - offset >= 0) && (rightIndex + offset < string.length)) {
    var left = string[leftIndex - offset], right = string[rightIndex + offset];

    if (left != right) {
      break;
    }

    offset++;
  }

  offset--;

  return string.substring(leftIndex - offset, rightIndex + offset + 1);
}

void main() {
  var string = stdin.readLineSync();

  trace("${string}");

  var solutions = [ '' ];

  for (var n = 0; n < string.length; n++) {
    var palindrome1 = search(string, n, n);

    trace("${palindrome1}");

    if (palindrome1.length > solutions[0].length) {
      solutions = [ palindrome1 ];
    } else if (palindrome1.length == solutions[0].length) {
      solutions.add(palindrome1);
    }

    if ((n + 1 < string.length) && (string[n] == string[n + 1])) {
      var palindrome2 = search(string, n, n + 1);

      trace("${palindrome2}");

      if (palindrome2.length > solutions[0].length) {
        solutions = [ palindrome2 ];
      } else if (palindrome2.length == solutions[0].length) {
        solutions.add(palindrome2);
      }
    }
  }

  print(solutions.join('\n'));
}
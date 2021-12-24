import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var lengths = <int>[];

  for (int i = 0; i < N; i++) {
    lengths.add(int.parse(stdin.readLineSync()));
  }

  trace("${lengths.join('\n')}");

  var longestSequence = <int>[];

  for (var i = 0; i < lengths.length; i++) {
    for (var j = i + 1; j < lengths.length; j++) {
      var delta = lengths[j] - lengths[i], sequence = [ lengths[i] ];

      // Start the search at index j
      var current = lengths[i], index = i;

      while (true)  {
        current += delta;
        index = lengths.indexOf(current, index + 1);

        if (index == -1) {
          break;
        }

        sequence.add(current);
      }

      if (sequence.length > longestSequence.length) {
        longestSequence = sequence;

        trace("New longest sequence: ${longestSequence}");
      }
    }
  }

  trace("${longestSequence}");

  print(longestSequence.length);
}
import 'dart:io';

void main() {
  var inputs = stdin.readLineSync().split(' ');
  var height = int.parse(inputs[0]);
  var width = int.parse(inputs[1]);

  var grid = List.generate(height, (index) => List.generate(width, (index) => false));

  for (int y = 0; y < height; y++) {
    var chunks = stdin.readLineSync().split(' ');

    stderr.writeln('${chunks.join(' ')}');

    for (int x = 0; x < width; x++) {
      // Just store whether the integer is positive
      grid[y][x] = (int.parse(chunks[x]) > 0);
    }
  }

  // Sequence of signs to check
  var sequence = <bool>[];

  for (int y = 0; y < height; y++) {
    var chunks = stdin.readLineSync().split(' ');

    stderr.writeln('${chunks.join(' ')}');

    for (int x = 0; x < width; x++) {
      if (chunks[x] == 'X') {
        // Retrieve the sign for the cell (x,y) and add it to the sequence
        sequence.add(grid[y][x]);
      }
    }
  }

  stderr.writeln('Sequence: ${sequence}');

  var valid = true;

  if (!sequence.isEmpty) {
    var expected = sequence[0];

    for (bool b in sequence) {
      if (b != expected) {
        valid = false;
        break;
      }

      expected = !expected;
    }
  } else {
    // Print "true" when the sequence is empty
  }

  print(valid);
}

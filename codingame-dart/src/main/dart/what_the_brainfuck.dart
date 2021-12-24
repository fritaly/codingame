import 'dart:collection';
import 'dart:io';

void trace(String message) {
  if (true) {
    stderr.writeln(message);
  }
}

bool checkSyntax(String program) {
  assert (program != null);

  var stack = Queue<String>();

  for (int n=0 ; n < program.length ; n++) {
    var char = program[n];

    if (char == '[') {
      stack.addLast(char);
    } else if (char == ']') {
      if (stack.isEmpty || (stack.removeLast() != '[')) {
        return false;
      }
    }
  }

  return stack.isEmpty;
}

void main() {
  var strings = stdin.readLineSync().split(' ');
  var L = int.parse(strings[0]);
  var S = int.parse(strings[1]);
  var N = int.parse(strings[2]);

  stderr.writeln("${strings.join(' ')}");

  var array = List.generate(S, (index) => 0);
  var arrayPointer = 0;
  var program = "";

  for (int i = 0; i < L; i++) {
    // A line of the Brainfuck program
    var line = stdin.readLineSync();

    stderr.writeln("${line}");

    program += line;
  }

  var inputs = <int>[];

  for (int i = 0; i < N; i++) {
    // An integer input to the Brainfuck program
    var value = int.parse(stdin.readLineSync());

    stderr.writeln("${value}");

    inputs.add(value);
  }

  // Check the syntax
  if (!checkSyntax(program)) {
    print('SYNTAX ERROR');

    return;
  }

  // Cursor pointing to the current instruction in the program
  var instructionPointer = 0;

  var output = "";

  try {
    while (instructionPointer < program.length) {
      var c = program[instructionPointer];

      switch (c) {
        case '>':
          arrayPointer++;
          instructionPointer++;
          trace("Array pointer: ${arrayPointer - 1} -> ${arrayPointer}");

          if (arrayPointer > array.length - 1) {
            throw "POINTER OUT OF BOUNDS";
          }
          break;
        case '<':
          arrayPointer--;
          instructionPointer++;
          trace("Array pointer: ${arrayPointer + 1} -> ${arrayPointer}");

          if (arrayPointer < 0) {
            throw "POINTER OUT OF BOUNDS";
          }
          break;
        case '+':
          var backup = array[arrayPointer];
          array[arrayPointer] = array[arrayPointer] + 1;
          instructionPointer++;
          trace("Array[${arrayPointer}]: ${backup} -> ${array[arrayPointer]}");

          if (array[arrayPointer] > 255) {
            throw "INCORRECT VALUE";
          }
          break;
        case '-':
          var backup = array[arrayPointer];
          array[arrayPointer] = array[arrayPointer] - 1;
          instructionPointer++;
          trace("Array[${arrayPointer}]: ${backup} -> ${array[arrayPointer]}");

          if (array[arrayPointer] < 0) {
            throw "INCORRECT VALUE";
          }
          break;
        case '.':
          output += String.fromCharCode(array[arrayPointer]);
          instructionPointer++;
          trace("Output: ${output}");
          break;
        case ',':
          array[arrayPointer] = inputs.removeAt(0);
          instructionPointer++;
          trace("Array[${arrayPointer}] = ${array[arrayPointer]}");
          break;
        case '[':
        // Jump to the instruction after the corresponding ']' if the pointed
        // cell's value is 0.
          trace("Detected '['. Array[${arrayPointer}] = ${array[arrayPointer]}");

          if (array[arrayPointer] == 0) {
            // Find the corresponding closing ']' in the program
            var stack = Queue<String>.from([ '[' ]);

            var i = instructionPointer + 1;

            while (!stack.isEmpty) {
              var instruction = program.substring(i, i+1);

              if (instruction == '[') {
                stack.addLast(instruction);
              } else if (instruction == ']') {
                if (stack.last == '[') {
                  // Only pop from the stack if the character is '['
                  stack.removeLast();
                } else {
                  throw "SYNTAX ERROR";
                }
              } else {
                // Ignore the character
              }

              i++;
            }

            if (!stack.isEmpty) {
              throw "SYNTAX ERROR";
            }

            // Move the pointer to the instruction after the closing ']'
            instructionPointer = i + 1;
          } else {
            instructionPointer++;
          }
          break;
        case ']':
        // Go back to the instruction after the corresponding '[' if the
        // pointed cell's value is different from 0.
          if (array[arrayPointer] != 0) {
            // Find the corresponding opening '[' in the program
            var stack = Queue<String>.from([ ']' ]);

            var i = instructionPointer - 1;

            while (!stack.isEmpty) {
              var instruction = program.substring(i, i+1);

              if (instruction == ']') {
                stack.addLast(instruction);
              } else if (instruction == '[') {
                if (stack.last == ']') {
                  // Only pop from the stack if the character is ']'
                  stack.removeLast();
                } else {
                  throw "SYNTAX ERROR";
                }
              } else {
                // Ignore the character
              }

              i--;
            }

            if (!stack.isEmpty) {
              throw "SYNTAX ERROR";
            }

            // Move the pointer to the instruction after the closing ']'
            instructionPointer = i + 1;
          } else {
            instructionPointer++;
          }
          break;
        default:
          // Ignore the character
          instructionPointer++;
          break;
      }
    }

    print(output);
  } catch (e) {
    print(e);
  }
}
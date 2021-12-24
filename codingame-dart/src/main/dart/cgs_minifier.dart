import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var output = new StringBuffer();

  var mappings = Map<String, String>();

  for (int i = 0; i < N; i++) {
    var line = stdin.readLineSync();

    trace("${line}");

    var n=0;

    while (n < line.length) {
      var char = line[n];

      if (char == ' ') {
        // Skip the blank (outside of strings)
        n++;
      } else if (char == '\$') {
        // Detected the start of a variable name, find the closing $
        var endIndex = line.indexOf('\$', n+1);
        var name = line.substring(n, endIndex + 1);

        trace("Variable ${name} detected");

        if (!mappings.containsKey(name)) {
          mappings[name] = '\$${String.fromCharCode('a'.codeUnitAt(0) + mappings.length)}\$';
        }

        output.write('${mappings[name]}');

        n += name.length;
      } else if (char == '\'') {
        // Detected the start of a string, copy the string as is to the output
        do {
          output.write(line[n++]);
        } while (line[n] != '\'');

        output.write('\'');
        n++;
      } else {
        output.write(line[n++]);
      }
    }
  }

  print(output);
}
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var words = List<String>();

  for (int i = 0; i < N; i++) {
    words.add(stdin.readLineSync());
  }

  trace("${words.join('\n')}");

  for (int i = 0; i < N; i++) {
    var word = words[i];

    var prefix = "";
    var matches = Set<String>.from(words);

    for (var j = 0; j < word.length; j++) {
      prefix += word[j];

      matches = matches.where((word) => word.startsWith(prefix)).toSet();

      if (matches.length == 1) {
        break;
      }
    }

    print(prefix);
  }
}
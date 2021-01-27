import 'dart:io';

void trace(String message) {
  if (true) {
    stderr.writeln(message);
  }
}

void main() {
  var X = stdin.readLineSync();
  var Y = stdin.readLineSync();

  trace("${X}");
  trace("${Y}");

  var mappings = Map<String, Set<String>>();

  for (int i = 0; i < X.length ; i++) {
    var c1 = X[i];
    var c2 = Y[i];

    if (!mappings.containsKey(c1)) {
      mappings[c1] = Set<String>();
    }

    mappings[c1].add(c2);
  }

  trace("${mappings}");

  // Remove all the mappings where a letter maps to itself
  mappings.removeWhere((key, value) => (value.length == 1) && value.contains(key));

  trace("${mappings}");

  if (mappings.isEmpty) {
    print("NONE");
  } else if (mappings.entries.any((entry) => entry.value.length > 1)) {
    print("CAN'T");
  } else {
    mappings.entries.forEach((entry) { 
      print("${entry.key}->${entry.value.single}");
    });
  }
}
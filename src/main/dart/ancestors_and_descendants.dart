import 'dart:io';

class Person {
  final String name;
  List<Person> children = [];
  Person parent;

  Person(this.name);

  Iterable<Person> ancestors() {
    var result = <Person>[ ];
    var current = this;

    while (current != null) {
      result.add(current);

      current = current.parent;
    }

    return result.reversed;
  }

  void dump() {
    if (children.isEmpty) {
      print(ancestors().map((e) => e.name).join(' > '));
    } else {
      children.forEach((child) {
        child.dump();
      });
    }
  }
}

void main() {
  var count = int.parse(stdin.readLineSync());

  var array = List<Person>(count);
  var roots = <Person>[];

  for (int i = 0; i < count; i++) {
    var line = stdin.readLineSync();

    var depth = 0;

    while (line.startsWith(".")) {
      line = line.substring(1);
      depth++;
    }

    var person = Person(line);

    array[depth] = person;

    if (depth > 0) {
      var parent = array[depth - 1];

      person.parent = parent;
      parent.children.add(person);
    } else {
      roots.add(person);
    }
  }

  roots.forEach((root) {
    root.dump();
  });
}
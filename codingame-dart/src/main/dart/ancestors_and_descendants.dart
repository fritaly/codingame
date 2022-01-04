/*
 * Copyright 2015-2022, Francois Ritaly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
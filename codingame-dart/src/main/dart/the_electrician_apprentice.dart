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
import 'dart:collection';
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

/// An electrical item
abstract class Item {

  /// Tells whether the item is on or off
  bool get on;
}

/// An electrical switch
class Switch extends Item {
  final String name;
  bool _on = false;

  Switch(this.name);

  @override
  bool get on => _on;

  /// Toggles the switch
  void toggle() {
    _on = !_on;
  }

  @override
  String toString() => "Switch[${name}, ${on}]";
}

/// A circuit where elements are mounted in parallel
class Parallel extends Item {
  final List<Item> _items = [];

  Parallel();

  @override
  bool get on => _items.any((element) => element.on);
}

/// A circuit where elements are mounted in serial
class Serial extends Item {
  final List<Item> _items = [];

  Serial();

  @override
  bool get on => _items.every((element) => element.on);
}

void main() {
  var count = int.parse(stdin.readLineSync());

  trace("${count}");

  var items = Map<String, Item>(), circuits = Map<String, Item>();

  for (int i = 0; i < count; i++) {
    var wiring = stdin.readLineSync();

    trace("${wiring}");

    var strings = wiring.split(' ');
    var name = strings[0];
    var stack = Queue<Item>();

    for(var j = 1; j < strings.length; j++) {
      var string = strings[j];

      if (string == '=') {
        var parallel = Parallel();

        if (!stack.isEmpty) {
          parallel._items.add(stack.removeLast());
        }

        stack.add(parallel);
      } else if (string == '-') {
        var serial = Serial();

        if (!stack.isEmpty) {
          serial._items.add(stack.removeLast());
        }

        stack.add(serial);
      } else {
        var item = items.putIfAbsent(string, () => Switch(string));

        if (stack.isEmpty) {
          throw "Unexpected state: ${stack}";
        }

        var top = stack.last;

        if (top is Parallel) {
          top._items.add(item);
        } else if (top is Serial) {
          top._items.add(item);
        } else {
          throw "Unexpected state: ${stack}";
        }
      }
    }

    var circuit = stack.removeLast();

    items[name] = circuit;
    circuits[name] = circuit;
  }

  var switchCount = int.parse(stdin.readLineSync());

  trace("${switchCount}");

  for (int i = 0; i < switchCount; i++) {
    var name = stdin.readLineSync();

    trace("${name}");

    (items[name] as Switch).toggle();
  }

  for (var entry in circuits.entries) {
    print("${entry.key} is ${entry.value.on ? 'ON' : 'OFF'}");
  }
}
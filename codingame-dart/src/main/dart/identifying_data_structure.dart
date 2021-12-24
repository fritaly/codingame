import 'dart:collection';
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

abstract class DataStructure {
  void add(int n);
  int remove();
  String name();
}

class QueueDS implements DataStructure {
  final Queue _queue = Queue<int>();

  @override
  String name() {
    return "queue";
  }

  @override
  void add(int n) {
    _queue.addLast(n);
  }

  @override
  int remove() {
    return _queue.removeFirst();
  }
}

class StackDS implements DataStructure {
  final Queue _stack = Queue<int>();

  @override
  String name() {
    return "stack";
  }

  @override
  void add(int n) {
    _stack.addLast(n);
  }

  @override
  int remove() {
    return _stack.removeLast();
  }
}

class PriorityQueueDS implements DataStructure {
  final List _list = List<int>();

  @override
  String name() {
    return "priority queue";
  }

  @override
  void add(int n) {
    _list.add(n);
    _list.sort();
  }

  @override
  int remove() {
    return _list.removeLast();
  }
}


void main() {
  var N = int.parse(stdin.readLineSync());

  trace('${N}');

  var lines = <String>[];

  for (int i = 0; i < N; i++) {
    lines.add(stdin.readLineSync());
  }

  trace('${lines.join('\n')}');

  for (int i = 0; i < lines.length; i++) {
    var commands = lines[i].split(' ');

    var dataStructures = [ QueueDS(), StackDS(), PriorityQueueDS() ];

    for (int n = 0; n < commands.length; n++) {
      var command = commands[n];

      trace('Parsing ${command} ...');

      if (command[0] == 'i') {
        var value = int.parse(command.substring(1));

        dataStructures.forEach((dataStructure) {
          dataStructure.add(value);
        });
      } else {
        var value = int.parse(command.substring(1));

        dataStructures.removeWhere((dataStructure) {
          try {
            return dataStructure.remove() != value;
          } catch (e) {
            return true;
          }
        });
      }
    }

    if (dataStructures.isEmpty) {
      print('mystery');
    } else if (dataStructures.length == 1) {
      print(dataStructures.single.name());
    } else {
      print('unsure');
    }
  }
}
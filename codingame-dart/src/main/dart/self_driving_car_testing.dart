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

void trace(String message) {
  stderr.writeln("${message}");
}

class Command {
  int _countdown;
  final String command;

  Command(this._countdown, this.command);

  bool over() {
    return (--_countdown == 0);
  }

  factory Command.parse(String string) {
    var command = string[string.length - 1];
    var countdown = int.parse(string.substring(0, string.length - 1));

    return Command(countdown, command);
  }
}

void main() {
  var N = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(';');

  trace("${N}");
  trace("${inputs.join(';')}");

  // The position is 1-indexed
  var position = int.parse(inputs[0]) - 1;

  var commands = <Command>[];

  for (int i = 1; i < inputs.length; i++) {
    commands.add(Command.parse(inputs[i]));
  }

  var road = <String>[];

  for (int i = 0; i < N; i++) {
    var inputs = stdin.readLineSync().split(';');

    trace("${inputs.join(';')}");

    var count = int.parse(inputs[0]);
    var pattern = inputs[1];

    for (int j = 0; j < count; j++) {
      road.add(pattern);
    }
  }

  var command = commands.removeAt(0);

  while (command != null) {
    switch (command.command) {
      case 'S':
        break;
      case 'L':
        position--;
        break;
      case 'R':
        position++;
        break;
      default:
        throw "Unexpected command: ${command.command}";
    }

    if (command.over()) {
      if (!commands.isEmpty) {
        command = commands.removeAt(0);
      } else {
        command = null;
      }
    }

    var list = road.removeAt(0).split('');
    list[position] = '#';

    print(list.join(''));
  }
}
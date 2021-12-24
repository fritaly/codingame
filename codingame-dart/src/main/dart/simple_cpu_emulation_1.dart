/*
 * Copyright 2021, Francois Ritaly
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

void main() {
  var program = stdin.readLineSync();

  trace("${program}");

  var registers = { '0': 0, '1': 0, '2': 0 };

  while (!program.isEmpty) {
    var instruction = program.substring(0, 4);

    trace("${instruction}");

    if (instruction == '0000') {
      break;
    } else if (instruction.startsWith('1')) {
      var registerId = instruction[1];
      var value = int.parse(instruction.substring(2), radix: 16);
      registers[registerId] = value;
    } else if (instruction.startsWith('20')) {
      // ADD x, y - Add the value of register y to register x and store the value
      // in register x, if the result is greater than 8 bits set register 2 to 1
      // else 0. Only the lowest 8-bits are stored in register x.
      var x = instruction[2], y = instruction[3];
      var result = registers[x] + registers[y];
      registers[x] = result & 0xFF;
      registers['2'] = (result > 255) ? 1 : 0;
    } else if (instruction.startsWith('30')) {
      // SUB x, y - Subtract the value of register y from register x and store
      // the value in register x, if register x < register y set register 2 to 1
      // else 0.
      var x = instruction[2], y = instruction[3];
      var result = registers[x] - registers[y];
      registers[x] = result & 0xFF;
      registers['2'] = (result < 0) ? 1 : 0;
    } else if (instruction.startsWith('40')) {
      // OR x,y – Bitwise OR on value of register x and register y, store value
      // in register x
      var x = instruction[2], y = instruction[3];
      registers[x] = registers[x] | registers[y];
    } else if (instruction.startsWith('50')) {
      // AND x,y – Bitwise AND on value of register x and register y, store
      // value in register x
      var x = instruction[2], y = instruction[3];
      registers[x] = registers[x] & registers[y];
    } else if (instruction.startsWith('60')) {
      // XOR x, y - Bitwise XOR on value of register x and register y, store
      // value in register x
      var x = instruction[2], y = instruction[3];
      registers[x] = registers[x] ^ registers[y];
    } else if (instruction.startsWith('7')) {
      // SE k, nn – Skip next instruction if value of register k equals nn
      var registerId = instruction[1];
      var value = int.parse(instruction.substring(2), radix: 16);

      if (registers[registerId] == value) {
        program = program.substring(4);
      }
    } else if (instruction.startsWith('8')) {
      // SNE k, nn – Skip next instruction if value of register k is not equal
      // to nn
      var registerId = instruction[1];
      var value = int.parse(instruction.substring(2), radix: 16);

      if (registers[registerId] != value) {
        program = program.substring(4);
      }
    } else if (instruction.startsWith('90')) {
      // SE x,y – Skip next instruction if value of register x equals value of
      // register y
      var x = instruction[2], y = instruction[3];

      if (registers[x] == registers[y]) {
        program = program.substring(4);
      }
    } else if (instruction.startsWith('A0')) {
      // SNE x,y - Skip next instruction if value of register x is not equal to
      // the value of register y
      var x = instruction[2], y = instruction[3];

      if (registers[x] != registers[y]) {
        program = program.substring(4);
      }
    } else {
      throw "Unexpected instruction: ${instruction}";
    }

    program = program.substring(4);
  }

  print('${registers['0']} ${registers['1']} ${registers['2']}');
}
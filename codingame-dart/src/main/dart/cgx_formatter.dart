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

abstract class Element {
  String indent(int depth) => '    ' * depth;
  String format(int depth);
}

class Block extends Element {
  final List<Element> elements;

  Block(this.elements);

  @override
  String toString() => "Block[${elements.join(';')}]";

  @override
  String format(int depth) {
    return "${indent(depth)}(${elements.map((e) => "\n${e.format(depth + 1)}").join(';')}\n${indent(depth)})";
  }
}

class Primitive<T> extends Element {
  final T value;

  Primitive(this.value);

  @override
  String toString() => "Primitive<${value.runtimeType}>";

  @override
  String format(int depth) {
    if (value is num || value is bool) {
      return "${indent(depth)}${value}";
    }
    if (value is String) {
      return "${indent(depth)}'${value}'";
    }
    if (value == null) {
      return "${indent(depth)}null";
    }

    throw "Unexpected value: ${value}";
  }
}

class KeyValue extends Element {
  final String key;
  final Element value;

  KeyValue(this.key, this.value): assert ((value is Block) || (value is Primitive));

  @override
  String toString() => "KeyValue[${key}=${value}]";

  @override
  String format(int depth) {
    if (value is Block) {
      return "${indent(depth)}'${key}'=\n${value.format(depth)}";
    }

    return "${indent(depth)}'${key}'=${value.format(0)}";
  }
}

/// Tokens are only used during the parsing phase
abstract class Token extends Element {

  @override
  String format(int depth) {
    throw UnimplementedError();
  }

  @override
  String toString() => "${runtimeType}";
}

class OpenPar extends Token {}
class ClosePar extends Token {}
class Equal extends Token {}

/// Tells whether the given string is numeric
bool isNumeric(String s) => (int.tryParse(s) != null);

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var lines = <String>[];

  for (int i = 0; i < N; i++) {
    lines.add(stdin.readLineSync());
  }

  trace("${lines.join('\n')}");

  var input = lines.join('');
  var index = 0;
  var inString = false, stringBuffer = StringBuffer();
  var stack = List<Element>();

  while (index < input.length) {
    var char = input[index];

    if (inString) {
      if (char == "'") {
        stack.add(new Primitive(stringBuffer.toString()));
        stringBuffer.clear();
        inString = false;
      } else {
        stringBuffer.write(char);
      }

      index++;
    } else {
      if (char == "'") {
        inString = true;
        index++;
      } else if (char == "(") {
        stack.add(OpenPar());
        index++;
      } else if (char == ")") {
        stack.add(ClosePar());
        index++;
      } else if (char == "=") {
        stack.add(Equal());
        index++;
      } else if (char == ";") {
        // Ignore the character
        index++;
      } else if (isNumeric(char)) {
        // Start of a number, search where it ends
        var endIndex = index + 1;
        
        while ((endIndex < input.length) && isNumeric(input[endIndex])) {
          endIndex++;
        }

        stack.add(Primitive(int.parse(input.substring(index, endIndex))));

        index = endIndex;
      } else if ((char == 't') && (input.substring(index, index + 4)) == 'true') {
        stack.add(Primitive(true));
        index += 4;
      } else if ((char == 'f') && (input.substring(index, index + 5)) == 'false') {
        stack.add(Primitive(false));
        index += 5;
      } else if (char == ' ' || char == '\u200b' || char == '\u0009') {
        // Ignore the whitespace
        index++;
      } else {
        throw "Unexpected char at index ${index}: '${char}' (${char.codeUnitAt(0)})";
      }
    }

    var reduced = true;

    // Reduce the stack as much as possible
    while (!stack.isEmpty && reduced) {
      reduced = false;

      trace("Stack Before: ${stack}");

      var last = stack.last;

      if (last is ClosePar) {
        // Reduce a block. Search the corresponding OpenPar
        stack.removeLast(); // ClosePar

        var elements = <Element>[];

        while (!(stack.last is OpenPar)) {
          elements.insert(0, stack.removeLast());
        }

        stack.removeLast(); // OpenPar
        stack.add(new Block(elements));

        reduced = true;
      } else if (stack.length >= 3) {
        var last3 = stack.getRange(stack.length - 3, stack.length).toList();
        var first = last3[0], second = last3[1], third = last3[2];

        if ((first is Primitive<String>) && (second is Equal) && ((third is Element) && !(third is Token))) {
          // Reduce a key / value
          stack.removeLast();
          stack.removeLast();
          stack.removeLast();
          stack.add(KeyValue(first.value, third));

          reduced = true;
        }
      }

      trace("Stack After: ${stack}");
    }
  }

  print(stack.removeLast().format(0));
}
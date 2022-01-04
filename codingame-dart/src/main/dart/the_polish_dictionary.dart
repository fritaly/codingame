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

abstract class Node {
  String format();
}

/// Operand is used for representing numeric values and variables
class Operand extends Node {
  final String value;

  Operand(this.value);

  @override
  String format() {
    return "${value}";
  }

  @override
  String toString() {
    return "Operand[${value}]";
  }
}

enum Precedence {
  ADDITION, MULTIPLICATION
}

int comparePrecedence(Precedence precedence1, Precedence precedence2) {
  if (precedence1 == precedence2) {
    return 0;
  }
  if (precedence1 == Precedence.MULTIPLICATION) {
    return 1;
  }

  return -1;
}

abstract class Operation extends Node {
  final Node left, right;

  Operation(this.left, this.right);

  String get symbol;

  Precedence get precedence;

  @override
  String format() {
    var list = <String>[];

    if (left is Operation) {
      if (comparePrecedence((left as Operation).precedence, this.precedence) < 0) {
        list.add("(${left.format()})");
      } else {
        list.add(left.format());
      }
    } else {
      list.add(left.format());
    }

    list.add(symbol);

    if (right is Operation) {
      if ((symbol == '-') || (symbol == '/') || comparePrecedence(this.precedence, (right as Operation).precedence) > 0) {
        list.add("(${right.format()})");
      } else {
        list.add(right.format());
      }
    } else {
      list.add(right.format());
    }

    return list.join(' ');
  }
}

class Addition extends Operation {
  Addition(Node left, Node right) : super(left, right);

  @override
  String get symbol => '+';

  @override
  Precedence get precedence => Precedence.ADDITION;
}

class Multiplication extends Operation {
  Multiplication(Node left, Node right) : super(left, right);

  @override
  String get symbol => '*';

  @override
  Precedence get precedence => Precedence.MULTIPLICATION;
}

class Division extends Operation {
  Division(Node left, Node right) : super(left, right);

  @override
  String get symbol => '/';

  @override
  Precedence get precedence => Precedence.MULTIPLICATION;
}

class Substraction extends Operation {
  Substraction(Node left, Node right) : super(left, right);

  @override
  String get symbol => '-';

  @override
  Precedence get precedence => Precedence.ADDITION;
}

void main() {
  var N = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(' ');

  trace("${N}");
  trace("${inputs.join(' ')}");

  var stack = Queue<Node>();

  for (int i = 0; i < N; i++) {
    var member = inputs[i];

    if (int.tryParse(member) != null) {
      // Numeric value
      stack.addLast(Operand(member));
    } else if (member == '*') {
      var right = stack.removeLast();
      var left = stack.removeLast();

      stack.addLast(Multiplication(left, right));
    } else if (member == '+') {
      var right = stack.removeLast();
      var left = stack.removeLast();

      stack.addLast(Addition(left, right));
    } else if (member == '/') {
      var right = stack.removeLast();
      var left = stack.removeLast();

      stack.addLast(Division(left, right));
    } else if (member == '-') {
      var right = stack.removeLast();
      var left = stack.removeLast();

      stack.addLast(Substraction(left, right));
    } else {
      // That's a variable
      stack.addLast(Operand(member));
    }
  }

  print(stack.removeLast().format());
}
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
import 'dart:math';

class Node {
  final String name;
  final List<Node> children = [];
  int _depth = -1;

  Node(this.name);
  
  int depth() {
    if (_depth == -1) {
      if (children.isEmpty) {
        // A node with no children has a depth of 1
        _depth = 1;
      } else {
        _depth = 1 + children.map((e) => e.depth()).reduce(max);
      }
    }
    
    return _depth;
  }

  @override
  String toString() {
    return "Node[${name}, children: ${children.map((e) => e.name)}]";
  }
}

void main() {
  // the number of relationships of influence
  var n = int.parse(stdin.readLineSync());

  var nodes = Map<String, Node>();

  for (int i = 0; i < n; i++) {
    var inputs = stdin.readLineSync().split(' ');

    // a relationship of influence between two people (x influences y)
    var nameX = inputs[0];
    var nameY = inputs[1];

    if (!nodes.containsKey(nameX)) {
      nodes[nameX] = Node(nameX);
    }
    if (!nodes.containsKey(nameY)) {
      nodes[nameY] = Node(nameY);
    }

    nodes[nameX].children.add(nodes[nameY]);
  }

  // stderr.writeln("${nodes}");

  var result = nodes.values.reduce((value, element) => (value.depth() > element.depth()) ? value : element);

  // stderr.writeln("${result}");

  print(result.depth());
}
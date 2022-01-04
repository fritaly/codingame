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

class Grid {
  final List<String> rows;

  Grid(this.rows);

  int get height => rows.length;
  int get width => rows[0].length;

  bool isValidX(int x) => (0 <= x) && (x < width);
  bool isValidY(int y) => (0 <= y) && (y < height);
  bool isValid(int x, int y) => isValidX(x) && isValidY(y);

  String charAt(int x, int y) => rows[y][x];
}

class Graph {
  final Map<String, Node> nodes;

  Graph(this.nodes);

  bool contains(String word) {
    return nodes.values.any((n) => n.matches(word, Set()));
  }

  factory Graph.from(Grid grid) {
    var nodes = Map<String, Node>();

    // Create all the nodes first
    for (var y = 0; y < grid.height; y++) {
      for (var x = 0; x < grid.width; x++) {
        var char = grid.charAt(x, y);
        var id = "(${x},${y})";

        nodes[id] = Node(id, char);
      }
    }

    // Associate each node to its neighbors
    for (var y = 0; y < grid.height; y++) {
      for (var x = 0; x < grid.width; x++) {
        var node = nodes["(${x},${y})"];

        var ids = [
          "(${x - 1},${y - 1})",
          "(${x - 1},${y})",
          "(${x - 1},${y + 1})",
          "(${x},${y - 1})",
          "(${x},${y + 1})",
          "(${x + 1},${y - 1})",
          "(${x + 1},${y})",
          "(${x + 1},${y + 1})"
        ];

        for (var neighborId in ids) {
          var neighbor = nodes[neighborId];

          if (neighbor != null) {
            node.neighbors.add(neighbor);
          }
        }
      }
    }

    return Graph(nodes);
  }
}

class Node {
  final String id;
  final String char;
  final List<Node> neighbors = [];

  Node(this.id, this.char);

  @override
  int get hashCode {
    var hash = 17;
    hash = 37 * hash + id.hashCode;

    return hash;
  }

  bool matches(String word, Set<Node> visited) {
    if (word.isEmpty) {
      return true;
    }

    if (word[0] != char) {
      return false;
    }

    // Check the remaining letters
    var set = Set<Node>.from(visited);
    set.add(this);

    return neighbors.any((n) => !visited.contains(n) && n.matches(word.substring(1), set));
  }

  @override
  String toString() => "Node[${id}, ${char}, neighbors: ${neighbors.map((n) => n.char)}]";
}

void main() {
  var line1 = stdin.readLineSync();
  var line2 = stdin.readLineSync();
  var line3 = stdin.readLineSync();
  var line4 = stdin.readLineSync();
  var n = int.parse(stdin.readLineSync());

  trace("${line1}\n${line2}\n${line3}\n${line4}\n${n}");

  var words = <String>[];

  for (var i = 0; i < n; i++) {
    words.add(stdin.readLineSync());
  }

  trace("${words.join('\n')}");

  var grid = Grid([ line1, line2, line3, line4 ]);
  var graph = Graph.from(grid);

  for (var word in words) {
    print(graph.contains(word));
  }
}
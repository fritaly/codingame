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
  stderr.writeln(message);
}

class Castle {
  final List<String> grid;

  Castle(this.grid);

  int get width {
    return grid[0].length;
  }

  int get height {
    return grid.length;
  }

  String charAt(int x, int y) {
    return grid[y][x];
  }

  bool checkStability() {
    for (int y = 0; y < height ; y++) {
      for (int x = 0; x < width ; x++) {
        trace("Checking (${x},${y}) ...");

        if (!isStable(x, y)) {
          trace("(${x},${y}) is unstable !");
          return false;
        }
      }
    }

    return true;
  }

  /// Tells whether the element in (x,y) is stable
  bool isStable(int x, int y) {
    var char = charAt(x, y);

    if (char == '.') {
      return true;
    }

    if ((char == '/') || (char == '\\')) {
      if (x > 0) {
        // Check the char on the left
        var pattern = "${charAt(x-1, y)}${char}";

        if ((pattern == '//') || (pattern == '\\\\') || (pattern == '.\\')) {
          trace("1. (${x},${y}) is unstable because of its left char");

          return false;
        }
      }

      if (x < width - 1) {
        // Check the char on the right
        var pattern = "${char}${charAt(x+1, y)}";

        if (pattern == "/." || pattern == "//") {
          trace("2. (${x},${y}) is unstable because of its right char");

          // Example: "/." or "//"
          return false;
        }
        if (pattern == "\\\\") {
          trace("3. (${x},${y}) is unstable because of its left char");

          // Example: "/." or "//"
          return false;
        }
        if ((char != '.') && !isStable(x+1, y)) {
          trace("4. (${x},${y}) is unstable");

          return false;
        }
      }

      // Check the cards below
      if (y == height - 1) {
        // The char is located on the last line
        return true;
      } else if (char == '/') {
        // Left card below
        if (x == 0) {
          trace("5. (${x},${y}) is unstable (no left card below)");

          return false;
        } else if ((charAt(x - 1, y + 1) != '/') || !isStable(x - 1, y + 1)) {
          trace(
              "6. (${x},${y}) is unstable (incorrect left card below: ${charAt(
                  x - 1, y + 1)})");

          return false;
        }

        // Card below
        if ((charAt(x, y + 1) != '\\') || !isStable(x + 1, y + 1)) {
          trace("8. (${x},${y}) is unstable (incorrect card below)");

          return false;
        }

        return true;
      } else if (char == '\\') {
        // Card below
        if ((charAt(x, y+1) != '/') || !isStable(x+1, y+1)) {
          trace("9. (${x},${y}) is unstable (incorrect card below)");

          return false;
        }

        // Right card below
        if (x == width - 1) {
          trace("10. (${x},${y}) is unstable (no right card below)");

          return false;
        } else if ((charAt(x+1, y+1) != '\\') || !isStable(x-1, y+1)) {
          trace("11. (${x},${y}) is unstable (incorrect right card below: ${charAt(x+1, y+1)})");

          return false;
        }

        return true;
      }
    } else {
      throw "Unexpected element: ${char}";
    }
  }
}

void main() {
  var height = int.parse(stdin.readLineSync());

  trace("${height}");

  var grid = List<String>();

  for (int i = 0; i < height; i++) {
    grid.add(stdin.readLineSync());
  }

  trace("${grid.join('\n')}");

  var castle = new Castle(grid);

  print(castle.checkStability() ? 'STABLE' : 'UNSTABLE');
}
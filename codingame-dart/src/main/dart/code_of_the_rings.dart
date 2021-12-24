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

/// One zone connected to 2 other zones on the left and right
class Zone {

  Zone left, right;

  /// The rune associated to this zone
  final Rune rune = Rune(' ');

  /// Finds and returns (as a string) the shortest sequence of instructions to
  /// move from this zone to the given target zone
  String findSequenceTo(Zone target) {
    if (this == target) {
      // No need to move
      return "";
    }

    // Search on the both sides and return the shortest solution
    var leftDistance = _leftDistanceTo(target), rightDistance = _rightDistanceTo(target);

    return (leftDistance < rightDistance) ? "<" * leftDistance : ">" * rightDistance;
  }

  int _leftDistanceTo(Zone target) {
    var distance = 0;
    var current = this;

    while (current != target) {
      distance++;
      current = current.left;
    }

    return distance;
  }

  int _rightDistanceTo(Zone target) {
    var distance = 0;
    var current = this;

    while (current != target) {
      distance++;
      current = current.right;
    }

    return distance;
  }
}

class Rune {
  static final String VALUES = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ ';

  /// The rune's current character
  String _value;

  Rune(this._value);

  String get value => _value;
  void set value(String char) { _value = char; }

  /// Finds and returns (as a string) the shortest sequence of instructions to
  /// change this rune's current character into the given target character
  String findSequence(String character) {
    if (_value == character) {
      return ".";
    }

    // Search on both sides and return the shortest solution
    var leftDistance = _leftDistance(character), rightDistance = _rightDistance(character);

    return ((leftDistance < rightDistance) ? "-" * leftDistance : "+" * rightDistance) + ".";
  }

  int _leftDistance(String character) {
    var distance = 0;

    var index = VALUES.indexOf(_value);

    while (VALUES[index] != character) {
      index = (index + VALUES.length - 1) % VALUES.length;
      distance++;
    }

    return distance;
  }

  int _rightDistance(String character) {
    var distance = 0;

    var index = VALUES.indexOf(_value);

    while (VALUES[index] != character) {
      index = (index + 1) % VALUES.length;
      distance++;
    }

    return distance;
  }
}

void main() {
  var zones = <Zone>[];

  // Create and connect the zones in the forest
  for (int n = 0; n < 30; n++) {
    zones.add(Zone());

    if (n > 0) {
      zones[n - 1].right = zones[n];
      zones[n].left = zones[n - 1];
    }
  }

  zones[zones.length - 1].right = zones[0];
  zones[0].left = zones[zones.length - 1];

  var phrase = stdin.readLineSync();

  trace("${phrase}");

  // Buffer where the instructions are aggregated
  var instructions = StringBuffer();

  var current = zones[0];

  // Index of the character from the magic phrase being processed
  var index = 0;

  // Loop over the phrase's characters
  while (index < phrase.length) {
    var char = phrase[index++];

    // Find the shortest sequence of instructions to output the character
    String solution = null;
    Zone nextZone = null;

    // Evaluate the 30 zones
    for (var zone in zones) {
      // Identify the sequence to reach the zone and change the rune's character
      var sequence = current.findSequenceTo(zone) + zone.rune.findSequence(char);

      if ((solution == null) || (sequence.length < solution.length)) {
        // First solution or best solution found so far
        solution = sequence;
        nextZone = zone;

        trace("Found a new optimal solution: ${sequence}");
      }
    }

    trace("Found a solution: ${solution}");

    // Write the sequence identified
    instructions.write(solution);

    // Update the state of the current zone and the corresponding rune
    current = nextZone;
    current.rune.value = char;
  }

  print(instructions);
}
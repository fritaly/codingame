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
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

class Coordinates {
  final int x, y;

  const Coordinates(this.x, this.y);

  double distance(Coordinates other) {
    assert (other != null);

    var dx = this.x - other.x;
    var dy = this.y - other.y;

    return sqrt(dx * dx + dy * dy);
  }

  @override
  String toString() {
    return "(${x},${y})";
  }
}

List<Coordinates> findSolution(List<Coordinates> steps, List<Coordinates> remainder) {
  if (remainder.isEmpty) {
    return steps;
  }

  var current = steps.last;
  var next = null, minDistance = double.maxFinite;

  for (var coordinates in remainder) {
    var distance = current.distance(coordinates);

    if ((next == null) || (distance < minDistance)) {
      minDistance = distance;
      next = coordinates;
    }
  }

  var remainder2 = List<Coordinates>.from(remainder);
  remainder2.remove(next);

  return findSolution(steps + [ next ], remainder2);
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var allCoordinates = <Coordinates>[];

  for (var i = 0; i < N; i++) {
    var inputs = stdin.readLineSync().split(' ');

    allCoordinates.add(Coordinates(int.parse(inputs[0]), int.parse(inputs[1])));
  }

  trace("${allCoordinates.map((e) => "${e.x} ${e.y}").join('\n')}");

  // Close the loop by adding the start position at the end of the trip
  var steps = findSolution([ (allCoordinates[0]) ], allCoordinates.sublist(1)) + [ (allCoordinates[0]) ];

  var totalDistance = 0.0;

  for (var i = 1; i < steps.length; i++) {
    totalDistance += steps[i - 1].distance(steps[i]);
  }

  print(totalDistance.round());
}
import 'dart:collection';
import 'dart:io';
import 'dart:math';

enum Direction {
  CLOCKWISE,
  COUNTER_CLOCKWISE,
  NOT_MOVING
}

Direction oppositeOf(Direction direction) {
  switch (direction) {
    case Direction.CLOCKWISE:
      return Direction.COUNTER_CLOCKWISE;
    case Direction.COUNTER_CLOCKWISE:
      return Direction.CLOCKWISE;
    case Direction.NOT_MOVING:
      return Direction.NOT_MOVING;
    default:
      throw "Unexpected direction: ${direction}";
  }
}

void trace(String message) {
  stderr.writeln("${message}");
}

double distance(Gear gear1, Gear gear2) {
  var dx = gear2.x - gear1.x, dy = gear2.y - gear1.y;

  return sqrt((dx * dx) + (dy * dy));
}

class Gear {
  final String name;
  final int x, y, radius;

  Direction direction;

  List<Gear> neighbors = [];

  Gear(this.name, this.x, this.y, this.radius);

  bool touches(Gear other) {
    return distance(this, other) <= (this.radius + other.radius);
  }

  @override
  String toString() {
    return "Gear[${name}, (${x},${y}), radius: ${radius}, direction: ${direction}, neighbors: ${neighbors.map((e) => e.name).toList()}]";
  }
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var gears = <Gear>[];

  for (int i = 0; i < N; i++) {
    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    gears.add(Gear("#${i+1}", int.parse(inputs[0]), int.parse(inputs[1]), int.parse(inputs[2])));
  }

  // Connect the gears to each other
  for (int n = 0 ; n < gears.length ; n++) {
    var gear1 = gears[n];

    for (int p = n + 1 ; p < gears.length ; p++) {
      var gear2 = gears[p];

      if (gear1.touches(gear2)) {
        gear1.neighbors.add(gear2);
        gear2.neighbors.add(gear1);
      }
    }
  }

  trace("${gears}");

  // The first gear is driven in a clockwise direction...
  gears.first.direction = Direction.CLOCKWISE;

  var queue = Queue<Gear>.from([ gears.first ]);

  while (!queue.isEmpty) {
    trace("${queue}");

    var gear = queue.removeFirst();

    trace("Processing ${gear} ...");

    for (Gear neighbor in gear.neighbors) {
      if (neighbor.direction == null) {
        neighbor.direction = oppositeOf(gear.direction);
        queue.add(neighbor);
      } else if (neighbor.direction == oppositeOf(gear.direction)) {
        // All good
      } else {
        // Gear trying to rotate in 2 opposite directions
        neighbor.direction = Direction.NOT_MOVING;
        queue.add(neighbor);
      }
    }
  }

  var lastGear = gears.last;

  if (lastGear.neighbors.isEmpty) {
    print("NOT MOVING");
    return;
  }

  switch (lastGear.direction) {
    case Direction.COUNTER_CLOCKWISE:
      print("CCW");
      break;
    case Direction.CLOCKWISE:
      print("CW");
      break;
    case Direction.NOT_MOVING:
      print("NOT MOVING");
      break;
    default:
      throw "Unexpected direction: ${lastGear.direction}";
  }
}
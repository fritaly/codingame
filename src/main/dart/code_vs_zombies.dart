import 'dart:io';

import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

class Position {
  final int x, y;

  Position(this.x, this.y);

  double distance(Position target) {
    var dx = target.x - this.x;
    var dy = target.y - this.y;

    return sqrt(dx * dx + dy * dy);
  }

  @override
  bool operator ==(Object that) {
    if (that is Position) {
      return (this.x == that.x) && (this.y == that.y);
    }

    return false;
  }

  @override
  String toString() {
    return "(${x},${y})";
  }
}

abstract class Entity {
  final Position position;

  Entity(this.position);

  int get x => position.x;
  int get y => position.y;

  double distanceTo(Position target) => this.position.distance(target);
}

class Human extends Entity {
  final int id;

  Human(this.id, Position position): super(position);

  factory Human.read(Stdin stdin) {
    var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    return Human(inputs[0], Position(inputs[1], inputs[2]));
  }
}

class Zombie extends Entity {
  final int id;
  final Position nextPosition;

  Zombie(this.id, Position position, this.nextPosition): super(position);

  factory Zombie.read(Stdin stdin) {
    var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    return Zombie(inputs[0], Position(inputs[1], inputs[2]), Position(inputs[3], inputs[4]));
  }
}

Comparator<Entity> compareDistanceTo(Position position) {
  return (a, b) => a.distanceTo(position).compareTo(b.distanceTo(position));
}

class Pair implements Comparable<Pair> {
  final Human human;
  final Zombie zombie;

  Pair(this.human, this.zombie);

  double get distance => zombie.distanceTo(human.position);

  /// Returns the entity (that is the human or the zombie) who's the closest to
  /// the given position
  Entity closestTo(Position position) {
    var distanceToHuman = human.distanceTo(position);
    var distanceToZombie = zombie.distanceTo(position);

    return (distanceToZombie < distanceToHuman) ? zombie : human;
  }

  @override
  String toString() {
    return "Pair[human: ${human}, zombie: ${zombie}]";
  }

  @override
  int compareTo(Pair other) {
    return this.distance.compareTo(other.distance);
  }
}

void main() {

  while (true) {
    var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    var playerPosition = Position(inputs[0], inputs[1]);

    var humans = <Human>[];

    var humanCount = int.parse(stdin.readLineSync());

    for (int i = 0; i < humanCount; i++) {
      humans.add(Human.read(stdin));
    }

    var zombies = <Zombie>[];

    var zombieCount = int.parse(stdin.readLineSync());

    for (int i = 0; i < zombieCount; i++) {
      zombies.add(Zombie.read(stdin));
    }

    zombies.sort(compareDistanceTo(playerPosition));

    if (!zombies.isEmpty) {
      var nearestZombie = zombies[0];

      var distance = nearestZombie.distanceTo(playerPosition);

      trace("Distance to nearest zombie: ${distance.toStringAsFixed(0)}");
    }

    // Find the closest zombie to a human
    var pairs = <Pair>[];

    for (var zombie in zombies) {
      for (var human in humans) {
        pairs.add(Pair(human, zombie));
      }
    }

    pairs.sort();

    if (!pairs.isEmpty) {
      var selection = pairs[0];

      trace("Selection: ${selection}");

      // Who's the closest ? the human or the zombie ?
      var target = selection.closestTo(playerPosition);

      if (target is Zombie) {
        // Anticipate the next zombie move and go where he's heading
        print('${target.nextPosition.x} ${target.nextPosition.y}');
      } else {
        // Humans are static
        print('${target.x} ${target.y}');
      }
    } else {
      print('${playerPosition.x} ${playerPosition.y}');
    }
  }
}
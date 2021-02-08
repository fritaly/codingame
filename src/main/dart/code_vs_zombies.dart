import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

// ================ //
// === Position === //
// ================ //

class Position {
  final int x, y;

  Position(this.x, this.y);

  double distance(Position target) {
    var dx = target.x - this.x;
    var dy = target.y - this.y;

    return sqrt(dx * dx + dy * dy);
  }

  operator +(Vector vector) => Position(x + vector.x, y + vector.y);

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

// ============== //
// === Vector === //
// ============== //

class Vector {
  final int x, y;

  Vector(this.x, this.y);

  operator *(int n) => Vector(x * n, y * n);

  factory Vector.from(Position start, Position end) {
    return Vector(end.x - start.x, end.y - start.y);
  }

  @override
  String toString() {
    return "Vector(${x}, ${y})";
  }
}

abstract class Entity {
  final Position position;

  /// The entity's speed (in units per turn)
  final int speed;

  Entity(this.position, this.speed);

  int get x => position.x;
  int get y => position.y;

  double distanceTo(Position target) => this.position.distance(target);

  /// Returns the estimated time (as a number of turns) for the entity to reach
  /// the target position
  double estimatedTimeTo(Position target) => distanceTo(target) / speed;

  /// Returns the estimated time (as a number of turns) to have the target
  /// position in shooting range
  double estimatedTimeToRange(Position target) => min(0, distanceTo(target) - 2000) / speed;
}

class Player extends Entity {

  Player(Position position): super(position, 1000);

  factory Player.read(Stdin stdin) {
    var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    return Player(Position(inputs[0], inputs[1]));
  }
}

class Human extends Entity {
  final int id;

  Human(this.id, Position position): super(position, 0);

  factory Human.read(Stdin stdin) {
    var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    return Human(inputs[0], Position(inputs[1], inputs[2]));
  }

  @override
  String toString() {
    return "Human[${id}, ${position}]";
  }
}

class Zombie extends Entity {
  final int id;
  final Position nextPosition;

  Zombie(this.id, Position position, this.nextPosition): super(position, 400);

  /// Returns a vector representing the zombie's movement
  Vector get vector => Vector.from(position, nextPosition);

  /// Extrapolates the zombie's position after n turns based on its current
  /// position and movement
  Position extrapolatePosition(int n) => position + (vector * n);

  factory Zombie.read(Stdin stdin) {
    var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    return Zombie(inputs[0], Position(inputs[1], inputs[2]), Position(inputs[3], inputs[4]));
  }

  @override
  String toString() {
    return "Zombie[${id}, ${position}]";
  }
}

class Pair implements Comparable<Pair> {
  final Human human;
  final Zombie zombie;

  Pair(this.human, this.zombie);

  double get distance => zombie.distanceTo(human.position);

  /// Returns the estimated time before the zombie kills the human
  double get estimatedTime => zombie.estimatedTimeTo(human.position);

  /// Returns the entity (that is the human or the zombie) who's the closest to
  /// the given position
  Entity closestTo(Position position) {
    var distanceToHuman = human.distanceTo(position);
    var distanceToZombie = zombie.distanceTo(position);

    return (distanceToZombie < distanceToHuman) ? zombie : human;
  }

  @override
  String toString() {
    return "Pair[${distance.toStringAsFixed(0)} - ${human} / ${zombie}]";
  }

  @override
  int compareTo(Pair other) {
    return this.distance.compareTo(other.distance);
  }
}

void main() {

  var round = 1;

  while (true) {
    var message = "### Round #${round++} ###";

    trace("");
    trace("${'#' * message.length}");
    trace("${message}");
    trace("${'#' * message.length}");
    trace("");

    var player = Player.read(stdin);

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

    // Find the closest zombie to a human
    var pairs = <Pair>[];

    for (var zombie in zombies) {
      for (var human in humans) {
        pairs.add(Pair(human, zombie));
      }
    }

    // Sort the human / zombie pairs by descending distance and distance from
    // the player
    pairs.sort((a, b) {
      if (a.distance == b.distance) {
        return a.zombie.distanceTo(player.position).compareTo(b.zombie.distanceTo(player.position));
      }

      return a.distance.compareTo(b.distance);
    });

    trace("Pairs:");
    trace("${pairs.join('\n')}");

    // Index of the zombie / human pair being considered
    var index = 0;
    var moved = false;

    while ((index < pairs.length) && !moved) {
      var selection = pairs[index];

      var zombie = selection.zombie;
      var human = selection.human;

      trace("Processing pair ${human} / ${zombie} ...");
      trace("Distance human/zombie: ${selection.distance} (ETA: ${selection.estimatedTime} turns)");
      trace('Distance zombie/player: ${zombie.distanceTo(player.position)}');
      trace('Distance zombie/player (minus shoot range): ${zombie.distanceTo(player.position) - 2000}');

      // Time before the zombie kills the human ?
      var zombieETA = selection.estimatedTime;

      // Can we reach the zombie before he kills the human ?
      var turns = 1;

      while (turns <= zombieETA) {
        trace("After ${turns} turns ...");

        var futurePosition = zombie.extrapolatePosition(turns);

        trace('The zombie position will be ${futurePosition}');

        // Can the player reach this position in N turns ?
        var playerETA = player.estimatedTimeToRange(futurePosition);

        trace("Player ETA: ${playerETA}");

        if (playerETA <= turns) {
          // Yes, the player can reach the position before the zombie gets the
          // human
          trace("The player can save the human in ${turns} turn(s)");

          print('${futurePosition.x} ${futurePosition.y}');
          moved = true;
          break;
        }

        trace("Checking next turn ...");

        // Check if we can kill the zombie with one more turn
        turns++;
      }

      if (!moved) {
        // We cannot save this human in time, check the next pair
        trace("No solution found, checking next pair ...");

        index++;
      }
    }

    if (!moved) {
      print("${player.x} ${player.y}");
    }
  }
}
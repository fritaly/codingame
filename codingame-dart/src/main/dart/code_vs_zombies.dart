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
  double estimatedTimeToRange(Position target) => max(0, distanceTo(target) - 2000) / speed;
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

class Triplet implements Comparable<Triplet> {
  final Human human;
  final Zombie zombie;
  final Player player;

  Triplet(this.human, this.zombie, this.player);

  /// Returns the distance between the zombie and the human
  double get zombieDistance => zombie.distanceTo(human.position);

  /// Returns the estimated time before the zombie kills the human
  double get zombieTime => zombie.estimatedTimeTo(human.position);

  /// Returns the estimated time before the player can save the human
  double get playerTime => player.estimatedTimeToRange(human.position);

  /// Tells whether the human can be saved
  bool get humanCanBeSaved => playerTime < zombieTime;

  @override
  String toString() {
    return "Pair[PT: ${playerTime.toStringAsFixed(1)} - ZT: ${zombieTime.toStringAsFixed(1)} - ${human} / ${zombie}]";
  }

  @override
  int compareTo(Triplet other) {
    return this.zombieDistance.compareTo(other.zombieDistance);
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

    var humans = <Human>[], humanCount = int.parse(stdin.readLineSync());

    for (int i = 0; i < humanCount; i++) {
      humans.add(Human.read(stdin));
    }

    var zombies = <Zombie>[], zombieCount = int.parse(stdin.readLineSync());

    for (int i = 0; i < zombieCount; i++) {
      zombies.add(Zombie.read(stdin));
    }

    var triplets = <Triplet>[];

    for (var zombie in zombies) {
      for (var human in humans) {
        triplets.add(Triplet(human, zombie, player));
      }
    }

    // Sort the triplets by ascending zombie distance (handle the most urgent
    // situation first)
    triplets.sort((a, b) {
      if (a.zombieDistance == b.zombieDistance) {
        return a.zombie.distanceTo(player.position).compareTo(b.zombie.distanceTo(player.position));
      }

      return a.zombieDistance.compareTo(b.zombieDistance);
    });

    trace("Pairs:");
    trace("${triplets.join('\n')}");

    // Only consider the situations where the human can still be saved
    triplets = triplets.where((pair) => pair.humanCanBeSaved).toList();

    print("${triplets[0].human.x} ${triplets[0].human.y}");
  }
}
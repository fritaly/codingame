import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

class Coordinates {
  final int x, y;

  Coordinates(this.x, this.y);

  double distance(Coordinates other) {
    assert (other != null);

    var dx = this.x - other.x;
    var dy = this.y - other.y;

    return sqrt(dx * dx + dy * dy);
  }

  double distanceTo(Site site) {
    assert (site != null);

    // Take into account the site's radius. Ensure the value returned isn't negative
    return max(0, distance(site.coordinates) - site.radius);
  }

  @override
  String toString() {
    return "(${x},${y})";
  }
}

// ============ //
// === Site === //
// ============ //

class Site {
  final int id;
  final Coordinates coordinates;
  final int radius;

  Site(this.id, this.coordinates, this.radius);

  @override
  String toString() {
    return "Site[id: ${id}, coordinates: ${coordinates}, radius: ${radius}]";
  }
}

// ===================== //
// === Building Site === //
// ===================== //

class BuildingSite {
  final int id;
  final StructureType structureType;
  final Owner owner; // null if no owner

  /// Si c'est une caserne, le nombre de tours restant avant que la caserne
  /// puisse à nouveau lancer un cycle d'entraînement d'armées, 0 si elle est
  /// disponible.
  final int param1;

  /// Si c'est une caserne, le type d'armée qu'elle produit 0 pour une caserne
  /// de chevaliers, 1 pour une caserne d'archers.
  final int param2;

  final Site site;

  BuildingSite(this.id, this.structureType, this.owner, this.param1, this.param2, this.site);

  @override
  String toString() {
    return "BuildingSite[id: ${id}, type: ${structureType}, owner: ${owner}, coordinates: ${coordinates}]";
  }

  /// Tells whether the site has a structure
  bool hasStructure() {
    return !isEmpty();
  }

  /// Tells whether the site is empty (it has no structure)
  bool isEmpty() {
    return (structureType == StructureType.NONE);
  }

  bool isNeutral() {
    return (owner == null);
  }

  bool isFriend() {
    return (owner == Owner.FRIEND);
  }

  bool isEnemy() {
    return (owner == Owner.ENEMY);
  }

  /// Tells whether the site is available for training an army immediately
  bool isAvailableForTraining() {
    return hasStructure() && isFriend() && (param1 == 0);
  }

  /// Returns the cost (in gold) for training an army on this site
  int getTrainingCost() {
    var unitType = getTrainedUnitType();

    if (unitType == null) {
      return -1;
    }

    switch (unitType) {
      case UnitType.KNIGHT:
        return 80;
      case UnitType.ARCHER:
        return 100;
      default:
        throw "Unexpected unit type: ${unitType}";
    }
  }

  /// Returns the type of the unit trained on this site
  UnitType getTrainedUnitType() {
    if (hasStructure()) {
      switch (param2) {
        case 0:
          return UnitType.KNIGHT;
        case 1:
          return UnitType.ARCHER;
        default:
          throw "Unexpected value: ${param2}";
      }
    }

    // Return null to indicate that no unit can be trained on this site
    return null;
  }

  Coordinates get coordinates => site.coordinates;
  int get x => coordinates.x;
  int get y => coordinates.y;
  int get radius => site.radius;

  factory BuildingSite.from(Stdin stdin, Map<int, Site> sitesById) {
    assert (stdin != null);

    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    var siteId = int.parse(inputs[0]);
    var ignore1 = int.parse(inputs[1]); // used in future leagues
    var ignore2 = int.parse(inputs[2]); // used in future leagues
    var structureType = getStructureType(int.parse(inputs[3])); // -1 = No structure, 2 = Barracks
    var owner = getOwner(int.parse(inputs[4])); // -1 = No structure, 0 = Friendly, 1 = Enemy
    var param1 = int.parse(inputs[5]);
    var param2 = int.parse(inputs[6]);

    return BuildingSite(siteId, structureType, owner, param1, param2, sitesById[siteId]);
  }
}

// ============ //
// === Unit === //
// ============ //

class Unit {
  final Coordinates coordinates;
  final Owner owner;
  final UnitType type;
  final int health;

  Unit(this.coordinates, this.owner, this.type, this.health);

  @override
  String toString() {
    return "Unit[type: ${type}, health: ${health}, owner: ${owner}, coordinates: ${coordinates}]";
  }

  double distanceTo(BuildingSite buildingSite) {
    return coordinates.distanceTo(buildingSite.site);
  }

  factory Unit.from(Stdin stdin) {
    assert (stdin != null);

    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    var x = int.parse(inputs[0]);
    var y = int.parse(inputs[1]);
    var owner = getOwner(int.parse(inputs[2]));
    var unitType = getUnitType(int.parse(inputs[3])); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
    var health = int.parse(inputs[4]);

    return Unit(Coordinates(x, y), owner, unitType, health);
  }
}

// ================= //
// === Unit Type === //
// ================= //

enum UnitType {
  QUEEN, KNIGHT, ARCHER
}

UnitType getUnitType(int value) {
  switch (value) {
    case -1:
      return UnitType.QUEEN;
    case 0:
      return UnitType.KNIGHT;
    case 1:
      return UnitType.ARCHER;
    default:
      throw "Unexpected unit type: ${value}";
  }
}

// ====================== //
// === Structure Type === //
// ====================== //

enum StructureType {
  NONE, BARRACKS
}

StructureType getStructureType(int value) {
  switch (value) {
    case -1:
      return StructureType.NONE;
    case 2:
      return StructureType.BARRACKS;
    default:
      throw "Unexpected structure type: ${value}";
  }
}

// ============= //
// === Owner === //
// ============= //

enum Owner {
  FRIEND, ENEMY
}

Owner getOwner(int value) {
  switch (value) {
    case -1:
      // No structure
      return null;
    case 0:
      return Owner.FRIEND;
    case 1:
      return Owner.ENEMY;
    default:
      throw "Unexpected owner: ${value}";
  }
}

// ============= //
// === Queen === //
// ============= //

class Queen {
  int get radius => 30;

  // Maximum distance per turn (1 turn = 60, 2 turns = 120, etc)
  int get speed => 60;

  int distance(int turns) {
    assert (turns >= 1);

    return turns * speed;
  }
}


void main() {
  var numSites = int.parse(stdin.readLineSync());

  trace("${numSites}");

  var sitesById = Map<int, Site>();

  for (int i = 0; i < numSites; i++) {
    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    var siteId = int.parse(inputs[0]);
    var x = int.parse(inputs[1]);
    var y = int.parse(inputs[2]);
    var radius = int.parse(inputs[3]);

    sitesById[siteId] = Site(siteId, Coordinates(x, y), radius);
  }

  // game loop
  var round = 1;

  while (true) {
    var message = "### Round ${round++} ###";

    trace('');
    trace('#' * message.length);
    trace(message);
    trace('#' * message.length);
    trace('');

    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    var gold = int.parse(inputs[0]);
    var touchedSiteId = int.parse(inputs[1]); // -1 if none

    var buildingSites = Map<int, BuildingSite>();

    for (int i = 0; i < numSites; i++) {
      var buildingSite = BuildingSite.from(stdin, sitesById);

      buildingSites[buildingSite.id] = buildingSite;
    }

    var numUnits = int.parse(stdin.readLineSync());

    var units = <Unit>[];

    for (int i = 0; i < numUnits; i++) {
      units.add(Unit.from(stdin));
    }

    // Identify the queens
    var queen = units.singleWhere((e) => (e.type == UnitType.QUEEN) && (e.owner == Owner.FRIEND));
    var enemyQueen = units.singleWhere((e) => (e.type == UnitType.QUEEN) && (e.owner == Owner.ENEMY));

    // Identify my own units
    var friendlyUnits = units.where((e) => (e.owner == Owner.FRIEND) && (e.type != UnitType.QUEEN)).toList();
    var knights = friendlyUnits.where((e) => e.type == UnitType.KNIGHT);
    var archers = friendlyUnits.where((e) => e.type == UnitType.ARCHER);

    trace("Knights: ${knights}");
    trace("Archers: ${archers}");

    if (touchedSiteId != -1) {
      // The queen is touching a site, is there a building on it ?
      var touchedSite = buildingSites[touchedSiteId];

      trace("The queen is touching site ${touchedSiteId} (${touchedSite})");

      if (touchedSite.isNeutral()) {
        // No building, create one on the site
        trace("The site is neutral, building barracks ...");

        // TODO Decide the type of the barracks to build
        print('BUILD ${touchedSiteId} BARRACKS-KNIGHT');
      } else if (touchedSite.isFriend()) {
        // The site is already owned (by me). Move to another empty one
        trace("The site is friendly, searching new site ...");

        // Identify the nearest empty sites
        var nearestSites = buildingSites.values.where((element) => (element.owner == null)).toList();

        nearestSites.sort((a, b) => queen.distanceTo(a).compareTo(queen.distanceTo(b)));

        trace("Nearest sites:\n${nearestSites.join('\n')}");

        var nearestSite = nearestSites[0];

        // print('BUILD ${nearestSite.id} BARRACKS-KNIGHT');
        print('MOVE ${nearestSite.x} ${nearestSite.y}');
      } else if (touchedSite.isEnemy()) {
        // The queen will destroy the site
        trace("The site is enemy, destroying building ...");

        print('WAIT');
      }
    } else {
      // The queen is not touching a site, find the nearest empty site
      trace("The queen isn't touching a site, searching destination ...");

      // Identify the nearest empty sites
      var nearestSites = buildingSites.values.where((element) => (element.owner == null)).toList();

      nearestSites.sort((a, b) => queen.distanceTo(a).compareTo(queen.distanceTo(b)));

      trace("Nearest sites:\n${nearestSites.join('\n')}");

      var nearestSite = nearestSites[0];

      // print('BUILD ${nearestSite.id} BARRACKS-KNIGHT');
      print('MOVE ${nearestSite.x} ${nearestSite.y}');
    }

    // First line: A valid queen action
    // Second line: A set of training instructions

    // Identify the barracks where I can train an army
    var barracks = buildingSites.values
        .where((e) => e.isFriend() && e.hasStructure() && e.isAvailableForTraining())
        .toList();

    // Favor the barracks closest to the enemy queen to train armies
    barracks.sort((a, b) => enemyQueen.distanceTo(a).compareTo(enemyQueen.distanceTo(b)));

    trace("Barracks: ${barracks}");

    var siteIds = <int>[];

    // Try to train armies based on the remaining gold and available sites
    for (var site in barracks) {
      if (site.getTrainingCost() <= gold) {
        siteIds.add(site.id);
        gold -= site.getTrainingCost();
      }
    }

    print('TRAIN ${siteIds.join(' ')}'.trim());
  }
}
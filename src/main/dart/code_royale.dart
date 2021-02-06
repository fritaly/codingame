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

  @override
  String toString() {
    return "(${x},${y})";
  }
}

class Range {
  /// The max is included in the range
  final int min, max;

  Range(this.min, this.max);

  bool contains(int n) => (min <= n) && (n <= max);
  bool below(int n) => (n < min);
  bool above(int n) => (n > max);

  @override
  String toString() {
    return "Range[${min}-${max}]";
  }
}

abstract class Entity {
  final Coordinates _coordinates;

  Entity(this._coordinates);

  Coordinates get coordinates => _coordinates;

  double distanceTo(Entity target) {
    return this._coordinates.distance(target._coordinates);
  }
}

// ============ //
// === Site === //
// ============ //

class Site extends Entity {
  final int id;
  final int radius;

  Site(this.id, Coordinates coordinates, this.radius): super(coordinates);

  @override
  String toString() {
    return "Site[id: ${id}, coordinates: ${coordinates}, radius: ${radius}]";
  }
}

// ===================== //
// === Building Site === //
// ===================== //

class BuildingSite extends Entity {
  final int id;
  final StructureType type;
  final Owner owner; // null if no owner

  /// Quand il n'y a pas de bâtiment construit : -1
  /// Si c'est une tour, son nombre de points de vie restants.
  /// Si c'est une caserne, le nombre de tours restant avant que la caserne
  /// puisse à nouveau lancer un cycle d'entraînement d'armées, 0 si elle est disponible.
  final int param1;

  /// Quand il n'y a pas de bâtiment construit : -1
  /// Si c'est une tour, son rayon de portée
  /// Si c'est une caserne, le type d'armée qu'elle produit 0 pour une caserne
  /// de chevaliers, 1 pour une caserne d'archers, 2 pour une caserne de géants.
  final int param2;

  BuildingSite(this.id, this.type, this.owner, this.param1, this.param2, Coordinates coordinates): super(coordinates);

  @override
  String toString() {
    return "BuildingSite[id: ${id}, type: ${type}, owner: ${owner}, coordinates: ${coordinates}]";
  }

  /// Tells whether the site has been claimed
  bool get claimed => (type != StructureType.NONE);

  bool get neutral => (owner == null);
  bool get friendly => (owner == Owner.FRIEND);
  bool get enemy => (owner == Owner.ENEMY);

  bool get barracks => type == StructureType.BARRACKS;
  bool get tower => type == StructureType.TOWER;

  /// Tells whether the site is available for training an army immediately
  bool isAvailableForTraining() {
    return claimed && friendly && barracks && (param1 == 0);
  }

  /// Tells whether this site is a barracks training the given type of unit
  bool withType(UnitType type) {
    assert (type != null);

    return claimed && barracks && (getTrainedUnitType() == type);
  }

  /// Returns the type of the unit trained on this site
  UnitType getTrainedUnitType() {
    if (barracks && claimed) {
      return UnitType.valueOf(param2);
    }

    // Return null to indicate that no unit can be trained on this site
    return null;
  }

  int get x => coordinates.x;
  int get y => coordinates.y;

  factory BuildingSite.from(Stdin stdin, Map<int, Site> sitesById) {
    assert (stdin != null);

    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    var siteId = int.parse(inputs[0]);
    var ignore1 = int.parse(inputs[1]); // used in future leagues
    var ignore2 = int.parse(inputs[2]); // used in future leagues
    var structureType = structureTypeOf(int.parse(inputs[3])); // -1 = No structure, 2 = Barracks
    var owner = ownerOf(int.parse(inputs[4])); // -1 = No structure, 0 = Friendly, 1 = Enemy
    var param1 = int.parse(inputs[5]);
    var param2 = int.parse(inputs[6]);

    return BuildingSite(siteId, structureType, owner, param1, param2, sitesById[siteId].coordinates);
  }
}

// ============ //
// === Unit === //
// ============ //

class Unit extends Entity {
  final Owner owner;
  final UnitType type;
  final int health;

  Unit(Coordinates coordinates, this.owner, this.type, this.health): super(coordinates);

  bool get queen => type == UnitType.QUEEN;
  bool get knight => type == UnitType.KNIGHT;
  bool get archer => type == UnitType.ARCHER;
  bool get giant => type == UnitType.GIANT;

  bool get friendly => owner == Owner.FRIEND;
  bool get enemy => owner == Owner.ENEMY;

  @override
  String toString() {
    return "Unit[type: ${type}, health: ${health}, owner: ${owner}, coordinates: ${coordinates}]";
  }

  factory Unit.from(Stdin stdin) {
    assert (stdin != null);

    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    var x = int.parse(inputs[0]);
    var y = int.parse(inputs[1]);
    var owner = ownerOf(int.parse(inputs[2]));
    var unitType = UnitType.valueOf(int.parse(inputs[3])); // -1 = QUEEN, 0 = KNIGHT, 1 = ARCHER
    var health = int.parse(inputs[4]);

    return Unit(Coordinates(x, y), owner, unitType, health);
  }
}

// ================= //
// === Unit Type === //
// ================= //

class UnitType {

  /// The int value identifying the unit type
  final int value;

  /// The cost (in gold) for training a unit with the given type
  final int cost;

  static const UnitType QUEEN = UnitType(-1, 0);
  static const UnitType KNIGHT = UnitType(0, 80);
  static const UnitType ARCHER = UnitType(1, 100);
  static const UnitType GIANT = UnitType(2, 140);

  /// Only return the types for the units which can be trained in barracks
  static List<UnitType> values() => [ KNIGHT, ARCHER, GIANT ];

  const UnitType(this.value, this.cost);

  factory UnitType.valueOf(int value) {
    switch (value) {
      case -1:
        return QUEEN;
      case 0:
        return KNIGHT;
      case 1:
        return ARCHER;
      case 2:
        return GIANT;
      default:
        throw "Unexpected unit type: ${value}";
    }
  }
}

// ====================== //
// === Structure Type === //
// ====================== //

enum StructureType {
  NONE, BARRACKS, TOWER
}

StructureType structureTypeOf(int value) {
  switch (value) {
    case -1:
      return StructureType.NONE;
    case 1:
      return StructureType.TOWER;
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

Owner ownerOf(int value) {
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

Comparator<Entity> compareDistanceFrom(Entity reference) {
  return (a, b) => reference.distanceTo(a).compareTo(reference.distanceTo(b));
}

class Units {
  final List<Unit> units;

  Units(this.units);

  int get count => units.length;

  Units where(bool test(Unit element)) => Units(units.where((e) => test(e)).toList());

  Units get knights => where((e) => e.knight);
  Units get archers => where((e) => e.archer);
  Units get giants => where((e) => e.giant);
  Unit get queen => units.firstWhere((e) => e.queen);

  Units withType(UnitType type) => where((e) => e.type == type);

  Range range(UnitType type, World world) {
    switch (type) {
      case UnitType.GIANT:
        return Range(1, 2);
      case UnitType.KNIGHT:
        // Knights are created per group of 4 !
        return Range(12, 20);
      case UnitType.ARCHER:
        return Range(1, 2);
      case UnitType.QUEEN:
        return Range(1, 1);
      default:
        throw "Unexpected unit type: ${type}";
    }
  }
}

class Sites {
  final List<BuildingSite> sites;

  Sites(this.sites);

  int get count => sites.length;

  Sites where(bool test(BuildingSite element)) => Sites(sites.where((e) => test(e)).toList());

  Sites get friendly => where((e) => e.friendly);
  Sites get enemy => where((e) => e.enemy);
  Sites get neutral => where((e) => e.neutral);

  Sites get barracks => where((e) => e.barracks);
  Sites get knightBarracks => where((e) => e.barracks && e.withType(UnitType.KNIGHT));
  Sites get archerBarracks => where((e) => e.barracks && e.withType(UnitType.ARCHER));
  Sites get giantBarracks => where((e) => e.barracks && e.withType(UnitType.GIANT));
  Sites get towers => where((e) => e.tower);

  Range range(StructureType type, UnitType unitType, World world) {
    switch (type) {
      case StructureType.BARRACKS:
        if (unitType == UnitType.ARCHER) {
          return Range(0, 0);
        } else if (unitType == UnitType.KNIGHT) {
          return Range(1, 1);
        } else if (unitType == UnitType.GIANT) {
          // Only build a barracks of giants if the enemy has at least one tower
          return (world.sites.enemy.towers.count > 0) ? Range(1, 1) : Range(
              0, 0);
        } else {
          throw "Unexpected unit type: ${unitType}";
        }
        break;

      case StructureType.TOWER:
        return Range(3, 10);

      default:
        throw "Unexpected structure type: ${type}";
    }
  }
}

// ============= //
// === World === //
// ============= //

class World {
  final Units units;
  final Sites sites;

  World(this.units, this.sites);
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

  // The coordinates where the queen appeared on the map
  Coordinates startPosition;

  var round = 1;

  // Game loop
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

    trace("${gold}");

    var touchedSiteId = int.parse(inputs[1]);

    trace("${touchedSiteId}");

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

    var allSites = Sites(buildingSites.values.toList());
    var allUnits = Units(units);

    var world = World(allUnits, allSites);

    // Identify my own sites and units
    var friendlySites = allSites.friendly;
    var friendlyUnits = Units(units.where((e) => e.friendly).toList());

    trace("Knights: ${friendlyUnits.knights.count}");
    trace("Giants: ${friendlyUnits.giants.count}");
    trace("Archers: ${friendlyUnits.archers.count}");

    // Identify the queens
    var queen = friendlyUnits.queen;
    var enemyQueen = allUnits.units.firstWhere((e) => e.enemy && e.queen);

    // Store the queen's start position
    if (startPosition == null) {
      startPosition = queen.coordinates;
    }

    // Identify the enemy units near the queen
    /* var nearbyEnemies = enemyUnits.units.where((e) => queen.distanceTo(e) < 300).toList();

    trace("Nearby enemies: ${nearbyEnemies}");

    if (!nearbyEnemies.isEmpty) {
      // The queen is under attack
      var nearestTowers = List<BuildingSite>.from(towers);
      nearestTowers.sort(compareDistanceFrom(queen));

      trace("Nearest towers: ${nearestTowers}");

      if (nearestTowers.isEmpty) {
        // No tower available !
        print('WAIT'); // TODO Handle this use case
      } else {
        // Flee to the closest tower
        var nearestTower = nearestTowers[0];

        print('MOVE ${nearestTower.x} ${nearestTower.y}');
      }
    } else */ if (touchedSiteId != -1) {
      // The queen is touching a site, is there a building on it ?
      var touchedSite = buildingSites[touchedSiteId];

      trace("The queen is touching site ${touchedSiteId} (${touchedSite})");

      if (touchedSite.neutral) {
        // No building, create one on the site
        trace("The site is neutral, building structure ...");

        if (allSites.range(StructureType.BARRACKS, UnitType.KNIGHT, world).below(friendlySites.knightBarracks.count)) {
          // Build one barracks for knights
          print('BUILD ${touchedSiteId} BARRACKS-KNIGHT');
        } else if (allSites.range(StructureType.BARRACKS, UnitType.ARCHER, world).below(friendlySites.archerBarracks.count)) {
          // Build one barracks for archers
          print('BUILD ${touchedSiteId} BARRACKS-ARCHER');
        } else if (allSites.range(StructureType.BARRACKS, UnitType.GIANT, world).below(friendlySites.giantBarracks.count)) {
          // Build one barracks for giants
          print('BUILD ${touchedSiteId} BARRACKS-GIANT');
        } else {
          // Else build a tower
          print('BUILD ${touchedSiteId} TOWER');
        }
      } else if (touchedSite.friendly) {
        // The site is already owned (by me). Move to another empty one
        trace("The site is friendly, searching new site ...");

        // Identify the nearest empty sites in my half of the map
        var nearestSites = buildingSites.values.where((e) => e.neutral && (startPosition.distance(e.coordinates) <= (1920 / 2)) ).toList();

        // Sort the sites based on their distance from the queen's start position
        // This ensures the queen doesn't wander towards the enemy's territory
        nearestSites.sort((a, b) => startPosition.distance(a.coordinates).compareTo(startPosition.distance(b.coordinates)));

        trace("Nearest sites:\n${nearestSites.join('\n')}");

        if (((friendlySites.towers.count >= 5) && (friendlySites.giantBarracks.count >= 1) && (friendlySites.knightBarracks.count >= 1)) || nearestSites.isEmpty) {
          // We already built all our sites or no empty site available, return to the start position
          print('MOVE ${startPosition.x} ${startPosition.y}');
        } else {
          var nearestSite = nearestSites[0];

          print('MOVE ${nearestSite.x} ${nearestSite.y}');
        }
      } else if (touchedSite.enemy) {
        // The queen will destroy the site
        trace("The site is enemy, destroying building ...");

        print('WAIT');
      }
    } else {
      // The queen is not touching a site

      if (friendlySites.barracks.count >= (buildingSites.length / 2)) {
        // Don't build more than N/2 sites and enter escape mode by returning
        // to the start position
        // TODO Send the queen next to a defensive tower
        print('MOVE ${startPosition.x} ${startPosition.y}');

      } else {
        // The queen is not touching a site, find the nearest empty site
        trace("The queen isn't touching a site, searching destination ...");

        // Identify the nearest empty sites
        var emptySites = buildingSites.values.where((e) => !e.claimed).toList();

        trace("Nearest sites:\n${emptySites.join('\n')}");

        if (!emptySites.isEmpty) {
          emptySites.sort(compareDistanceFrom(queen));

          var nearestSite = emptySites[0];

          print('MOVE ${nearestSite.x} ${nearestSite.y}');
        } else {
          // No empty site, return to the start position
          print('MOVE ${startPosition.x} ${startPosition.y}');
        }
      }
    }

    // Identify the barracks where I can train an army
    var availableBarracks = friendlySites.barracks.sites.where((e) => e.isAvailableForTraining()).toList();

    // Favor the barracks closest to the enemy queen to train armies
    availableBarracks.sort(compareDistanceFrom(enemyQueen));

    trace("Barracks: ${availableBarracks}");

    // Dispatch the barracks per unit type
    var availableArcherBarracks = availableBarracks.where((e) => e.withType(UnitType.ARCHER)).toList();
    var availableKnightBarracks = availableBarracks.where((e) => e.withType(UnitType.KNIGHT)).toList();
    var availableGiantsBarracks = availableBarracks.where((e) => e.withType(UnitType.GIANT)).toList();

    trace("Available archer barracks: ${availableArcherBarracks}");
    trace("Available knight barracks: ${availableKnightBarracks}");
    trace("Available giant barracks : ${availableGiantsBarracks}");

    var siteIds = <int>[];

    var random = Random();

    // Try to train armies based on the remaining gold and available barracks
    // Only decide when we have enough money to afford all the unit types
    // otherwise we'll only train the cheapest units

    // Consider first the units which are not present in enough numbers on the
    // ground
    var candidateTypes = UnitType.values().where((type) {
      var count = friendlyUnits.withType(type).count;
      var range = friendlyUnits.range(type, world);

      return count < range.min;
    });

    if (candidateTypes.isEmpty) {
      // All the units are present in the min number, consider the ones which
      // are under the max number allowed
      candidateTypes = UnitType.values().where((type) {
        var count = friendlyUnits.withType(type).count;
        var range = friendlyUnits.range(type, world);

        return (count > range.min) && (count < range.max);
      });
    }

    if (candidateTypes.isEmpty) {
      // All the units have reached their maximum number
    }

    while (true) {
      var candidates = <UnitType>[];

      // The order defines the precedence for creating units
      for (UnitType type in candidateTypes) {
        var count = friendlyUnits.withType(type).count;

        if (type.cost > gold) {
          // Not enough gold
          continue;
        }

        if (!availableBarracks.any((e) => e.withType(type))) {
          // No barracks available for training this type of unit
          continue;
        }

        if (friendlyUnits.range(type, world).below(count)) {
          // Not enough units of this type
          candidates.add(type);
          break;
        }

        if (friendlyUnits.range(type, world).above(count)) {
          // We already trained enough of those units
          continue;
        }

        candidates.add(type);
      }

      if (candidates.isEmpty) {
        // Running out of available barracks or gold
        break;
      }

      var type = candidates[0]; // candidates[random.nextInt(candidates.length)];
      var site = availableBarracks.firstWhere((e) => e.withType(type));

      availableBarracks.remove(site);

      siteIds.add(site.id);

      gold -= type.cost;
    }

    print('TRAIN ${siteIds.join(' ')}'.trim());
  }
}
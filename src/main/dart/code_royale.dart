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

  @override
  String toString() {
    return "Range[${min}-${max}]";
  }
}

/// Store all the information about a given type of building to drive the
/// decision to build
class BuildingStatus {
  final Range range;

  /// The number of buildings with this type
  final int count;

  /// The type of the building
  final BuildingType buildingType;

  BuildingStatus(this.range, this.count, this.buildingType);

  /// Tells whether one building must be built because the minimum number hasn't
  /// yet been reached
  bool mustBuild() => (count < range.min);

  /// Tells whether one building can be built because the maximum number hasn't
  /// yet been reached
  bool canBuild() => (range.min <= count) && (count < range.max);
}

/// Store all the information about a given type of unit to drive the decision
/// to train
class UnitStatus {
  final Range range;

  /// The number of units with this type
  final int count;

  /// The type of the unit
  final UnitType unitType;

  UnitStatus(this.range, this.count, this.unitType);

  /// Tells whether one unit must be trained because the minimum number hasn't
  /// yet been reached
  bool mustTrain() => (count < range.min);

  /// Tells whether one unit can be trained because the maximum number hasn't
  /// yet been reached
  bool canTrain() => (range.min <= count) && (count < range.max);
}

abstract class Entity {
  final Coordinates _coordinates;

  Entity(this._coordinates);

  Coordinates get coordinates => _coordinates;

  double distanceTo(Coordinates target) {
    return this._coordinates.distance(target);
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

// ==================== //
// === BuildingType === //
// ==================== //

class BuildingType {
  final StructureType structureType;
  final UnitType unitType;

  static const BuildingType BARRACKS_KNIGHT = BuildingType(StructureType.BARRACKS, UnitType.KNIGHT);
  static const BuildingType BARRACKS_ARCHER = BuildingType(StructureType.BARRACKS, UnitType.ARCHER);
  static const BuildingType BARRACKS_GIANT = BuildingType(StructureType.BARRACKS, UnitType.GIANT);
  static const BuildingType TOWER = BuildingType(StructureType.TOWER, null);

  static List<BuildingType> values() => [ BARRACKS_KNIGHT, BARRACKS_ARCHER, BARRACKS_GIANT, TOWER ];

  const BuildingType(this.structureType, this.unitType);

  bool matches(BuildingSite site) => (site.type == structureType) && (site.getTrainedUnitType() == unitType);

  String buildOrder(int siteId) {
    switch (this) {
      case BARRACKS_KNIGHT:
        return "BUILD ${siteId} BARRACKS-KNIGHT";
      case BARRACKS_ARCHER:
        return "BUILD ${siteId} BARRACKS-ARCHER";
      case BARRACKS_GIANT:
        return "BUILD ${siteId} BARRACKS-GIANT";
      case TOWER:
        return "BUILD ${siteId} TOWER";
      default:
        throw "Unexpected building type: ${this}";
    }
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

Comparator<Entity> compareDistanceTo(Coordinates reference) {
  return (a, b) => a.distanceTo(reference).compareTo(b.distanceTo(reference));
}

class Units {
  final List<Unit> units;

  Units(this.units);

  int get count => units.length;

  Units where(bool test(Unit element)) => Units(units.where((e) => test(e)).toList());

  Units get friendly => Units(units.where((e) => e.friendly).toList());
  Units get enemy => Units(units.where((e) => e.enemy).toList());

  Units get knights => where((e) => e.knight);
  Units get archers => where((e) => e.archer);
  Units get giants => where((e) => e.giant);
  Unit get queen => units.firstWhere((e) => e.queen);

  Units withType(UnitType type) => where((e) => e.type == type);
  Units withSide(Side side) => where((e) => side.contains(e.coordinates));
}

class Sites {
  final List<BuildingSite> sites;

  Sites(this.sites);

  int get count => sites.length;

  BuildingSite site(int id) => sites.firstWhere((e) => e.id == id);

  Sites where(bool test(BuildingSite element)) => Sites(sites.where((e) => test(e)).toList());

  Sites get friendly => where((e) => e.friendly);
  Sites get enemy => where((e) => e.enemy);
  Sites get neutral => where((e) => e.neutral);

  Sites get barracks => where((e) => e.barracks);
  Sites get knightBarracks => where((e) => e.barracks && e.withType(UnitType.KNIGHT));
  Sites get archerBarracks => where((e) => e.barracks && e.withType(UnitType.ARCHER));
  Sites get giantBarracks => where((e) => e.barracks && e.withType(UnitType.GIANT));
  Sites get towers => where((e) => e.tower);

  Sites withSide(Side side) => where((e) => side.contains(e.coordinates));
  Sites withType(BuildingType type) => where((e) => type.matches(e));
}

// ============ //
// === Side === //
// ============ //

/// Enumerates the 2 sides on the map where the queen can spawn
class Side {
  final String name;
  final int minX, maxX;

  static const Side LEFT = Side('LEFT', 0, 1920 ~/ 2);
  static const Side RIGHT = Side('RIGHT', 1920 ~/ 2, 1920);

  static List<Side> values() => [ LEFT, RIGHT ];

  const Side(this.name, this.minX, this.maxX);

  bool contains(Coordinates coordinates) {
    // Just check the X value
    return (minX <= coordinates.x) && (coordinates.x < maxX);
  }

  Side opposite() => (this == LEFT) ? RIGHT : LEFT;
}

// ============= //
// === World === //
// ============= //

class World {
  final Units units;
  final Sites sites;
  final Coordinates startPosition;
  final int touchedSiteId;

  World(this.units, this.sites, this.startPosition, this.touchedSiteId);

  Unit get queen => units.friendly.queen;

  BuildingSite touchedSite() => sites.sites.firstWhere((e) => e.id == touchedSiteId);

  /// Returns on which side of the map the queen spawn
  Side get side => Side.values().firstWhere((e) => e.contains(startPosition));

  Range range(BuildingType type) {
    switch (type) {
      case BuildingType.BARRACKS_KNIGHT:
        return Range(1, 1);
      case BuildingType.BARRACKS_ARCHER:
        return Range(0, 0);
      case BuildingType.BARRACKS_GIANT:
        // Only build a barracks of giants if the enemy has at least one tower !
        return (sites.enemy.towers.count > 0) ? Range(1, 1) : Range(0, 0);
      case BuildingType.TOWER:
        // Don't build more than 5 towers
        return Range(3, 5);

      default:
        throw "Unexpected structure type: ${type}";
    }
  }

  Range unitRange(UnitType type) {
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

  BuildingStatus buildingStatus(BuildingType type) {
    // Count the number of friendly sites matching this build type
    return BuildingStatus(range(type), sites.sites.where((site) => site.friendly && type.matches(site)).length, type);
  }

  UnitStatus unitStatus(UnitType type) {
    // Count the number of friendly units matching this unit type
    return UnitStatus(unitRange(type), units.units.where((unit) => unit.friendly && (unit.type == type)).length, type);
  }
}

abstract class Mode {
  Mode run(World world);
}

class NormalMode extends Mode {

  @override
  Mode run(World world) {
    var startPosition = world.startPosition;
    var touchedSiteId = world.touchedSiteId;

    if (touchedSiteId != -1) {
      // The queen is touching a site, is there a building on it ?
      var touchedSite = world.sites.site(touchedSiteId);

      trace("The queen is touching site ${touchedSiteId} (${touchedSite})");

      if (touchedSite.neutral) {
        // No building, create one on the site
        trace("The site is neutral, building structure ...");

        var statuses = BuildingType.values().map((type) => world.buildingStatus(type)).toList();

        var built = false;

        // Check first whether some buildings must be built
        for (var status in statuses) {
          if (status.mustBuild()) {
            // Not enough sites of this type, build one
            print(status.buildingType.buildOrder(touchedSiteId));
            built = true;
            break;
          }
        }

        if (!built) {
          // Check next whether some buildings can be built
          for (var status in statuses) {
            if (status.canBuild()) {
              // We can have more sites of this type, build one
              print(status.buildingType.buildOrder(touchedSiteId));
              built = true;
              break;
            }
          }
        }

        if (!built) {
          // All the possible buildings have been built, return to the start
          // position (the further from the enemy units, the better since they
          // take longer to reach the queen and have a limited life span)
          print('MOVE ${startPosition.x} ${startPosition.y}');
        }
      } else if (touchedSite.friendly) {
        // The site is already owned (by me). Move to another empty one
        trace("The site is friendly, searching new site ...");

        // Identify the nearest empty sites in my half of the map
        var nearestSites = world.sites.neutral.where((e) => (startPosition.distance(e.coordinates) <= (1920 / 2))).sites;

        // Sort the sites based on their distance from the queen's start position
        // This ensures the queen doesn't wander towards the enemy's territory
        nearestSites.sort((a, b) => startPosition.distance(a.coordinates).compareTo(startPosition.distance(b.coordinates)));

        trace("Nearest sites:\n${nearestSites.join('\n')}");

        // TODO Change this to remove hard-coded values
        var friendlySites = world.sites.friendly;

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
      var friendlySites = world.sites.friendly;

      if (friendlySites.barracks.count >= (world.sites.count / 2)) {
        // Don't build more than N/2 sites and enter escape mode by returning
        // to the start position
        // TODO Send the queen next to a defensive tower
        print('MOVE ${startPosition.x} ${startPosition.y}');

      } else {
        // The queen is not touching a site, find the nearest empty site
        trace("The queen isn't touching a site, searching destination ...");

        // Identify the nearest empty sites
        var emptySites = world.sites.neutral.sites;

        trace("Nearest sites:\n${emptySites.join('\n')}");

        if (!emptySites.isEmpty) {
          emptySites.sort(compareDistanceTo(world.queen.coordinates));

          var nearestSite = emptySites[0];

          print('MOVE ${nearestSite.x} ${nearestSite.y}');
        } else {
          // No empty site, return to the start position
          print('MOVE ${startPosition.x} ${startPosition.y}');
        }
      }
    }

    return this;
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

  // The coordinates where the queen appeared on the map
  Coordinates startPosition;

  var round = 1;
  var previousHealth = -1, healthLost = 0;
  Mode mode = new NormalMode();

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

    var world = World(allUnits, allSites, startPosition, touchedSiteId);

    // Identify my own sites and units
    var friendlySites = allSites.friendly;

    // Identify the queens
    var queen = allUnits.friendly.queen;
    var enemyQueen = allUnits.enemy.queen;

    if (previousHealth != -1) {
      healthLost = previousHealth - queen.health;

      if (healthLost > 0) {
        trace("The queen lost ${healthLost} health");
      }
    }

    previousHealth = queen.health;

    // Store the queen's start position
    if (startPosition == null) {
      startPosition = queen.coordinates;
    }

    trace("Invoking mode ${mode} ...");

    // Delegate to the current mode and update the current mode
    mode = mode.run(world);

    // Identify the barracks where I can train an army
    var availableBarracks = friendlySites.barracks.sites.where((e) => e.isAvailableForTraining()).toList();

    // Favor the barracks closest to the enemy queen to train armies
    availableBarracks.sort(compareDistanceTo(enemyQueen.coordinates));

    trace("Barracks: ${availableBarracks}");

    var siteIds = <int>[];

    var random = Random();

    // Try to train armies based on the remaining gold and available barracks
    // Only decide when we have enough money to afford all the unit types
    // otherwise we'll only train the cheapest units

    // Only consider the unit types for which we have at least one barracks
    // available ! Shuffle the list of statuses to introduce some randomness in
    // the training of units
    var statuses = UnitType.values()
        .where((type) => availableBarracks.any((e) => e.getTrainedUnitType() == type))
        .map((type) => world.unitStatus(type)).toList();
    statuses.shuffle(random);

    // Wait until we have enough gold to train all the candidate types of units
    // otherwise we'll end up always training the cheapest ones (the knights)
    if (!statuses.any((e) => (e.unitType.cost > gold))) {
      while (true) {
        // Whether a unit has been trained during this iteration
        var trained = false;

        // Check first whether some units must be trained
        for (var status in statuses) {
          // Barracks available to train this type of unit ?
          var barracks = availableBarracks.where((e) => (e.getTrainedUnitType() == status.unitType)).toList();

          if (!barracks.isEmpty && status.mustTrain() && (gold >= status.unitType.cost)) {
            // Not enough units of this type, train one
            siteIds.add(barracks.removeAt(0).id);
            gold -= status.unitType.cost;
            trained = true;
            break;
          }
        }

        if (!trained) {
          // Check next whether some units can be trained
          for (var status in statuses) {
            // Barracks available to train this type of unit ?
            var barracks = availableBarracks.where((e) => (e.getTrainedUnitType() == status.unitType)).toList();

            if (!barracks.isEmpty && status.canTrain() && (gold >= status.unitType.cost)) {
              // Not enough units of this type, train one
              siteIds.add(barracks.removeAt(0).id);
              gold -= status.unitType.cost;
              trained = true;
              break;
            }
          }
        }

        if (!trained) {
          // All the possible units have been trained
          break;
        }
      }
    } else {
      trace("Waiting: not enough gold to train all the possible unit types");
    }

    print('TRAIN ${siteIds.join(' ')}'.trim());
  }
}
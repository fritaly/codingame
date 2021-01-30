import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

enum Order {
  ASCENDING, DESCENDING
}

class SortCriteria {
  final String property;
  final Order order;
  String type; // the type of the property

  SortCriteria(this.property, this.order);

  @override
  String toString() {
    return "SortCriteria[${(order == Order.ASCENDING) ? '+' : '-'}${property}]";
  }
}

class Sort {

  final List<SortCriteria> criteria;

  Sort(this.criteria);

  int compare(GenericObject o1, GenericObject o2) {
    for (SortCriteria c in criteria) {
      var value1 = o1.properties[c.property];
      var value2 = o2.properties[c.property];

      if (value1 != value2) {
        return (c.order == Order.ASCENDING) ? value1.compareTo(value2) : value2.compareTo(value1);
      }
    }

    return 0;
  }

  @override
  String toString() {
    return "Sort[${criteria}]";
  }
}

class GenericObject {
  final Map<String, Comparable> properties;

  GenericObject(this.properties);

  @override
  String toString() {
    return "GenericObject[${properties}]";
  }
}

void main() {
  var expression = stdin.readLineSync();
  var types = stdin.readLineSync().split(',');
  var N = int.parse(stdin.readLineSync());

  trace("${expression}");
  trace("${types.join(',')}");
  trace("${N}");

  // Parse the expression and create a Sort object
  var regexp = RegExp('([+-])([^+-]+)');

  var sort = Sort(regexp.allMatches(expression).map((match) {
    var order = (match.group(1) == '+') ? Order.ASCENDING : Order.DESCENDING;
    var property = match.group(2);

    return SortCriteria(property, order);
  }).toList());

  trace("${sort}");

  for (int i = 0; i < types.length; i++) {
    sort.criteria[i].type = types[i];
  }

  var objects = <GenericObject>[];

  for (int i = 0; i < N; i++) {
    var row = stdin.readLineSync();

    trace("${row}");

    var pairs = row.split(',');

    var properties = Map<String, Comparable>();

    for (int n = 0; n < pairs.length; n++) {
      var array = pairs[n].split(':');

      var name = array[0];
      var type = (name == 'id') ? 'int' : sort.criteria.firstWhere((e) => e.property == name).type;
      var value = (type == 'int') ? int.parse(array[1]) : array[1];

      properties[name] = value;
    }

    objects.add(GenericObject(properties));
  }

  trace("${objects.join('\n')}");

  trace("BEFORE:\n${objects.join('\n')}");

  objects.sort((a, b) => sort.compare(a, b));

  trace("AFTER:\n${objects.join('\n')}");

  objects.forEach((element) { print(element.properties['id']); });
}
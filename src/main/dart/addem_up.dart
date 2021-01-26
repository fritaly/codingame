import 'dart:io';

void main() {
  var N = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(' ');

  stderr.writeln("${N}");
  stderr.writeln("${inputs.join(' ')}");

  var points = <int>[];

  for (int i = 0; i < N; i++) {
    var x = int.parse(inputs[i]);

    points.add(x);
  }

  var cost = 0;

  while (points.length > 1) {
    // Ensure that the cards are sorted by ascending health points
    points.sort();

    // Merge the 2 first cards into a new one
    var sum = points.removeAt(0) + points.removeAt(0);

    cost += sum;

    // Add the new card to the deck
    points.add(sum);
  }

  print(cost);
}
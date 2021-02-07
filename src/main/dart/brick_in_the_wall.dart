import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

double work(int weight, int height) {
  assert (height >= 1);

  // W = ((L-1) * 6.5 / 100) * g * m
  return ((height - 1) * 6.5 / 100) * 10 * weight;
}

void main() {
  var bricksPerRow = int.parse(stdin.readLineSync());
  var numberOfBricks = int.parse(stdin.readLineSync());
  var inputs = stdin.readLineSync().split(' ');

  trace("${bricksPerRow}");
  trace("${numberOfBricks}");
  trace("${inputs.join(' ')}");

  var weights = inputs.map((s) => int.parse(s)).toList();

  // Sort the bricks from heaviest to lightest
  weights.sort((a, b) => b.compareTo(a));

  var totalWork = 0.0;

  var row = 1;
  var remainingSpace = bricksPerRow;

  while (!weights.isEmpty) {
    var weight = weights.removeAt(0);

    if (remainingSpace == 0) {
      row++;
      remainingSpace = bricksPerRow;
    }

    totalWork += work(weight, row);
    remainingSpace--;
  }

  print(totalWork.toStringAsFixed(3));
}
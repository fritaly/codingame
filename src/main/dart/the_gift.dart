import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

void main() {
  var N = int.parse(stdin.readLineSync());
  var price = int.parse(stdin.readLineSync());
  var budgets = <int>[];

  for (var i = 0; i < N; i++) {
    budgets.add(int.parse(stdin.readLineSync()));
  }

  trace("${N}");
  trace("${price}");
  trace("${budgets.join('\n')}");

  var totalBudget = budgets.reduce((value, element) => value + element);

  trace("Total budget: ${totalBudget}");

  if (totalBudget < price) {
    print('IMPOSSIBLE');
  } else {
    // Process the smallest budgets first
    budgets.sort();

    var remainingPrice = price;

    var pricesToPay = <int>[];

    while (!budgets.isEmpty) {
      var budget = budgets.removeAt(0);
      var priceToPay = min(budget, remainingPrice ~/ (1 + budgets.length));
      pricesToPay.add(priceToPay);
      remainingPrice -= priceToPay;
    }

    print(pricesToPay.join('\n'));
  }
}
/*
 * Copyright 2021, Francois Ritaly
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
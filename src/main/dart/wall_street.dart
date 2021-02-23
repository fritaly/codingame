import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

class Order {
  final int id; // Used for detecting the orders created first
  final String symbol;
  final bool sell;
  final int quantity;
  final double price;

  bool get buy => !sell;
  String get verb => sell ? 'SELL' : 'BUY';

  int compareByDate(Order that) => this.id.compareTo(that.id);

  Order(this.id, this.symbol, this.sell, this.quantity, this.price);

  @override
  String toString() => "Order[#${id} ${verb} ${quantity} ${symbol} at ${price.toStringAsFixed(1)}]";
}

class Orders {
  final String symbol;
  final List<Order> _buyOrders = [], _sellOrders = [];

  Orders(this.symbol);

  List<Trade> add(Order order) {
    assert (order != null);

    trace('\n===');
    trace('=== Processing order ${order} ===');
    trace('===\n');

    var trades = <Trade>[];

    if (order.buy) {
      var buyOrder = order;

      // Search a sell order with the highest compatible price
      var sellOrders = _sellOrders.where((e) => e.price <= buyOrder.price).toList();
      sellOrders.sort((a,b) {
        // Match the orders with the smallest difference first
        var diffb = buyOrder.price - b.price;
        var diffa = buyOrder.price - a.price;

        if (diffa == diffb) {
          // If same difference, favor the orders created first
          return a.id.compareTo(b.id);
        }

        return diffb.compareTo(diffa);
      });

      trace('Found sell orders: ${sellOrders}');

      var quantityToBuy = buyOrder.quantity;

      while (!sellOrders.isEmpty && (quantityToBuy > 0)) {
        trace('Quantity to buy: ${quantityToBuy}');
        trace('Sell orders: ${sellOrders}');

        var sellOrder = sellOrders.removeAt(0);

        trace('Checking sell order ${sellOrder} ...');

        var quantityToSell = sellOrder.quantity;
        var quantityBought = min(quantityToSell, quantityToBuy);

        trace('Quantity bought: ${quantityBought}');

        quantityToBuy -= quantityBought;
        quantityToSell -= quantityBought;

        trades.add(Trade(symbol, quantityBought, sellOrder.price));

        trace('Created ${trades.last}');

        _sellOrders.remove(sellOrder);

        trace('Removed order ${sellOrder}');

        if (quantityToSell > 0) {
          // Add a new order with the remaining quantity to sell
          _sellOrders.add(Order(sellOrder.id, symbol, sellOrder.sell, quantityToSell, sellOrder.price));

          trace('Added order ${_sellOrders.last} for the remaining to sell');
        }
      }

      if (quantityToBuy > 0) {
        // Record the buy order
        var order2 = Order(buyOrder.id, symbol, false, quantityToBuy, buyOrder.price);

        _buyOrders.add(order2);

        trace('Recorded buy order ${order2}');
      }
    }
    if (order.sell) {
      var sellOrder = order;

      // Search a buy order with the lowest compatible price
      var buyOrders = _buyOrders.where((e) => e.price >= sellOrder.price).toList();
      buyOrders.sort((a,b) {
        // Match the orders with the smallest difference first
        var diffb = b.price - sellOrder.price;
        var diffa = a.price - sellOrder.price;

        if (diffa == diffb) {
          // If same difference, favor the orders created first
          return a.id.compareTo(b.id);
        }

        return diffb.compareTo(diffa);
      });

      trace('Found buy orders: ${buyOrders}');

      var quantityToSell = sellOrder.quantity;

      while (!buyOrders.isEmpty && (quantityToSell > 0)) {
        trace('Quantity to sell: ${quantityToSell}');
        trace('Buy orders: ${buyOrders}');

        var buyOrder = buyOrders.removeAt(0);

        trace('Checking buy order ${buyOrder} ...');

        var quantityToBuy = buyOrder.quantity;
        var quantitySold = min(quantityToBuy, quantityToSell);

        trace('Quantity sold: ${quantitySold}');

        quantityToBuy -= quantitySold;
        quantityToSell -= quantitySold;

        trades.add(Trade(symbol, quantitySold, buyOrder.price));

        trace('Created ${trades.last}');

        _buyOrders.remove(buyOrder);

        trace('Removed order ${buyOrder}');

        if (quantityToBuy > 0) {
          // Add a new order with the remaining quantity to buy
          _buyOrders.add(Order(buyOrder.id, symbol, buyOrder.sell, quantityToBuy, buyOrder.price));

          trace('Added order ${_buyOrders.last} for the remaining to buy');
        }
      }

      if (quantityToSell > 0) {
        // Record the sell order
        var order2 = Order(sellOrder.id, symbol, true, quantityToSell, sellOrder.price);

        _sellOrders.add(order2);

        trace('Recorded sell order ${order2}');
      }
    }

    return trades;
  }
}

class Trade {
  final String symbol;
  final int quantity;
  final double price;

  Trade(this.symbol, this.quantity, this.price);

  @override
  String toString() => "Trade[${quantity} ${symbol} at ${price.toStringAsFixed(1)}]";
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var orders = <Order>[], orderId = 1;

  for (int i = 0; i < N; i++) {
    var inputs = stdin.readLineSync().split(' ');

    trace("${inputs.join(' ')}");

    orders.add(Order(orderId++, inputs[0], inputs[1] == 'SELL', int.parse(inputs[2]), double.parse(inputs[3])));
  }

  trace("${orders.join('\n')}");

  var ordersBySymbol = Map<String, Orders>();

  var trades = <Trade>[];

  for (var order in orders) {
    var symbolOrders = ordersBySymbol.putIfAbsent(order.symbol, () => Orders(order.symbol));

    trades.addAll(symbolOrders.add(order));
  }

  if (!trades.isEmpty) {
    print(trades.map((t) => '${t.symbol} ${t.quantity} ${t.price.toStringAsFixed(2)}').join('\n'));
  } else {
    print('NO TRADE');
  }
}
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

class Color {
  final String symbol;

  static const Color D = Color('D');
  static const Color H = Color('H');
  static const Color C = Color('C');
  static const Color S = Color('S');

  const Color(this.symbol);

  factory Color.of(String symbol) {
    switch (symbol) {
      case 'D':
        return D;
      case 'H':
        return H;
      case 'C':
        return C;
      case 'S':
        return S;
      default:
        throw "Unexpected symbol: ${symbol}";
    }
  }
}

class Value implements Comparable<Value> {
  final String symbol;
  final int strength;

  static const TWO = Value('2', 2);
  static const THREE = Value('3', 3);
  static const FOUR = Value('4', 4);
  static const FIVE = Value('5', 5);
  static const SIX = Value('6', 6);
  static const SEVEN = Value('7', 7);
  static const EIGHT = Value('8', 8);
  static const NINE = Value('9', 9);
  static const TEN = Value('10', 10);
  static const JOKER = Value('J', 11);
  static const QUEEN = Value('Q', 12);
  static const KING = Value('K', 13);
  static const ACE = Value('A', 14);

  const Value(this.symbol, this.strength);

  factory Value.of(String symbol) {
    switch (symbol) {
      case '2':
        return TWO;
      case '3':
        return THREE;
      case '4':
        return FOUR;
      case '5':
        return FIVE;
      case '6':
        return SIX;
      case '7':
        return SEVEN;
      case '8':
        return EIGHT;
      case '9':
        return NINE;
      case '10':
        return TEN;
      case 'J':
        return JOKER;
      case 'Q':
        return QUEEN;
      case 'K':
        return KING;
      case 'A':
        return ACE;
      default:
        throw "Unexpected symbol: ${symbol}";
    }
  }

  @override
  int compareTo(Value other) => this.strength.compareTo(other.strength);
}

class Card {
  final Value value;
  final Color color;

  Card(this.value, this.color);

  @override
  String toString() => "${value.symbol}${color.symbol}";

  operator <(Card other) => (this.value.compareTo(other.value) < 0);
  operator <=(Card other) => (this.value.compareTo(other.value) <= 0);
  operator >(Card other) => (this.value.compareTo(other.value) > 0);
  operator >=(Card other) => (this.value.compareTo(other.value) >= 0);

  @override
  operator ==(Object other) {
   if (other is Card) {
     return (this.value.compareTo(other.value) == 0);
   }

   return false;
  }

  factory Card.parse(String id) {
    // Example: 4H
    return Card(Value.of(id.substring(0, id.length - 1)), Color.of(id[id.length - 1]));
  }
}

void main() {
  // the number of cards for player 1
  var n = int.parse(stdin.readLineSync());

  trace("${n}");

  // The head of the list represents the top of the stack 
  var cards1 = <Card>[], cards2 = <Card>[];

  for (var i = 0; i < n; i++) {
    // the n cards of player 1
    cards1.add(Card.parse(stdin.readLineSync()));
  }

  trace("${cards1.join('\n')}");

  // the number of cards for player 2
  var m = int.parse(stdin.readLineSync());

  trace("${m}");

  for (var i = 0; i < m; i++) {
    // the m cards of player 2
    cards2.add(Card.parse(stdin.readLineSync()));
  }

  trace("${cards2.join('\n')}");

  var round = 0;

  while (!cards1.isEmpty && !cards2.isEmpty) {
    trace("=== Round ${round + 1} === ");
    trace("Cards 1: ${cards1}");
    trace("Cards 2: ${cards2}");

    var removedCards1 = <Card>[], removedCards2 = <Card>[];

    removedCards1.add(cards1.removeAt(0));
    removedCards2.add(cards2.removeAt(0));
    
    while (removedCards1.last == removedCards2.last) {
      if ((cards1.length < 3) || (cards2.length < 3)) {
        print('PAT');
        return;
      }

      for (var i = 0; i < 3; i++) {
        removedCards1.add(cards1.removeAt(0));
        removedCards2.add(cards2.removeAt(0));
      }

      removedCards1.add(cards1.removeAt(0));
      removedCards2.add(cards2.removeAt(0));
    }
    
    if (removedCards1.last > removedCards2.last) {
      // Player 1 wins this round
      cards1.addAll(removedCards1);
      cards1.addAll(removedCards2);
    } else {
      // Player 2 wins this round
      cards2.addAll(removedCards1);
      cards2.addAll(removedCards2);
    }

    round++;
  }

  if (cards1.isEmpty) {
    print('2 ${round}');
  } else if (cards2.isEmpty) {
    print('1 ${round}');
  }
}
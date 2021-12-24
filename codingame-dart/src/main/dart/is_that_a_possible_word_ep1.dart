import 'dart:io';

class State {

  String name;

  Map<String, State> transitions = Map();

  State(this.name);
}

void main() {
  // the alphabet which can lead to changes in state, separated with space
  var alphabet = stdin.readLineSync().split(' ');

  // the possible states indexed by name
  var states = Map.fromIterable(
      stdin.readLineSync().split(' '),
      key: (e) => e,
      value: (e) => State(e)
  );

  var numberOfTransitions = int.parse(stdin.readLineSync());

  for (int i = 0; i < numberOfTransitions; i++) {
    var chunks = stdin.readLineSync().split(' ');

    var startState = states[chunks[0]];
    var transition = chunks[1];
    var endState = states[chunks[2]];

    startState.transitions[transition] = endState;
  }

  final startState = states[stdin.readLineSync()];
  final endStates = stdin.readLineSync().split(' ').map((e) => states[e]).toList();

  var numberOfWords = int.parse(stdin.readLineSync());

  for (int i = 0; i < numberOfWords; i++) {
    var word = stdin.readLineSync();

    State current = startState;

    for (int i = 0; (i < word.length) && (current != null); i++) {
      final letter = word.substring(i, i+1);
     
      // Transition to the next state based on the letter processed
      current = current.transitions[letter];
    }
    
    if (current == null) {
      print('false');
    } else {
      print(endStates.contains(current));
    }
  }
}
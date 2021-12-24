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
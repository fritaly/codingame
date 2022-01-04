/*
 * Copyright 2015-2022, Francois Ritaly
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

void trace(String message) {
  stderr.writeln("${message}");
}

String draw(int errors) {
  switch (errors) {
    case 0:
      return '+--+\n|\n|\n|\\';
    case 1:
      return '+--+\n|  o\n|\n|\\';
    case 2:
      return '+--+\n|  o\n|  |\n|\\';
    case 3:
      return '+--+\n|  o\n| /|\n|\\';
    case 4:
      return '+--+\n|  o\n| /|\\\n|\\';
    case 5:
      return '+--+\n|  o\n| /|\\\n|\\/';
    case 6:
      return '+--+\n|  o\n| /|\\\n|\\/ \\';
    default:
      throw "Unexpected value: ${errors}";
  }
}

bool equalsIgnoreCase(String s1, String s2) => s1.toLowerCase() == s2.toLowerCase();

class Hangman {
  final String word;
  final List<String> characters = [];
  int errors = 0;

  Hangman(this.word) {
    this.characters.addAll(word.split('').map((c) => (c != ' ') ? '_' : ' '));
  }

  bool get success => characters.every((c) => c != '_');
  bool get failure => characters.any((c) => c == '_') || (errors >= 6);

  void guess(String letter) {
    var found = false;
    for (var n = 0; n < word.length; n++) {
      if (equalsIgnoreCase(word[n], letter) && (characters[n] == '_')) {
        characters[n] = word[n];
        found = true;
      }
    }

    if (!found) {
      errors++;
    }
  }

  @override
  String toString() => "Hangman[${characters.join()}]";
}

void main() {
  var word = stdin.readLineSync();
  var chars = stdin.readLineSync().split(' ').toList();

  trace("${word}");
  trace("${chars.join(' ')}");

  var hangman = Hangman(word);

  for (var letter in chars) {
    hangman.guess(letter);

    trace("${hangman} - ${hangman.success} - ${hangman.errors} error(s)");
  }

  print(draw(hangman.errors));
  print(hangman.characters.join());
}
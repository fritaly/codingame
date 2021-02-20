import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

/// Tells whether the given string denotes a punctuation sign
bool isPunctuation(String s) => [ '.', ',', ';', '!', '?', ':' ].contains(s);

/// Tells whether the given string is a letter
bool isLetter(String s) => 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.contains(s.toUpperCase());

String capitalize(String s) => s[0].toUpperCase() + s.substring(1).toLowerCase();

class Token {
  final String value;

  Token(this.value);

  /// Tells whether the token denotes a punctuation mark
  bool get punctuation => isPunctuation(value);

  /// Tells whether the token denotes a punctuation mark after which words must
  /// be capitalized
  bool get capitalize => isPunctuation(value) && ((value != ',') && (value != ';'));

  @override
  String toString() => "${value}";
}

/// Parses the given string and returns a list of tokens
List<Token> parse(String text) {
  var index = 0, tokens = <Token>[];

  while (index < text.length) {
    var first = text[index];

    if (first == ' ') {
      // Ignore whitespaces
      index++;
      continue;
    }
    if (isPunctuation(first)) {
      tokens.add(Token(first));
      index++;
      continue;
    }

    // Search the end of the word
    var endIndex = index;

    while ((endIndex < text.length) && isLetter(text[endIndex])) {
      endIndex++;
    }

    tokens.add(Token(text.substring(index, endIndex)));

    index = endIndex;
  }

  return tokens;
}

String format(String text) {
  var buffer = StringBuffer();
  Token previous = null;

  for (var token in parse(text)) {
    if (token.punctuation) {
      if (previous == null || !previous.punctuation) {
        // Remove repeated punctuation marks
        buffer.write(token.value);
      }
    } else {
      if (previous == null) {
        // Capialize at the beginning of the sentence
        buffer.write(capitalize(token.value));
      } else if (previous.punctuation) {
        buffer.write(' ');

        if (previous.capitalize) {
          buffer.write(capitalize(token.value));
        } else {
          buffer.write(token.value.toLowerCase());
        }
      } else {
        // Use only lowercase letters
        buffer.write(' ');
        buffer.write(token.value.toLowerCase());
      }
    }

    previous = token;
  }

  return buffer.toString();
}

void main() {
  print(format(stdin.readLineSync()));
}
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

void trace(String message) {
  stderr.writeln("${message}");
}

var map = {
  1: 'one',
  2: 'two',
  3: 'three',
  4: 'four',
  5: 'five',
  6: 'six',
  7: 'seven',
  8: 'eight',
  9: 'nine',
  10: 'ten',
  11: 'eleven',
  12: 'twelve',
  13: 'thirteen',
  14: 'fourteen',
  15: 'fifteen',
  16: 'sixteen',
  17: 'seventeen',
  18: 'eighteen',
  19: 'nineteen'
};

final hundred = 100;
final thousand = 1000;
final million = 1000 * 1000;
final billion = 1000 * 1000 * 1000;
final trillion = 1000 * 1000 * 1000 * 1000;
final quadrillion = 1000 * 1000 * 1000 * 1000 * 1000;
final quintillion = 1000 * 1000 * 1000 * 1000 * 1000 * 1000;

String composite(String s1, String s2) => !s2.isEmpty ? "${s1}-${s2}" : "${s1}";

List<String> split(String number) {
  var list = <String>[];

  while (number.length > 3) {
    list.insert(0, number.substring(number.length - 3));
    number = number.substring(0, number.length - 3);
  }

  list.insert(0, number);

  return list;
}

String format(int n) {
  assert ((0 <= n) && (n < 1000));

  var result = <String>[];

  if (n >= hundred) {
    result.add(format(n ~/ hundred) + " hundred");

    n = n % hundred;
  }

  if (n > 90) {
    result.add(composite("ninety", format(n % 90)));
  } else if (n == 90) {
    result.add("ninety");
  } else if (n > 80) {
    result.add(composite("eighty", format(n % 80)));
  } else if (n == 80) {
    result.add("eighty");
  } else if (n > 70) {
    result.add(composite("seventy", format(n % 70)));
  } else if (n == 70) {
    result.add("seventy");
  } else if (n > 60) {
    result.add(composite("sixty", format(n % 60)));
  } else if (n == 60) {
    result.add("sixty");
  } else if (n > 50) {
    result.add(composite("fifty", format(n % 50)));
  } else if (n == 50) {
    result.add("fifty");
  } else if (n > 40) {
    result.add(composite("forty", format(n % 40)));
  } else if (n == 40) {
    result.add("forty");
  } else if (n > 30) {
    result.add(composite("thirty", format(n % 30)));
  } else if (n == 30) {
    result.add("thirty");
  } else if (n > 20) {
    result.add(composite("twenty", format(n % 20)));
  } else if (n == 20) {
    result.add("twenty");
  } else if (n > 0) {
    result.add(map[n] ?? '');
  } else {
    // n == 0
    if (result.isEmpty) {
      result.add('zero');
    }
  }

  return result.join(' ').trim();
}

String toEnglish(String n) {
  var result = <String>[];

  if (n.startsWith('-')) {
    result.add("negative");

    n = n.substring(1);
  }

  var chunks = split(n);

  trace("${chunks}");

  while (!chunks.isEmpty) {
    var n = int.parse(chunks.removeAt(0));

    if (chunks.length == 6) {
      if (n > 0) {
        result.add(format(n) + " quintillion");
      }
    } else if (chunks.length == 5) {
      if (n > 0) {
        result.add(format(n) + " quadrillion");
      }
    } else if (chunks.length == 4) {
      if (n > 0) {
        result.add(format(n) + " trillion");
      }
    } else if (chunks.length == 3) {
      if (n > 0) {
        result.add(format(n) + " billion");
      }
    } else if (chunks.length == 2) {
      if (n > 0) {
        result.add(format(n) + " million");
      }
    } else if (chunks.length == 1) {
      if (n > 0) {
        result.add(format(n) + " thousand");
      }
    } else if (chunks.length == 0) {
      if (result.isEmpty || (n > 0)) {
        result.add(format(n));
      }
    }
  }

  return result.join(' ').trim();
}

void main() {
  var n = int.parse(stdin.readLineSync());

  for (var i = 0; i < n; i++) {
    var number = stdin.readLineSync();

    trace(number);

    print(toEnglish(number));
  }
}
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

String complement(String binary) {
  return binary.split('').map((c) => (c == '0') ? '1' : '0').join();
}

String reverse(String string) {
  return string.split('').reversed.join();
}

String nameOf(Encoding value) {
  return value.toString().split('.').last;
}

int getDigitFromPattern(String pattern) {
  switch (pattern) {
    case 'LLLLLLRRRRRR':
      return 0;
    case 'LLGLGGRRRRRR':
      return 1;
    case 'LLGGLGRRRRRR':
      return 2;
    case 'LLGGGLRRRRRR':
      return 3;
    case 'LGLLGGRRRRRR':
      return 4;
    case 'LGGLLGRRRRRR':
      return 5;
    case 'LGGGLLRRRRRR':
      return 6;
    case 'LGLGLGRRRRRR':
      return 7;
    case 'LGLGGLRRRRRR':
      return 8;
    case 'LGGLGLRRRRRR':
      return 9;
    default:
      throw "Invalid pattern '${pattern}'";
  }
}

enum Encoding {
  L, R, G
}

class Code {
  final String binary;
  final int digit;
  final Encoding encoding;

  static final List<Code> L_CODES = [
    Code('0001101', 0, Encoding.L),
    Code('0011001', 1, Encoding.L),
    Code('0010011', 2, Encoding.L),
    Code('0111101', 3, Encoding.L),
    Code('0100011', 4, Encoding.L),
    Code('0110001', 5, Encoding.L),
    Code('0101111', 6, Encoding.L),
    Code('0111011', 7, Encoding.L),
    Code('0110111', 8, Encoding.L),
    Code('0001011', 9, Encoding.L)
  ];

  static final Map<String, Code> ALL_CODES = Map<String, Code>.fromIterable(L_CODES
    .expand((lCode) => <Code>[ lCode, lCode.toR(), lCode.toG() ]),
    key: (n) => n.binary
  );

  static Code fromBinary(String binary) {
    var result = ALL_CODES[binary];

    if (result == null) {
      throw "Unknown code: ${binary}";
    }

    return result;
  }

  Code(this.binary, this.digit, this.encoding);

  @override
  toString() {
    return "Code[${digit}, ${encoding}, ${binary}]";
  }

  Code toR() {
    switch (encoding) {
      case Encoding.L:
        return Code(complement(binary), this.digit, Encoding.R);
      case Encoding.R:
        return this;
      case Encoding.G:
        return Code(reverse(binary), this.digit, Encoding.R);
      default:
        throw "Unexpected encoding: ${encoding}";
    }
  }

  Code toG() {
    switch (encoding) {
      case Encoding.L:
        return Code(reverse(toR().binary), this.digit, Encoding.G);
      case Encoding.R:
        return Code(reverse(binary), this.digit, Encoding.G);
      case Encoding.G:
        return this;
      default:
        throw "Unexpected encoding: ${encoding}";
    }
  }
}

class ScanCode {
  static final String LEFT_GUARD = '101';
  static final String RIGHT_GUARD = '101';
  static final String CENTRAL_GUARD = '01010';

  final String _value;

  ScanCode(this._value): assert ((_value != null) && (_value.length == 95));

  String leftGuard() {
    return _value.substring(0, 3);
  }

  String rightGuard() {
    return _value.substring(92, 95);
  }

  String centralGuard() {
    return _value.substring(45, 50);
  }

  String leftPart() {
    return _value.substring(3, 3 + 6 * 7);
  }

  List<Code> parseCodes(String s) {
    return List.generate(6, (index) => index)
        .map((n) => s.substring(n * 7, (n + 1) * 7))
        .map((s) => Code.fromBinary(s))
        .toList();
  }
  
  List<Code> leftCodes() {
    return parseCodes(leftPart());
  }

  String leftPattern() {
    return leftCodes().map((e) => "${nameOf(e.encoding)}").join();
  }

  String rightPart() {
    return _value.substring(3 + 6 * 7 + 5, 3 + 6 * 7 + 5 + 6 * 7);
  }

  List<Code> rightCodes() {
    return parseCodes(rightPart());
  }

  String rightPattern() {
    return rightCodes().map((e) => "${nameOf(e.encoding)}").join();
  }

  /// Decodes the scan code (which may or may not be valid). Returns null if the
  /// decoding fails
  String _decode() {
    if ((leftGuard() != LEFT_GUARD) || (centralGuard() != CENTRAL_GUARD) || (rightGuard() != RIGHT_GUARD)) {
      return null;
    }

    try {
      return "${getDigitFromPattern(leftPattern() + rightPattern())}"
          + leftCodes().map((e) => "${e.digit}").join()
          + rightCodes().map((e) => "${e.digit}").join();
    } catch (e) {
      stderr.writeln("The decoding failed: " + e);

      return null;
    }
  }

  String decode() {
    var decoded = _decode();

    if (decoded != null) {
      return decoded;
    }

    // The decoding failed, try reversing the scan code
    return ScanCode(reverse(_value))._decode();
  }

  bool verify() {
    var decoded = decode();

    if (decoded == null) {
      return false;
    }

    // Multiply every second digit by 3, then add up all the digits. If barcode
    // is correct it's checksum would be a multiple of 10.
    var checksum = List.generate(decoded.length, (index) => index)
        .map((n) => ((n + 1) % 2 == 0) ? int.parse(decoded[n]) * 3 : int.parse(decoded[n]))
        .reduce((value, element) => value + element);

    stderr.writeln("Checksum=${checksum}");

    return (checksum % 10 == 0);
  }
}

void main() {
  var line = stdin.readLineSync();

  stderr.writeln("${line}");

  if (ScanCode(line).verify()) {
    print('${ScanCode(line).decode()}');
  } else {
    print('INVALID SCAN');
  }
}
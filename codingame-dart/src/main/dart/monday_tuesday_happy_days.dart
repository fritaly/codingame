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

class Month {
  final String name;

  Month(this.name);

  /// Returns the number of days in the month
  int days(bool leap) {
    switch (name) {
      case 'Jan':
      case 'Mar':
      case 'May':
      case 'Jul':
      case 'Aug':
      case 'Oct':
      case 'Dec':
        return 31;

      case 'Feb':
        return leap ? 29 : 28;

      case 'Apr':
      case 'Jun':
      case 'Sep':
      case 'Nov':
        return 30;
    }
  }


  @override
  bool operator ==(Object other) {
    if (other is Month) {
      return this.name == other.name;
    }

    return false;
  }

  static List<Month> all() {
    return [
      'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct',
      'Nov', 'Dec'
    ].map((e) => Month(e)).toList();
  }

  static Month fromName(String name) {
    return all().firstWhere((element) => element.name == name);
  }
}

class DayOfYear {
  final Month month;
  final int day;

  DayOfYear(this.month, this.day);

  /// Returns the rank (1-366) of this day in the year. 1st Jan has rank 1
  int rank(bool leap) {
    var result = 0;

    for (Month m in Month.all()) {
      if (m == month) {
        break;
      }

      // Take into account the number of days in the months before
      result += m.days(leap);
    }

    return result + day;
  }
}

void main() {
  var leapYear = stdin.readLineSync() == '1';

  stderr.writeln("${leapYear ? '1' : '0'}");

  var inputs = stdin.readLineSync().split(' ');

  stderr.writeln("${inputs.join(' ')}");

  var sourceDayOfWeek = inputs[0];
  var sourceMonth = inputs[1];
  var sourceDayOfMonth = int.parse(inputs[2]);

  var sourceDay = new DayOfYear(Month.fromName(sourceMonth), sourceDayOfMonth);

  inputs = stdin.readLineSync().split(' ');

  stderr.writeln("${inputs.join(' ')}");

  var targetMonth = inputs[0];
  var targetDayOfMonth = int.parse(inputs[1]);

  var targetDay = new DayOfYear(Month.fromName(targetMonth), targetDayOfMonth);

  var delta = targetDay.rank(leapYear) - sourceDay.rank(leapYear);

  var daysOfWeek = [ 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday' ];

  var targetDayOfWeek = daysOfWeek[(daysOfWeek.indexOf(sourceDayOfWeek) + delta) % 7];

  print(targetDayOfWeek);
}
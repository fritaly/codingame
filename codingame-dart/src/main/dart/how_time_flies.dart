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

bool leapYear(int year) => (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
int daysInYear(int year) => leapYear(year) ? 366 : 365;
int daysInMonth(int year, int month) {
  switch (month) {
    case 1:
    case 3:
    case 5:
    case 7:
    case 8:
    case 10:
    case 12:
      return 31;
    case 2:
      return leapYear(year) ? 29 : 28;
    case 4:
    case 6:
    case 9:
    case 11:
      return 30;
    default:
      throw "Unexpected month: ${month}";
  }
}

class Date implements Comparable<Date> {
  final int day, month, year;

  Date(this.day, this.month, this.year);

  @override
  String toString() => "${day}.${month}.${year}";

  factory Date.parse(String text) {
    var list = text.split('.').map((e) => int.parse(e)).toList();

    return Date(list[0], list[1], list[2]);
  }

  Date addYear(int n) => Date(day, month, year + n);

  Date addMonth(int n) {
    if (month == 12) {
      return Date(day, 1, year + 1);
    }

    return Date(day, month + n, year);
  }

  Date addDay(int n) {
    if (day + n > daysInMonth(year, month)) {
      if (month == 12) {
        return Date(1, 1, year + 1);
      } else {
        return Date(1, month + 1, year);
      }
    }

    return Date(day + n, month, year);
  }

  String difference(Date other) {
    assert (this < other);

    var diffYears = 0, diffMonths = 0, diffDays = 0;
    var current = Date(day, month, year);

    while (current.addYear(1) <= other) {
      diffYears++;
      current = current.addYear(1);
    }
    while (current.addMonth(1) <= other) {
      diffMonths++;
      current = current.addMonth(1);
    }
    while (current.addDay(1) <= other) {
      diffDays++;
      current = current.addDay(1);
    }

    var strings = <String>[];

    if (diffYears == 1) {
      strings.add("1 year");
    } else if (diffYears > 1) {
      strings.add("${diffYears} years");
    }

    if (diffMonths == 1) {
      strings.add("1 month");
    } else if (diffMonths > 1) {
      strings.add("${diffMonths} months");
    }

    /* if (diffDays == 1) {
      strings.add("1 day");
    } else if (diffDays > 1) {
      strings.add("${diffDays} days");
    } */

    return strings.join(', ');
  }

  int get quantified {
    var result = 0;

    // 1st January 1970 is day 1
    var currentYear = 1970, currentMonth = 1, currentDay = 1;

    while (currentYear < year) {
      result += daysInYear(currentYear++);
    }
    while (currentMonth < month) {
      result += daysInMonth(currentYear, currentMonth++);
    }
    if (currentDay < day) {
      result += (day - currentDay);
    }

    return result;
  }

  @override
  operator ==(Object other) {
    if (other is Date) {
      return (this.compareTo(other) == 0);
    }

    return false;
  }

  operator <(Date other) => (this.compareTo(other) < 0);
  operator <=(Date other) => (this.compareTo(other) <= 0);

  @override
  int compareTo(Date other) {
    if (this.year == other.year) {
      if (this.month == other.month) {
        return this.day.compareTo(other.day);
      }

      return this.month.compareTo(other.month);
    }

    return this.year.compareTo(other.year);
  }
}

void main() {
  var begin = Date.parse(stdin.readLineSync());
  var end = Date.parse(stdin.readLineSync());

  trace("${begin}");
  trace("${end}");

  // 'YY year[s], MM month[s], total NN days'
  var difference = begin.difference(end);

  if (difference.isEmpty) {
    print("total ${end.quantified - begin.quantified} days");
  } else {
    print("${difference}, total ${end.quantified - begin.quantified} days");
  }
}
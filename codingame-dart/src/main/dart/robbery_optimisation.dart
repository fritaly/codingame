import 'dart:io';
import 'dart:math';

void trace(String message) {
  stderr.writeln("${message}");
}

// Map used for memoizing the results returned by the method find()
var cache = Map<int, int>();

int find(List<int> values, int index) {
  if (!cache.containsKey(index)) {
    // Rob the first house ?
    var sum1 = 0;

    if (values[index] < 0) {
      // Ignore this house (negative value)

      if (index + 1 < values.length) {
        // Check the next house
        sum1 += find(values, index + 1);
      }
    } else {
      // Rob the house
      sum1 = values[index];

      if (index + 2 < values.length) {
        // Check the remaining houses
        sum1 += find(values, index + 2);
      }
    }

    // Rob the second house (if any) ?
    var sum2 = 0;

    if (index + 1 < values.length) {
      if (values[index + 1] < 0) {
        // Ignore this house (negative value)

        if (index + 2 < values.length) {
          // Check the next house
          sum2 += find(values, index + 2);
        }
      } else {
        // Rob the house
        sum2 = values[index + 1];

        if (index + 3 < values.length) {
          // Check the remaining houses
          sum2 += find(values, index + 3);
        }
      }
    }

    // Keep the highest sum
    cache[index] = max(sum1, sum2);
  }

  // Return the cached result
  return cache[index];
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var values = <int>[];

  for (int i = 0; i < N; i++) {
    values.add(int.parse(stdin.readLineSync()));
  }

  trace("${values.join('\n')}");

  print(find(values, 0));
}
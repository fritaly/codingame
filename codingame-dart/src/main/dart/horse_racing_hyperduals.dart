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

class Horse {
  
  int velocity, elegance;

  Horse(this.velocity, this.elegance);

  int distance(Horse other) {
    return (this.velocity - other.velocity).abs() + (this.elegance - other.elegance).abs();
  }
}

void main() {
  var N = int.parse(stdin.readLineSync());

  var horses = List<Horse>();

  for (int i = 0; i < N; i++) {
    var chunks = stdin.readLineSync().split(' ');

    var horse = new Horse(int.parse(chunks[0]), int.parse(chunks[1]));

    horses.add(horse);
  }

  var minDistance = double.maxFinite.toInt();

  for (int i = 0; i < N; i++) {
    for (int j = i+1; j < N; j++) {
      var distance = horses[i].distance(horses[j]);

      if (distance < minDistance) {
        minDistance = distance;
      }
    }
  }

  print('${minDistance}');
}
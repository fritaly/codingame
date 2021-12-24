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
import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

enum Color {
  GREEN, RED
}

class TrafficLight {
  final int distance, duration;

  TrafficLight(this.distance, this.duration);

  /// Returns the time necessary to reach the traffic light at the given speed.
  /// The speed is in km/h, the distance in meters. The resulting time is in
  /// seconds.
  int time(int speed) => ((3600 * distance) / (1000 * speed)).round();

  Color getColor(int speed) {
    // How long to reach the traffic light at the given speed ? What color will
    // the traffic light be by that time ?
    var timeSeconds = time(speed) % (2 * duration);

    return (timeSeconds < duration) ? Color.GREEN : Color.RED;
  }

  @override
  String toString() => "TrafficLight[distance: ${distance}, duration: ${duration}]";
}

void main() {
  var maxSpeed = int.parse(stdin.readLineSync());
  var lightCount = int.parse(stdin.readLineSync());

  trace("${maxSpeed}");
  trace("${lightCount}");

  var trafficLights = <TrafficLight>[];

  for (int i = 0; i < lightCount; i++) {
    var inputs = stdin.readLineSync().split(' ').map((e) => int.parse(e)).toList();

    trace("${inputs.join(' ')}");

    trafficLights.add(TrafficLight(inputs[0], inputs[1]));
  }

  trace("${trafficLights.join('\n')}");

  var speed = maxSpeed;

  while (speed > 0) {
    if (trafficLights.every((e) => e.getColor(speed) == Color.GREEN)) {
      print(speed);
      break;
    }

    speed--;
  }
}
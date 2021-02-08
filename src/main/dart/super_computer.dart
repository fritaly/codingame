import 'dart:io';

void trace(String message) {
  stderr.writeln("${message}");
}

class Task implements Comparable<Task> {
  final int startDay;
  final int duration;

  Task(this.startDay, this.duration);

  int get endDay => startDay + duration - 1;

  @override
  int compareTo(Task that) {
    if (this.endDay == that.endDay) {
      return this.duration.compareTo(that.duration);
    }

    return this.endDay.compareTo(that.endDay);
  }

  @override
  String toString() => "Task[${startDay}-${endDay}, duration: ${duration}d]";

  /// Tells whether this task ends before the given day 
  bool endsBefore(int day) => (endDay < day);

  /// Tells whether this task starts after the given day
  bool startsAfter(int day) => (startDay > day);

  /// Tells whether the given day belongs to this task's time span
  bool contains(int day) => (startDay <= day) && (day <= endDay);

  /// Tells whether this task overlaps with the given task
  bool overlaps(Task task) => contains(task.startDay) || contains(task.endDay) || task.contains(this.startDay) || task.contains(this.endDay);
}

/// Plans the execution of the given tasks
int plan(List<Task> tasks) {
  var plannedTasks = 0;
  var endDay = 0;
  var index = 0;

  // Sorts the tasks by ascending end date (to process the tasks finishing early
  // first)
  tasks.sort();

  while (index < tasks.length) {
    var task = tasks[index];

    if (task.startsAfter(endDay)) {
      // No overlap, plan the task
      plannedTasks++;
      index++;
      endDay = task.endDay;
    } else {
      // Overlap, skip the task
      index++;
    }
  }

  return plannedTasks;
}

void main() {
  var N = int.parse(stdin.readLineSync());

  trace("${N}");

  var tasks = <Task>[];

  for (int i = 0; i < N; i++) {
    var inputs = stdin.readLineSync().split(' ');

    tasks.add(Task(int.parse(inputs[0]), int.parse(inputs[1])));
  }

  trace("${tasks.join('\n')}");

  var result = plan(tasks);

  print('${result}');
}
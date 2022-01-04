/**
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
package codingame.ngr_basic_radar;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class Record {
        final String plate, radar;
        final Instant timestamp;

        Record(Scanner scanner) {
            this.plate = scanner.next();
            this.radar = scanner.next();
            this.timestamp = Instant.ofEpochMilli(scanner.nextLong());

            System.err.printf("%s %s %d%n", plate, radar, timestamp.toEpochMilli());
        }
    }

    private static class Data {
        Record start, end;

        boolean isComplete() {
            return (start != null) && (end != null);
        }

        String plate() {
            return (start != null) ? start.plate : (end != null) ? end.plate : null;
        }

        Integer speed() {
            if (!isComplete()) {
                return null;
            }

            return (int) Math.floor(TimeUnit.HOURS.toMillis(1) * 13.0 / (Duration.between(start.timestamp, end.timestamp).toMillis()));
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int N = scanner.nextInt();

        System.err.println(N);

        final List<Record> records = IntStream.range(0, N)
                .mapToObj(i -> new Record(scanner))
                .collect(Collectors.toList());

        final Map<String, Data> map = new TreeMap<>();

        for (Record record : records) {
            if (!map.containsKey(record.plate)) {
                map.put(record.plate, new Data());
            }

            final Data data = map.get(record.plate);

            if ("A21-42".equals(record.radar)) {
                data.start = record;
            } else {
                data.end = record;
            }
        }

        map.values().stream()
                .filter(d -> d.speed() != null)
                .filter(d -> d.speed() > 130)
                .sorted(Comparator.comparing(a -> a.plate()))
                .forEach(r -> System.out.printf("%s %d%n", r.plate(), r.speed()));
    }
}
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
package codingame.hello_world;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class GeoLocation {
        final Latitude latitude;
        final Longitude longitude;

        private GeoLocation(String data) {
            final String[] array = data.split(" ");

            this.latitude = new Latitude(array[0]);
            this.longitude = new Longitude(array[1]);
        }

        int distanceTo(GeoLocation that) {
            // See formula at https://en.wikipedia.org/wiki/Great-circle_distance
            double deltaLambda = Math.abs(this.longitude.angle() - that.longitude.angle());

            // Compute the "central angle" in radians
            double deltaSigma = Math.acos(
                    Math.sin(this.latitude.angle()) * Math.sin(that.latitude.angle())
                    +
                    Math.cos(this.latitude.angle()) * Math.cos(that.latitude.angle()) * Math.cos(deltaLambda));

            // Distance = angle * radius
            return (int) Math.round(deltaSigma * 6371);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", GeoLocation.class.getSimpleName() + "[", "]")
                    .add("latitude=" + latitude)
                    .add("longitude=" + longitude)
                    .toString();
        }
    }

    private static class Latitude {
        // Example: "Nddmmss"
        private final String value;

        private Latitude(String value) {
            this.value = value;
        }

        boolean isNorth() {
            return value.startsWith("N");
        }

        int degrees() {
            return Integer.parseInt(value.substring(1, 3));
        }

        int minutes() {
            return Integer.parseInt(value.substring(3, 5));
        }

        int seconds() {
            return Integer.parseInt(value.substring(5));
        }

        double angle() {
            double angle = degrees() + (minutes() / 60.0) + (seconds() / 3600.0);

            return Math.toRadians(isNorth() ? angle : -angle);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Latitude.class.getSimpleName() + "[", "]")
                    .add("value='" + value + "'")
                    .toString();
        }
    }

    private static class Longitude {
        // Example: "Edddmmss"
        private final String value;

        private Longitude(String value) {
            this.value = value;
        }

        boolean isEast() {
            return value.startsWith("E");
        }

        int degrees() {
            return Integer.parseInt(value.substring(1, 4));
        }

        int minutes() {
            return Integer.parseInt(value.substring(4, 6));
        }

        int seconds() {
            return Integer.parseInt(value.substring(6));
        }

        double angle() {
            double angle = degrees() + (minutes() / 60.0) + (seconds() / 3600.0);

            return Math.toRadians(isEast() ? angle : -angle);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Longitude.class.getSimpleName() + "[", "]")
                    .add("value='" + value + "'")
                    .toString();
        }
    }

    private static class Capital {
        final String name;
        final GeoLocation location;
        String message;

        private Capital(String data) {
            final String[] array = data.split(" ");

            this.name = array[0];
            this.location = new GeoLocation(array[1] + " " + array[2]);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Capital.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("location=" + location)
                    .add("message='" + message + "'")
                    .toString();
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int numberOfCapitals = scanner.nextInt();
        final int numberOfGeolocations = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<Capital> capitals = IntStream
                .range(0, numberOfCapitals)
                .mapToObj(i -> new Capital(scanner.nextLine()))
                .collect(Collectors.toList());

        for (Capital capital : capitals) {
            capital.message = scanner.nextLine();
        }

        System.err.println(String.join("\n", capitals.stream().map(c -> c.toString()).collect(Collectors.toList())));

        final List<GeoLocation> locations = IntStream
                .range(0, numberOfGeolocations)
                .mapToObj(i -> new GeoLocation(scanner.nextLine()))
                .collect(Collectors.toList());

        System.err.println(locations.stream().map(l -> l.toString()).collect(Collectors.joining("\n")));

        for (GeoLocation location : locations) {
            // Find the minimal distance
            final int minDistance = capitals
                    .stream()
                    .mapToInt(capital -> capital.location.distanceTo(location))
                    .min()
                    .getAsInt();

            // Find the closest capitals
            final List<Capital> closestCapitals = capitals
                    .stream()
                    .filter(capital -> capital.location.distanceTo(location) == minDistance)
                    .collect(Collectors.toList());

            System.out.println(closestCapitals.stream().map(c -> c.message).collect(Collectors.joining(" ")));
        }
    }
}
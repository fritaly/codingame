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
package codingame.personal_best;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class Gymnast {
        final String name;
        final float bars, beam, floor;

        Gymnast(String name, float bars, float beam, float floor) {
            this.name = name;
            this.bars = bars;
            this.beam = beam;
            this.floor = floor;
        }

        Gymnast(Scanner scanner) {
            final String[] array = scanner.nextLine().split(",");

            this.name = array[0];
            this.bars = Float.parseFloat(array[1]);
            this.beam = Float.parseFloat(array[2]);
            this.floor = Float.parseFloat(array[3]);
        }

        String render(List<String> categories) {
            final ArrayList<Float> list = new ArrayList<>();
            if (categories.contains("bars")) {
                list.add(bars);
            }
            if (categories.contains("beam")) {
                list.add(beam);
            }
            if (categories.contains("floor")) {
                list.add(floor);
            }
            return list.stream().map(f -> new DecimalFormat("#.##").format(f)).collect(Collectors.joining(","));
        }

        Gymnast merge(Gymnast other) {
            return new Gymnast(this.name, Math.max(this.bars, other.bars), Math.max(this.beam, other.beam), Math.max(this.floor, other.floor));
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final String[] gymnasts = scanner.nextLine().split(",");
        final List<String> categories = Arrays.asList(scanner.nextLine().split(","));
        final int N = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<Gymnast> list = IntStream.range(0, N).mapToObj(i -> new Gymnast(scanner)).collect(Collectors.toList());

        final Map<String, Gymnast> map = new TreeMap<>();

        for (Gymnast gymnast : list) {
            if (!map.containsKey(gymnast.name)) {
                map.put(gymnast.name, gymnast);
            } else {
                map.put(gymnast.name, map.get(gymnast.name).merge(gymnast));
            }
        }

        for (String gymnast : gymnasts) {
            System.out.println(map.get(gymnast).render(categories));;
        }
    }
}
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
package codingame.bender_episode_3;

import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private enum Complexity {
        CONSTANT("O(1)", (s) -> s.elapsed),
        LOGARITHMIC("O(log n)", (s) -> s.elapsed / Math.log(s.n)),
        LINEAR("O(n)", (s) -> s.elapsed / s.n),
        LOG_LINEAR("O(n log n)", (s) -> s.elapsed / (s.n * Math.log(s.n))),
        QUADRATIC("O(n^2)", (s) -> s.elapsed / Math.pow(s.n, 2)),
        LOG_QUADRATIC("O(n^2 log n)", (s) -> s.elapsed / (Math.pow(s.n, 2) * Math.log(s.n))),
        CUBIC("O(n^3)", (s) -> s.elapsed / Math.pow(s.n, 2.1)), // for validator #7
        EXPONENTIAL("O(2^n)", (s) -> s.elapsed / Math.pow(2, s.n));

        final String label;
        final ToDoubleFunction<Sample> function;

        Complexity(String label, ToDoubleFunction<Sample> function) {
            this.label = label;
            this.function = function;
        }
    }

    private static class Sample {
        final double n, elapsed;

        private Sample(long n, long elapsed) {
            this.n = n;
            this.elapsed = elapsed;
        }
    }

    public static double mean(double[] array) {
        return sum(array) / array.length;
    }

    public static double variance(double[] array) {
        final double mean = mean(array);

        double variance = 0;

        for (double n: array) {
            variance += Math.pow(n - mean, 2);
        }

        return variance / Math.pow(mean, 2);
    }

    private static double sum(double[] array) {
        double result = 0;

        for (double n : array) {
            result += n;
        }

        return result;
    }

    private static class Statistics {
        final Complexity complexity;
        final double variance;

        private Statistics(Complexity complexity, double variance) {
            this.complexity = complexity;
            this.variance = variance;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Statistics.class.getSimpleName() + "[", "]")
                    .add("variance=" + variance)
                    .toString();
        }
    }

    private static Statistics process(List<Sample> samples, Complexity complexity) {
        final double[] values = samples.stream().mapToDouble(complexity.function).toArray();

        final Statistics statistics = new Statistics(complexity, variance(values));

        System.err.printf("%s: variance=%e%n", complexity, statistics.variance);

        return statistics;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int N = scanner.nextInt();

        System.err.println(N);

        final List<Sample> samples = IntStream.range(0, N)
                .mapToObj(i -> new Sample(scanner.nextLong(), scanner.nextLong()))
                .collect(Collectors.toList());

        for (Sample sample : samples) {
            System.err.printf("%d %d%n", (int) sample.n, (int) sample.elapsed);
        }

        System.out.println(Arrays.asList(Complexity.values())
                .stream().map(c -> process(samples, c))
                .min(Comparator.comparingDouble(a -> a.variance))
                .get().complexity.label);
    }
}
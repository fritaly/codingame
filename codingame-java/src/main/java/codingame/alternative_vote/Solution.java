/**
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
package codingame.alternative_vote;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static class Voter {

        final List<String> votes;

        Voter(String line, List<String> candidates) {
            this.votes = Arrays.stream(line.split(" "))
                    .mapToInt(s -> Integer.parseInt(s))
                    .mapToObj(i -> candidates.get(i - 1))
                    .collect(Collectors.toList());
        }

        String vote(List<String> candidates) {
            for (String vote : votes) {
                if (candidates.contains(vote)) {
                    return vote;
                }
            }

            throw new IllegalStateException();
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int C = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<String> candidates = IntStream.range(0, C)
                .mapToObj(i -> scanner.nextLine())
                .collect(Collectors.toList());

        int V = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<Voter> voters = IntStream.range(0, V)
                .mapToObj(i -> new Voter(scanner.nextLine(), candidates))
                .collect(Collectors.toList());

        final List<String> remainingCandidates = new ArrayList<>(candidates);

        while (remainingCandidates.size() > 1) {
            final Map<String, Integer> votes = new LinkedHashMap<>();
            remainingCandidates.forEach(name -> votes.put(name, 0));

            for (Voter voter : voters) {
                final String candidate = voter.vote(remainingCandidates);

                votes.put(candidate, votes.get(candidate) + 1);
            }

            // Find the candidate with the least votes
            String found = null;
            int minVotes = Integer.MAX_VALUE;

            for (Map.Entry<String, Integer> entry : votes.entrySet()) {
                if (entry.getValue() < minVotes) {
                    minVotes = entry.getValue();
                    found = entry.getKey();
                }
            }

            System.out.println(found);

            remainingCandidates.remove(found);
        }

        System.out.println("winner:" + remainingCandidates.iterator().next());
    }
}
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
package codingame.texas_holdem;

import java.util.*;
import java.util.stream.Collectors;

class Solution {

    private enum Value {
        TWO('2'),
        THREE('3'),
        FOUR('4'),
        FIVE('5'),
        SIX('6'),
        SEVEN('7'),
        EIGHT('8'),
        NINE('9'),
        TEN('T'),
        JOKER('J'),
        QUEEN('Q'),
        KING('K'),
        ACE('A');

        final char symbol;

        Value(char symbol) {
            this.symbol = symbol;
        }

        static Value fromSymbol(char c) {
            for (Value value : values()) {
                if (value.symbol == c) {
                    return value;
                }
            }

            throw new RuntimeException("Unexpected char: " + c);
        }

        Value next() {
            return equals(ACE) ? TWO : values()[ordinal() + 1];
        }
    }

    private enum Suit {
        C, D, H, S
    }

    private static class Card implements Comparable<Card> {
        final Value value;
        final Suit suit;

        private Card(Value value, Suit suit) {
            this.value = value;
            this.suit = suit;
        }

        @Override
        public int compareTo(Card other) {
            return this.value.compareTo(other.value);
        }

        static Card parse(String s) {
            return new Card(Value.fromSymbol(s.charAt(0)), Suit.valueOf(s.substring(1)));
        }

        static List<Card> parseCards(String string) {
            return Arrays.asList(string.split(" ")).stream().map(s -> Card.parse(s)).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return value.symbol + suit.name();
        }
    }

    private enum HandType {
        HIGH_CARD,
        PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH
    }

    private static int compare(List<Card> cards1, List<Card> cards2) {
        // Sort the cards from highest to lowest
        Collections.sort(cards1, Comparator.reverseOrder());
        Collections.sort(cards2, Comparator.reverseOrder());

        for (int i = 0; i < cards1.size(); i++) {
            final Card card1 = cards1.get(i), card2 = cards2.get(i);

            if (card1.value != card2.value) {
                return card1.value.compareTo(card2.value);
            }
        }

        return 0;
    }

    private static class Hand implements Comparable<Hand> {
        final HandType type;
        final List<Card> cards, kickers;

        private Hand(HandType type, List<Card> cards, List<Card> kickers) {
            if (cards.size() + kickers.size() > 5) {
                throw new IllegalArgumentException("The total number of cards should be 5 (cards: " + cards + ", kickers: " + kickers + ")");
            }

            this.type = type;
            this.cards = cards;
            this.kickers = kickers;
        }

        @Override
        public int compareTo(Hand other) {
            if (this.type == other.type) {
                if (type == HandType.STRAIGHT_FLUSH) {
                    final Card best1 = this.cards.stream().max(Comparator.comparing(a -> a.value)).get();
                    final Card best2 = other.cards.stream().max(Comparator.comparing(a -> a.value)).get();

                    return best1.value.compareTo(best2.value);
                }
                if (type == HandType.FOUR_OF_A_KIND) {
                    final Card best1 = this.cards.stream().max(Comparator.comparing(a -> a.value)).get();
                    final Card best2 = other.cards.stream().max(Comparator.comparing(a -> a.value)).get();

                    if (best1.value == best2.value) {
                        // Compare the kickers
                        return compare(this.kickers, other.kickers);
                    }

                    return best1.value.compareTo(best2.value);
                }
                if (type == HandType.FULL_HOUSE) {
                    // The cards represent the 3 of a kind and the kickers the pair
                    final Card best1 = this.cards.stream().max(Comparator.comparing(a -> a.value)).get();
                    final Card best2 = other.cards.stream().max(Comparator.comparing(a -> a.value)).get();

                    if (best1.value == best2.value) {
                        // Compare the kickers (the pair actually)
                        return compare(this.kickers, other.kickers);
                    }

                    return best1.value.compareTo(best2.value);
                }
                if (type == HandType.FLUSH) {
                    return compare(this.cards, other.cards);
                }
                if (type == HandType.STRAIGHT) {
                    return compare(this.cards, other.cards);
                }
                if (type == HandType.THREE_OF_A_KIND) {
                    final Card best1 = this.cards.stream().max(Comparator.comparing(a -> a.value)).get();
                    final Card best2 = other.cards.stream().max(Comparator.comparing(a -> a.value)).get();

                    if (best1.value == best2.value) {
                        // Compare the kickers
                        return compare(this.kickers, other.kickers);
                    }

                    return best1.value.compareTo(best2.value);
                }
                if (type == HandType.TWO_PAIR) {
                    final int result = compare(this.cards, other.cards);

                    if (result == 0) {
                        // Compare the kickers
                        return compare(this.kickers, other.kickers);
                    }

                    return result;
                }
                if (type == HandType.PAIR) {
                    final int result = compare(this.cards, other.cards);

                    if (result == 0) {
                        // Compare the kickers
                        return compare(this.kickers, other.kickers);
                    }

                    return result;
                }
                if (type == HandType.HIGH_CARD) {
                    return compare(this.cards, other.cards);
                }

                throw new IllegalStateException();
            }

            return this.type.compareTo(other.type);
        }

        @Override
        public String toString() {
            return cards.stream().sorted(Comparator.reverseOrder()).map(c -> Character.toString(c.value.symbol)).collect(Collectors.joining())
                    + kickers.stream().sorted(Comparator.reverseOrder()).map(c -> Character.toString(c.value.symbol)).collect(Collectors.joining());
        }
    }

    private static boolean sameSuit(List<Card> cards) {
        return cards.stream().map(c -> c.suit).distinct().count() == 1;
    }

    private static boolean consecutive(List<Card> cards) {
        final List<Card> list = new ArrayList<>(cards);

        // Sort the cards by natural order (ACE is the highest card) from highest to lowest
        Collections.sort(list, Comparator.reverseOrder());

        boolean consecutive = true;

        for (int i = 0; i < list.size() - 1; i++) {
            final Card card1 = list.get(i), card2 = list.get(i+1);

            if (card1.value.next() != card2.value) {
                consecutive = false;
                break;
            }
        }

        if (!consecutive) {
            // Sort the cards again (ACE is the lowest card now)
            Collections.sort(list, (a, b) -> {
                if (a.value == Value.ACE) {
                    return (b.value == Value.ACE) ? 0 : -1; // a < b
                }
                if (b.value == Value.ACE) {
                    return +1; // a > b
                }

                return a.value.compareTo(b.value);
            });

            consecutive = true;

            for (int i = 0; i < list.size() - 1; i++) {
                final Card card1 = list.get(i), card2 = list.get(i+1);

                if (card1.value.next() != card2.value) {
                    consecutive = false;
                    break;
                }
            }
        }

//        // Reverse the cards (lowest -> highest) -> (highest -> lowest)
//        Collections.reverse(list);
//
//        if (consecutive) {
//            // Use a side effect (don't do this, this is wrong !!!) to propagate the sorted cards to the caller
//            cards.clear();
//            cards.addAll(list);
//        }

        return consecutive;
    }

    private static Map<Value, Integer> countValues(List<Card> cards) {
        final Map<Value, Integer> result = new TreeMap<>();

        for (Card card : cards) {
            if (!result.containsKey(card.value)) {
                result.put(card.value, 1);
            } else {
                result.put(card.value, result.get(card.value) + 1);
            }
        }

        return result;
    }

    /**
     * <p>Analyzes the given list of 7 cards and returns the best hand found.</p>
     */
    private static Hand analyze7(List<Card> cards) {
        Hand result = null;

        for (List<Card> combination : skipTwo(cards)) {
            final Hand hand = analyze5(combination);

            if ((result == null) || (hand.compareTo(result) > 0)) {
                result = hand;
            }
        }

        return result;
    }

    /**
     * <p>Analyzes the given list of 5 cards and returns the best hand found.</p>
     */
    private static Hand analyze5(List<Card> cards) {
        if (sameSuit(cards) && consecutive(cards)) {
            return new Hand(HandType.STRAIGHT_FLUSH, cards, Collections.emptyList());
        }

        // Count the number of times a given card value appears
        final Map<Value, Integer> counts = countValues(cards);

        if (counts.containsValue(4)) {
            final List<Card> main = new ArrayList<>(), kickers = new ArrayList<>();

            for (Map.Entry<Value, Integer> entry : counts.entrySet()) {
                if (entry.getValue() == 4) {
                    main.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                } else {
                    kickers.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                }
            }

            return new Hand(HandType.FOUR_OF_A_KIND, main, kickers);
        }
        if (counts.containsValue(3) && counts.containsValue(2)) {
            final List<Card> main = new ArrayList<>(), kickers = new ArrayList<>();

            for (Map.Entry<Value, Integer> entry : counts.entrySet()) {
                if (entry.getValue() == 3) {
                    main.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                } else {
                    kickers.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                }
            }

            return new Hand(HandType.FULL_HOUSE, main, kickers);
        }
        if (sameSuit(cards)) {
            return new Hand(HandType.FLUSH, cards, Collections.emptyList());
        }
        if (consecutive(cards)) {
            return new Hand(HandType.STRAIGHT, cards, Collections.emptyList());
        }
        if (counts.entrySet().stream().filter(e -> e.getValue() == 2).count() == 2) {
            final List<Card> main = new ArrayList<>(), kickers = new ArrayList<>();

            for (Map.Entry<Value, Integer> entry : counts.entrySet()) {
                if (entry.getValue() == 2) {
                    main.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                } else {
                    kickers.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                }
            }

            return new Hand(HandType.TWO_PAIR, main, kickers);
        }
        if (counts.entrySet().stream().filter(e -> e.getValue() == 2).count() == 1) {
            final List<Card> main = new ArrayList<>(), kickers = new ArrayList<>();

            for (Map.Entry<Value, Integer> entry : counts.entrySet()) {
                if (entry.getValue() == 2) {
                    main.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                } else {
                    kickers.addAll(cards.stream().filter(c -> c.value == entry.getKey()).collect(Collectors.toList()));
                }
            }

            return new Hand(HandType.PAIR, main, kickers);
        }

        return new Hand(HandType.HIGH_CARD, cards, Collections.emptyList());
    }

    /**
     * <p>Returns all the combinations of cards created by removing one card from the given list of cards.</p>
     */
    private static List<List<Card>> skipOne(List<Card> cards) {
        final List<List<Card>> result = new ArrayList<>();

        for (Card card : cards) {
            final List<Card> list = new ArrayList<>(cards);
            list.remove(card);

            result.add(list);
        }

        return result;
    }

    /**
     * <p>Returns all the combinations of cards created by removing two cards from the given list of cards.</p>
     */
    private static List<List<Card>> skipTwo(List<Card> cards) {
        final List<List<Card>> result = new ArrayList<>();

        for (List<Card> list : skipOne(cards)) {
            result.addAll(skipOne(list));
        }

        return result;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final String player1 = scanner.nextLine();
        final String player2 = scanner.nextLine();
        final String communityCards = scanner.nextLine();

        System.err.println(player1);
        System.err.println(player2);
        System.err.println(communityCards);

        final List<Card> cards1 = Card.parseCards(player1);
        cards1.addAll(Card.parseCards(communityCards));

        System.err.println(cards1);

        final List<Card> cards2 = Card.parseCards(player2);
        cards2.addAll(Card.parseCards(communityCards));

        System.err.println(cards2);

        // Find the best hand for each player by picking 5 cards out of the 7 available
        final Hand hand1 = analyze7(cards1), hand2 = analyze7(cards2);

        if (hand1.compareTo(hand2) < 0) {
            System.out.println("2 " + hand2.type.name() + " " + hand2);
        } else if (hand1.compareTo(hand2) > 0) {
            System.out.println("1 " + hand1.type.name() + " " + hand1);
        } else {
            System.out.println("DRAW");
        }
    }
}
package codingame.scrabble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

class Solution {
	
	private static int getPoints(String word) {
		int score = 0;
		
		for (char c : word.toCharArray()) {
			final String value = Character.toString(c);
			
			if ("eaionrtlsu".contains(value)) {
				score += 1;
			} else if ("dg".contains(value)) {
				score += 2;
			} else if ("bcmp".contains(value)) {
				score += 3;
			} else if ("fhvwy".contains(value)) {
				score += 4;
			} else if ("k".contains(value)) {
				score += 5;
			} else if ("jx".contains(value)) {
				score += 8;
			} else if ("qz".contains(value)) {
				score += 10;
			}
		}
		
		return score;
	}

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numberOfWords = in.nextInt();
        in.nextLine();
        
        // Map containing all the words with a given letter
        final Map<Character, Set<String>> wordsByLetter = new TreeMap<Character, Set<String>>();
        
        // List of all the dictionary words
        final List<String> allWords = new ArrayList<String>();
        
        for (char c = 'a'; c <= 'z'; c++) {
        	wordsByLetter.put(c, new TreeSet<String>());
		}
        
        for (int i = 0; i < numberOfWords; i++) {
            final String word = in.nextLine();
            
            for (char c : word.toCharArray()) {
				wordsByLetter.get(c).add(word);
			}
            
            allWords.add(word);
        }
        
        String letters = in.nextLine();
        
        // Find all the possible candidate words given our 7 letters
        Set<String> candidates = new TreeSet<String>(allWords);
        
        for (char c = 'a'; c <= 'z'; c++) {
        	if (!letters.contains(Character.toString(c))) {
        		candidates.removeAll(wordsByLetter.get(c));
        	}
		}
        
        // Remove all the words longer than 7 letters
        for (Iterator<String> it = candidates.iterator(); it.hasNext();) {
			final String candidate = (String) it.next();
			
			if (candidate.length() > 7) {
				it.remove();
			} else {
				final List<Character> chars = new ArrayList<Character>();
				
				for (char c : letters.toCharArray()) {
					chars.add(c);
				}
				
				for (char c : candidate.toCharArray()) {
					if (!chars.remove(Character.valueOf(c))) {
						it.remove();
						break;
					}
				}
			}
		}
        
        Set<String> solutions = new TreeSet<String>();
        int bestScore = 0;
        
        for (String candidate : candidates) {
			int score = getPoints(candidate);
			
			if (score > bestScore) {
				solutions.clear();
				solutions.add(candidate);
				
				bestScore = score;
			} else if (score == bestScore) {
				solutions.add(candidate);
			}
		}
        
        int index = -1;
        
        for (String solution : solutions) {
			if (index == -1) {
				index = allWords.indexOf(solution);
			} else {
				index = Math.min(index, allWords.indexOf(solution));
			}
		}

        System.out.println(allWords.get(index));
    }
}
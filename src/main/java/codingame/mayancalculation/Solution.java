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
package codingame.mayancalculation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Solution {
	
	private static String transpose(List<String> lines, int width) {
		final StringBuilder buffer = new StringBuilder();
		
		for (int i = 0; i < lines.get(0).length() / width; i++) {
			for (String line : lines) {
				buffer.append(line.substring(i * width, (i + 1) * width));
			}
		}
		
		return buffer.toString();
	}
	
	private static List<String> transposeDigits(List<Digit> digits, int height) {
		final List<StringBuilder> buffers = new ArrayList<StringBuilder>();
		
		for (int i = 0; i < height; i++) {
			buffers.add(new StringBuilder());
		}
		
		for (Digit digit : digits) {
			final List<String> lines = digit.getLines();
			
			for (int i = 0; i < lines.size(); i++) {
				buffers.get(i).append(lines.get(i));
			}
		}
		
		final List<String> lines = new ArrayList<String>();
		
		for (StringBuilder buffer : buffers) {
			lines.add(buffer.toString());
		}
		
		return lines;
	}
	
	private static class Digit {
		final String pattern;
		
		final long value;
		
		final int width, height;
	
		Digit(long value, String pattern, int width, int height) {
			this.value = value;
			this.pattern = pattern;
			this.width = width;
			this.height = height;
		}
		
		List<String> getLines() {
			final List<String> lines = new ArrayList<String>();
			
			for (int i = 0; i < pattern.length() / width; i++) {
				lines.add(pattern.substring(i * width, (i + 1) * width));
			}
			
			return lines;
		}
		
		String getPattern() {
			return pattern;
		}
		
		@Override
		public String toString() {
			return String.format("Digit[value=%d]", value);
		}
		
		void print() {
			for (String line : transposeDigits(Collections.singletonList(this), height)) {
				System.out.println(line);
			}
		}
	}
	
	private static class MayanNumber {
		final List<Digit> digits = new ArrayList<Digit>();
		
		final int height;
		
		public MayanNumber(int height) {
			this.height = height;
		}
		
		long getValue() {
			long result = 0;
			long exponent = 1;
			
			for (int i = digits.size() - 1; i >= 0; i--) {
				result += (digits.get(i).value * exponent);
				
				exponent *= 20;
			}
			
			return result;
		}
		
		void print() {
			for (Digit digit : digits) {
				digit.print();
			}
		}
	}
	
	private static List<String> readLines(Scanner scanner, int n) {
		 final List<String> lines = new ArrayList<String>();
		 
		 for (int i = 0; i < n; i++) {
			lines.add(scanner.next());
		}
		 
		 return lines;
	}
	
	private static MayanNumber parseMayanNumber(List<String> lines, int width, int height, Map<String, Digit> digits) {
        final MayanNumber number = new MayanNumber(height);
        
        final String data = transpose(lines, width);
        
        for (int i = 0; i < data.length() / (width * height); i++) {
			String pattern = data.substring(i * width * height, (i + 1) * width * height);
        	
        	number.digits.add(digits.get(pattern));
		}
        
        return number;
	}

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        
        int width = in.nextInt();
        int height = in.nextInt();
        
        List<String> input = readLines(in, height);
        
        final Map<String, Digit> digits = new TreeMap<String, Digit>();
        final Map<Long, Digit> digitsByValue = new TreeMap<Long, Digit>();
        
        String data = transpose(input, width);
        
        for (int i = 0; i < data.length() / (width * height); i++) {
			String pattern = data.substring(i * width * height, (i + 1) * width * height);
        	final Digit digit = new Digit(i, pattern, width, height);
        	
        	digits.put(digit.getPattern(), digit);
        	digitsByValue.put((long) i, digit);
		}
        
        int s1 = in.nextInt();

        final MayanNumber number1 = parseMayanNumber(readLines(in, s1), width, height, digits);
        
        int s2 = in.nextInt();

        final MayanNumber number2 = parseMayanNumber(readLines(in, s2), width, height, digits);
        
        System.err.println(number1.getValue());
        System.err.println(number2.getValue());
        
        String operation = in.next();
        
        long result = 0;
        
        if ("*".equals(operation)) {
        	result = number1.getValue() * number2.getValue();
        } else if ("+".equals(operation)) {
        	result = number1.getValue() + number2.getValue();        	
        } else if ("-".equals(operation)) {
        	result = number1.getValue() - number2.getValue();
        } else if ("/".equals(operation)) {
        	result = number1.getValue() / number2.getValue();
        } else {
        	throw new RuntimeException("Unexpected operation: " + operation);
        }
        
        long n = 20;
        
        final LinkedList<Digit> digits3 = new LinkedList<Digit>();
        
        while (result > 0) {
        	long remainder = result % n;
        	
        	digits3.addFirst(digitsByValue.get(remainder / (n / 20)));
        	
        	result -= remainder;
        	
        	n *= 20;
        }
        
        if (digits3.isEmpty()) {
        	digits3.add(digitsByValue.get(0L));
        }
        
        final MayanNumber number = new MayanNumber(height);
        number.digits.addAll(digits3);
        number.print();
    }
}
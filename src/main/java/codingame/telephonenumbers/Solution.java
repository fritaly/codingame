package codingame.telephonenumbers;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

class Solution {
	
	private static class Directory extends Node {
		
		public Directory() {
			super(-1);
		}
		
		void addPhoneNumber(String number) {
			Node current = this;
			
			for (char c : number.toCharArray()) {
				final int digit = Integer.parseInt(Character.toString(c));
				
				if (!current.children.containsKey(digit)) {
					current.children.put(digit, new Node(digit));
				}
				
				current = current.children.get(digit);		
			}
		}
	}
	
	private static class Node {
		
		final int digit;
		
		final Map<Integer, Node> children = new LinkedHashMap<Integer, Node>();
		
		public Node(int digit) {
			this.digit = digit;
		}
		
		int getChildCount() {
			if (children.isEmpty()) {
				return 0;
			}
			
			int count = 0;
			
			for (Node child : children.values()) {
				count += 1 + child.getChildCount();
			}
			
			return count;
		}
	}

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        
        final Directory directory = new Directory();
        
        for (int i = 0; i < n; i++) {
            directory.addPhoneNumber(in.next());
        }

        // The number of elements (referencing a number) stored in the structure.
        System.out.println(directory.getChildCount());
    }
}
package codingame.conway;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class Solution {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);
		int r = in.nextInt();
		int l = in.nextInt();
		
		final LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(r);
		
		for (int i = 1; i < l; i++) {
			final List<Integer> tmp = new ArrayList<Integer>();
			
			while (!list.isEmpty()) {
				int count = 1;
				int value = list.removeFirst();
				
				while (!list.isEmpty() && (list.getFirst() == value)) {
					list.removeFirst();
					
					count++;
				}
				
				tmp.add(count);
				tmp.add(value);
			}
			
			list.clear();
			list.addAll(tmp);	
		}
		
		final StringBuilder buffer = new StringBuilder();

		for (Integer i : list) {
			buffer.append(i).append(" ");
		}
		
		System.out.println(buffer.toString().trim());
	}
}
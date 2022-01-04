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
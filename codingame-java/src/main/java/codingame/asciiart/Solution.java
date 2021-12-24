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
package codingame.asciiart;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt();
        in.nextLine();
        int height = in.nextInt();
        in.nextLine();
        String text = in.nextLine().toUpperCase();
        
        List<String> rows = new ArrayList<String>();
        
        for (int i = 0; i < height; i++) {
            rows.add(in.nextLine());
        }
        
        for (String row : rows) {
        	StringBuilder buffer = new StringBuilder();
            
            for (char c : text.toCharArray()) {
            	if (('A' <= c) && (c <= 'Z')) {
                	int index = c - 'A';
                	
                	buffer.append(row.substring(index * width, (index + 1) * width));            		
            	} else {
                	int index = (rows.iterator().next().length() / width) - 1;
                	
                	buffer.append(row.substring(index * width, (index + 1) * width));
            	}
    		}
            
            System.out.println(buffer.toString());
		}
    }
}
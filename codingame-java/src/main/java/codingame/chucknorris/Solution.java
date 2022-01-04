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
package codingame.chucknorris;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Scanner;

class Solution {

    public static void main(String args[]) throws IOException {
        Scanner in = new Scanner(System.in);
        String message = in.nextLine();

        StringBuilder buffer = new StringBuilder();
        
        for (char c : message.toCharArray()) {
        	String binary = Integer.toBinaryString(c);

        	// Left pad the binary value with '0'
        	for (int i = binary.length(); i < 7; i++) {
        		buffer.append("0");
			}
        	
        	buffer.append(binary);
		}
        
        PushbackReader reader = new PushbackReader(new StringReader(buffer.toString()));

        StringBuilder result = new StringBuilder();
        
        while (true) {
        	int c = reader.read();
        	
        	if (c == -1) {
        		break;
        	}
        	
        	int count = 1;
        	
        	while (true) {
        		int d = reader.read();
        		
        		if (d == -1) {
        			break;
        		} else if (c != d) {
        			reader.unread(d);

        			break;
        		}
        		
        		count++;
        	} 
        	
        	if (c == '0') {
            	result.append("00 ");
        	} else {
            	result.append("0 ");
        	}

        	while (count-- > 0) {
        		result.append("0");
        	}
        	
        	result.append(" ");
        }
        
        System.out.println(result.toString().trim());
    }
}
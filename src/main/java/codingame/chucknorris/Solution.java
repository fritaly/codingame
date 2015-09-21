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
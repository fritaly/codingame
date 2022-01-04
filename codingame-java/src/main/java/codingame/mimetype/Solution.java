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
package codingame.mimetype;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Solution {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt(); // Number of elements which make up the association table.
        in.nextLine();
        int q = in.nextInt(); // Number Q of file names to be analyzed.
        in.nextLine();
        
        Map<String, String> mappings = new TreeMap<String, String>();
        
        for (int i = 0; i < n; i++) {
            String extension = in.next().toLowerCase(); // file extension
            String mimeType = in.next(); // MIME type.
            in.nextLine();
            
            mappings.put(extension, mimeType);
        }
        
        // For each of the Q filenames, display on a line the corresponding MIME type. If there is no corresponding type, then display UNKNOWN.
        for (int i = 0; i < q; i++) {
            String filename = in.nextLine(); // One file name per line.
            
            if (filename.contains(".")) {
            	String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
            	String type = mappings.get(extension);
            	
            	System.out.println((type == null) ? "UNKNOWN" : type);
            } else {
            	System.out.println("UNKNOWN");
            }
        }
    }
}
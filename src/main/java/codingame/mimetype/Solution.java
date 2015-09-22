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
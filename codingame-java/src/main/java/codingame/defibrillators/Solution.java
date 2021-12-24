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
package codingame.defibrillators;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

class Solution {
	
	private static class Defibrillator {
		final String name;
		final double longitude;
		final double latitude;

		Defibrillator(String line) {
			String[] values = line.split(";");
			
			name = values[1];
			longitude = Double.parseDouble(values[4].replace(',', '.'));
			latitude = Double.parseDouble(values[5].replace(',', '.'));
		}
		
		double distance(double _latitude, double _longitude) {
			final double x = (Math.toRadians(_longitude) - Math.toRadians(longitude)) * Math.cos((Math.toRadians(latitude) + Math.toRadians(_latitude)) / 2);
			final double y = (Math.toRadians(_latitude) - Math.toRadians(latitude));
			
			return Math.sqrt(x * x + y * y) * 6371;
		}
	}

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        final double longitude = Double.parseDouble(in.next().replace(',', '.'));
        in.nextLine();
        final double latitude = Double.parseDouble(in.next().replace(',', '.'));
        in.nextLine();
        int n = in.nextInt();
        in.nextLine();
        
        final Set<Defibrillator> defibrillators = new TreeSet<Defibrillator>(new Comparator<Defibrillator>() {
        	@Override
        	public int compare(Defibrillator o1, Defibrillator o2) {
        		final double d = o1.distance(latitude, longitude) - o2.distance(latitude, longitude);
        		
        		return (d < 0) ? -1 : (d > 0) ? +1 : 0;
        	}
		});
        
        for (int i = 0; i < n; i++) {
        	defibrillators.add(new Defibrillator(in.nextLine()));
        }

        System.out.println(defibrillators.iterator().next().name);
    }
}
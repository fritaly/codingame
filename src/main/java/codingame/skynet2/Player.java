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
package codingame.skynet2;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

class Player {
	
	private static class Link {
		final int node1, node2;
		
		public Link(int node1, int node2) {
			this.node1 = node1;
			this.node2 = node2;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (obj == this) {
				return true;
			}
			if (obj instanceof Link) {
				final Link link = (Link) obj;
				
				return ((this.node1 == link.node1) && (this.node2 == link.node2)) 
						|| ((this.node1 == link.node2) && (this.node2 == link.node1));
			}
			
			return false;
		}
	}
	
	private static class Network {
		final List<Link> links = new ArrayList<Link>();
		
		final Set<Integer> gateways = new TreeSet<Integer>();
		
		boolean isGateway(int node) {
			return gateways.contains(node);
		}
		
		void addLink(Link link) {
			links.add(link);
		}
		
		void removeLink(Link link) {
			links.remove(link);
		}
		
		List<Integer> getAccessibleNodes(int node) {
			final List<Integer> list = new ArrayList<Integer>();
			
			for (Link link : links) {
				if (link.node1 == node) {
					list.add(link.node2);
				} else if (link.node2 == node) {
					list.add(link.node1);
				}
			}
			
			return list;
		}
	}
	
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int numberOfNodes = in.nextInt(); // the total number of nodes in the level, including the gateways
        int numberOfLinks = in.nextInt(); // the number of links
        int numberOfGateways = in.nextInt(); // the number of exit gateways

        final Network network = new Network();
        
        for (int i = 0; i < numberOfLinks; i++) {
            int n1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int n2 = in.nextInt();
            
            network.addLink(new Link(n1, n2));
        }
        
        for (int i = 0; i < numberOfGateways; i++) {
            network.gateways.add(in.nextInt());
        }

        // game loop
        while (true) {
            int sI = in.nextInt(); // The index of the node on which the Skynet agent is positioned this turn
            
            List<Integer> accessibleNodes = network.getAccessibleNodes(sI);
            
            boolean found = false;
            
            for (Integer node : accessibleNodes) {
				if (network.isGateway(node)) {
					// Sever the link between the agent and this node
					network.removeLink(new Link(sI, node));
					
					System.out.println(String.format("%d %d", sI, node));
					found = true;
					break;
				}
			}
            
            if (!found) {
            	// Sever any remaining link
            	Link link = network.links.iterator().next();
            	
				network.removeLink(link);
				
				System.out.println(String.format("%d %d", link.node1, link.node2));
            }
        }
    }
}
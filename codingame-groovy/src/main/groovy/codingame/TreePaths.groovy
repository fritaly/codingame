/*
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
package codingame

class Node {

    final int index
    
    Node left, right

    Node(int index) {
        this.index = index
    }

    @Override
    String toString() {
        if (left) {
            "Node[${index}, left: ${left}, right: ${right}]"
        } else {
            "Node[${index}]"
        }
    }

    String find(int target, String path) {
        if (target == index) {
            return path.trim() ?: 'Root'
        }

        if (left) {
            def leftPath = left.find(target, path + " Left")

            if (leftPath) {
                return leftPath
            }
        }
        if (right) {
            def rightPath = right.find(target, path + " Right")

            if (rightPath) {
                return rightPath
            }
        }

        null
    }
}

def input = new Scanner(System.in)

def N = input.nextInt() // the number of nodes in the tree
def V = input.nextInt() // the index of the target node
def M = input.nextInt() // the number of nodes with two children

System.err.println("${N} ${V} ${M}")

def nodes = [:] as Map<Integer, Node>

(1..N).each {index ->
    nodes[index] = new Node(index)
}

def children = new HashSet()

for (i = 0; i < M; ++i) {
    def P = input.nextInt() // the node index
    def L = input.nextInt() // the left children of P
    def R = input.nextInt() // the right children of P

    children << L << R

    System.err.println("${P} ${L} ${R}")

    nodes[P].left = nodes[L]
    nodes[P].right = nodes[R]
}

// Identify the root node
def root = nodes.values().find { !children.contains(it.index) }

System.err.println("Root: ${root}")

println root.find(V, "")
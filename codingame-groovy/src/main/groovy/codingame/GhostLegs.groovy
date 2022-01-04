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

input = new Scanner(System.in);

W = input.nextInt()
H = input.nextInt()
input.nextLine()

def letters = []
def indices = []

for (i = 0; i < H; ++i) {
    line = input.nextLine()

    if (i == 0) {
        System.err << "${line}\n"

        letters = line.findAll { it != ' ' }
        indices = new ArrayList((0 .. letters.size() - 1))
    } else if (i == H-1) {
        def digits = line.findAll { it != ' ' }

        System.err << "${letters}\n"
        System.err << "${digits}\n"

        letters.eachWithIndex { letter, index ->
            println "${letter}${digits[indices.indexOf(index)]}"
        }
    } else {
        def chunks = line.split('\\|')

        System.err << "\n"
        System.err << "${indices}\n"
        System.err << " ${line}\n"

        for (i in 1 .. chunks.length - 1) {
            if (chunks[i] == '--') {
                // Swap the 2 elements
                def temp = indices[i-1]
                indices[i-1] = indices[i]
                indices[i] = temp
            }
        }

        System.err << "${indices}\n"
        System.err << "\n"
    }
}
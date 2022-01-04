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

expression = input.next()

def stack = new Stack()
def valid = true

for (c in expression) {
    if ((c == '(') || (c == '[') || (c == '{')) {
        stack << c
    } else if (c == ')') {
        if (stack.empty || (stack.pop() != '(')) {
            valid = false
            break
        }
    } else if (c == ']') {
        if (stack.empty || (stack.pop() != '[')) {
            valid = false
            break
        }
    } else if (c == '}') {
        if (stack.empty || (stack.pop() != '{')) {
            valid = false
            break
        }
    }
}

if (!stack.empty) {
    valid = false
}

println valid
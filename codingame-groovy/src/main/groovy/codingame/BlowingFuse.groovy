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

class Appliance {

    boolean on = false

    private final int consumption

    Appliance(int consumption) {
        this.consumption = consumption
    }

    void toggle() {
        this.on = !this.on
    }

    int getConsumption() {
        on ? consumption : 0
    }
}

input = new Scanner(System.in);

n = input.nextInt()
m = input.nextInt()
c = input.nextInt()

def appliances = []

for (i = 0; i < n; ++i) {
    nx = input.nextInt()

    appliances << new Appliance(nx)
}

def maxConsumption = 0

for (i = 0; i < m; ++i) {
    mx = input.nextInt()

    appliances[mx - 1].toggle()

    def consumption = appliances.sum { it.getConsumption() }

    if (consumption > maxConsumption) {
        maxConsumption = consumption
    }
}

if (maxConsumption > c) {
    println "Fuse was blown."
} else {
    println "Fuse was not blown."
    println "Maximal consumed current was ${maxConsumption} A."
}
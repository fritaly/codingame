/*
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
package codingame

class Person implements Comparable<Person> {
    String name
    String parentName
    Person parent
    int yearOrBirth
    String death
    String religion
    String gender
    Set<Person> children = new TreeSet<Person>()

    boolean isDead() {
        death != '-'
    }

    boolean isAnglican() {
        religion == 'Anglican'
    }

    String toString() {
        "Person[${name}]"
    }

    boolean isMale() {
        gender == 'M'
    }

    int compareTo(Person other) {
        if (this.male && !other.male) {
            return -1
        }
        if (!this.male && other.male) {
            return +1
        }

        this.yearOrBirth <=> other.yearOrBirth
    }
}

input = new Scanner(System.in);

n = input.nextInt()

def people = []

for (i = 0; i < n; ++i) {
    def person = new Person()
    person.name = input.next()
    person.parentName = input.next()
    person.yearOrBirth = input.nextInt()
    person.death = input.next()
    person.religion = input.next()
    person.gender = input.next()

    people << person
}

System.err.println "Found ${people.size()} people"

def peopleByName = people.collectEntries { [(it.name): it] }

for (person in people) {
    def parent = peopleByName[person.parentName]

    if (parent) {
        person.parent = parent
        parent.children << person
    }
}

for (person in people) {
    System.err.println person.dump()
}

def root = people.find { !it.parent }

System.err.println "Root: ${root.name}"

void traverse(Person person) {
    if (!person.dead && person.anglican) {
        println person.name
    }
    for (child in person.children) {
        if (!child.dead && child.anglican) {
            println child.name
        }
        for (child2 in child.children) {
            traverse(child2)
        }
    }

}

traverse(root)
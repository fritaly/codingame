package codingame.orderofsuccession

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
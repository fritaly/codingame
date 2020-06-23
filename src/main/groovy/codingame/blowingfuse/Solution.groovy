package codingame.blowingfuse

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
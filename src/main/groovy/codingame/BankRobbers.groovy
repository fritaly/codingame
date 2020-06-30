package codingame

class Vault {

    // 5 vowels: A, E, I, O and U
    int vowels

    // 10 digits
    int digits

    // The total number of combinations to test to unlock the vault
    long combinations

    // The remaining number of combinations to test to unlock the vault
    long remainingTime

    Vault(int vowels, int digits) {
        this.vowels = vowels
        this.digits = digits

        this.combinations = (long) Math.pow(10, digits) * Math.pow(5, vowels)
        this.remainingTime = combinations
    }

    String toString() {
        "Vault[vowels: ${vowels}, digits: ${digits}, remainingTime: ${remainingTime}]"
    }
}

class Robber {
    List<Vault> vaults = []

    // The current vault the robber is working on
    Vault vault = null

    long cumulatedTime() {
        vaults.collect { it.combinations }.sum() ?: 0
    }

    String toString() {
        "Robber[vault: ${vault}]"
    }
}

input = new Scanner(System.in);

def R = input.nextInt()
def V = input.nextInt()

LinkedList<Vault> vaults = new LinkedList<>()

for (i = 0; i < V; ++i) {
    def C = input.nextInt()
    def N = input.nextInt()

    vaults << new Vault(C - N, N)
}

Robber[] robbers = new Robber[R]

for (i = 0; i < R; ++i) {
    robbers[i] = new Robber()
}

while (true) {
    System.err.println("=== Start of loop ===")

    robbers.each {
        System.err.println("${it}")
    }

    System.err.println("=== Assigning vaults to robbers ===")

    // Keep the robbers busy
    robbers.each { robber ->
        if (!robber.vault && vaults) {
            // The robber is idle and there are remaining vaults to handle
            robber.vault = vaults.removeFirst()
        }
    }

    robbers.each {
        System.err.println("${it}")
    }

    System.err.println("=== Computing next milestone ===")

    // Which robber will be the first to unlock its vault ?
    def firstRobber = robbers.findAll { it.vault }.min { it.vault.remainingTime }

    // Time necessary to unlock the vault ?
    def elapsed = firstRobber.vault.remainingTime

    System.err.println("Elapsed: ${elapsed}")

    System.err.println("=== Updating remaining time ===")

    // Update the remaining time for all the robber / vaults
    robbers.eachWithIndex { robber, n ->
        if (robber.vault) {
            robber.vault.remainingTime -= elapsed

            System.err.println("Robber #${n+1} unlocked ${robber.vault}")

            if (robber.vault.remainingTime == 0) {
                robber.vaults << robber.vault
                robber.vault = null
            }
        } else {
            // This robber is idle
        }
    }

    if (!vaults && robbers.every { !it.vault }) {
        break
    }
}

println "${robbers.collect { it.cumulatedTime() }.max()}"
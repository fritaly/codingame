package codingame.autopickup

def input = new Scanner(System.in);
def n = input.nextInt()
def packet = input.next()

def output = new StringBuilder()

// [instruction id (3 bits)][packet length (4 bits)][packet info (packet length bits)]

while (packet) {
    def instruction = packet.substring(0, 3)

    // Skip the instruction id
    packet = packet.substring(3)

    def lengthAsText = packet.substring(0, 4)
    def length = Integer.parseInt(lengthAsText, 2)

    // Skip the packet length
    packet = packet.substring(4)

    def id = packet.substring(0, length)

    packet = packet.substring(length)

    if (instruction == '101') {
        output << '001' << lengthAsText << id
    }
}

println output.toString()
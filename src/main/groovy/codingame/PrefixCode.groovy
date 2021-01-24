package codingame

def input = new Scanner(System.in)

def n = input.nextInt() // number of mappings

def map = [:]

for (i = 0; i < n; ++i) {
    def binary = input.next()
    def character = input.nextInt() as char

    map[binary] = "${character}"
}

System.err.println("Map: ${map}")

def encoded = input.next()

String decode(String input, int index, Map<String, String> mappings, String result) {
    if (input == '') {
        return result
    }

    for (entry in mappings.entrySet()) {
        def key = entry.key
        def character = entry.value

        if (input.startsWith(key)) {
            def decoded = decode(input.substring(key.length()), index + key.length(), mappings, result + character)

            if (decoded) {
                return decoded
            }
        }
    }

    "DECODE FAIL AT INDEX ${index}"
}

println decode(encoded, 0, map, "")
package codingame

def input = new Scanner(System.in)

def sequence = input.nextLine()

System.err.println("Sequence: ${sequence}")

def stack = new java.util.Stack()

def reader = new StringReader(sequence)

def map = [:] as TreeMap<String, Double>

while (true) {
    def c = reader.read()

    if (c == -1) {
        break
    }

    def s1 = Character.toString(c as char)

    if (s1 == '-') {
        s2 = Character.toString(reader.read() as char)

        stack.pop()

        System.err.println("</${s2}>")
    } else {
        //  We define the depth of a tag as 1 + the number of tags in which it is nested
        def depth = 1 + stack.size()

        stack.push(s1)

        System.err.println("<${s1}> - depth = ${depth}")

        if (!map[s1]) {
            map[s1] = (1 / depth)
        } else {
            map[s1] += (1 / depth)
        }
    }
}

println "${map.max { e -> e.value }.key}"
package codingame

input = new Scanner(System.in);

class River {

    private long value

    River(long value) {
        this.value = value
    }

    long next() {
        def result = value

        Long.toString(value).each { digit ->
            result += Integer.parseInt(digit)
        }

        this.value = result

        result
    }
}

r1 = input.nextLong()
r2 = input.nextLong()

def river1 = new River(r1), river2 = new River(r2)
def v1 = r1, v2 = r2

while (v1 != v2) {
    if (v1 < v2) {
        v1 = river1.next()
    } else {
        v2 = river2.next()
    }
}

println v1
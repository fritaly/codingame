package codingame

def input = new Scanner(System.in).nextLine()

System.err.println("Input: ${input}")

def indices = []

for (i in 0 .. input.length() - 1) {
    if (input.charAt(i) == '0') {
        indices << i
    }
}

System.err.println("Indices: ${indices}")

def result = 0

for (i = 0 ; i < indices.size() ; i++) {
    def index = indices[i]
    def previousIndex = (i > 0) ? indices[i-1] : -1
    def nextIndex = (i < indices.size() - 1) ? indices[i+1] : input.length()

    def leftLength = (index - (previousIndex + 1))
    def rightLength = ((nextIndex - 1) - index)

    def length = leftLength + 1 + rightLength

    System.err.println("Index: ${index} -> previous: ${previousIndex} / next: ${nextIndex} -> Lengths: ${leftLength} + 1 + ${rightLength} = ${length}")

    result = Math.max(result, length)
}

println "${result}"
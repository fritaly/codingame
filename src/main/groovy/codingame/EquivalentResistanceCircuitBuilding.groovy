package codingame

abstract class Element {

    abstract double evaluate(Map<String, Integer> context)
}

class Resistance extends Element {

    final String name

    Resistance(String name) {
        this.name = name
    }

    @Override
    double evaluate(Map<String, Integer> context) {
        context[name]
    }
}

class Serial extends Element {
    final List<Element> elements

    Serial(List<Element> elements) {
        this.elements = elements
    }

    @Override
    double evaluate(Map<String, Integer> context) {
        double result = 0

        elements.each { e -> result += e.evaluate(context)}

        result
    }
}

class Parallel extends Element {
    final List<Element> elements

    Parallel(List<Element> elements) {
        this.elements = elements
    }

    @Override
    double evaluate(Map<String, Integer> context) {
        double result = 0

        elements.each { e -> result += (1 / e.evaluate(context))}

        (1 / result)
    }
}

def input = new Scanner(System.in)

def N = input.nextInt()

System.err.println("${N}")

def resistances = [:] as Map<String, Integer>

for (i = 0; i < N; ++i) {
    def name = input.next()
    def resistance = input.nextInt()

    resistances[name] = resistance
}

System.err.println("${resistances}")

input.nextLine()

def circuit = input.nextLine()

System.err.println("${circuit}")

def stack = new Stack()

for (token in (circuit.split(' '))) {
    if (token == '[') {
        // Start of parallel
        stack.push(token)
    } else if (token == ']') {
        // End of parallel, reduce the stack
        def elements = []

        while (stack.peek() != '[') {
            elements << stack.pop()
        }

        stack.pop() // '['

        stack.push(new Parallel(elements))
    } else if (token == '(') {
        // Start of serial
        stack.push(token)
    } else if (token == ')') {
        // End of serial, reduce the stack
        def elements = []

        while (stack.peek() != '(') {
            elements << stack.pop()
        }

        stack.pop() // '('

        stack.push(new Serial(elements))
    } else {
        // The token denotes a resistance
        stack.push(new Resistance(token))
    }
}

if (stack.size() != 1) {
    throw new IllegalStateException("Unexpected state: ${stack}")
}

printf("%.1f",  ((Element) stack.pop()).evaluate(resistances))
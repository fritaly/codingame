package codingame

def input = new Scanner(System.in)

def N = input.nextInt()

def stack = new Stack()

for (i = 0; i < N; ++i) {
    def instruction = input.next()

    System.err.println("${instruction}")

    if (instruction.matches('[+-]?[0-9]+')) {
        stack.push(Integer.parseInt(instruction))
    } else if (instruction == 'ADD') {
        if (stack.size() < 2) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        def a = stack.pop() as int
        def b = stack.pop() as int

        stack.push(a + b)
    } else if (instruction == 'SUB') {
        if (stack.size() < 2) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        def a = stack.pop() as int
        def b = stack.pop() as int

        stack.push(a - b)
    } else if (instruction == 'MUL') {
        if (stack.size() < 2) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        def a = stack.pop() as int
        def b = stack.pop() as int

        stack.push(a * b)
    } else if (instruction == 'DIV') {
        if (stack.size() < 2) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        def a = stack.pop() as int
        def b = stack.pop() as int

        if (a == 0) {
            stack.push('ERROR')
            break
        }

        stack.push(a.intdiv(b))
    } else if (instruction == 'MOD') {
        if (stack.size() < 2) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        def a = stack.pop() as int
        def b = stack.pop() as int

        stack.push(a % b)
    } else if (instruction == 'POP') {
        if (stack.size() < 1) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        stack.pop()
    } else if (instruction == 'DUP') {
        if (stack.size() < 1) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        def a = stack.peek()

        stack.push(a)
    } else if (instruction == 'SWP') {
        if (stack.size() < 2) {
            stack.clear()
            stack.push('ERROR')
            break
        }

        def a = stack.pop() as int
        def b = stack.pop() as int

        stack.push(a)
        stack.push(b)
    } else if (instruction == 'ROL') {
        if (stack.size() < 1) {
            stack.push('ERROR')
            break
        }

        def a = stack.pop() as int

        if (stack.size() < a) {
            stack.push('ERROR')
            break
        }

        def buffer = []

        (a - 1).times {
            buffer.add(0, stack.pop())
        }

        def b = stack.pop()

        stack.addAll(buffer)
        stack.push(b)
    } else {
        throw new RuntimeException("Unexpected instruction: ${instruction}")
    }

    System.err.println("Stack: ${stack}")
}

println stack.join(' ')

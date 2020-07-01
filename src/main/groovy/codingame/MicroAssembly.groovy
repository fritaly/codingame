package codingame

class Interpreter {

    List<String> instructions = []

    int pointer = 0

    Map<String, Integer> registers = [:]

    private int parseRegisterOrImmediateValue(String value) {
        if (registers.containsKey(value)) {
            return registers[value]
        }

        Integer.parseInt(value)
    }

    void execute(String instruction) {
        assert instruction

        def chunks = instruction.split(' ')

        switch (chunks[0]) {
            case 'MOV':
                registers[chunks[1]] = parseRegisterOrImmediateValue(chunks[2])
                pointer++
                break
            case 'ADD':
                registers[chunks[1]] = parseRegisterOrImmediateValue(chunks[2]) + parseRegisterOrImmediateValue(chunks[3])
                pointer++
                break
            case 'SUB':
                registers[chunks[1]] = parseRegisterOrImmediateValue(chunks[2]) - parseRegisterOrImmediateValue(chunks[3])
                pointer++
                break
            case 'JNE':
                def target = Integer.parseInt(chunks[1])
                def source = registers[chunks[2]]
                def value = parseRegisterOrImmediateValue(chunks[3])

                if (source != value) {
                    pointer = target
                } else {
                    pointer++
                }
                break
            default:
                throw new RuntimeException("Unexpected instruction: ${chunks[0]}")
        }
    }

    void run() {
        while (pointer < instructions.size()) {
            def instruction = instructions[pointer]

            execute(instruction)
        }
    }
}

def input = new Scanner(System.in)

def interpreter = new Interpreter()
interpreter.registers['a'] = input.nextInt()
interpreter.registers['b'] = input.nextInt()
interpreter.registers['c'] = input.nextInt()
interpreter.registers['d'] = input.nextInt()

def n = input.nextInt()
input.nextLine()

for (i = 0; i < n; ++i) {
    interpreter.instructions << input.nextLine()
}

interpreter.run()

println "${interpreter.registers['a']} ${interpreter.registers['b']} ${interpreter.registers['c']} ${interpreter.registers['d']}"
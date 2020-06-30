package codingame

input = new Scanner(System.in);

expression = input.next()

def stack = new Stack()
def valid = true

for (c in expression) {
    if ((c == '(') || (c == '[') || (c == '{')) {
        stack << c
    } else if (c == ')') {
        if (stack.empty || (stack.pop() != '(')) {
            valid = false
            break
        }
    } else if (c == ']') {
        if (stack.empty || (stack.pop() != '[')) {
            valid = false
            break
        }
    } else if (c == '}') {
        if (stack.empty || (stack.pop() != '{')) {
            valid = false
            break
        }
    }
}

if (!stack.empty) {
    valid = false
}

println valid
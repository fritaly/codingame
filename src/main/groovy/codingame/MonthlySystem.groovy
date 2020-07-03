package codingame

enum Month {
    Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec
}

def input = new Scanner(System.in)

def N = input.nextInt()
input.nextLine()

int decode(String s) {
    def result = 0

    while (s) {
        def temp = s.substring(0, 3)
        s = s.substring(3)

        result = (result * 12) + Month.valueOf(temp).ordinal()
    }

    result
}

String encode(int value) {
    def result = ''

    while (value > 0) {
        def temp = value % 12

        value = ((value - temp) / 12)

        result = Month.values()[temp].name() + result
    }

    result
}

def sum = 0

for (i = 0; i < N; ++i) {
    sum += decode(input.nextLine())
}

println encode(sum)
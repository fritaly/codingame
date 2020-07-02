package codingame

def input = new Scanner(System.in)

def N = input.nextInt()

def times = []

for (i = 0; i < N; ++i) {
    times << input.next()
}

def solution = times.min { time ->
    def array = time.split(':')

    (Integer.parseInt(array[0]) * 3600) + (Integer.parseInt(array[1]) * 60) + Integer.parseInt(array[2])
}

println solution
package codingame

class Pattern {

    String pattern
    int tempo

    Pattern(String pattern, int tempo) {
        this.pattern = pattern
        this.tempo = tempo
    }

    boolean matches(int n) {
        (n % tempo == 0)
    }

    String merge(String other) {
        [ 0, 1, 2, 3 ].collect { "${(pattern.charAt(it) == 'X') || (other.charAt(it) == 'X') ? 'X' : '0'}" }.join('')
    }
}

def input = new Scanner(System.in)

def L = input.nextInt()
def N = input.nextInt()

def patterns = [] as List<Pattern>

for (i = 0; i < N; ++i) {
    def pattern = input.next()
    def tempo = input.nextInt()

    patterns << new Pattern(pattern, tempo)
}

for (i = L; i >= 1; i--) {
    def result = '0000'

    patterns.findAll { it.matches(i) }.each { result = it.merge(result) }

    println result
}
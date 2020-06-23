package codingame.ghostlegs

input = new Scanner(System.in);

W = input.nextInt()
H = input.nextInt()
input.nextLine()

def letters = []
def indices = []

for (i = 0; i < H; ++i) {
    line = input.nextLine()

    if (i == 0) {
        System.err << "${line}\n"

        letters = line.findAll { it != ' ' }
        indices = new ArrayList((0 .. letters.size() - 1))
    } else if (i == H-1) {
        def digits = line.findAll { it != ' ' }

        System.err << "${letters}\n"
        System.err << "${digits}\n"

        letters.eachWithIndex { letter, index ->
            println "${letter}${digits[indices.indexOf(index)]}"
        }
    } else {
        def chunks = line.split('\\|')

        System.err << "\n"
        System.err << "${indices}\n"
        System.err << " ${line}\n"

        for (i in 1 .. chunks.length - 1) {
            if (chunks[i] == '--') {
                // Swap the 2 elements
                def temp = indices[i-1]
                indices[i-1] = indices[i]
                indices[i] = temp
            }
        }

        System.err << "${indices}\n"
        System.err << "\n"
    }
}
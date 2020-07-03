package codingame

enum Location {
    ABOVE, ON, BELOW
}

enum NoteType {
    A, B, C, D, E, F, G
}

class Note {
    int startX, endX
    Color color
    NoteType type
    char[][] array

    /**
     * Returns a list of integers representing the values of y where the node is the widest.
     */
    List<Integer> getYValues() {
        assert array

        def maxWidth = 0
        def yValues = [] as List<Integer>

        // Search the values of Y for which the note is the widest. Search the first and last black dot on the line
        for (int y = 0; y < array.length; y++) {
            int firstBlack = -1, lastBlack = -1

            for (int x = startX; x <= endX; x++) {
                def c = array[y][x]

                if (c == 'B') {
                    if (firstBlack == -1) {
                        firstBlack = x
                    }

                    lastBlack = x
                }
            }

            if (firstBlack != -1) {
                // The line isn't completely white, what's the width between the first and last black dots ?
                def width = (lastBlack - firstBlack) + 1

                if (width > maxWidth) {
                    yValues.clear()
                    yValues << y
                    maxWidth = width
                } else if (width == maxWidth) {
                    yValues << y
                } else {
                    // Do nothing
                }
            }
        }

        System.err.println("Max width (${maxWidth}) found for y in ${yValues}")

        yValues
    }

    @Override
    String toString() {
        "Note[color: ${color}, type: ${type}, x=[${startX},${endX}]]"
    }
}

enum Color {
    BLACK, WHITE

    String symbol() {
        // Black -> Q (Quarter)
        // White -> H (Half)
        (this == BLACK) ? 'Q': 'H'
    }
}

class Portee {

    /** The index of the row where the portee starts */
    int startY

    /** The index (inclusive) of the row where the portee ends. */
    int endY

    /** The note associated to the portee. */
    NoteType note

    Location locate(Note _note) {
        assert _note

        // Find all the values of y where the note is the widest
        def yValues = _note.getYValues()

        // Compare each value of y and sum all the values
        def sum = yValues.collect { y -> compare(y) }.sum()

        if (sum == (-1 * yValues.size())) {
            // All the y values are above the portee
            return Location.ABOVE
        } else if (sum == (+1 * yValues.size())) {
            // All the y values are below the portee
            return Location.BELOW
        }

        Location.ON
    }

    int compare(int y) {
        if ((startY <= y) && (y <= endY)) {
            return 0
        }

        if (y < startY) {
            // Dot above the portee
            return -1
        }

        // Dot under the portee
        +1
    }

    @Override
    String toString() {
        "Portee[${startY}-${endY}, note: ${note}]"
    }
}

private void dump(char[][] array) {
    array.each { row ->
        // Replace all the characters 'W' by '.' to make the dump easier to read
        System.err.println "${new String(row).replace('W', '.')}"
    }
}

private void fill(String image, array, width) {
    def strings = image.split(' ')

    def cursor = 0

    for (int i = 0; i < strings.length; i += 2) {
        def color = strings[i].charAt(0)
        def count = Integer.parseInt(strings[i + 1])

        count.times {
            array[cursor.intdiv(width)][cursor % width] = color

            cursor++
        }
    }
}

// Encodes a given column as a string. Example: "W175", "W33 B4 W20 B4 W20 B4 W20 B4 W20 B4 W42"
private String encodeColumn(char[][] array, int x) {
    def result = [], count = 0

    def previousColor = ' '

    for (int y = 0; y < array.length; y++) {
        def color = array[y][x]

        if ((color == previousColor) || (previousColor == ' ')) {
            count++
        } else if (previousColor != ' ') {
            result << "${previousColor}${count}"

            count = 1
        }

        previousColor = color
    }

    if (count > 0) {
        result << "${previousColor}${count}"
    }

    result.join(' ')
}

/**
 * Hides the portees by replacing their black dots by white ones (but only when relevant)
 */
private List<Portee> hidePortees(List<Portee> portees, width, array) {
    portees.each { portee ->
        // Hide from top to bottom
        for (int y = portee.startY; y <= portee.endY; y++) {
            for (int x = 0; x < width; x++) {
                def c = array[y][x]
                def top = array[y - 1][x]

                if (c == 'B' && (top != 'B')) {
                    array[y][x] = 'W'
                }
            }
        }

        // Hide from bottom to top
        for (int y = portee.endY; y >= portee.startY; y--) {
            for (int x = 0; x < width; x++) {
                def c = array[y][x]
                def bottom = array[y + 1][x]

                if (c == 'B' && (bottom != 'B')) {
                    array[y][x] = 'W'
                }
            }
        }
    }
}

def input = new Scanner(System.in)

def width = input.nextInt()
def height = input.nextInt()

System.err.println("${width} ${height}")

input.nextLine()

def array = new char[height][width]

def image = input.nextLine()

System.err.println("${image}")

fill(image, array, width)

dump(array)

// Identify the indices of the 5 "portees" by encoding the columns: the first column with an encoding matching the
// pattern "W33 B4 W20 B4 W20 B4 W20 B4 W20 B4 W42" is the start of the "portees"
System.err.println("Searching portees ...")

def portees = [] as List<Portee>

for (int x = 0; x < width; x++) {
    def encoding = encodeColumn(array, x)

    System.err.println("Column #${x} -> ${encoding}")

    if (!encoding.matches('W[0-9]+')) {
        System.err.println("Found the start of the portees")

        // Example: "W33 B4 W20 B4 W20 B4 W20 B4 W20 B4 W42" where all the portees MUST have the exact same height
        def pattern = java.util.regex.Pattern.compile('W(\\d+) B(?<portee>\\d+) W(?<line>\\d+) B(\\2) W(\\3) B(\\2) W(\\3) B(\\2) W(\\3) B(\\2) W(\\d+)')

        def matcher = pattern.matcher(encoding)

        if (!matcher.matches()) {
            throw new RuntimeException("Unable to parse the string '${encoding}'")
        }

        def index = 0
        def porteeHeight = Integer.parseInt(matcher.group('portee'))
        def lineHeight = Integer.parseInt(matcher.group('line'))

        System.err.println("Portee height: ${porteeHeight}")
        System.err.println("Line height: ${lineHeight}")

        // The notes associated to the portees detected upon scanning (from top to bottom):) F, D, B, G, E
        def notes = [ NoteType.F, NoteType.D, NoteType.B, NoteType.G, NoteType.E ]

        for (int i = 1; i < matcher.groupCount(); i++) {
            def count = Integer.parseInt(matcher.group(i))

            if (i % 2 == 0) {
                def portee = new Portee()
                portee.startY = index
                portee.endY = index + count - 1
                portee.note = notes.remove(0)

                portees << portee
            }

            index += count
        }

        break
    }
}

System.err.println("Portees detected: ${portees}")

hidePortees(portees, width, array)

System.err.println("===")

dump(array)

// Detect the notes by analyzing the columns
def buffer = [] as List<Color>
def startX = -1
def notes = [] as List<Note>

for (int x = 0; x < width; x++) {
    def encoding = encodeColumn(array, x)

    if (encoding.matches('W\\d+')) {
        if (buffer) {
            def note = new Note()
            note.array = array
            note.startX = startX
            note.endX = x-1
            note.color = (buffer.contains(Color.WHITE)) ? Color.WHITE : Color.BLACK

            notes << note

            System.err.println("Detected a ${note.color} note in [${note.startX},${note.endX}]")

            startX = -1
            buffer.clear()
        }

        System.err.println("Column #${x}: [empty]")
    } else {
        // Examples: "W90 B13 W73" (black note) or "W50 B2 W17 B2 W105" (white note)
        // Remove the leading and trailing white sections
        def pattern = java.util.regex.Pattern.compile('(W\\d+) (?<signature>.+) (W\\d+)')

        def matcher = pattern.matcher(encoding)

        if (!matcher.matches()) {
            throw new RuntimeException("Unable to parse encoding '${encoding}'")
        }

        if (startX == -1) {
            startX = x
        }

        def signature = matcher.group('signature')

        def color = signature.contains('W') ? Color.WHITE : Color.BLACK

        buffer << color

        System.err.println("Column #${x}: ${encoding} -> ${signature} (${color})")
    }
}

System.err.println("Notes: ${notes}")

notes.each { note ->
    // Find where the note is located

    for (portee in portees) {
        def location = portee.locate(note)

        switch (location) {
            case Location.ABOVE:
                System.err.println("Note above the portee (${portee.note})")
                break
            case Location.BELOW:
                System.err.println("Note below the portee (${portee.note})")
                break
            case Location.ON:
                System.err.println("Note on the portee (${portee.note})")
                note.type = portee.note
                break
        }
    }
}

System.err.println("Notes: ${notes}")

println "AQ DH"
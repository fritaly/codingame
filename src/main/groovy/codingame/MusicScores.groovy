package codingame

// Whether to enable debug logs
def debug = false

/**
 * Enumerates the location of a note relative to a portee: above, on it or below.
 */
enum Location {
    ABOVE, ON, BELOW
}

/**
 * Enumerates the 7 different notes.
 */
enum NoteType {
    A, B, C, D, E, F, G

    NoteType noteAfter() {
        def values = NoteType.values()

        values[(ordinal() + 1) % values.length]
    }

    NoteType noteBefore() {
        def values = NoteType.values()

        values[(ordinal() - 1 + values.length) % values.length]
    }
}

class Note {

    /** The value of X where the node starts (in the partition). */
    int startX

    /** The value of X where the node ends (in the partition). */
    int endX

    /** The color of the node (black or white). */
    Color color

    /** The type of the note (A, B, C, etc). */
    NoteType type

    /** Reference to the partition where the note appears. */
    char[][] array

    /** Internal cache used by the method Note#getYValues(). */
    private List<Integer> cache

    /** Formats the node as a 2-character string. Example: "AH", "DQ". */
    String asText() {
        "${type.name()}${color.symbol()}"
    }

    Integer getYValue() {
        def values = getYValues()

        (values.sum() / values.size())
    }

    /**
     * Returns a list of integers representing the values of y where the note is the widest.
     */
    List<Integer> getYValues() {
        assert array

        if (cache == null) {
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

            cache = yValues
        }

        cache
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

        // Find the (average) y-value where the note is the widest
        def yValue = _note.getYValue()

        // Compare each value of y and sum all the values
        def comparison = compare(yValue)

        if (comparison == -1) {
            // The note is above the portee
            return Location.ABOVE
        } else if (comparison == +1) {
            // The note is below the portee
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

private NoteType identifyType(List<Portee> portees, Note note) {

    def porteesAbove = [] as List<Portee>, porteesUnder = [] as List<Portee>

    NoteType result = null

    for (portee in portees) {
        def location = portee.locate(note)

        switch (location) {
            case Location.ABOVE:
                porteesUnder << portee
                break
            case Location.BELOW:
                porteesAbove << portee
                break
            case Location.ON:
                result = portee.note
                break
        }

        if (result) {
            // Stop looping, we identified the note
            break
        }
    }

    if (!result) {
        // The node is between 2 portees, identify it
        if (porteesAbove) {
            result = porteesAbove.last().note.noteBefore()
        } else if (porteesUnder) {
            result = porteesUnder.first().note.noteAfter()
        } else {
            throw new IllegalStateException()
        }
    }

    result
}

private void dump(char[][] array) {
    array.each { row ->
        // Replace all the characters 'W' by '.' to make the dump easier to read
        System.err.println "${new String(row).replace('W', '.')}"
    }
}

private char[][] decode(String image, int width, int height) {
    def array = new char[height][width]

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

    array
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

def image = input.nextLine()

if (debug) {
    System.err.println("${image}")
}

def array = decode(image, width, height)

if (debug) {
    dump(array)
}

// Identify the indices of the 5 "portees" by encoding the columns: the first column with an encoding matching the
// pattern "W33 B4 W20 B4 W20 B4 W20 B4 W20 B4 W42" is the start of the "portees"
System.err.println("Searching portees ...")

def portees = [] as List<Portee>

// Regex pattern used for detecting the column where the 5 portees start
// Example: "W33 B4 W20 B4 W20 B4 W20 B4 W20 B4 W42" where all the portees MUST have the exact same height
def pattern1 = java.util.regex.Pattern.compile('W(\\d+) B(?<portee>\\d+) W(?<line>\\d+) B(\\2) W(\\3) B(\\2) W(\\3) B(\\2) W(\\3) B(\\2) W(\\d+)')

// Regex pattern used for detecting white rows / columns
def pattern2 = java.util.regex.Pattern.compile('W[0-9]+')

for (int x = 0; x < width; x++) {
    def encoding = encodeColumn(array, x)

    if (debug) {
        System.err.println("Column #${x} -> ${encoding}")
    }

    if (!pattern2.matcher(encoding).matches()) {
        System.err.println("Found the start of the portees")

        // Example: "W33 B4 W20 B4 W20 B4 W20 B4 W20 B4 W42" where all the portees MUST have the exact same height
        def matcher = pattern1.matcher(encoding)

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

// Add a 6th portee
def portee5 = portees[4]
def portee4 = portees[3]

def portee6 = new Portee()
portee6.note = NoteType.C
portee6.startY = portee5.startY + (portee5.startY - portee4.startY)
portee6.endY = portee5.endY + (portee5.endY - portee4.endY)

portees << portee6

System.err.println("Portees detected: ${portees}")

hidePortees(portees, width, array)

if (debug) {
    System.err.println("===")

    dump(array)
}

// Detect the notes by analyzing the columns
def buffer = [] as List<Color>
def startX = -1
def notes = [] as List<Note>

for (int x = 0; x < width; x++) {
    // Encode each column as a string. The encoding will be "W[n]" for a completely white column, "W[n] B[n] W[n]" or
    // "W[n] B[n] W[n] B[n] W[n]". Examples: "W90 B13 W73" (black note) or "W50 B2 W17 B2 W105" (white note)
    def encoding = encodeColumn(array, x)

    // Split the encoding to count the number of "segments"
    def segments = encoding.split(' ')

    if (segments.length == 1) {
        // Only one segment -> the column is completely white (a completely black column is impossible)
        if (buffer) {
            def note = new Note()
            note.array = array
            note.startX = startX
            note.endX = x-1
            note.color = (buffer.contains(Color.WHITE)) ? Color.WHITE : Color.BLACK
            note.type = identifyType(portees, note)

            notes << note

            System.err.println("Detected a ${note.color} note in [${note.startX},${note.endX}]")

            startX = -1
            buffer.clear()
        }

        if (debug) {
            System.err.println("Column #${x}: [empty]")
        }
    } else {
        if (startX == -1) {
            startX = x
        }

        // Infer the color associated to the encoding:
        // 3 segments -> the note can be black or white
        // 5 segments -> the note is white (cannot be black)
        def color = (segments.length == 3) ? Color.BLACK : Color.WHITE

        buffer << color

        if (debug) {
            System.err.println("Column #${x}: ${encoding} -> (${color})")
        }
    }
}

if (debug) {
    System.err.println("Notes: ${notes}")
}

// Print all the notes detected in order as 2 characters
println notes.collect { it.asText() }.join(' ')
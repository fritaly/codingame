package codingame

class LicensePlate {

    // Example: "AA-001-AA"
    String text

    LicensePlate(String text) {
        this.text = text
    }

    long getId() {
        long id = 0

        def c0 = Character.getNumericValue(text.charAt(0))
        def c1 = Character.getNumericValue(text.charAt(1))
        def c7 = Character.getNumericValue(text.charAt(7))
        def c8 = Character.getNumericValue(text.charAt(8))

        int number = Integer.parseInt(text.substring(3, 6))

        id += ((c0 - Character.getNumericValue('A' as char)) * 26 * 26 * 26 * 999)
        id += ((c1 - Character.getNumericValue('A' as char)) * 26 * 26 * 999)
        id += ((c7 - Character.getNumericValue('A' as char)) * 26 * 999)
        id += ((c8 - Character.getNumericValue('A' as char)) * 999)
        id += (number - 1)

        id
    }

    static String fromId(long id) {
        assert (id >= 0)

        def base = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'

        def c0 = 'A', c1 = 'A', c7 = 'A', c8 = 'A'

        if (id > (26 * 26 * 26 * 999)) {
            c0 = base.charAt(id.intdiv(26 * 26 * 26 * 999) as int)

            id = id % (26 * 26 * 26 * 999)
        }
        if (id > (26 * 26 * 999)) {
            c1 = base.charAt(id.intdiv(26 * 26 * 999) as int)

            id = id % (26 * 26 * 999)
        }
        if (id > (26 * 999)) {
            c7 = base.charAt(id.intdiv(26 * 999) as int)

            id = id % (26 * 999)
        }
        if (id > 999) {
            c8 = base.charAt(id.intdiv(999) as int)

            id = id % 999
        }

        def number = String.format("%03d", (id + 1))

        "${c0}${c1}-${number}-${c7}${c8}"
    }
}

static void dump(String text) {
    def plate = new LicensePlate(text)

    System.err.println " ${text} -> ${plate.id} -> ${LicensePlate.fromId(plate.id)}"
}

def input = new Scanner(System.in)

def licensePlate = new LicensePlate(input.nextLine())
def n = input.nextInt()

println LicensePlate.fromId(licensePlate.id + n)
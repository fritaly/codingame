package codingame.enigma

interface Cipher {

    String encode(String plain)

    String decode(String ciphered)

}

class Caesar implements Cipher {

    static final String ALPHABET = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'

    final int offset

    Caesar(int offset) {
        this.offset = offset
    }

    String encode(String plain) {
        def result = ''

        plain.eachWithIndex { c, n ->
            result += ALPHABET.charAt((ALPHABET.indexOf(c) + offset + n) % 26)
        }

        result
    }

    String decode(String ciphered) {
        def result = ''

        ciphered.eachWithIndex { c, n ->
            result += ALPHABET.charAt((ALPHABET.indexOf(c) - offset - n + 260) % 26)
        }

        result
    }
}

class Rotor implements Cipher {

    static final String ALPHABET = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'

    final String scrambled

    Rotor(String scrambled) {
        this.scrambled = scrambled
    }

    String encode(String plain) {
        def result = ''

        plain.eachWithIndex { c, n ->
            result += scrambled.charAt(ALPHABET.indexOf(c))
        }

        result
    }

    String decode(String ciphered) {
        def result = ''

        ciphered.eachWithIndex { c, n ->
            result += ALPHABET.charAt(scrambled.indexOf(c))
        }

        result
    }
}

class CompositeCipher implements Cipher {

    final List<Cipher> ciphers = []

    String encode(String plain) {
        def result = plain

        ciphers.each { cipher ->
            result = cipher.encode(result)
        }

        result
    }

    String decode(String ciphered) {
        def result = ciphered

        ciphers.reverse().each { cipher ->
            result = cipher.decode(result)
        }

        result
    }
}

input = new Scanner(System.in)

operation = input.nextLine()
pseudoRandomNumber = input.nextInt()
input.nextLine()

def ciphers = new CompositeCipher()
ciphers.ciphers << new Caesar(pseudoRandomNumber)

for (i = 0; i < 3; ++i) {
    ciphers.ciphers << new Rotor(input.nextLine())
}

def message = input.nextLine()

if (operation == 'ENCODE') {
    println ciphers.encode(message)
} else {
    println ciphers.decode(message)
}
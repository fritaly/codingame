/*
 * Copyright 2021, Francois Ritaly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codingame.graffiti

class Range implements Comparable<Range> {
    int start, end

    Range(int start, int end) {
        this.start = start
        this.end = end
    }

    boolean isEmpty() {
        start == end
    }

    boolean contains(int n) {
        (start <= n) && (n <= end)
    }

    boolean includes(Range other) {
        this.contains(other.start) && this.contains(other.end)
    }

    boolean overlaps(Range other) {
        this.contains(other.start) || this.contains(other.end) || other.contains(this.start) || other.contains(this.end)
    }

    @Override
    int compareTo(Range other) {
        this.start <=> other.start
    }

    @Override
    String toString() {
        "[${start}-${end}]"
    }
}

class Fence {
    int length

    Set<Range> ranges = new TreeSet()

    Fence(int length) {
        this.length = length
        this.ranges << new Range(0, length)
    }

    void substract(Range other) {
        System.err.println("Processing range '${other}' ...")

        def toRemove = []
        def toAdd = []

        ranges.each { range ->
            if (range.includes(other)) {
                toRemove << range
                def r1 = new Range(range.start, other.start)
                def r2 = new Range(other.end, range.end)

                if (!r1.empty) {
                    toAdd << r1
                }
                if (!r2.empty) {
                    toAdd << r2
                }
            }
            if (range.contains(other.start)) {
                toRemove << range
                def r1 = new Range(range.start, other.start)

                if (!r1.empty) {
                    toAdd << r1
                }
            }
            if (range.contains(other.end)) {
                toRemove << range
                def r1 = new Range(other.end, range.end)

                if (!r1.empty) {
                    toAdd << r1
                }
            }
            if (other.includes(range)) {
                toRemove << range
            }
        }

        ranges.removeAll(toRemove)
        ranges.addAll(toAdd)

        System.err.println "Ranges: ${ranges}"
    }
}

def input = new Scanner(System.in)

def length = input.nextInt()
def reports = input.nextInt()

def fence = new Fence(length)

for (i = 0; i < reports; ++i) {
    def start = input.nextInt()
    def end = input.nextInt()

    def range = new Range(start, end)

    fence.substract(range)
}

if (fence.ranges) {
    fence.ranges.each {
        println "${it.start} ${it.end}"
    }
} else {
    println 'All painted'
}

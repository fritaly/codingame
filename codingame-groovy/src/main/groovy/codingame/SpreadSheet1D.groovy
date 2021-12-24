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
package codingame

interface Cell {
    int getValue(Map<String, Cell> map)
}

class ValueCell implements Cell {

    int value

    ValueCell(int value) {
        this.value = value
    }

    int getValue(Map<String, Cell> map) { value }
}

class LookupCell implements Cell {

    String arg1

    LookupCell(String arg1) {
        this.arg1 = arg1
    }

    int getValue(Map<String, Cell> map) { map[arg1].getValue(map) }
}

abstract class BaseCell implements Cell {

    String arg1, arg2

    Integer cache

    BaseCell(String arg1, String arg2) {
        this.arg1 = arg1
        this.arg2 = arg2
    }

    protected Cell getCell(Map<String, Cell> map, String text) {
        if (text.startsWith('$')) {
            return map[text]
        }

        new ValueCell(Integer.parseInt(text))
    }
}

class AddCell extends BaseCell {

    AddCell(String arg1, String arg2) {
        super(arg1, arg2)
    }

    int getValue(Map<String, Cell> map) {
        if (!cache) {
            cache = getCell(map, arg1).getValue(map) + getCell(map, arg2).getValue(map)
        }

        cache
    }
}

class SubCell extends BaseCell {

    SubCell(String arg1, String arg2) {
        super(arg1, arg2)
    }

    int getValue(Map<String, Cell> map) {
        if (!cache) {
            cache = getCell(map, arg1).getValue(map) - getCell(map, arg2).getValue(map)
        }

        cache
    }
}

class MultCell extends BaseCell {

    MultCell(String arg1, String arg2) {
        super(arg1, arg2)
    }

    int getValue(Map<String, Cell> map) {
        if (!cache) {
            cache = getCell(map, arg1).getValue(map) * getCell(map, arg2).getValue(map)
        }

        cache
    }
}

def input = new Scanner(System.in)

def N = input.nextInt()

def cells = [:] as LinkedHashMap<String, Cell>

def count = 0

for (i = 0; i < N; ++i) {
    def operation = input.next()
    def arg1 = input.next()
    def arg2 = input.next()

    def name = "\$${count++}".toString()

    switch (operation) {
        case 'VALUE':
            cells[name] = (arg1.startsWith('$')) ? new LookupCell(arg1) : new ValueCell(Integer.parseInt(arg1))
            break

        case 'ADD':
            cells[name] = new AddCell(arg1, arg2)
            break

        case 'SUB':
            cells[name] = new SubCell(arg1, arg2)
            break

        case 'MULT':
            cells[name] = new MultCell(arg1, arg2)
            break

        default:
            throw new RuntimeException("Unsupported operation: ${operation}")
    }
}

cells.values().each {
    println it.getValue(cells)
}
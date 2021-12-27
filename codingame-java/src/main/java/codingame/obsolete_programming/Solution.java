/**
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
package codingame.obsolete_programming;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static abstract class Instruction {

        abstract void evaluate(Stack<Integer> stack, Map<String, Instruction> functions);
    }

    private static class Add extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.push(stack.pop() + stack.pop());
        }

        @Override
        public String toString() {
            return "ADD";
        }
    }

    private static class Substract extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            final Integer first = stack.pop(), second = stack.pop();

            stack.push(second - first);
        }

        @Override
        public String toString() {
            return "SUB";
        }
    }

    private static class Multiply extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.push(stack.pop() * stack.pop());
        }

        @Override
        public String toString() {
            return "MUL";
        }
    }

    private static class Divide extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            final Integer first = stack.pop(), second = stack.pop();

            stack.push(second / first);
        }

        @Override
        public String toString() {
            return "DIV";
        }
    }

    private static class Modulo extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            final Integer first = stack.pop(), second = stack.pop();

            stack.push(second % first);
        }

        @Override
        public String toString() {
            return "MOD";
        }
    }

    private static class Pop extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.pop();
        }

        @Override
        public String toString() {
            return "POP";
        }
    }

    private static class Push extends Instruction {

        final int value;

        private Push(int value) {
            this.value = value;
        }

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.push(value);
        }

        @Override
        public String toString() {
            return "PUSH(" + value + ")";
        }
    }

    private static class Duplicate extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.push(stack.peek());
        }

        @Override
        public String toString() {
            return "DUP";
        }
    }

    private static class Swap extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            final Integer first = stack.pop(), second = stack.pop();
            stack.push(first);
            stack.push(second);
        }

        @Override
        public String toString() {
            return "SWAP";
        }
    }

    private static class Rotate extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            final Integer third = stack.remove(stack.size() - 3);
            stack.push(third);
        }

        @Override
        public String toString() {
            return "ROTATE";
        }
    }

    private static class Overlay extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.push(stack.get(stack.size() - 2));
        }

        @Override
        public String toString() {
            return "OVERLAY";
        }
    }

    private static class Positive extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.push(stack.pop() >= 0 ? 1 : 0);
        }

        @Override
        public String toString() {
            return "POSITIVE";
        }
    }

    private static class Not extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            stack.push(stack.pop() == 0 ? 1 : 0);
        }

        @Override
        public String toString() {
            return "NOT";
        }
    }

    private static class Out extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            System.out.println(stack.pop());
        }

        @Override
        public String toString() {
            return "OUT";
        }
    }

    private static class Block extends Instruction {

        private final List<Instruction> instructions = new ArrayList<>();

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            for (Instruction instruction : instructions) {
                instruction.evaluate(stack, functions);
            }
        }

        boolean isEmpty() {
            return instructions.isEmpty();
        }

        @Override
        public String toString() {
            return "BLOCK(" + instructions.stream().map(i -> i.toString()).collect(Collectors.joining(", ")) + ")";
        }
    }

    private static class End extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            throw new UnsupportedOperationException();
        }
    }

    private static class EndIf extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            throw new UnsupportedOperationException();
        }
    }

    private static class Else extends Instruction {

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            throw new UnsupportedOperationException();
        }
    }

    private static class CallFunction extends Instruction {

        final String name;

        private CallFunction(String name) {
            this.name = name;
        }

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            functions.get(name).evaluate(stack, functions);
        }

        @Override
        public String toString() {
            return "CALL(" + name + ")";
        }
    }

    private static class IfThenElse extends Instruction {

        final Block ifBranch = new Block(), elseBranch = new Block();

        @Override
        void evaluate(Stack<Integer> stack, Map<String, Instruction> functions) {
            final Integer value = stack.pop();

            if (value != 0) {
                ifBranch.evaluate(stack, functions);
            } else {
                elseBranch.evaluate(stack, functions);
            }
        }

        @Override
        public String toString() {
            if (elseBranch == null) {
                return "IF_THEN { " + ifBranch + " }";
            }

            return "IF_THEN { " + ifBranch + " } ELSE { " + elseBranch + " }";
        }
    }

    private static Instruction parse(List<String> queue, Map<String, Instruction> functions) {
        while (!queue.isEmpty()) {
            final String string = queue.remove(0);

            switch (string) {
                case "ADD":
                    return new Add();
                case "SUB":
                    return new Substract();
                case "MUL":
                    return new Multiply();
                case "DIV":
                    return new Divide();
                case "MOD":
                    return new Modulo();
                case "POP":
                    return new Pop();
                case "DUP":
                    return new Duplicate();
                case "SWP":
                    return new Swap();
                case "ROT":
                    return new Rotate();
                case "OVR":
                    return new Overlay();
                case "POS":
                    return new Positive();
                case "NOT":
                    return new Not();
                case "OUT":
                    return new Out();
                case "END":
                    return new End();
                case "DEF":
                    function: {
                        // Start of a function definition
                        final String name = queue.remove(0);
                        final Block function = new Block();

                        // Register the function now to support recursive definitions
                        functions.put(name, null);

                        while (true) {
                            final Instruction instruction = parse(queue, functions);

                            if (instruction instanceof End) {
                                break;
                            }

                            function.instructions.add(instruction);
                        }

                        functions.put(name, function);

                        System.err.println("Added function " + name + " = " + function);

                        continue;
                    }
                case "FI":
                    return new EndIf();
                case "ELS":
                    return new Else();
                case "IF":
                    if_then: {
                        final IfThenElse ifThen = new IfThenElse();
                        boolean inIf = true;

                        while (true) {
                            final Instruction instruction = parse(queue, functions);

                            if (instruction instanceof EndIf) {
                                break;
                            }
                            if (instruction instanceof Else) {
                                inIf = false;
                                continue;
                            }

                            if (inIf) {
                                ifThen.ifBranch.instructions.add(instruction);
                            } else {
                                ifThen.elseBranch.instructions.add(instruction);
                            }
                        }

                        return ifThen;
                    }
            }

            if (functions.containsKey(string)) {
                // That's a function call
                return new CallFunction(string);
            }

            if (string.matches("[+-]?\\d+")) {
                return new Push(Integer.parseInt(string));
            } else {
                throw new IllegalStateException("Unexpected token: " + string);
            }

        } // while queue isn't empty
        return null;
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);
        final int N = scanner.nextInt();

        System.err.println(N);

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<String> lines = IntStream.range(0, N)
                .mapToObj(i -> scanner.nextLine())
                .collect(Collectors.toList());

        lines.forEach(l -> System.err.println(l));

        System.err.println(String.join(" ", lines));

        final List<String> queue = new ArrayList<>(Arrays.asList(String.join(" ", lines).split("[ ]+")));

        final Map<String, Instruction> functions = new LinkedHashMap<>();

        final Block program = new Block();

        while (!queue.isEmpty()) {
            program.instructions.add(parse(queue, functions));
        }

        System.err.println(program);

        program.evaluate(new Stack<>(), functions);
    }
}
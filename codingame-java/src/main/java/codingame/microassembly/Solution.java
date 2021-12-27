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
package codingame.microassembly;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution {

    private static abstract class Instruction {

        abstract void evaluate(Map<String, Integer> context, String instruction);

        final int evaluateSrcOrImm(Map<String, Integer> context, String value) {
            switch (value) {
                case "a":
                case "b":
                case "c":
                case "d":
                    return context.get(value);

                default:
                    return Integer.parseInt(value);
            }
        }
    }

    private static class Move extends Instruction {

        @Override
        public void evaluate(Map<String, Integer> context, String instruction) {
            final String[] array = instruction.split(" ");

            context.put(array[1], evaluateSrcOrImm(context, array[2]));
            context.put("ip", context.get("ip") + 1);
        }
    }

    private static class Add extends Instruction {

        @Override
        public void evaluate(Map<String, Integer> context, String instruction) {
            final String[] array = instruction.split(" ");

            context.put(array[1], evaluateSrcOrImm(context, array[2]) + evaluateSrcOrImm(context, array[3]));
            context.put("ip", context.get("ip") + 1);
        }
    }

    private static class Substract extends Instruction {

        @Override
        public void evaluate(Map<String, Integer> context, String instruction) {
            final String[] array = instruction.split(" ");

            context.put(array[1], evaluateSrcOrImm(context, array[2]) - evaluateSrcOrImm(context, array[3]));
            context.put("ip", context.get("ip") + 1);
        }
    }

    private static class JumpIfNotEqual extends Instruction {

        @Override
        public void evaluate(Map<String, Integer> context, String instruction) {
            final String[] array = instruction.split(" ");

            final String source = array[2];
            final int value = evaluateSrcOrImm(context, array[3]);

            if (context.get(source) != value) {
                context.put("ip", Integer.parseInt(array[1]));
            } else {
                context.put("ip", context.get("ip") + 1);
            }
        }
    }

    public static void main(String args[]) {
        final Scanner scanner = new Scanner(System.in);

        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        int d = scanner.nextInt();

        final int n = scanner.nextInt();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        final List<String> instructions = IntStream.range(0, n)
                .mapToObj(i -> scanner.nextLine())
                .collect(Collectors.toList());

        final Map<String, Integer> context = new LinkedHashMap<>();
        context.put("a", a);
        context.put("b", b);
        context.put("c", c);
        context.put("d", d);
        context.put("ip", 0); // Instruction pointer

        while (context.get("ip") < instructions.size()) {
            final String line = instructions.get(context.get("ip"));
            final Instruction instruction;

            if (line.startsWith("MOV ")) {
                instruction = new Move();
            } else if (line.startsWith("ADD ")) {
                instruction = new Add();
            } else if (line.startsWith("SUB ")) {
                instruction = new Substract();
            } else if (line.startsWith("JNE ")) {
                instruction = new JumpIfNotEqual();
            } else {
                throw new IllegalStateException("Unexpected instruction: " + line);
            }

            instruction.evaluate(context, line);
        }

        System.out.printf("%d %d %d %d", context.get("a"), context.get("b"), context.get("c"), context.get("d"));
    }
}
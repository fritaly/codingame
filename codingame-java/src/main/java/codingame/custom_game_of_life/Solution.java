/**
 * Copyright 2015-2022, Francois Ritaly
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
package codingame.custom_game_of_life;

import java.util.Scanner;

class Solution {

	private static char[][] process(final char[][] input, String alive, String dead) {
		final int height = input.length, width = input[0].length;

		final char[][] output = new char[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int count = 0;

				if (y > 0) {
					if (x > 0) {
						if (input[y-1][x-1] == 'O') {
							count++;
						}
					}
					if (input[y-1][x] == 'O') {
						count++;
					}
					if (x < width - 1) {
						if (input[y-1][x+1] == 'O') {
							count++;
						}
					}
				}

				if (x > 0) {
					if (input[y][x-1] == 'O') {
						count++;
					}
				}
				if (x < width - 1) {
					if (input[y][x+1] == 'O') {
						count++;
					}
				}

				if (y + 1 < height) {
					if (x > 0) {
						if (input[y+1][x-1] == 'O') {
							count++;
						}
					}
					if (input[y+1][x] == 'O') {
						count++;
					}
					if (x < width - 1) {
						if (input[y+1][x+1] == 'O') {
							count++;
						}
					}
				}

				final String condition = (input[y][x] == 'O') ? alive : dead;

				output[y][x] = (condition.charAt(count) == '1') ? 'O' : '.';
			}
		}

		return output;
	}

	public static void main(String args[]) {
		final Scanner scanner = new Scanner(System.in);
		final int height = scanner.nextInt(), width = scanner.nextInt(), n = scanner.nextInt();

		if (scanner.hasNextLine()) {
			scanner.nextLine();
		}

		final String alive = scanner.nextLine(), dead = scanner.nextLine();

		char[][] grid = new char[height][width];

		for (int y = 0; y < height; y++) {
			grid[y] = scanner.nextLine().toCharArray();
		}

		for (int i = 0; i < n; i++) {
			grid = process(grid, alive, dead);
		}

		for (char[] array : grid) {
			System.out.println(new String(array));
		}
	}
}
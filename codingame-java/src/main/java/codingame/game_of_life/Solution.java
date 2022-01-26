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
package codingame.game_of_life;

import java.util.Scanner;

class Solution {

	private static char[][] process(final char[][] input) {
		final int height = input.length, width = input[0].length;

		final char[][] output = new char[height][width];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int count = 0;

				if (y > 0) {
					if (x > 0) {
						if (input[y-1][x-1] == '1') {
							count++;
						}
					}
					if (input[y-1][x] == '1') {
						count++;
					}
					if (x < width - 1) {
						if (input[y-1][x+1] == '1') {
							count++;
						}
					}
				}

				if (x > 0) {
					if (input[y][x-1] == '1') {
						count++;
					}
				}
				if (x < width - 1) {
					if (input[y][x+1] == '1') {
						count++;
					}
				}

				if (y + 1 < height) {
					if (x > 0) {
						if (input[y+1][x-1] == '1') {
							count++;
						}
					}
					if (input[y+1][x] == '1') {
						count++;
					}
					if (x < width - 1) {
						if (input[y+1][x+1] == '1') {
							count++;
						}
					}
				}

				if (input[y][x] == '1') {
					if (count < 2) {
						output[y][x] = '0';
					} else if (count == 2 || count == 3) {
						output[y][x] = '1';
					} else {
						output[y][x] = '0';
					}
				} else {
					output[y][x] = (count == 3) ? '1' : '0';
				}
			}
		}

		return output;
	}

	public static void main(String args[]) {
		final Scanner scanner = new Scanner(System.in);
		final int width = scanner.nextInt(), height = scanner.nextInt();

		System.err.printf("%d %d%n", width, height);

		scanner.nextLine();

		char[][] grid = new char[height][width];

		for (int y = 0; y < height; y++) {
			final String line = scanner.nextLine();

			System.err.println(line);

			grid[y] = line.toCharArray();
		}

		grid = process(grid);

		for (char[] array : grid) {
			System.out.println(new String(array));
		}
	}
}
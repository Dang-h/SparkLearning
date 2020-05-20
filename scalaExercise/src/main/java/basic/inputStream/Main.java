package basic.inputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Scanner;
import java.util.StringTokenizer;

class Main {
	public static void main(String[] args) throws Exception {
		StringBuilder stringBuilder = new StringBuilder();
		try (Reader reader = new BufferedReader(new InputStreamReader(System.in))) {
			// start coding here
			Scanner scanner = new Scanner(reader);
			int i = 0;
			while (scanner.hasNext()) {
				scanner.next();
				i++;
			}
			System.out.println(i);

			// 法二：
			int read = reader.read();

			while (read != -1) {
				stringBuilder.append((char) read);
				read = reader.read();
				if (read == 10) {
					break;
				}
			}

			String line = stringBuilder.toString().trim();
			String[] words = line.split("\\s+");
			int count = 0;
			for (String word : words) {
				if (!word.equals("")) {
				count++;
				}
			}
			System.out.println(count);

		}
	}

}

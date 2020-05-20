package basic.inputStream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class Test2 {
	public static void main(String[] args) {
		try (Reader reader = new BufferedReader(new InputStreamReader(System.in))) {
			// start coding here

			StringBuilder builder = new StringBuilder();
			int read = reader.read();
			while (read != -1) {
				builder.append((char) read);
				read = reader.read();
				if (read == 10) {
					break;
				}
			}
			System.out.println(builder.reverse());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

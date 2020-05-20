package basic.inputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

class Main2 {
	public static void main(String[] args) throws Exception {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			// start coding here
			StringTokenizer tokens = new StringTokenizer(reader.readLine());
			System.out.println(tokens.countTokens());
		}
	}

}

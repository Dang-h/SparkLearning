package basic.inputStream;

import java.io.IOException;
import java.io.InputStream;

public class Test1 {
	public static void main(String[] args) {
		InputStream inputStream = System.in;

		try {
			int currentByte = inputStream.read();
			while (currentByte != -1) {
				if (currentByte != 10) {
					System.out.print(currentByte);
				}
				currentByte = inputStream.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

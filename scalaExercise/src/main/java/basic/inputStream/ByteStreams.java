package basic.inputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ByteStreams {
	public static void main(String[] args) {
		// 获取file.txt前5个字节
		byte[] bytes = new byte[5];
		try {
			// 字节流使用FileInputStream获取
			FileInputStream inputStream = new FileInputStream("D:\\Develop\\Coding\\offerGoExercise\\scalaExercise\\src\\main\\java\\basic\\inputStream" +
					"\\file.txt");
			int numberOfBytes = inputStream.read(bytes);
			System.out.println(numberOfBytes);
			for (byte aByte : bytes) {
				System.out.println(aByte);
			}

			// 方法2：
			int currentByte = inputStream.read();
			while (currentByte != -1) {
//				System.out.println(currentByte);
				currentByte = inputStream.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

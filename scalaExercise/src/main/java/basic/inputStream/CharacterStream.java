package basic.inputStream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CharacterStream {
	public static void main(String[] args) throws IOException {
		// 字符流获取数据使用FileReader
		FileReader reader = new FileReader("D:\\Develop\\Coding\\offerGoExercise\\scalaExercise\\src\\main\\java\\basic\\inputStream\\file.txt");

		// 使用read获取数据
		char first = (char)reader.read();
		char second = (char)reader.read();

		// 方法1
//		char[] others = new char[12];
//		int number = reader.read(others);

		// 方法2
		int charAsNumber = reader.read();
		while (charAsNumber != -1) {
			char character = (char) charAsNumber;
			System.out.println(character);
			charAsNumber = reader.read();
		}
	}
}

package basic.swingTest.npe;

import java.util.Scanner;
import java.util.Locale;

public class Test1 {

	/* Fix this method */
	public static String toUpperCase(String str) {
//		String result = "";
//		if (str == null) {
//			return result;
//		} else {
//			result = str.toUpperCase(Locale.ENGLISH);
//		}
//		return result;
		return str == null ? "" : str.toUpperCase(Locale.ENGLISH);
	}

	/* Do not change code below */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		line = "none".equalsIgnoreCase(line) ? null : line;
		System.out.println(toUpperCase(line));
	}
}
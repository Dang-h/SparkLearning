package basic.swingTest.npe;

import java.util.*;

class FixingNullPointerException {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String string = scanner.nextLine();
		string = "null".equals(string) ? null : string;
		/* Do not change code above */

		System.out.println(string == null ? "NPE":string.toLowerCase());
	}
}
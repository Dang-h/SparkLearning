import org.junit.Test;

import java.util.ArrayList;
import java.util.Scanner;

public class TestMethod {

	@Test
	public void testFor() {
		int num = 5;
		int count = 0;

		for (int i = 1; i <= num; i++) {
			System.out.print(count++ + " ");
		}
	}

	@Test
	public void burgs() {
//		System.out.println(extractInt(0.55));

//		Scanner sc = new Scanner(System.in);
		System.out.println(isComposite(3));
		System.out.println(isComposite1(3));

		System.out.println(Short.MAX_VALUE);
		System.out.println(Integer.MAX_VALUE);

	}

	public static int extractInt(double d) {
		return (int) d;
	}

	public static boolean isVowel(char ch) {
		ArrayList<Character> vowelList = new ArrayList<>();
		vowelList.add('a');
		vowelList.add('e');
		vowelList.add('i');
		vowelList.add('o');
		vowelList.add('u');
		vowelList.add('A');
		vowelList.add('E');
		vowelList.add('I');
		vowelList.add('O');
		vowelList.add('U');

		return vowelList.contains(ch);
	}

	public static boolean isVowel2(char ch) {
		char[] vowel = {'a', 'e', 'i', 'o', 'u'};
		ch = Character.toLowerCase(ch);


		for (char c : vowel) {
			if (ch == c) {
				return true;
			}
		}

		return false;
	}

	public static boolean isComposite(long number) {
		if (number < 2) return false;
		if (number == 2) return false;
		if (number % 2 == 0) return true;
		for (int i = 3; i < number; i += 2) {
			if (number % i == 0) {
				return true;
			}
		}
		return false;
	}

	public static boolean isComposite1(long number) {
		boolean flag = true;

		for (int i = 2; i < number; i++) {
			if (number % i == 0) {
				flag = false;
				break;
			}
		}
		return !flag;
	}

	public static boolean isComposite2(long number) {
		for (int i = 2; i < (int) Math.sqrt(number) + 1; i++) {
			if (number % i == 0) {
				return true;
			}
		}

		return false;
	}

	@Test
	public void clock() {

	}


}

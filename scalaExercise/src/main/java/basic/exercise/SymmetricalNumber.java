package basic.exercise;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class SymmetricalNumber {
	public static void main(String[] args) {
		/*
		Scanner scanner = new Scanner(System.in);
		String input = scanner.next();
		ArrayList<Character> numChar = new ArrayList<>();

		if (input.equals("0") || input.equals("00") || input.equals("000")) {
			System.out.println(new Random().nextInt());
		} else if (input.length() != 4) {
			System.out.println(new Random().nextInt());
		} else {
			numChar.add(input.charAt(0));
			numChar.add(input.charAt(1));
			numChar.add(input.charAt(2));
			numChar.add(input.charAt(3));
			if (numChar.get(0).equals(numChar.get(3)) && numChar.get(1).equals(numChar.get(2))) {
				System.out.println(1);
			}else {
				System.out.println(new Random().nextInt());
			}
		}
		 */
		StringBuilder input = new StringBuilder(new Scanner(System.in).next());
		boolean isSymmetric = true;

		while (input.length() < 4) {
			input.insert(0, "0");
		}
		for (int i = 0; i < input.length() / 2; i++) {
			isSymmetric = isSymmetric && input.charAt(i) == input.charAt((input.length() - 1) - i);
		}
		System.out.println(isSymmetric ? 1 : 0);
	}
}

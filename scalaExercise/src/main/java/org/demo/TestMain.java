package org.demo;

import java.util.Scanner;

public class TestMain {
	public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);

		String str = scanner.next();

		if (str.toUpperCase().startsWith("J")) {
			System.out.println(true);
		} else {
			System.out.println(false);
		}

	}
}

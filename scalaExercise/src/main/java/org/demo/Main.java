package org.demo;


import java.util.Scanner;

public class Main {

	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	private long balance;
	private String ownerName = null;
	private boolean locked;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		int m = scanner.nextInt();
		double p = scanner.nextInt();
		int k = scanner.nextInt();

		int count = 0;


		while (k > m) {
			m = (int) (m + m * p / 100);
			count++;
		}

		System.out.println(count);
	}
}


package org.demo;

public class CheckingAccount extends BankAccount {
	private double fee;

	public CheckingAccount(String number, Long balance, double fee) {
		super(number, balance);
		this.fee = fee;
	}
}

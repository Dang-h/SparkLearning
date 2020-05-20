package org.demo;

public class SavingAccount extends BankAccount {
	private double interestRate;

	public SavingAccount(String number, Long balance, double interestRate) {
		super(number, balance);
		this.interestRate = interestRate;
	}
}

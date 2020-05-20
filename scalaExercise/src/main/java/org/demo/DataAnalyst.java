package org.demo;

public class DataAnalyst extends Employee{
	boolean phd;
	String[] method;

	public DataAnalyst(String name, String email, int experience, boolean phd, String[] method) {
		super(name, email, experience);
		this.phd = phd;
		this.method = method;
	}

	public boolean isPhd() {
		return phd;
	}

	public void setPhd(boolean phd) {
		this.phd = phd;
	}

	public String[] getMethod() {
		return method;
	}

	public void setMethod(String[] method) {
		this.method = method;
	}
}

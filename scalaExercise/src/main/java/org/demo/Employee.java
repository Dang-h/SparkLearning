package org.demo;

public class Employee {
	String name;
	String email;
	int experience;

	public Employee(String name, String email, int experience) {
		this.name = name;
		this.email = email;
		this.experience = experience;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}
}

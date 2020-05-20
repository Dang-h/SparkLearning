package org.demo;

public class Developer extends Employee{
	String mainLanguage;
	String[] skills;

	public Developer(String name, String email, int experience, String mainLanguage, String[] skills) {
		super(name, email, experience);
		this.mainLanguage = mainLanguage;
		this.skills = skills;
	}

	public String getMainLanguage() {
		return mainLanguage;
	}

	public void setMainLanguage(String mainLanguage) {
		this.mainLanguage = mainLanguage;
	}

	public String[] getSkills() {
		return skills;
	}

	public void setSkills(String[] skills) {
		this.skills = skills;
	}
}

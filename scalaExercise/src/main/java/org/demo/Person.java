package org.demo;

public class Person {
	private static long nextId = 1;

	long id;
	String name;

	public Person(String name) {
		this.name = name; // (2)
		this.id = nextId;
		nextId++; // (3)
	}

	public static long getNextId() { // (4)
		return nextId;
	}
}

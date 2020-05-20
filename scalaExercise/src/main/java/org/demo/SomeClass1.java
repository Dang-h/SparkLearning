package org.demo;

class SomeClass1 {

	int val = 50;
	String str = "default";

	public SomeClass1() {
		this(100);
	}

	public SomeClass1(int val) {
		this.val = val;
	}

	public SomeClass1(String str) {
		this();
		this.str = "some-value";
	}

	public SomeClass1(int val, String str) {
		this(str);
	}
}

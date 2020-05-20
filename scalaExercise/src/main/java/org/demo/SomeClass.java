package org.demo;

class SomeClass {

	int val = 50;
	String str = "default";

	public SomeClass() {
		this(100);
	}

	public SomeClass(int valOfInstance) {
		valOfInstance = val;
	}

	public SomeClass(String strOfInstance) {
		this();
		this.str = "some-value";
	}

	public SomeClass(int valOfInstance, String strOfInstance) {
		this(strOfInstance);
	}
}

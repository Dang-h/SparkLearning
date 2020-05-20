package org.demo.myPackage;

public class B {
	// if two classes are in the same package you can use them without import.
	// And to access class without importing package you have to use full name
	B b = new B();
	C c1 = new C();
	C c2 = new org.demo.myPackage.C();
	org.demo.A a = new org.demo.A();
}

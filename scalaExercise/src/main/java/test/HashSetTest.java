package test;

import java.util.HashSet;

public class HashSetTest {
	public static void main(String[] args) {
		HashSet<String> stringHashSet = new HashSet<>();
		stringHashSet.add("Tom");
		stringHashSet.add("Tom");
		stringHashSet.add("Jack");
		stringHashSet.add("John");

		System.out.println(stringHashSet);
	}
}

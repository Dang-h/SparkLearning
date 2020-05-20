package test;

import java.util.HashMap;

public class HashMapTest {
	public static void main(String[] args) {
		// 底层在jdk1.8之前是数组+链表实现的，在jdk1.8中是由数组+链表+红黑树实现的
		HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();

		stringIntegerHashMap.put("one", 1);
		stringIntegerHashMap.put("two", 2);
		stringIntegerHashMap.put("three", 3);

		System.out.println(stringIntegerHashMap);
		System.out.println(("=========================="));

		String sentence = "AAAAAAAAAABBBBBBBBCCCCCDDDDDDD";
		HashMap<Character, Integer> charCountMap = new HashMap<>();
		char[] character = sentence.toCharArray();

		for (char c : character) {
			if (charCountMap.containsKey(c)) {
				Integer count = charCountMap.get(c);
				charCountMap.put(c, count + 1);
			} else{
				charCountMap.put(c, 1);
			}
		}
		System.out.println(charCountMap);

	}
}

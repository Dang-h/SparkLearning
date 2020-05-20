package basic.anonymousClass;

import java.lang.reflect.Array;

public class Test2 {

	public static Runnable createRunnable(String text, int repeats) {
		return new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < repeats; i++) {
					System.out.println(text);
				}
				text.length();
				Array[] arr = new Array[10];
				System.out.println(arr.length);
			}
		};
	}
}
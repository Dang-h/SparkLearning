package basic.anonymousClass;

import com.sun.deploy.util.DeploySysAction;
import org.demo.myPackage.C;

import java.util.Scanner;

public class CallbackExample {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		int a = sc.nextInt();
		int b = sc.nextInt();

		Divider.divide(a, b, new Callback() {
			@Override
			public void calculated(int result) {
				String testToPrint = String.format("%d / %d is %d", a, b, result);
				print(testToPrint);
			}

			@Override
			public void failed(String errorMsg) {
				print(errorMsg);
			}
		});
	}

	public static void print(String text) {
		System.out.println(text);
	}
}

interface Callback{
	void calculated(int result);

	void failed(String errorMsg);
}

class Divider{
	public static void divide(int a, int b, Callback callback) {
		if (b == 0) {
			callback.failed("Division by zero!");
			return;
		}

		callback.calculated(a / b);
	}
}

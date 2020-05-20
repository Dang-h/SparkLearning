package basic.anonymousClass;

public class Test1 {

	ThreeMethodsInterface instance = new ThreeMethodsInterface() {
		@Override
		public void do1() {
			System.out.println("Implemented do1");
		}

		@Override
		public void do2() {
			System.out.println("Implemented do2");
		}

		@Override
		public void do3() {
			System.out.println("Implemented do3");
		}
	};
}

interface ThreeMethodsInterface {

	void do1();

	void do2();

	void do3();
}
package basic.hidingAndOverriding;

public class Cat extends Animal {
	@Override
	public void say() {
		System.out.println("meow-meow");
	}
}

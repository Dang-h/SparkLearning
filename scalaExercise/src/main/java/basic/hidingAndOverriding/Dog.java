package basic.hidingAndOverriding;

public class Dog extends Animal{
	@Override
	public void say() {
		System.out.println("arf-arf");
	}
}

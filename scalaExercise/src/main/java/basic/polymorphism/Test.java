package basic.polymorphism;

public class Test {
	public static void main(String[] args) {
		MythicalAnimal chimera = new Chimera();
		MythicalAnimal dragon = new Dragon();
		MythicalAnimal animal = new MythicalAnimal();

		// the result of a method call depends on the actual type of instance, not the reference type
		chimera.hello(); // Hello! Hello!
		dragon.hello(); // Rrrr...
		animal.hello(); // Hello, i'm an unknown animal

		File img = new ImageFile("\\path\\image.png",480, 640, 12); // assigning an object
		// run-time polymorphism allows you to invoke an overridden method of a subclass having a reference to the base class.
		img.printFileInfo();
	}
}

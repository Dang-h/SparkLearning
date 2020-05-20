package mammals;

import birds.Bird;
import mammals.felines.Cat;

public class Main {
	public static void main(String[] args) {
		Cat cat = new Cat();
		Bird bird = new Bird();
		Mammal mammal = new Mammal();

		cat.motherChild();
		bird.fly();
	}
}

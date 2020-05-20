package mammals;

public class Mammal {
	// 同包和子类访问
	protected void motherChild(){
		System.out.println("My baby has just born");
	}

	// 只能同包访问
	void yell(){
		System.out.println("I am a mammal!");
	}
}

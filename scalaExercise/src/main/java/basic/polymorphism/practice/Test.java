package basic.polymorphism.practice;

public class Test {
	public static void main(String[] args) {
		BaseNumberGenerator generator = new MagicNumberGenerator(10);
		System.out.println(generator.generate());
	}
}

class BaseNumberGenerator {

	protected int base;

	public BaseNumberGenerator(int base) {
		this.base = base;
	}

	public int generate() {
		return base + 11;
	}
}

class NumberGenerator extends BaseNumberGenerator {


	public NumberGenerator(int base) {
		super(base);
	}

	@Override
	public int generate() {

		return super.generate() + getNumber();
	}


	protected int getNumber() {
		return this.base - 7;
	}
}

class MagicNumberGenerator extends NumberGenerator {


	public MagicNumberGenerator(int base) {
		super(base);
	}

	@Override
	protected int getNumber() {
		return this.base + 7;
	}
}
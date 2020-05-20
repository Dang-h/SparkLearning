package basic.hidingAndOverriding;

public class Circle extends Shape{
	double radius;

	@Override
	public double area() {
		return Math.PI * radius * radius;
	}
}

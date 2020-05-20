package basic.hidingAndOverriding;

public class Square extends Shape{
	double side;

	@Override
	public double area() {
		return side * side;
	}
}

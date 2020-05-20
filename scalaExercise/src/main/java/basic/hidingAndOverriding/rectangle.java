package basic.hidingAndOverriding;

public class rectangle extends  Shape{
	double width;
	double height;

	@Override
	public double area() {
		return width * height;
	}
}

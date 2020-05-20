package basic.abstractPrac;

public class Test3 {
	public static void main(String[] args) {
		Circle circle = new Circle(10);
		Triangle triangle = new Triangle(3, 4, 5);
		Rectangle rectangle = new Rectangle(5, 10);

		System.out.println(circle.getArea() + " " + circle.getPerimeter());
		System.out.println(triangle.getArea() + " " + triangle.getPerimeter());
		System.out.println(rectangle.getArea() + " " + rectangle.getPerimeter());
	}
}

abstract class Shape {

	abstract double getPerimeter();

	abstract double getArea();
}

class Triangle extends Shape {
	double a;
	double b;
	double c;


	public Triangle(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	double getPerimeter() {
		return a + b + c;
	}

	@Override
	double getArea() {
		double p = (a + b + c) / 2;
		return Math.sqrt(p * (p - a) * (p - b) * (p - c));
	}
}

class Circle extends Shape {
	double radius;

	public Circle(double radius) {
		this.radius = radius;
	}

	@Override
	double getPerimeter() {
		return 2 * Math.PI * radius;
	}

	@Override
	double getArea() {
		return Math.PI * Math.pow(radius, 2);
	}
}

class Rectangle extends Shape {
	double a;
	double b;

	public Rectangle(double a, double b) {
		this.a = a;
		this.b = b;
	}

	@Override
	double getPerimeter() {
		return (a + b) * 2;
	}

	@Override
	double getArea() {
		return a * b;
	}
}
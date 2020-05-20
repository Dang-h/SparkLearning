package basic.interfacceTest;

public class Test1 {
	public static void main(String[] args) {
		MutableShape circle = new Circle(2.0f, 3.5f, 10.1f);
		circle.move(10.1f, 20.2f);
		circle.scale(2.2f);
		System.out.println(((Circle) circle).getRadius());
	}
}

interface Movable {

	void move(float dx, float dy);
}

interface Scalable {

	void scale(float factor);
}

interface MutableShape extends Movable, Scalable{

}

final class Circle implements MutableShape{

	/**
	 * Defines the horizontal position of the center of the circle
	 */
	private float centerX;

	/**
	 * Defines the vertical position of the center of the circle
	 */
	private float centerY;

	/**
	 * Defines the radius of the circle
	 */
	private float radius;

	public Circle(float centerX, float centerY, float radius) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.radius = radius;
	}

	public float getCenterX() {
		return centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public float getRadius() {
		return radius;
	}

	@Override
	public void move(float dx, float dy) {
		this.centerX += dx;
		this.centerY += dy;
	}

	@Override
	public void scale(float factor) {
		this.radius *= factor;
	}
}

final class Rectangle implements MutableShape {

	/**
	 * Defines the X coordinate of the upper-left corner of the rectangle.
	 */
	private float x;

	/**
	 * Defines the Y coordinate of the upper-left corner of the rectangle.
	 */
	private float y;

	/**
	 * Defines the width of the rectangle.
	 */
	private float width;

	/**
	 * Defines the height of the rectangle.
	 */
	private float height;

	public Rectangle(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	@Override
	public void move(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	@Override
	public void scale(float factor) {
		this.height *= factor;
		this.width *= factor;
	}
}
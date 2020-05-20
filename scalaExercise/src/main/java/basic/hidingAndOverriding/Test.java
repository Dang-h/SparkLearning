package basic.hidingAndOverriding;

public class Test {
	public static void main(String[] args) {
		Newspaper newspaper = new Newspaper("Football results", "Sport news");
		System.out.println(newspaper.getDetails());

		System.out.println("********");
		System.out.println("      **");
		System.out.println("      **");
		System.out.println("      **");
		System.out.println("**    **");
		System.out.println("**    **");
		System.out.println(" *******");
	}
}

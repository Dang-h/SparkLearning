package basic.hidingAndOverriding;

public class Publication {
	private String title;

	public Publication(String title) {
		this.title = title;
	}

	public String getDetails() {
		return "title=\"" + title + "\"";
	}
}

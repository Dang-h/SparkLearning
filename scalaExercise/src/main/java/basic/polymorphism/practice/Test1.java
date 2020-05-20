package basic.polymorphism.practice;

public class Test1 {
	public static void main(String[] args) {
		TeamLead programmer = new Programmer(1);
	}

	public static class TeamLead {
		private int numTeamLead;

		public TeamLead(int numTeamLead) {
			this.numTeamLead = numTeamLead;
			employ();
		}

		private void employ() {
			System.out.println(numTeamLead + " teamlead");
		}

	}

	public static class Programmer extends TeamLead {
		private int numProgrammer;

		public Programmer(int numProgrammer) {
			super(numProgrammer);
			this.numProgrammer = numProgrammer;
			employ();

		}

		protected void employ() {
			System.out.println(numProgrammer + " programmer");
		}
	}
}

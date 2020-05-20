package basic.objectTest;

public class Test1 {
	public static void createArmy(){
		Unit u1 = new Unit("one");
		Unit u2 = new Unit("two");
		Unit u3 = new Unit("three");
		Unit u4 = new Unit("four");
		Unit u5 = new Unit("five");

		Knight k1 = new Knight("k-one");
		Knight k2 = new Knight("k-two");
		Knight k3 = new Knight("k-three");

		General tom = new General("Tom");
		Doctor jerry = new Doctor("Jerry");
	}
}

class Unit {
	String nameUnit;

	public Unit(String name){
		nameUnit = name;
	}
}

class Knight {
	String nameKnight;

	public Knight(String name){
		nameKnight = name;
	}
}

class General {
	String nameGeneral;

	public General(String name){
		nameGeneral = name;
	}
}

class Doctor {
	String nameDoctor;

	public Doctor(String name){
		nameDoctor = name;
	}
}

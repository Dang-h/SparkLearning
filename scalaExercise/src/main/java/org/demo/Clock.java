package org.demo;

public class Clock {

	public static void main(String[] args) {
		Clock clock = new Clock();
		clock.next();
	}

	int hours = 12;
	int minutes = 0;

	void next() {
		if (minutes == 59) {
			if (hours == 12) {
				hours = 1;
			} else {
				hours += 1;
			}
			minutes = 0;
		} else {
			minutes += 1;
		}
	}
}

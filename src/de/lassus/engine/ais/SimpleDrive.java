package de.lassus.engine.ais;

import de.lassus.engine.*;

public class SimpleDrive implements AI {

	Car car;
	
	@Override
	public void init(Car c) {
		car = c;
	}

	@Override
	public double act() {
		return 0.1;
	}

}

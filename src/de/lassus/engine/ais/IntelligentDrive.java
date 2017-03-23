package de.lassus.engine.ais;

import java.util.Random;

import de.lassus.engine.AI;
import de.lassus.engine.Car;

public class IntelligentDrive implements AI{

	Car car;
	
	double acceleration;
	
	int driver = 0;
	String[] drivers = {"Hausfrau", "Unternehmer", "Oma", "Teenager"};
	double[] maxVelocity = {0,0,0,0};
	double[] minVelocity = {0,0,0,0};
	double[] maxAcceleration = {0,0,0,0};
	double[] maxBreak = {0,0,0,0};
	double[] minDist = {0,0,0,0};
	double[] k;
			
	Random rnd = new Random();
	
	@Override
	public void init(Car c) {
		car = c;
		int rndDriver = rnd.nextInt(4);
		driver = rndDriver;
		setVars();
	}

	@Override
	public double act() {
		
		return acceleration;
	}

	public void setVars() {
		car.setMaxAccel(maxAcceleration[driver]);
		car.setMinAccel(maxBreak[driver]);
		car.setMaxVel(maxVelocity[driver]);
		car.setMinVel(minVelocity[driver]);
	}
	
}

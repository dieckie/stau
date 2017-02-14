package de.lassus.engine.ais;

import java.awt.Color;
import java.util.Random;

import de.lassus.engine.AI;
import de.lassus.engine.Car;
import de.lassus.engine.Engine;

public class BetterDrive implements AI{

	final static int VORRAUSSICHT = 30;
	Random rnd = new Random();
	boolean breaked = false;
	
	Car car;
	Color col;
	
	@Override
	public void init(Car c) {
		car = c;
		col = car.getColor();
	}

	@Override
	public double act() {
		Car[] nearestCars = Engine.engine.getNearestCars(car);
		double acceleration = 0;
		if (nearestCars[1] != null) {
			double x = simMovement(nearestCars[1].getX(), nearestCars[1].getVelocity(), nearestCars[1].getAcceleration(), VORRAUSSICHT);
			acceleration = calcAcceleration(car.getX(), x - 7, car.getVelocity(), VORRAUSSICHT);
			
			//if(breaked) {
			//	car.setColor(col);
			//	breaked = false;
			//}
			
			int rndBrChance = rnd.nextInt(10000);
			double rndBrValue = (rnd.nextDouble()/5)-0.1;
			if(rndBrChance == 0) {
				acceleration += rndBrValue;
				car.setColor(Color.black);
				breaked = true;
			}
			

		} else {
			acceleration = 0;
			if (car.getVelocity() < 0.9) {
				acceleration = 0.05;
				car.setColor(Color.BLACK);
				breaked = true;
			}
			
		}
		if (Double.isInfinite(acceleration)) {
			System.out.println(this);
		}
		return acceleration;
	}
	
	public double calcAcceleration(double currX, double destX, double v, double t) {
		return 2 * (destX - currX - v * t) / (t * t);
	}

	public double simMovement(double x, double v, double a, double t) {
		return 0.5 * a * t * t + v * t + x;
	}
}

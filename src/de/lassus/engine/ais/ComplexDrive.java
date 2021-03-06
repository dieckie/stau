package de.lassus.engine.ais;

import de.lassus.engine.*;

public class ComplexDrive implements AI {

	final static int VORRAUSSICHT = 30;

	Car car;

	@Override
	public void init(Car c) {
		car = c;
	}

	@Override
	public double act() {
		Car[] nearestCars = Engine.engine.getNearestCars(car);
		double acceleration = 0;
		if (nearestCars[0] != null && nearestCars[1] != null) {
			double x1 = simMovement(nearestCars[0].getX(), nearestCars[0].getVelocity(), nearestCars[0].getAcceleration(), VORRAUSSICHT);
			double x2 = simMovement(nearestCars[1].getX(), nearestCars[1].getVelocity(), nearestCars[1].getAcceleration(), VORRAUSSICHT);
			acceleration = calcAcceleration(car.getX(), (x1 + x2) * 0.5 - 0.1, car.getVelocity(), VORRAUSSICHT);

		} else {
			acceleration = 0;
			if (car.getVelocity() < 0.9) {
				acceleration = 0.05;
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

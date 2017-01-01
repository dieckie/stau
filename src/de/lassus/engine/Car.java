package de.lassus.engine;

import java.awt.Color;

public class Car {

	Color c;

	int id;
	
	//GEOMETRISCH
	public final static double WIDTH = 2D;
	public final static double LENGTH = 4;
	
	//AUTOKONTROLLE
	final static double MAX_VELOCITY = 1;
	final static double MIN_VELOCITY = -0.1;
	
	final static double MAX_ACCEL = 0.01;
	final static double MIN_ACCEL = -0.01;
	
	//KIKONTROLLE
	final static double VORRAUSSICHT = 100;
	

	private double x;
	private double velocity;
	private double acceleration = 0;

	public Car(Color c, int id) {
		this.c = c;
		velocity = 0.4;
		x = -LENGTH;
		this.id = id;
	}

	public void act() {
		Car[] nearestCars = Engine.engine.getNearestCars(this);
		if (nearestCars[0] != null && nearestCars[1] != null) {
			double x1 = simMovement(nearestCars[0].getX(), nearestCars[0].getVelocity(), nearestCars[0].getAcceleration(), VORRAUSSICHT);
			double x2 = simMovement(nearestCars[1].getX(), nearestCars[1].getVelocity(), nearestCars[1].getAcceleration(), VORRAUSSICHT);
			
			double perfectX = calcAcceleration(getX(), (x1 + x2) * 0.5, getVelocity(), VORRAUSSICHT);
			double deltaPerfectX = perfectX - x;
			acceleration = Math.pow(Math.abs(deltaPerfectX), 0.001);
			if(deltaPerfectX < 0) {
				acceleration *= -1;
			}
			acceleration = calcAcceleration(getX(), (x1 + x2) * 0.5, getVelocity(), VORRAUSSICHT);
		} else {
			acceleration = 0;
			if(velocity < 0.7) {
				acceleration = 0.05;
			}
		}
		
		acceleration = Math.min(MAX_ACCEL, acceleration);
		acceleration = Math.max(MIN_ACCEL, acceleration);
		velocity += acceleration;
		velocity = Math.min(MAX_VELOCITY, velocity);
		velocity = Math.max(MIN_VELOCITY, velocity);
		x += velocity;
	}
	
	public double calcAcceleration(double currX, double destX, double v, double t) {
		if(t == 1) {
			
		}
		return 2 * (destX - currX - v*t) / (t * t);
	}
	
	public double simMovement(double x, double v, double a, double t) {
		return 0.5 * a * t * t + v * t + x;
	}


	public Color getColor() {
		return c;
	}

	public double getX() {
		return x;
	}

	public double getVelocity() {
		return velocity;
	}
	
	public double getAcceleration() {
		return acceleration;
	}

	public double getWidth() {
		return WIDTH;
	}

	public double getLength() {
		return LENGTH;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Car(" + id + ":x=" + x + ",v=" + velocity + ")";
	}

}

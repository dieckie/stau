package de.lassus.engine;

import java.awt.Color;

import de.lassus.engine.ais.*;

public class Car {

	Color c;

	int id;

	// GEOMETRISCH
	public final static double WIDTH = 2D;
	public final static double LENGTH = 4;

	// AUTOKONTROLLE
	final static double MAX_VELOCITY = 1;
	final static double MIN_VELOCITY = -0.1;

	final static double MAX_ACCEL = 0.003;
	final static double MIN_ACCEL = -0.01;

	// KIKONTROLLE
	private AI ai;

	private double x;
	private double velocity;
	private double acceleration = 0;

	public Car(Color c, int id, AIType aiType) {
		this.c = c;
		velocity = 0.6;
		x = -LENGTH;
		this.id = id;

		switch (aiType) {
		case ComplexDrive:
			ai = new ComplexDrive();
			break;
		case SimpleDrive:
			ai = new SimpleDrive();
			break;
		case BetterDrive:
			ai = new BetterDrive();
			break;
		default:
			break;

		}
		ai.init(this);

	}

	public void act() {
		acceleration = ai.act();
		acceleration = Math.min(MAX_ACCEL, acceleration);
		acceleration = Math.max(MIN_ACCEL, acceleration);
		velocity += acceleration;
		velocity = Math.min(MAX_VELOCITY, velocity);
		velocity = Math.max(MIN_VELOCITY, velocity);
		x += velocity;
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

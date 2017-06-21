package de.lassus.engine;

import java.awt.Color;

import de.lassus.engine.ais.*;

public class Car {

	Color c;

	int id;

	// GEOMETRISCH
	public final static double WIDTH = 2;
	public final static double LENGTH = 4;

	// AUTOKONTROLLE
	public final static double MAX_VELOCITY = 2;
	public final static double MIN_VELOCITY = -0.5;

	public final static double MAX_ACCEL = 0.003;
	public final static double MIN_ACCEL = -0.005;

	// KIKONTROLLE
	private AI ai;

	private double x;
	private double velocity;
	private double acceleration = 0;
	private int lane = 0;
	
	double maxVel = 1;
	double minVel = 0;
	double maxAccel = 0;
	double minAccel = 0;

	public Car(Color c, int id, AIType aiType) {
		this.c = c;
		velocity = 0;
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
		case IntelligentDrive:
			ai = new IntelligentDrive();
			break;
		case HumanDrive:
		    ai = new HumanDrive();
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
		maxVel = Math.min(MAX_VELOCITY, maxVel);
		velocity = Math.min(MAX_VELOCITY, velocity);
		velocity = Math.max(MIN_VELOCITY, velocity);
		x += velocity * Engine.SIMULATION_SPEED;
	}
	
	public Color getColor() {
		return c;
	}
	
	public int getLane() {
		return lane;
	}

	public double getX() {
		return x;
	}
	
	public void setX(double x) {
	    this.x = x;
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
	
	public void setColor(Color col) {
		c = col;
	}
	
	public void setMaxVel(double maxvel) {
		maxVel = maxvel;
	}
	
	public void setMinVel(double minvel) {
		minVel = minvel;
	}
	
	public void setMaxAccel(double maxaccel) {
		maxAccel = maxaccel;
	}
	
	public void setMinAccel(double minaccel) {
		minAccel = minaccel;
	}

	@Override
	public String toString() {
		return "Car(" + id + ":x=" + x + ",v=" + velocity + ")";
	}

}

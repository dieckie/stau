package de.lassus.engine.ais;

import java.awt.Color;
import java.util.Random;

import de.lassus.engine.AI;
import de.lassus.engine.Car;
import de.lassus.engine.Engine;

public class BetterDrive implements AI{

	final static int VORRAUSSICHT = 30;
	
	Random rnd = new Random();
	boolean braking = false;
	double brActVal = 0;
	double brMaxVal = 0;
	double brActLength = 0;
	double brMaxLength = 0;
	double lastAccel = 0;
	double MaxVel = 1;
	
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
			lastAccel = acceleration;
			
			//bremsen mit einer bestimmten Wahrscheinlichkeit mit einer zufälligen Starke pro tick zufällig lange bremsen bis die Zeit um ist oder das Auto steht
			int rndBrChance = rnd.nextInt(1000);
			if(rndBrChance == 0 && !braking) {
				double rndBrLength = (rnd.nextDouble()*30)+5;
				double rndBrMaxVal = rnd.nextDouble();
				brActVal = brMaxVal = rndBrMaxVal+0.5;
				brActLength = brMaxLength = rndBrLength;
				car.setColor(Color.black);
				braking = true;
			}
			if(braking) {
				acceleration -= brMaxVal;
				brActLength--;
				
				/*
				if(brActLength > (brMaxLength/4)*3) {
					//vor erstem Viertel
					//TODO Intensität von 0 auf 100% aufdrehen
				} else if (brActLength == (brMaxLength/4)*2) {
					//ende erstes Viertel
					//TODO Intensität == maxHigh
				} else if (brActLength < (brMaxLength/4)*3) {
					//letzte drei Viertel
					//TODO Intensität von 100% auf 0 runterfahren
				} else {
					System.out.println("Wie?????");
				}
				*/
				
			}
			if(car.getVelocity() <= 0 || brActLength <= 0) {
				car.setColor(col);
				braking = false;
			}
			
		} else {
			acceleration = 0;
			if (car.getVelocity() < 0.9) {
				acceleration = 0.05;
				//car.setColor(Color.BLACK);
				//Braked = true;
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

package de.lassus.engine.ais;

import java.awt.Color;
import java.util.Random;

import de.lassus.engine.AI;
import de.lassus.engine.Car;
import de.lassus.engine.Engine;

public class BetterDrive implements AI{

	final static int VORRAUSSICHT = 30;
	
	Random rnd = new Random();
	
	//Bremsen
	boolean breaking = false;
	double brMaxVal = 0;
	double brActLength = 0;
	boolean near = false;
	
	//maxGesch anpassen
	double MaxVel = 1;
	double dXcars = 1;
	
	double maxRangeToNext = 10;
	
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
			acceleration = calcAcceleration(car.getX(), x - maxRangeToNext, car.getVelocity(), VORRAUSSICHT);
			
			//Anpassung der maximalen Geschwindigkeit in Bezug auf die Entfernung zum vorderen Auto
			dXcars = nearestCars[1].getX() - car.getX();
			MaxVel = 0.075*dXcars+1;
			car.setMaxVel(MaxVel);
			
			//bremsen bei kleinem Abstand
			if(dXcars < maxRangeToNext && car.getVelocity() >= 0) {
				car.setColor(Color.white);
				double destX = simMovement(nearestCars[1].getX(), nearestCars[1].getVelocity(), nearestCars[1].getAcceleration(), 1);
				acceleration = (calcAcceleration(car.getX(), destX - maxRangeToNext, car.getVelocity(), 1))+0.1;
				//acceleration = 0;
				//acceleration -= maxRangeToNext - dXcars;
				near = true;
			} else {
				if(breaking) {
					car.setColor(col);
				}
				near = false;
			}
			
			//bremsen mit einer bestimmten Wahrscheinlichkeit mit einer zufälligen Starke pro tick zufällig lange bremsen bis die Zeit um ist oder das Auto steht
			//zufälliges bremsen initialisieren
			int rndBrChance = rnd.nextInt(10000);
			if(rndBrChance == 0 && !breaking) {
				double rndBrLength = (rnd.nextDouble()*30)+5;
				double rndBrMaxVal = rnd.nextDouble();
				brMaxVal = rndBrMaxVal+0.5;
				brActLength = rndBrLength;
				car.setColor(Color.black);
				breaking = true;
			}
			//bremsen
			if(breaking) {
				acceleration -= brMaxVal;
				brActLength--;
			}
			//bremsen beenden
			if(car.getVelocity() <= 0 || brActLength <= 0) {
				if(!near) {
				car.setColor(col);
				}
				breaking = false;
			}
			
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
	


	//=================================================================================================================//
	
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
	
	//=================================================================================================================//

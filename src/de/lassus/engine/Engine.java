package de.lassus.engine;

import java.awt.Color;
import java.util.*;

import javax.swing.JFrame;

import de.lassus.window.StauPanel;

public class Engine {

	public final static double SCALE = 8;
	public final static double ROWS = 10;
	public final static double LANES = 2;
	
	public static Engine engine;

	List<Car> cars;
	StauPanel stau;

	public Engine() {
		engine = this;
		cars = new LinkedList<>();
		JFrame frame = new JFrame();
		stau = new StauPanel(this);
		frame.add(stau);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		new Thread(new Runnable() {

			@Override
			public synchronized void run() {
				int i = 0;
				int car = 0;
				while (true) {
					i++;
					//if (Math.random() > 0.955 && getLastCarDist() > 0) {
					if (getLastCarDist() > 20) {
						Color color = Color.getHSBColor((car * 3 % 360) / 360f, 1, 1);
						cars.add(new Car(color, car++, AIType.BetterDrive));
					}
					ListIterator<Car> lit = cars.listIterator();
					while (lit.hasNext()) {
						Car c = lit.next();
						c.act();
						if (c.getX() > getWidth() + Car.LENGTH) {
							lit.remove();
						}
						stau.repaint();
					}
					try {
						wait(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).run();
	}

	/**
	 * Gibt das naechste Auto vor und hinter dem Uebergebenen Auto zurueck.
	 * 
	 * @param car
	 * @return Ein Car-Array: {Auto_hinter_car, Auto_vor_car};
	 */
	public Car[] getNearestCars(Car car) {
		Car[] nearestCars = new Car[2];
		double nextCarFront = Double.MAX_VALUE;
		double nextCarBehind = -Double.MAX_VALUE;
		for (Car c : cars) {
			if (c != car) {
				double deltaX = c.getX() - car.getX();
				if (deltaX > 0) {
					if (deltaX < nextCarFront) {
						nextCarFront = deltaX;
						nearestCars[1] = c;
					}
				} else {
					if (deltaX > nextCarBehind) {
						nextCarBehind = deltaX;
						nearestCars[0] = c;
					}
				}

			}
		}
		return nearestCars;
	}

	public double getLastCarDist() {
		double minDist = Double.MAX_VALUE;
		for (Car c : cars) {
			if (c.getX() < minDist) {
				minDist = c.getX();
			}
		}
		return minDist;
	}

	public List<Car> getCars() {
		return cars;
	}

	public double getRowWidth() {
		return stau.getWidth() / SCALE + Car.LENGTH;
	}

	public double getWidth() {
		return getRowWidth() * ROWS;
	}

}

package de.lassus.engine.ais;

import de.lassus.engine.AI;
import de.lassus.engine.Car;
import de.lassus.engine.Engine;

public class HumanDrive implements AI {

    Car car;

    final int REACTION_TIME = 5;
    final double MAX_DEVIATION = 0.1;

    @Override
    public void init(Car c) {
        this.car = c;
    }

    double deviation = 0;

    @Override
    public double act() {
        Car[] nearestCars = Engine.engine.getNearestCars(car);
        Car front = nearestCars[1];
        double destVel = 0.5;
        if(front != null) {
            double frontX = front.getX();
            if(frontX < car.getX()) {
                frontX += Engine.TRACKLENGTH * Engine.ROWS;
            }
            double distance = frontX - car.getX();
            double stopNextDistance = - 0.5 * front.getVelocity() * front.getVelocity() / Car.MIN_ACCEL;
            double stopXNext = frontX - 0.5 * front.getVelocity() * front.getVelocity() / Car.MIN_ACCEL;
            double stopMeDistance = -0.5 * car.getVelocity() * car.getVelocity() / Car.MIN_ACCEL + car.getVelocity() * REACTION_TIME;
            double maxPos = stopXNext - stopMeDistance + Car.LENGTH * 1.5;
            if(car.getId() == 24) {
                System.out.println(front.getId() + ", " + stopXNext + ", " + stopMeDistance + ", " + distance);
            }
            if(distance < stopMeDistance + Car.LENGTH * 1.5) {
                destVel = 0;
                return destVel - car.getVelocity();
            }
        }
        if(Math.random() < 0.05) {
            deviation += Math.random() * 0.01 - 0.005;
            if(deviation > MAX_DEVIATION) {
                deviation = MAX_DEVIATION;
            } else if(deviation < -MAX_DEVIATION) {
                deviation = -MAX_DEVIATION;
            }
        }
        destVel += deviation;
        return destVel - car.getVelocity();
    }

}

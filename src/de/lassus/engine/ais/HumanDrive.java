package de.lassus.engine.ais;

import de.lassus.engine.AI;
import de.lassus.engine.Car;
import de.lassus.engine.Engine;

public class HumanDrive implements AI {

    Car car;

    final int REACTION_TIME = 5;
    private boolean isComputer = false;
    final double MAX_DEVIATION = 0.3;

    double[] reactions = new double[REACTION_TIME];
    int current = 0;

    @Override
    public void init(Car c) {
        this.car = c;
    }

    double deviation = 0;

    @Override
    public double act() {
        Car[] nearestCars = Engine.engine.getNearestCars(car);
        Car front = nearestCars[1];
        double destVel = 1;
        if(front != null) {
            double frontX = front.getX();
            if(frontX < car.getX()) {
                frontX += Engine.TOTAL_LENGTH;
            }
            double distance = frontX - car.getX();
            double stopNextDistance = -0.5 * front.getVelocity() * front.getVelocity() / Car.MIN_ACCEL;
            double stopMeDistance = -0.5 * car.getVelocity() * car.getVelocity() / Car.MIN_ACCEL + car.getVelocity() * REACTION_TIME;
            if(distance + stopNextDistance < stopMeDistance + Car.LENGTH * 2) {
                destVel = 0;
            }
        }
        if(isComputer) {
            return destVel - car.getVelocity();
        } else {
            if(Math.random() < 0.5) {
                deviation += (Math.random() * 0.2 - 0.1);
                if(deviation > MAX_DEVIATION) {
                    deviation = MAX_DEVIATION;
                } else if(deviation < -MAX_DEVIATION) {
                    deviation = -MAX_DEVIATION;
                }
            }
            destVel += destVel * deviation;
            if(destVel < 0) {
                destVel = 0;
            }
            double nowVel = reactions[current];
            reactions[current] = destVel - car.getVelocity();
            current++;
            current %= REACTION_TIME;
            return nowVel;
        }

    }

    public void setComputer(boolean isComputer) {
        this.isComputer = isComputer;
    }

}

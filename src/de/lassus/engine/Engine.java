package de.lassus.engine;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;

import de.lassus.engine.ais.HumanDrive;
import de.lassus.window.StauPanel;

public class Engine {

    // public final static double SCALE = 8;
    public final static int ROWS = 20;
    public final static int LANES = 1;
    public final static double TRACKLENGTH = 80;
    public final static int CARS = 201;
    public final static double SIMULATION_SPEED = 1;

    public final static double COMPUTER_RATIO = 0.95;

    public final static double TOTAL_LENGTH = TRACKLENGTH * ROWS + 2 * Car.LENGTH;

    public static Engine engine;

    List<Car> cars;
    StauPanel stau;

    public Engine() {
        engine = this;
        cars = new LinkedList<>();
        JFrame frame = new JFrame("Stauprojekt");
        stau = new StauPanel(this);
        frame.add(stau);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        double margin = Engine.TOTAL_LENGTH / (CARS);

        int computerCars = (int) (CARS * COMPUTER_RATIO);
        for(int i = 0; i < CARS; i++) {
            Car c = new Car(Color.BLUE, i, AIType.HumanDrive);
            c.setX(-4 + margin * i);
            cars.add(c);
        }
        List<Car> forComputer = new LinkedList<Car>();
        forComputer.addAll(cars);
        for(int i = 0; i < computerCars; i++) {
            int index = (int)(Math.random() * forComputer.size());
            Car c = forComputer.get(index);
            forComputer.remove(index);
            ((HumanDrive) c.getAi()).setComputer(true);
            c.setColor(Color.ORANGE);
        }

        new Thread(new Runnable() {

            @SuppressWarnings("resource")
            @Override
            public synchronized void run() {
                FileWriter fw = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
                    fw = new FileWriter(new File(sdf.format(new Date()) + "data.csv"));
                } catch(IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    fw.write("Anzahl Computercars: " + (int) (CARS * COMPUTER_RATIO) + "\n");
                } catch(IOException e1) {
                    e1.printStackTrace();
                }
                while(true) {
                    long time = System.currentTimeMillis();
                    ListIterator<Car> lit = cars.listIterator();
                    while(lit.hasNext()) {
                        Car c = lit.next();
                        c.act();
                        try {
                            if(lit.hasNext()) {
                                fw.write((c.getX() + ";").replace(".", ","));
                            } else {
                                fw.write((c.getX() + "\n").replace(".", ","));
                            }
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                        if(c.getX() > getWidth() - Car.LENGTH) {
                            c.setX(-Car.LENGTH + (c.getX() - (getWidth() - Car.LENGTH)));
                        }

                    }
                    stau.repaint();
                    try {
                        if(System.currentTimeMillis() - time < 30) {
                            wait(30 - (System.currentTimeMillis() - time));
                        }
                    } catch(InterruptedException e) {
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
        for(Car c : cars) {
            if(c != car) {
                double deltaBehindX = c.getX() - car.getX();
                if(deltaBehindX > 0) {
                    deltaBehindX -= getWidth();
                }
                double deltaInFrontX = c.getX() - car.getX();
                if(deltaInFrontX < 0) {
                    deltaInFrontX += getWidth();
                }
                if(deltaInFrontX < nextCarFront) {
                    nextCarFront = deltaInFrontX;
                    nearestCars[1] = c;
                }
                if(deltaBehindX > nextCarBehind) {
                    nextCarBehind = deltaBehindX;
                    nearestCars[0] = c;
                }
            }
        }
        return nearestCars;
    }

    public double getLastCarDist() {
        double minDist = Double.MAX_VALUE;
        for(Car c : cars) {
            if(c.getX() < minDist) {
                minDist = c.getX();
            }
        }
        return minDist;
    }

    public List<Car> getCars() {
        return cars;
    }

    public double getRowWidth() {
        return Engine.TRACKLENGTH;
    }

    public double getWidth() {
        return getRowWidth() * ROWS + 2 * Car.LENGTH;
    }

}

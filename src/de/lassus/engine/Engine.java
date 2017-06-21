package de.lassus.engine;

import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.lassus.window.StauPanel;

public class Engine {

    // public final static double SCALE = 8;
    public final static int ROWS = 20;
    public final static int LANES = 1;
    public final static double TRACKLENGTH = 80;
    public final static double TOTAL_LENGTH = TRACKLENGTH * ROWS + 2 * Car.LENGTH;
    public final static int CARS = 120;
    public final static double SIMULATION_SPEED = 1;

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
        for(int i = 0; i < CARS; i++) {
            Color color = Color.getHSBColor((i * 3 % 360) / 360f, 1, 1);
            Car c = new Car(color, i, AIType.HumanDrive);
            c.setX(-4 + margin * i);
            cars.add(c);
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

                int i = 0;
                int car = 0;
                boolean addCars = true;
                while(true) {
                    i++;

                    if(addCars && getLastCarDist() > 70) {
                        // if (getLastCarDist() > 20) {
                        // Color color = Color.getHSBColor((car * 3 % 360) / 360f, 1, 1);
                        // cars.add(new Car(color, car++, AIType.HumanDrive));
                    }
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
                        if(addCars && c.getX() > getWidth() - Engine.TRACKLENGTH) {
                            // Autospawn deaktivieren, bevor der Kreis geschlossen wird
                            addCars = false;
                        }
                        if(c.getX() > getWidth() - Car.LENGTH) {
                            c.setX(-Car.LENGTH);
                        }
                        stau.repaint();
                    }
                    try {
                        wait(30);
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

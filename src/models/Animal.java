package models;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@code Animal} class represents one of the animals, which move
 * {@code Truck} object in the direction of a given angle
 *
 * @author Valeriy Klyukin
 * @see Runnable
 */

public class Animal extends Thread {

    /**
     * Borders of uniform distribution
     */
    public final static Map<String, String> defaultParams = new HashMap<String, String>() {
        {
            put("Min", "1000");
            put("Max", "5000");
            put("ShMin", "1");
            put("ShMax", "10");
        }
    };

    /**
     * Name of the animal
     */
    private final String name;
    /**
     * Reference of moving truck
     */
    private final Truck mTruck;
    /**
     * Angle in radians, which points the direction of movement
     */
    private double angleRad;

    /**
     *  Minimum time of sleep in ms
     */
    private int sleepMin;

    /**
     *  Maximum time of sleep in ms
     */
    private int sleepMax;

    /**
     *  Minimum shift of the truck
     */
    private int shiftMin;

    /**
     *  Maximum shift of the truck
     */
    private int shiftMax;

    /**
     * Initializes new {@code Animal} object with specified name, direction of movement and related to the concrete {@code Truck} object
     *
     * @param name Name of the animal
     * @param mTruck Related truck
     */
    public Animal(String name, Truck mTruck) {
        if (mTruck == null)
            throw new NullPointerException("Null track variable can not be assigned to Animal's field");
        else if (name == null) {
            throw new NullPointerException("Name of the animal can not be null");
        } else {
            this.name = name;
            this.mTruck = mTruck;
        }
    }

    /**
     * Implemented {@code run} method provided by {@code Runnable} interface
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                mTruck.shiftBy(angleRad, shiftMin, shiftMax);
                Thread.sleep(ThreadLocalRandom.current().nextInt(sleepMin, sleepMax));
            } catch (InterruptedException e) {
                System.out.println(this + " is totally exhausted");
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setSleepMin(int sleepMin) {
        this.sleepMin = sleepMin;
    }

    public void setSleepMax(int sleepMax) {
        this.sleepMax = sleepMax;
    }

    public void setShiftMin(int shiftMin) {
        this.shiftMin = shiftMin;
    }

    public void setShiftMax(int shiftMax) {
        this.shiftMax = shiftMax;
    }

    public void setAngle(double angleRad) {
        this.angleRad = Math.toRadians(angleRad);
    }

    public int getSleepMin() {
        return sleepMin;
    }

    public int getSleepMax() {
        return sleepMax;
    }

    public int getShiftMin() {
        return shiftMin;
    }

    public int getShiftMax() {
        return shiftMax;
    }

    public String getAnimalName() {
        return name;
    }

    /**
     * Overriden {@code toString} method
     * @return Name of the animal
     */
    public String toString() {
        return name;
    }
}

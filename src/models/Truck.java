package models;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@code Truck} class represents a model of truck motion (see given fable)
 *
 * @author Valeriy Klyukin
 */

public class Truck {

    /**
     * Coordinates of this current
     */
    private double x;
    private double y;
    private PositionBook positionBook;
    private long startTime;

    /**
     * Constructor with parameters
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Truck(double x, double y, PositionBook positionBook) {
        this.x = x;
        this.y = y;
        this.positionBook = positionBook;
        positionBook.clear();
        positionBook.add(new Position(0, x, y));
        startTime = System.currentTimeMillis();
    }

    /**
     * method, that implements shift of truck by random distance and predefined angle
     *
     * @param angleRad angle of truck's motion direction
     */
    synchronized void shiftBy(double angleRad, int shiftMin, int shiftMax) {
        x += ThreadLocalRandom.current().nextDouble(shiftMin, shiftMax) * Math.cos(angleRad);
        y += ThreadLocalRandom.current().nextDouble(shiftMin, shiftMax) * Math.sin(angleRad);
        positionBook.add(new Position((System.currentTimeMillis() - startTime) / 1000.0, x, y));
    }

    /**
     * Get information about current truck's location
     * @return information provided via {@code String} object
     */
    synchronized String getInfo() {
        return String.format("Truck's position: x = %.2f, y = %.2f", x, y);
    }
}

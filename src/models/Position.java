package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Class of the 2d-position at the concrete time
 */
public class Position {
    /**
     * Time of assigning
     */
    private DoubleProperty time;
    /**
     * x-coordinate
     */
    private DoubleProperty x;
    /**
     * y-coordinate
     */
    private DoubleProperty y;

    Position(double time, double x, double y) {
        this.time = new SimpleDoubleProperty(time);
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
    }

    public double getTime() {
        return time.get();
    }

    public DoubleProperty timeProperty() {
        return time;
    }

    public double getX() {
        return x.get();
    }

    public DoubleProperty xProperty() {
        return x;
    }

    public double getY() {
        return y.get();
    }

    public DoubleProperty yProperty() {
        return y;
    }
}

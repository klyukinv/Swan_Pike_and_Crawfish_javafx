package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Implementation of the observable PositionBook
 */
public class CollectionPositionBook implements PositionBook{

    /**
     * List of the Position objects
     */
    private ObservableList<Position> posList = FXCollections.observableArrayList();

    @Override
    public synchronized void add(Position positionObj) { posList.add(positionObj); }

    @Override
    public void clear() { posList.clear(); }

    /**
     * Getter for position list
     * @return position list
     */
    public synchronized ObservableList<Position> getPositionList() {
        return posList;
    }
}

package fxapp;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.CollectionPositionBook;
import models.*;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


public class Controller {

    /**
     * Instance of PositionBook implementation
     */
    private CollectionPositionBook positionBook;

    /**
     * Reference to Truck object
     */
    private Truck truck;
    /**
     * Thread pool
     */
    private Animal[] threads;

    private Double xZero;
    private Double yZero;

    @FXML
    private Button startBtn;
    @FXML
    private Button stopBtn;
    @FXML
    private Button closeBtn;

    @FXML
    private Canvas canvas;
    private GraphicsContext gc;

    @FXML
    private TextField swanAngle;
    @FXML
    private TextField swanShiftMin;
    @FXML
    private TextField swanSleepMin;
    @FXML
    private TextField swanShiftMax;
    @FXML
    private TextField swanSleepMax;

    @FXML
    private TextField pikeAngle;
    @FXML
    private TextField pikeShiftMin;
    @FXML
    private TextField pikeSleepMin;
    @FXML
    private TextField pikeShiftMax;
    @FXML
    private TextField pikeSleepMax;

    @FXML
    private TextField cfAngle;
    @FXML
    private TextField cfShiftMin;
    @FXML
    private TextField cfSleepMin;
    @FXML
    private TextField cfShiftMax;
    @FXML
    private TextField cfSleepMax;

    @FXML
    private TextField duration;
    @FXML
    private TextField startY;
    @FXML
    private TextField startX;

    private Timer timer;

    @FXML
    private TableView truckPositionData;
    @FXML
    private TableColumn<Position, String> columnTime;
    @FXML
    private TableColumn<Position, String> columnX;
    @FXML
    private TableColumn<Position, String> columnY;
    @FXML
    private AnchorPane canvasPane;

    @FXML
    private void initialize() {
        columnTime.setCellValueFactory(cellData ->
                Bindings.format("%.2f", cellData.getValue().timeProperty()));
        columnX.setCellValueFactory(cellData ->
                Bindings.format("%.2f", cellData.getValue().xProperty()));
        columnY.setCellValueFactory(cellData ->
                Bindings.format("%.2f", cellData.getValue().yProperty()));

        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(2);
        gc.setLineDashes(5);

        positionBook = new CollectionPositionBook();
        positionBook.getPositionList().addListener((ListChangeListener<Position>) c -> {
            gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            xZero = canvas.getWidth() / 2;
            yZero = canvas.getHeight() / 2;
            int size = positionBook.getPositionList().size();
            for (int i = 1; i < size; i++) {
                Position p0 = positionBook.getPositionList().get(i - 1);
                Position p1 = positionBook.getPositionList().get(i);
                gc.strokeLine(p0.getX() + xZero, -p0.getY() + yZero,
                        p1.getX() + xZero, -p1.getY() + yZero);
            }
            if (size > 0) {
                Position lastPoint = positionBook.getPositionList().get(size - 1);
                drawTruckAndAnimals(lastPoint, xZero, yZero);
            }
        });
        truckPositionData.setItems(positionBook.getPositionList());
        stopBtn.setDisable(true);
        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());
    }

    /**
     * Start drawing
     * @param actionEvent ActionEvent instance
     */
    public void startDraw(ActionEvent actionEvent) {
        gc = canvas.getGraphicsContext2D();
        setTruckParams();
        fillThreadPool();
        if (setAnimalParams()) {
            startBtn.setDisable(true);
            stopBtn.setDisable(false);
            for (Thread thread : threads) {
                thread.start();
            }
            setTimer();
        } else gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Extract truck's data from fields
     */
    private void setTruckParams() {
        threads = new Animal[3];
        double x = 0, y = 0;
        if (!startX.getText().equals(""))
            x = Double.valueOf(startX.getText());
        if (!startY.getText().equals(""))
            y = Double.valueOf(startY.getText());
        truck = new Truck(x, y, positionBook);
    }

    /**
     * Fill thread pool with animals
     */
    private void fillThreadPool() {
        threads[0] = new Animal("swan", truck);
        threads[1] = new Animal("pike", truck);
        threads[2] = new Animal("crawfish", truck);
    }

    /**
     * Extract animals' data from field
     * @return success or failure
     */
    private boolean setAnimalParams() {
        try {
            // Angles
            if (!swanAngle.getText().equals(""))
                threads[0].setAngle(Double.valueOf(swanAngle.getText()));
            else {
                showAlertWithHeaderText("angle of swan's movement");
                return false;
            }
            if (!pikeAngle.getText().equals(""))
                threads[1].setAngle(Double.valueOf(pikeAngle.getText()));
            else {
                showAlertWithHeaderText("angle of pike's movement");
                return false;
            }
            if (!cfAngle.getText().equals(""))
                threads[2].setAngle(Double.valueOf(cfAngle.getText()));
            else {
                showAlertWithHeaderText("angle of crawfish's movement");
                return false;
            }
            // Sleep minimum
            if (!swanSleepMin.getText().equals(""))
                threads[0].setSleepMin(Integer.valueOf(swanSleepMin.getText()));
            else {
                showAlertWithHeaderText("min sleep of the swan");
                return false;
            }
            if (!pikeSleepMin.getText().equals(""))
                threads[1].setSleepMin(Integer.valueOf(pikeSleepMin.getText()));
            else {
                showAlertWithHeaderText("min sleep of the pike");
                return false;
            }
            if (!cfSleepMin.getText().equals(""))
                threads[2].setSleepMin(Integer.valueOf(cfSleepMin.getText()));
            else {
                showAlertWithHeaderText("max sleep of the swan");
                return false;
            }
            // Sleep maximum
            if (!swanSleepMax.getText().equals(""))
                threads[0].setSleepMax(Integer.valueOf(swanSleepMax.getText()));
            else {
                showAlertWithHeaderText("max sleep of the swan");
                return false;
            }
            if (!pikeSleepMax.getText().equals(""))
                threads[1].setSleepMax(Integer.valueOf(pikeSleepMax.getText()));
            else {
                showAlertWithHeaderText("max sleep of the pike");
                return false;
            }
            if (!cfSleepMax.getText().equals(""))
                threads[2].setSleepMax(Integer.valueOf(cfSleepMax.getText()));
            else {
                showAlertWithHeaderText("min sleep of the crawfish");
                return false;
            }
            // Shift minimum
            if (!swanShiftMin.getText().equals(""))
                threads[0].setShiftMin(Integer.valueOf(swanShiftMin.getText()));
            else {
                showAlertWithHeaderText("min shift of the swan");
                return false;
            }
            if (!pikeShiftMin.getText().equals(""))
                threads[1].setShiftMin(Integer.valueOf(pikeShiftMin.getText()));
            else {
                showAlertWithHeaderText("min shift of the pike");
                return false;
            }
            if (!cfShiftMin.getText().equals(""))
                threads[2].setShiftMin(Integer.valueOf(cfShiftMin.getText()));
            else {
                showAlertWithHeaderText("min shift of the crawfish");
                return false;
            }
            // Shift maximum
            if (!swanShiftMax.getText().equals(""))
                threads[0].setShiftMax(Integer.valueOf(swanShiftMax.getText()));
            else {
                showAlertWithHeaderText("max shift of the swan");
                return false;
            }
            if (!pikeShiftMax.getText().equals(""))
                threads[1].setShiftMax(Integer.valueOf(pikeShiftMax.getText()));
            else {
                showAlertWithHeaderText("max shift of the pike");
                return false;
            }
            if (!cfShiftMax.getText().equals(""))
                threads[2].setShiftMax(Integer.valueOf(cfShiftMax.getText()));
            else {
                showAlertWithHeaderText("max shift of the crawfish");
                return false;
            }
        } catch (NumberFormatException e) {
            showExceptionAlert(e.getMessage());
            return false;
        }
        return validate();
    }

    /**
     * Stops drawing process
     * @param actionEvent ActionEvent instance
     */
    public void stopDraw(ActionEvent actionEvent) {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        stopBtn.setDisable(true);
        timer.cancel();
    }

    /**
     * Reset canvas and truck's data
     * @param actionEvent ActionEvent instance
     */
    public void resetDraw(ActionEvent actionEvent) {
        gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        positionBook.clear();
        startBtn.setDisable(false);
        for (Thread thread : threads) {
            thread.interrupt();
        }
        stopBtn.setDisable(true);
    }

    /**
     * Draw truck and animals
     * @param lastPos last position of the truck
     * @param xZero 'zero' x of the canvas
     * @param yZero 'zero' y of the canvas
     */
    private void drawTruckAndAnimals(Position lastPos, double xZero, double yZero) {
        double shiftSwan = Double.valueOf(swanShiftMax.getText()) - Double.valueOf(swanShiftMin.getText());
        double shiftPike = Double.valueOf(pikeShiftMax.getText()) - Double.valueOf(pikeShiftMin.getText());
        double shiftCf = Double.valueOf(cfShiftMax.getText()) - Double.valueOf(cfShiftMin.getText());
        double truckScale = (shiftCf + shiftPike + shiftSwan) / 3;
        double[] angles = new double[3];
        Color[] colors = {Color.CADETBLUE, Color.SANDYBROWN, Color.DARKSEAGREEN};
        angles[0] = Math.toRadians(Double.valueOf(swanAngle.getText()));
        angles[1] = Math.toRadians(Double.valueOf(pikeAngle.getText()));
        angles[2] = Math.toRadians(Double.valueOf(cfAngle.getText()));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        String sep = File.separator;
        for (int i = 0; i < 3; i++) {
            drawArrow(lastPos.getX() + Math.cos(angles[i]) * truckScale + xZero,
                    -lastPos.getY() - Math.sin(angles[i]) * truckScale + yZero,
                    lastPos.getX() + 2 * Math.cos(angles[i]) * truckScale + xZero,
                    -lastPos.getY() - 2 * Math.sin(angles[i]) * truckScale + yZero, colors[i]);
            gc.setStroke(colors[i]);
            gc.strokeOval(lastPos.getX() + Math.cos(angles[i]) * truckScale + xZero - truckScale / 4,
                    -lastPos.getY() - Math.sin(angles[i]) * truckScale + yZero - truckScale / 4,
                    truckScale / 2, truckScale / 2);
        }
        gc.setFill(Color.DARKRED);
        gc.fillRoundRect(lastPos.getX() + xZero - truckScale / 4, -lastPos.getY() + yZero - truckScale / 4,
                truckScale / 2, truckScale / 2, 10, 10);
        drawArrow(lastPos.getX() + xZero, -lastPos.getY() + yZero, lastPos.getX() + truckScale + xZero,
                -lastPos.getY() + yZero, Color.BLACK);
    }

    /**
     * Set threads' actions timer
     */
    private void setTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                for (Thread thread : threads) {
                    thread.interrupt();
                }
                stopBtn.setDisable(true);
            }

        };
        timer.schedule(task, Long.valueOf(duration.getText()) * 1000);
    }

    /**
     * Draws an arrow at fixed coordinates
     * @param x1 x of the beginning
     * @param y1 y of the beginning
     * @param x2 x of the end
     * @param y2 y of the end
     * @param color color of the arrow
     */
    private void drawArrow(double x1, double y1, double x2, double y2, Color color) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(color);
        gc.setLineWidth(1);
        gc.setLineDashes(0);

        gc.strokeLine(x1, y1, x2, y2);

        double arrowHeadSize = 3;

        //ArrowHead
        double angleArr = Math.atan2((y2 - y1), (x2 - x1)) - Math.PI / 2.0;
        double sin = Math.sin(angleArr);
        double cos = Math.cos(angleArr);
        //point1
        double x3 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + x2;
        double y3 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + y2;
        //point2
        double x4 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + x2;
        double y4 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + y2;

        gc.strokeLine(x2, y2, x3, y3);
        gc.strokeLine(x3, y3, x4, y4);
        gc.strokeLine(x4, y4, x2, y2);

        gc.setStroke(Color.RED);
        gc.setLineWidth(2);
        gc.setLineDashes(5);
    }

    /**
     * Show an error alert with notification
     * @param name name of the field
     */
    private void showAlertWithHeaderText(String name) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong field value");
        alert.setHeaderText("Results:");
        alert.setContentText("Please set the" + name);

        alert.showAndWait();
    }

    /**
     * Show an exception alert
     * @param text of the message
     */
    private void showExceptionAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Results:");
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Validate bounds of animals' shift and sleep
     * @return success or not
     */
    private boolean validate() {
        for (Animal thread : threads) {
            if (thread.getShiftMax() < thread.getShiftMin()) {
                showExceptionAlert(thread.getAnimalName() + "'s shift bounds are illegal");
                return false;
            } else if (thread.getSleepMax() < thread.getSleepMin()) {
                showExceptionAlert(thread.getAnimalName() + "'s sleep bounds are illegal");
                return false;
            }
        }
        return true;
    }

    /**
     * OnAction method of close button
     * @param actionEvent ActionEvent instance
     */
    public void closeButtonAction(ActionEvent actionEvent) {
        // get a handle to the stage
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
    }

    /**
     * OnAction method of about button
     * @param actionEvent ActionEvent instance
     */
    public void aboutButtonAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Program info");
        alert.setContentText("Name: Swan, Pike and Crawfish Assignment (GUI)\nAuthor: Klyukin Valeriy\n" +
                "Group: BSE173(1)");

        alert.showAndWait();
    }
}

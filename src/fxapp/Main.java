package fxapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Animal;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private static Map<String, String> commonParams = new HashMap<String, String>() {
        {
            put("duration", "25");
            put("startX", "0");
            put("startY", "0");
            put("swanAngle", "60");
            put("pikeAngle", "180");
            put("cfAngle", "300");
        }
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Swan, Pike & Crawfish");
        primaryStage.setMinHeight(540);
        primaryStage.setMinWidth(905);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        final String[] names = {"swan", "pike", "cf"};
        setSleepHints(scene, names);
        setDefautlValues(scene, names);
    }

    private static void setSleepHints(Scene scene, String[] names) {
        TextField sleepField;
        for (String name : names) {
            sleepField = (TextField) scene.lookup("#" + name + "Min");
            sleepField.setPromptText("min"); //to set the hint text as "min"
            sleepField = (TextField) scene.lookup("#" + name + "Max");
            sleepField.setPromptText("max"); //to set the hint text as "max"
            sleepField = (TextField) scene.lookup("#" + name + "ShMin");
            sleepField.setPromptText("min"); //to set the hint text as "min"
            sleepField = (TextField) scene.lookup("#" + name + "ShMax");
            sleepField.setPromptText("max"); //to set the hint text as "max"
            if (name.equals("cf"))
                sleepField.getParent().requestFocus();
        }
    }

    private static void setDefautlValues(Scene scene, String[] names) {
        for (String name : names) {
            Animal.defaultParams.forEach((k, v) -> {
                TextField paramField = (TextField) scene.lookup("#" + name + k);
                paramField.setText(v);
            });
        }
        commonParams.forEach((k, v) -> {
            TextField paramField = (TextField) scene.lookup("#" + k);
            paramField.setText(v);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}

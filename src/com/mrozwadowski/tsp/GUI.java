package com.mrozwadowski.tsp;/**
 * Created by rozwad on 13.01.17.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Ant colony");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        Controller controller = loader.getController();
        controller.setMainStage(primaryStage); // or what you want to do

        primaryStage.show();
    }
}

package com.dsavisualizer;

import com.dsavisualizer.controller.HomeController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        HomeController homeController = new HomeController();
        Scene homeScene = homeController.createHomeScene(stage);

        stage.setTitle("DSA Visualizer");
        stage.setScene(homeScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
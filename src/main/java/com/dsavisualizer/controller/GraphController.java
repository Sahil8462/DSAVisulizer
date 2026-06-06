package com.dsavisualizer.controller;

import java.awt.Desktop;
import java.net.URI;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GraphController {

    public Scene createGraphScene(Stage stage) {

        Label title = new Label("Graph Visualizer");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        Label message = new Label(
                "React Graph Visualizer browser me open ho raha hai.\n" +
                "Pehle graph-ui folder me npm run dev running hona chahiye."
        );
        message.setStyle("-fx-font-size: 16px; -fx-text-fill: #333;");

        Button openBrowserBtn = new Button("Open React Graph Visualizer");
        openBrowserBtn.setStyle(
                "-fx-background-color: #2b2d42;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 10 18 10 18;"
        );

        openBrowserBtn.setOnAction(e -> openReactGraph());

        Button backBtn = new Button("Back");
        backBtn.setStyle(
                "-fx-background-color: #2b2d42;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;" +
                "-fx-padding: 8 16 8 16;"
        );

        backBtn.setOnAction(e -> {
            HomeController homeController = new HomeController();
            Scene homeScene = homeController.createHomeScene(stage);
            stage.setScene(homeScene);
        });

        VBox centerBox = new VBox(20);
        centerBox.setPadding(new Insets(40));
        centerBox.setStyle("-fx-background-color: #f8f9fa;");
        centerBox.getChildren().addAll(title, message, openBrowserBtn);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setTop(backBtn);
        root.setCenter(centerBox);

        openReactGraph();

        return new Scene(root, 1000, 650);
    }

    private void openReactGraph() {
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:5173/"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
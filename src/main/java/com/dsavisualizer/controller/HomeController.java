package com.dsavisualizer.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeController {

    public Scene createHomeScene(Stage stage) {

        Label title = new Label("DSA Visualizer Platform");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitle = new Label("Learn Data Structures and Algorithms with Visual Animation");
        subtitle.setStyle("-fx-font-size: 16px;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        Button sortingBtn = createMenuButton("Sorting Visualizer");
        Button searchingBtn = createMenuButton("Searching Visualizer");
        Button stackBtn = createMenuButton("Stack Visualizer");
        Button queueBtn = createMenuButton("Queue Visualizer");
        Button linkedListBtn = createMenuButton("Linked List Visualizer");
        Button treeBtn = createMenuButton("Tree Visualizer");
        Button graphBtn = createMenuButton("Graph Visualizer");
        Button recursionBtn = createMenuButton("Recursion Visualizer");

        sortingBtn.setOnAction(e -> openSorting(stage));
        searchingBtn.setOnAction(e -> openSearching());
        stackBtn.setOnAction(e -> openStack());
        queueBtn.setOnAction(e -> openQueue());
        linkedListBtn.setOnAction(e -> openLinkedList());
        treeBtn.setOnAction(e -> openTree());
        graphBtn.setOnAction(e -> openGraph());
        recursionBtn.setOnAction(e -> openRecursion());

        grid.add(sortingBtn, 0, 0);
        grid.add(searchingBtn, 1, 0);
        grid.add(stackBtn, 2, 0);

        grid.add(queueBtn, 0, 1);
        grid.add(linkedListBtn, 1, 1);
        grid.add(treeBtn, 2, 1);

        grid.add(graphBtn, 0, 2);
        grid.add(recursionBtn, 1, 2);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(title, subtitle, grid);

        return new Scene(root, 1000, 650);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefSize(220, 90);
        button.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 15;" +
                "-fx-border-radius: 15;" +
                "-fx-background-color: #2b2d42;" +
                "-fx-text-fill: white;" +
                "-fx-cursor: hand;"
        );
        return button;
    }

  private void openSorting(Stage stage) {
    SortingController sortingController = new SortingController();
    stage.setScene(sortingController.createSortingScene(stage));
}

    private void openSearching() {
        System.out.println("Searching Visualizer Opened");
    }

    private void openStack() {
        System.out.println("Stack Visualizer Opened");
    }

    private void openQueue() {
        System.out.println("Queue Visualizer Opened");
    }

    private void openLinkedList() {
        System.out.println("Linked List Visualizer Opened");
    }

    private void openTree() {
        System.out.println("Tree Visualizer Opened");
    }

    private void openGraph() {
        System.out.println("Graph Visualizer Opened");
    }

    private void openRecursion() {
        System.out.println("Recursion Visualizer Opened");
    }
}
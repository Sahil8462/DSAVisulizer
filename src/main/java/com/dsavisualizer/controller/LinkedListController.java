package com.dsavisualizer.controller;

import java.util.ArrayList;
import java.util.List;

import com.dsavisualizer.algorithm.linkedlist.LinkedListOperation;
import com.dsavisualizer.model.LinkedListStep;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LinkedListController {

    private HBox linkedListBox;

    private Label messageLabel;
    private Label operationLabel;
    private Label pointerLabel;

    private TextField arrayInputField;
    private TextField searchValueField;
    private TextField insertValueField;
    private TextField positionInputField;

    private ComboBox<String> listTypeComboBox;
    private ComboBox<String> operationComboBox;

    private List<LinkedListStep> steps;
    private int currentStepIndex;

    private Timeline timeline;

    public Scene createLinkedListScene(Stage stage) {

        Label title = new Label("Linked List Visualizer");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        Label subtitle = new Label("Singly: [data | next]     Doubly: [prev | data | next]");
        subtitle.setStyle("-fx-font-size: 15px;");

        Label conceptLabel = new Label(
                "Search uses Search Target field. Insert uses Insert Value and Position field."
        );
        conceptLabel.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #444444;"
        );

        arrayInputField = new TextField();
        arrayInputField.setPromptText("Example: 4,5,6,8");
        arrayInputField.setPrefWidth(240);

        searchValueField = new TextField();
        searchValueField.setPromptText("Search Target");
        searchValueField.setPrefWidth(110);

        insertValueField = new TextField();
        insertValueField.setPromptText("Insert Value");
        insertValueField.setPrefWidth(110);

        positionInputField = new TextField();
        positionInputField.setPromptText("Position");
        positionInputField.setPrefWidth(90);

        listTypeComboBox = new ComboBox<>();
        listTypeComboBox.getItems().addAll("Singly Linked List", "Doubly Linked List");
        listTypeComboBox.setValue("Singly Linked List");
        listTypeComboBox.setPrefWidth(160);

        operationComboBox = new ComboBox<>();
        operationComboBox.getItems().addAll(
                "Traversal",
                "Search",
                "Insert At Beginning",
                "Insert At End",
                "Insert At Position"
        );
        operationComboBox.setValue("Traversal");
        operationComboBox.setPrefWidth(180);

        Button startBtn = new Button("Start");
        Button nextBtn = new Button("Next Step");
        Button resetBtn = new Button("Reset");
        Button backBtn = new Button("Back");

        startBtn.setOnAction(e -> startVisualization());
        nextBtn.setOnAction(e -> showNextStep());
        resetBtn.setOnAction(e -> resetVisualization());
        backBtn.setOnAction(e -> goBack(stage));

        styleButton(startBtn, "#2b9348");
        styleButton(nextBtn, "#4361ee");
        styleButton(resetBtn, "#ef233c");
        styleButton(backBtn, "#2b2d42");

        HBox firstInputRow = new HBox(12);
        firstInputRow.setAlignment(Pos.CENTER);
        firstInputRow.getChildren().addAll(
                new Label("Input Array:"),
                arrayInputField,
                new Label("List Type:"),
                listTypeComboBox,
                new Label("Operation:"),
                operationComboBox
        );

        HBox secondInputRow = new HBox(12);
        secondInputRow.setAlignment(Pos.CENTER);
        secondInputRow.getChildren().addAll(
                new Label("Search Target:"),
                searchValueField,
                new Label("Insert Value:"),
                insertValueField,
                new Label("Position:"),
                positionInputField,
                startBtn,
                nextBtn,
                resetBtn,
                backBtn
        );

        VBox topBox = new VBox(12);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(18));
        topBox.getChildren().addAll(title, subtitle, conceptLabel, firstInputRow, secondInputRow);

        linkedListBox = new HBox(15);
        linkedListBox.setAlignment(Pos.CENTER_LEFT);
        linkedListBox.setPadding(new Insets(45));

        ScrollPane scrollPane = new ScrollPane(linkedListBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefHeight(420);
        scrollPane.setStyle("-fx-background-color: white; -fx-border-color: #dddddd;");

        operationLabel = new Label("Operation: None");
        operationLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        messageLabel = new Label("Enter array values and click Start");
        messageLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #333333;");

        pointerLabel = new Label("Pointer movement will appear here");
        pointerLabel.setWrapText(true);
        pointerLabel.setMaxWidth(1150);
        pointerLabel.setAlignment(Pos.CENTER);
        pointerLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;"
        );

        VBox bottomBox = new VBox(8);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(14));
        bottomBox.getChildren().addAll(operationLabel, messageLabel, pointerLabel);

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(scrollPane);
        root.setBottom(bottomBox);
        root.setStyle("-fx-background-color: #f8f9fa;");

        steps = new ArrayList<>();
        currentStepIndex = 0;

        return new Scene(root, 1300, 760);
    }

    private void startVisualization() {

        try {
            int[] inputArray = parseInputArray(arrayInputField.getText());

            LinkedListOperation linkedListOperation = new LinkedListOperation();

            String selectedOperation = operationComboBox.getValue();
            boolean isDoubly = listTypeComboBox.getValue().equals("Doubly Linked List");

            if (selectedOperation.equals("Traversal")) {

                steps = linkedListOperation.generateTraversalSteps(inputArray, isDoubly);
            }
            else if (selectedOperation.equals("Search")) {

                if (searchValueField.getText().trim().isEmpty()) {
                    operationLabel.setText("Operation: Input Required");
                    messageLabel.setText("Search ke liye Search Target field me value do");
                    pointerLabel.setText("Example: Input Array = 4,5,6,8 and Search Target = 6");
                    return;
                }

                int target = Integer.parseInt(searchValueField.getText().trim());
                steps = linkedListOperation.generateSearchSteps(inputArray, target, isDoubly);
            }
            else if (selectedOperation.equals("Insert At Beginning")) {

                if (insertValueField.getText().trim().isEmpty()) {
                    operationLabel.setText("Operation: Input Required");
                    messageLabel.setText("Insert ke liye Insert Value field me value do");
                    pointerLabel.setText("Example: Insert Value = 9");
                    return;
                }

                int value = Integer.parseInt(insertValueField.getText().trim());
                steps = linkedListOperation.generateInsertAtBeginningSteps(inputArray, value, isDoubly);
            }
            else if (selectedOperation.equals("Insert At End")) {

                if (insertValueField.getText().trim().isEmpty()) {
                    operationLabel.setText("Operation: Input Required");
                    messageLabel.setText("Insert ke liye Insert Value field me value do");
                    pointerLabel.setText("Example: Insert Value = 9");
                    return;
                }

                int value = Integer.parseInt(insertValueField.getText().trim());
                steps = linkedListOperation.generateInsertAtEndSteps(inputArray, value, isDoubly);
            }
            else if (selectedOperation.equals("Insert At Position")) {

                if (insertValueField.getText().trim().isEmpty()
                        || positionInputField.getText().trim().isEmpty()) {

                    operationLabel.setText("Operation: Input Required");
                    messageLabel.setText("Insert At Position ke liye Insert Value aur Position dono do");
                    pointerLabel.setText("Example: Insert Value = 9 and Position = 2");
                    return;
                }

                int value = Integer.parseInt(insertValueField.getText().trim());
                int position = Integer.parseInt(positionInputField.getText().trim());

                steps = linkedListOperation.generateInsertAtPositionSteps(inputArray, value, position, isDoubly);
            }

            currentStepIndex = 0;
            showCurrentStep();

            if (timeline != null) {
                timeline.stop();
            }

            timeline = new Timeline(new KeyFrame(Duration.seconds(1.4), e -> showNextStep()));
            timeline.setCycleCount(steps.size() - 1);
            timeline.play();

        }
        catch (Exception e) {
            operationLabel.setText("Operation: Error");
            messageLabel.setText("Invalid input. Array example: 4,5,6,8");
            pointerLabel.setText("Search Target, Insert Value, Position should be valid numbers.");
        }
    }

    private void showNextStep() {

        if (steps == null || steps.isEmpty()) {
            return;
        }

        if (currentStepIndex < steps.size() - 1) {
            currentStepIndex++;
            showCurrentStep();
        }
    }

    private void showCurrentStep() {

        LinkedListStep step = steps.get(currentStepIndex);

        drawLinkedList(step);

        operationLabel.setText("Operation: " + step.getOperation());
        messageLabel.setText(step.getMessage());

        if (step.getPointerMessage() != null && !step.getPointerMessage().isEmpty()) {
            pointerLabel.setText(step.getPointerMessage());
        }
        else {
            pointerLabel.setText(generatePointerExplanation(step));
        }
    }

    private void drawLinkedList(LinkedListStep step) {

        linkedListBox.getChildren().clear();

        int[] values = step.getNodeValues();

        Label headLabel = new Label("HEAD");
        headLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #d90429;");
        linkedListBox.getChildren().add(headLabel);

        if (values.length == 0) {
            Label arrow = new Label(" → ");
            arrow.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

            Label nullLabel = new Label("NULL");
            nullLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ef233c;");

            linkedListBox.getChildren().addAll(arrow, nullLabel);
            return;
        }

        Label headArrow = new Label(" → ");
        headArrow.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        linkedListBox.getChildren().add(headArrow);

        for (int i = 0; i < values.length; i++) {

            VBox nodeWrapper = createNode(values[i], i, step);
            linkedListBox.getChildren().add(nodeWrapper);

            if (i != values.length - 1) {
                Label arrowLabel = createArrow(i, step);
                linkedListBox.getChildren().add(arrowLabel);
            }
        }

        Label nullArrow = new Label(" → ");
        nullArrow.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label nullLabel = new Label("NULL");
        nullLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ef233c;");

        linkedListBox.getChildren().addAll(nullArrow, nullLabel);
    }

    private VBox createNode(int value, int index, LinkedListStep step) {

        Label pointerLabel = new Label("");

        if (index == step.getCurrentIndex()) {
            pointerLabel.setText("CURRENT");
        }
        else if (index == step.getHeadIndex() && index == step.getTailIndex()) {
            pointerLabel.setText("HEAD / TAIL");
        }
        else if (index == step.getHeadIndex()) {
            pointerLabel.setText("FIRST");
        }
        else if (index == step.getTailIndex()) {
            pointerLabel.setText("TAIL");
        }

        pointerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #d90429;");

        HBox nodeBox;

        if (step.isDoublyLinkedList()) {
            nodeBox = createDoublyNode(value, index == step.getCurrentIndex());
        }
        else {
            nodeBox = createSinglyNode(value, index == step.getCurrentIndex());
        }

        Label addressLabel = new Label("Node " + index);
        addressLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555555;");

        VBox nodeWrapper = new VBox(5);
        nodeWrapper.setAlignment(Pos.CENTER);
        nodeWrapper.getChildren().addAll(pointerLabel, nodeBox, addressLabel);

        return nodeWrapper;
    }

    private HBox createSinglyNode(int value, boolean active) {

        Label dataPart = new Label(String.valueOf(value));
        dataPart.setAlignment(Pos.CENTER);
        dataPart.setPrefSize(65, 55);

        Label nextPart = new Label("next");
        nextPart.setAlignment(Pos.CENTER);
        nextPart.setPrefSize(60, 55);

        String dataBg = active ? "#ffd166" : "#ffffff";
        String nextBg = active ? "#ffe8a3" : "#e9ecef";
        String border = active ? "#f77f00" : "#2b2d42";

        dataPart.setStyle(getNodePartStyle(dataBg, border, "10 0 0 10", "3 1.5 3 3", 20));
        nextPart.setStyle(getNodePartStyle(nextBg, border, "0 10 10 0", "3 3 3 1.5", 13));

        HBox nodeBox = new HBox(0);
        nodeBox.setAlignment(Pos.CENTER);
        nodeBox.getChildren().addAll(dataPart, nextPart);

        return nodeBox;
    }

    private HBox createDoublyNode(int value, boolean active) {

        Label prevPart = new Label("prev");
        prevPart.setAlignment(Pos.CENTER);
        prevPart.setPrefSize(55, 55);

        Label dataPart = new Label(String.valueOf(value));
        dataPart.setAlignment(Pos.CENTER);
        dataPart.setPrefSize(65, 55);

        Label nextPart = new Label("next");
        nextPart.setAlignment(Pos.CENTER);
        nextPart.setPrefSize(55, 55);

        String pointerBg = active ? "#ffe8a3" : "#e9ecef";
        String dataBg = active ? "#ffd166" : "#ffffff";
        String border = active ? "#f77f00" : "#2b2d42";

        prevPart.setStyle(getNodePartStyle(pointerBg, border, "10 0 0 10", "3 1.5 3 3", 12));
        dataPart.setStyle(getNodePartStyle(dataBg, border, "0", "3 1.5 3 1.5", 20));
        nextPart.setStyle(getNodePartStyle(pointerBg, border, "0 10 10 0", "3 3 3 1.5", 12));

        HBox nodeBox = new HBox(0);
        nodeBox.setAlignment(Pos.CENTER);
        nodeBox.getChildren().addAll(prevPart, dataPart, nextPart);

        return nodeBox;
    }

    private String getNodePartStyle(
            String backgroundColor,
            String borderColor,
            String radius,
            String borderWidth,
            int fontSize
    ) {
        return "-fx-font-size: " + fontSize + "px;" +
                "-fx-font-weight: bold;" +
                "-fx-background-color: " + backgroundColor + ";" +
                "-fx-border-color: " + borderColor + ";" +
                "-fx-border-width: " + borderWidth + ";" +
                "-fx-border-radius: " + radius + ";" +
                "-fx-background-radius: " + radius + ";" +
                "-fx-text-fill: black;";
    }

    private Label createArrow(int arrowIndex, LinkedListStep step) {

        Label arrowLabel;

        if (step.isDoublyLinkedList()) {
            arrowLabel = new Label("prev ⇄ next");
        }
        else {
            arrowLabel = new Label("next →");
        }

        if (arrowIndex == step.getActiveArrowIndex()) {
            if (step.isDoublyLinkedList()) {
                arrowLabel.setText("LINK UPDATE ⇄");
            }
            else {
                arrowLabel.setText("NEXT UPDATE →");
            }

            arrowLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f77f00;");
        }
        else {
            arrowLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: black;");
        }

        return arrowLabel;
    }

    private String generatePointerExplanation(LinkedListStep step) {

        int[] values = step.getNodeValues();

        if (values == null || values.length == 0) {
            return "HEAD points to NULL";
        }

        StringBuilder explanation = new StringBuilder();

        if (step.isDoublyLinkedList()) {
            explanation.append("Doubly Links: ");

            for (int i = 0; i < values.length; i++) {
                String prev = i == 0 ? "NULL" : String.valueOf(values[i - 1]);
                String next = i == values.length - 1 ? "NULL" : String.valueOf(values[i + 1]);

                explanation.append(values[i])
                        .append(".prev → ")
                        .append(prev)
                        .append(", ")
                        .append(values[i])
                        .append(".next → ")
                        .append(next);

                if (i != values.length - 1) {
                    explanation.append("  |  ");
                }
            }
        }
        else {
            explanation.append("Singly Links: ");

            for (int i = 0; i < values.length; i++) {

                if (i == values.length - 1) {
                    explanation.append(values[i]).append(".next → NULL");
                }
                else {
                    explanation.append(values[i]).append(".next → ").append(values[i + 1]);
                }

                if (i != values.length - 1) {
                    explanation.append("  |  ");
                }
            }
        }

        return explanation.toString();
    }

    private int[] parseInputArray(String input) {

        if (input == null || input.trim().isEmpty()) {
            return new int[]{};
        }

        String[] parts = input.split(",");
        int[] array = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            array[i] = Integer.parseInt(parts[i].trim());
        }

        return array;
    }

    private void resetVisualization() {

        if (timeline != null) {
            timeline.stop();
        }

        linkedListBox.getChildren().clear();

        messageLabel.setText("Enter array values and click Start");
        operationLabel.setText("Operation: None");
        pointerLabel.setText("Pointer movement will appear here");

        currentStepIndex = 0;
    }

    private void goBack(Stage stage) {

        if (timeline != null) {
            timeline.stop();
        }

        HomeController homeController = new HomeController();
        Scene homeScene = homeController.createHomeScene(stage);
        stage.setScene(homeScene);
    }

    private void styleButton(Button button, String color) {

        button.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        button.setPrefHeight(35);
    }
}
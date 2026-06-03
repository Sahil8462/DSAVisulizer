package com.dsavisualizer.controller;

import java.util.List;

import com.dsavisualizer.algorithm.stackqueue.QueueOperation;
import com.dsavisualizer.model.QueueStep;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class QueueController {

    private TextField valueInputField;

    private TextArea algorithmCodeArea;

    private HBox queueBox;

    private Label messageLabel;
    private Label operationLabel;
    private Label frontLabel;
    private Label rearLabel;
    private Label sizeLabel;

    private Button enqueueButton;
    private Button dequeueButton;
    private Button peekButton;
    private Button resetButton;
    private Button backButton;

    private final int MAX_QUEUE_SIZE = 7;

    private QueueOperation queueOperation;

    public Scene createQueueScene(Stage stage) {

        queueOperation = new QueueOperation(MAX_QUEUE_SIZE);

        Label titleLabel = new Label("Queue Visualizer");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1d3557;");

        Label logicTitle = new Label("Queue Algorithm Logic");
        logicTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");

        algorithmCodeArea = new TextArea();
        algorithmCodeArea.setEditable(false);
        algorithmCodeArea.setWrapText(false);
        algorithmCodeArea.setPrefWidth(360);
        algorithmCodeArea.setPrefHeight(540);
        algorithmCodeArea.setStyle(
                "-fx-font-family: 'Consolas';" +
                        "-fx-font-size: 13px;" +
                        "-fx-control-inner-background: white;" +
                        "-fx-border-color: #90caf9;" +
                        "-fx-border-width: 2;"
        );

        algorithmCodeArea.setText(getQueueLogicCode());

        VBox leftPanel = new VBox(12);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(25, 10, 10, 25));
        leftPanel.getChildren().addAll(logicTitle, algorithmCodeArea);

        Label inputLabel = new Label("Enter value:");
        inputLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        valueInputField = new TextField();
        valueInputField.setPromptText("Example: 10");
        valueInputField.setPrefWidth(180);

        HBox inputBox = new HBox(12);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(inputLabel, valueInputField);

        enqueueButton = createButton("Enqueue");
        dequeueButton = createButton("Dequeue");
        peekButton = createButton("Peek");
        resetButton = createButton("Reset");
        backButton = createButton("Back");

        enqueueButton.setOnAction(e -> enqueueValue());
        dequeueButton.setOnAction(e -> dequeueValue());
        peekButton.setOnAction(e -> peekValue());
        resetButton.setOnAction(e -> resetQueue());
        backButton.setOnAction(e -> goBackToHome(stage));

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                enqueueButton,
                dequeueButton,
                peekButton,
                resetButton,
                backButton
        );

        queueBox = new HBox(10);
        queueBox.setAlignment(Pos.CENTER);
        queueBox.setPadding(new Insets(25));
        queueBox.setMinHeight(260);
        queueBox.setPrefWidth(780);
        queueBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d9d9d9;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;"
        );

        operationLabel = createInfoLabel("Operation: -");
        frontLabel = createInfoLabel("Front: -");
        rearLabel = createInfoLabel("Rear: -");
        sizeLabel = createInfoLabel("Size: 0 / " + MAX_QUEUE_SIZE);

        HBox infoBox = new HBox(15);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getChildren().addAll(operationLabel, frontLabel, rearLabel, sizeLabel);

        messageLabel = new Label("Queue is empty. Enter value and click Enqueue.");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(760);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #222222;" +
                        "-fx-background-color: #f1faee;" +
                        "-fx-padding: 14;" +
                        "-fx-background-radius: 12;"
        );

        VBox centerPanel = new VBox(18);
        centerPanel.setAlignment(Pos.TOP_CENTER);
        centerPanel.setPadding(new Insets(25, 25, 25, 10));
        centerPanel.getChildren().addAll(
                titleLabel,
                inputBox,
                buttonBox,
                queueBox,
                infoBox,
                messageLabel
        );

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        root.setLeft(leftPanel);
        root.setCenter(centerPanel);

        showQueueStep(queueOperation.getInitialStep());

        return new Scene(root, 1300, 720);
    }

    private void enqueueValue() {

        try {
            int value = Integer.parseInt(valueInputField.getText().trim());

            QueueStep step = queueOperation.enqueue(value);
            showQueueStep(step);

            if (!"OVERFLOW".equals(step.getPhase())) {
                valueInputField.clear();
            }

        } catch (Exception e) {
            messageLabel.setText("Invalid input. Please enter a number.");
        }
    }

    private void dequeueValue() {

        QueueStep step = queueOperation.dequeue();
        showQueueStep(step);
    }

    private void peekValue() {

        QueueStep step = queueOperation.peek();
        showQueueStep(step);
    }

    private void resetQueue() {

        valueInputField.clear();

        QueueStep step = queueOperation.reset();
        showQueueStep(step);
    }

    private void showQueueStep(QueueStep step) {

        queueBox.getChildren().clear();

        List<Integer> currentQueue = step.getQueueState();

        if (currentQueue.isEmpty()) {
            Label emptyLabel = new Label("Queue Empty");
            emptyLabel.setStyle(
                    "-fx-font-size: 22px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #6c757d;"
            );

            queueBox.getChildren().add(emptyLabel);
        } else {

            Label frontArrow = new Label("FRONT →");
            frontArrow.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #d62828;"
            );

            queueBox.getChildren().add(frontArrow);

            for (int i = 0; i < currentQueue.size(); i++) {

                VBox elementBox = createQueueElement(currentQueue.get(i), i, step);
                queueBox.getChildren().add(elementBox);
            }

            Label rearArrow = new Label("← REAR");
            rearArrow.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #0077b6;"
            );

            queueBox.getChildren().add(rearArrow);
        }

        operationLabel.setText("Operation: " + step.getOperation());

        if (step.getFrontIndex() == -1) {
            frontLabel.setText("Front: -");
        } else {
            frontLabel.setText("Front Index: " + step.getFrontIndex());
        }

        if (step.getRearIndex() == -1) {
            rearLabel.setText("Rear: -");
        } else {
            rearLabel.setText("Rear Index: " + step.getRearIndex());
        }

        sizeLabel.setText("Size: " + currentQueue.size() + " / " + MAX_QUEUE_SIZE);
        messageLabel.setText(step.getMessage());
    }

    private VBox createQueueElement(int value, int index, QueueStep step) {

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setPrefSize(75, 65);
        valueLabel.setStyle(getElementStyle(index, step));

        Label indexLabel = new Label("Index " + index);
        indexLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #444444;");

        Label pointerLabel = new Label(" ");
        pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        if (index == step.getFrontIndex() && index == step.getRearIndex()) {
            pointerLabel.setText("F / R");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6a4c93;");
        } else if (index == step.getFrontIndex()) {
            pointerLabel.setText("Front");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #d62828;");
        } else if (index == step.getRearIndex()) {
            pointerLabel.setText("Rear");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #0077b6;");
        }

        VBox box = new VBox(7);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(valueLabel, indexLabel, pointerLabel);

        return box;
    }

    private String getElementStyle(int index, QueueStep step) {

        String baseStyle =
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-width: 2;" +
                        "-fx-alignment: center;";

        if ("ENQUEUED".equals(step.getPhase()) && index == step.getRearIndex()) {
            return baseStyle +
                    "-fx-background-color: #2a9d8f;" +
                    "-fx-border-color: #1b4332;" +
                    "-fx-text-fill: white;";
        }

        if ("PEEKED".equals(step.getPhase()) && index == step.getFrontIndex()) {
            return baseStyle +
                    "-fx-background-color: #ffd166;" +
                    "-fx-border-color: #f77f00;" +
                    "-fx-text-fill: black;";
        }

        if ("OVERFLOW".equals(step.getPhase())) {
            return baseStyle +
                    "-fx-background-color: #e63946;" +
                    "-fx-border-color: #9d0208;" +
                    "-fx-text-fill: white;";
        }

        if ("DEQUEUED".equals(step.getPhase()) && index == step.getFrontIndex()) {
            return baseStyle +
                    "-fx-background-color: #a8dadc;" +
                    "-fx-border-color: #457b9d;" +
                    "-fx-text-fill: #1d3557;";
        }

        return baseStyle +
                "-fx-background-color: #a8dadc;" +
                "-fx-border-color: #457b9d;" +
                "-fx-text-fill: #1d3557;";
    }

    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefSize(130, 42);
        button.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: #1d3557;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        return button;
    }

    private Label createInfoLabel(String text) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1d3557;" +
                        "-fx-background-color: white;" +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #d9d9d9;" +
                        "-fx-border-radius: 10;"
        );

        return label;
    }

    private String getQueueLogicCode() {

        return "Queue Logic:\n\n" +

                "Queue follows FIFO rule:\n" +
                "First In First Out\n\n" +

                "Enqueue Operation:\n\n" +
                "void enqueue(int value) {\n" +
                "    if (rear == maxSize - 1) {\n" +
                "        print(\"Queue Overflow\");\n" +
                "        return;\n" +
                "    }\n\n" +
                "    rear++;\n" +
                "    queue[rear] = value;\n" +
                "}\n\n" +

                "Dequeue Operation:\n\n" +
                "int dequeue() {\n" +
                "    if (front > rear) {\n" +
                "        print(\"Queue Underflow\");\n" +
                "        return -1;\n" +
                "    }\n\n" +
                "    int removedValue = queue[front];\n" +
                "    front++;\n" +
                "    return removedValue;\n" +
                "}\n\n" +

                "Peek Operation:\n\n" +
                "int peek() {\n" +
                "    if (front > rear) {\n" +
                "        print(\"Queue Empty\");\n" +
                "        return -1;\n" +
                "    }\n\n" +
                "    return queue[front];\n" +
                "}\n\n" +

                "Time Complexity:\n" +
                "Enqueue: O(1)\n" +
                "Dequeue: O(1)\n" +
                "Peek: O(1)";
    }

    private void goBackToHome(Stage stage) {

        HomeController homeController = new HomeController();
        Scene homeScene = homeController.createHomeScene(stage);
        stage.setScene(homeScene);
    }
}
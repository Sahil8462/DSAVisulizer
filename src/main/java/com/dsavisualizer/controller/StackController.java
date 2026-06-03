package com.dsavisualizer.controller;

import java.util.List;

import com.dsavisualizer.algorithm.stackqueue.StackOperation;
import com.dsavisualizer.model.StackStep;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class StackController {

    private TextField valueInputField;

    private TextArea algorithmCodeArea;

    private VBox stackBox;

    private Label messageLabel;
    private Label operationLabel;
    private Label topLabel;
    private Label sizeLabel;

    private Button pushButton;
    private Button popButton;
    private Button peekButton;
    private Button resetButton;
    private Button backButton;

    private final int MAX_STACK_SIZE = 6;

    private StackOperation stackOperation;

    public Scene createStackScene(Stage stage) {

        stackOperation = new StackOperation(MAX_STACK_SIZE);

        Label titleLabel = new Label("Stack Visualizer");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1d3557;");

        Label logicTitle = new Label("Stack Algorithm Logic");
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

        algorithmCodeArea.setText(getStackLogicCode());

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

        pushButton = createButton("Push");
        popButton = createButton("Pop");
        peekButton = createButton("Peek");
        resetButton = createButton("Reset");
        backButton = createButton("Back");

        pushButton.setOnAction(e -> pushValue());
        popButton.setOnAction(e -> popValue());
        peekButton.setOnAction(e -> peekValue());
        resetButton.setOnAction(e -> resetStack());
        backButton.setOnAction(e -> goBackToHome(stage));

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                pushButton,
                popButton,
                peekButton,
                resetButton,
                backButton
        );

        stackBox = new VBox(8);
        stackBox.setAlignment(Pos.BOTTOM_CENTER);
        stackBox.setPadding(new Insets(25));
        stackBox.setMinHeight(390);
        stackBox.setPrefWidth(420);
        stackBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d9d9d9;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;"
        );

        operationLabel = createInfoLabel("Operation: -");
        topLabel = createInfoLabel("Top: -");
        sizeLabel = createInfoLabel("Size: 0 / " + MAX_STACK_SIZE);

        HBox infoBox = new HBox(18);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getChildren().addAll(operationLabel, topLabel, sizeLabel);

        messageLabel = new Label("Stack is empty. Enter value and click Push.");
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
                stackBox,
                infoBox,
                messageLabel
        );

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        root.setLeft(leftPanel);
        root.setCenter(centerPanel);

        showStackStep(stackOperation.getInitialStep());

        return new Scene(root, 1300, 720);
    }

    private void pushValue() {

        try {
            int value = Integer.parseInt(valueInputField.getText().trim());

            StackStep step = stackOperation.push(value);
            showStackStep(step);

            if (!"OVERFLOW".equals(step.getPhase())) {
                valueInputField.clear();
            }

        } catch (Exception e) {
            messageLabel.setText("Invalid input. Please enter a number.");
        }
    }

    private void popValue() {

        StackStep step = stackOperation.pop();
        showStackStep(step);
    }

    private void peekValue() {

        StackStep step = stackOperation.peek();
        showStackStep(step);
    }

    private void resetStack() {

        valueInputField.clear();

        StackStep step = stackOperation.reset();
        showStackStep(step);
    }

    private void showStackStep(StackStep step) {

        stackBox.getChildren().clear();

        List<Integer> currentStack = step.getStackState();

        if (currentStack.isEmpty()) {
            Label emptyLabel = new Label("Stack Empty");
            emptyLabel.setStyle(
                    "-fx-font-size: 22px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #6c757d;"
            );

            stackBox.getChildren().add(emptyLabel);
        } else {

            for (int i = currentStack.size() - 1; i >= 0; i--) {

                VBox elementBox = createStackElement(currentStack.get(i), i, step);
                stackBox.getChildren().add(elementBox);
            }

            Label baseLabel = new Label("Stack Base");
            baseLabel.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #444444;"
            );

            stackBox.getChildren().add(baseLabel);
        }

        operationLabel.setText("Operation: " + step.getOperation());

        if (step.getTopIndex() == -1) {
            topLabel.setText("Top: -");
        } else {
            topLabel.setText("Top Index: " + step.getTopIndex());
        }

        sizeLabel.setText("Size: " + currentStack.size() + " / " + MAX_STACK_SIZE);
        messageLabel.setText(step.getMessage());
    }

    private VBox createStackElement(int value, int index, StackStep step) {

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setPrefSize(220, 48);
        valueLabel.setStyle(getElementStyle(index, step));

        Label pointerLabel = new Label(" ");

        if (index == step.getTopIndex()) {
            pointerLabel.setText("← TOP");
            pointerLabel.setStyle(
                    "-fx-font-size: 12px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #d62828;"
            );
        }

        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);
        row.getChildren().addAll(valueLabel, pointerLabel);

        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(row);

        return box;
    }

    private String getElementStyle(int index, StackStep step) {

        String baseStyle =
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-width: 2;" +
                        "-fx-alignment: center;";

        if ("PUSHED".equals(step.getPhase()) && index == step.getTopIndex()) {
            return baseStyle +
                    "-fx-background-color: #2a9d8f;" +
                    "-fx-border-color: #1b4332;" +
                    "-fx-text-fill: white;";
        }

        if ("PEEKED".equals(step.getPhase()) && index == step.getTopIndex()) {
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

        if ("POPPED".equals(step.getPhase())) {
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

    private String getStackLogicCode() {

        return "Stack Logic:\n\n" +

                "Stack follows LIFO rule:\n" +
                "Last In First Out\n\n" +

                "Push Operation:\n\n" +
                "void push(int value) {\n" +
                "    if (top == maxSize - 1) {\n" +
                "        print(\"Stack Overflow\");\n" +
                "        return;\n" +
                "    }\n\n" +
                "    top++;\n" +
                "    stack[top] = value;\n" +
                "}\n\n" +

                "Pop Operation:\n\n" +
                "int pop() {\n" +
                "    if (top == -1) {\n" +
                "        print(\"Stack Underflow\");\n" +
                "        return -1;\n" +
                "    }\n\n" +
                "    int removedValue = stack[top];\n" +
                "    top--;\n" +
                "    return removedValue;\n" +
                "}\n\n" +

                "Peek Operation:\n\n" +
                "int peek() {\n" +
                "    if (top == -1) {\n" +
                "        print(\"Stack Empty\");\n" +
                "        return -1;\n" +
                "    }\n\n" +
                "    return stack[top];\n" +
                "}\n\n" +

                "Time Complexity:\n" +
                "Push: O(1)\n" +
                "Pop: O(1)\n" +
                "Peek: O(1)";
    }

    private void goBackToHome(Stage stage) {

        HomeController homeController = new HomeController();
        Scene homeScene = homeController.createHomeScene(stage);
        stage.setScene(homeScene);
    }
}
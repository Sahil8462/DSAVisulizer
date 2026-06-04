package com.dsavisualizer.controller;

import java.util.List;

import com.dsavisualizer.algorithm.tree.TreeOperation;
import com.dsavisualizer.model.TreeStep;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TreeController {

    private TreeOperation treeOperation;

    private Pane treePane;

    private TextField arrayField;
    private TextField valueField;

    private Label messageLabel;
    private Label traversalLabel;
    private Label operationLabel;

    private Label loopLabel;
    private Label currentLabel;
    private Label compareLabel;
    private Label directionLabel;
    private Label pathLabel;

    private Label speedLabel;
    private Slider speedSlider;

    private TextArea codeArea;

    private List<TreeStep> currentSteps;
    private int stepIndex;

    private Timeline timeline;
    private boolean isPlaying;

    public Scene createTreeScene(Stage stage) {

        treeOperation = new TreeOperation();

        Label title = new Label("Binary Search Tree Visualizer");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Button backBtn = createActionButton("Back");
        backBtn.setOnAction(e -> {
            stopTimeline();
            HomeController homeController = new HomeController();
            stage.setScene(homeController.createHomeScene(stage));
        });

        HBox topBar = new HBox(20, backBtn, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #edf2f4;");

        arrayField = new TextField();
        arrayField.setPromptText("Enter array: 50,30,70,20,40,60,80");
        arrayField.setPrefWidth(330);

        valueField = new TextField();
        valueField.setPromptText("Value");
        valueField.setPrefWidth(90);

        Button buildBtn = createActionButton("Build Tree");
        Button insertBtn = createActionButton("Insert");
        Button searchBtn = createActionButton("Search");
        Button deleteBtn = createActionButton("Delete");

        Button inorderBtn = createActionButton("Inorder");
        Button preorderBtn = createActionButton("Preorder");
        Button postorderBtn = createActionButton("Postorder");
        Button levelOrderBtn = createActionButton("Level Order");

        Button clearBtn = createActionButton("Clear");

        buildBtn.setOnAction(e -> buildTreeFromArray());
        insertBtn.setOnAction(e -> insertValue());
        searchBtn.setOnAction(e -> searchValue());
        deleteBtn.setOnAction(e -> deleteValue());

        inorderBtn.setOnAction(e -> runInorder());
        preorderBtn.setOnAction(e -> runPreorder());
        postorderBtn.setOnAction(e -> runPostorder());
        levelOrderBtn.setOnAction(e -> runLevelOrder());

        clearBtn.setOnAction(e -> clearTree());

        speedLabel = new Label("Speed: Normal");
        speedLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        /*
         * Important:
         * Slider ka value delay hai.
         * 0.15 seconds = fast
         * 2.00 seconds = slow
         */
        speedSlider = new Slider(0.15, 2.00, 0.80);
        speedSlider.setPrefWidth(170);
        speedSlider.setShowTickMarks(true);
        speedSlider.setShowTickLabels(true);
        speedSlider.setMajorTickUnit(0.5);
        speedSlider.setMinorTickCount(1);

        speedSlider.valueProperty().addListener((obs, oldValue, newValue) -> updateSpeedLabel());

        updateSpeedLabel();

        HBox inputBox1 = new HBox(10);
        inputBox1.setAlignment(Pos.CENTER);
        inputBox1.setPadding(new Insets(10, 10, 2, 10));
        inputBox1.getChildren().addAll(
                arrayField,
                buildBtn,
                valueField,
                insertBtn,
                searchBtn,
                deleteBtn
        );

        HBox inputBox2 = new HBox(10);
        inputBox2.setAlignment(Pos.CENTER);
        inputBox2.setPadding(new Insets(2, 10, 10, 10));
        inputBox2.getChildren().addAll(
                inorderBtn,
                preorderBtn,
                postorderBtn,
                levelOrderBtn,
                clearBtn,
                speedLabel,
                speedSlider
        );

        VBox inputSection = new VBox(5, inputBox1, inputBox2);
        inputSection.setAlignment(Pos.CENTER);

        treePane = new Pane();
        treePane.setPrefSize(1500, 500);
        treePane.setStyle("-fx-background-color: white; -fx-border-color: #ced4da;");

        ScrollPane scrollPane = new ScrollPane(treePane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(false);

        operationLabel = new Label("Operation: ");
        operationLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        messageLabel = new Label("Array enter karke Build Tree button dabao.");
        messageLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        traversalLabel = new Label("Traversal Output: ");
        traversalLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        loopLabel = new Label("Loop Step: ");
        loopLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        currentLabel = new Label("Current Node: ");
        currentLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        compareLabel = new Label("Compare: ");
        compareLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        directionLabel = new Label("Direction: ");
        directionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        pathLabel = new Label("Path: ");
        pathLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox trackerBox = new HBox(25);
        trackerBox.setAlignment(Pos.CENTER);
        trackerBox.getChildren().addAll(
                loopLabel,
                currentLabel,
                compareLabel,
                directionLabel
        );

        codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setPrefHeight(90);
        codeArea.setWrapText(true);
        codeArea.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-family: Consolas;" +
                        "-fx-control-inner-background: #ffffff;" +
                        "-fx-text-fill: #000000;"
        );

        VBox bottomBox = new VBox(8);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(12));
        bottomBox.setStyle("-fx-background-color: #edf2f4;");
        bottomBox.getChildren().addAll(
                operationLabel,
                messageLabel,
                traversalLabel,
                trackerBox,
                pathLabel,
                codeArea
        );

        VBox centerBox = new VBox(10, inputSection, scrollPane);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centerBox);
        root.setBottom(bottomBox);
        root.setStyle("-fx-background-color: #f8f9fa;");

        return new Scene(root, 1250, 780);
    }

    private Button createActionButton(String text) {

        Button button = new Button(text);

        button.setPrefHeight(36);
        button.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: #2b2d42;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 8;" +
                        "-fx-cursor: hand;"
        );

        return button;
    }

    private void updateSpeedLabel() {

        if (speedSlider == null || speedLabel == null) {
            return;
        }

        double speed = speedSlider.getValue();

        if (speed <= 0.35) {
            speedLabel.setText("Speed: Very Fast");
        }
        else if (speed <= 0.70) {
            speedLabel.setText("Speed: Fast");
        }
        else if (speed <= 1.20) {
            speedLabel.setText("Speed: Normal");
        }
        else {
            speedLabel.setText("Speed: Slow");
        }
    }

    private double getCurrentSpeedDelay() {

        if (speedSlider == null) {
            return 0.80;
        }

        return speedSlider.getValue();
    }

    private void buildTreeFromArray() {

        int[] values = getInputArray();

        if (values == null) {
            return;
        }

        currentSteps = treeOperation.buildTreeFromArray(values);
        playSteps();
    }

    private void insertValue() {

        Integer value = getSingleValue();

        if (value == null) {
            return;
        }

        currentSteps = treeOperation.insertValue(value);
        playSteps();

        valueField.clear();
    }

    private void searchValue() {

        Integer value = getSingleValue();

        if (value == null) {
            return;
        }

        currentSteps = treeOperation.searchValue(value);
        playSteps();

        valueField.clear();
    }

    private void deleteValue() {

        Integer value = getSingleValue();

        if (value == null) {
            return;
        }

        currentSteps = treeOperation.deleteValue(value);
        playSteps();

        valueField.clear();
    }

    private void runInorder() {
        currentSteps = treeOperation.inorderTraversal();
        playSteps();
    }

    private void runPreorder() {
        currentSteps = treeOperation.preorderTraversal();
        playSteps();
    }

    private void runPostorder() {
        currentSteps = treeOperation.postorderTraversal();
        playSteps();
    }

    private void runLevelOrder() {
        currentSteps = treeOperation.levelOrderTraversal();
        playSteps();
    }

    private void clearTree() {

        stopTimeline();

        treeOperation.clearTree();
        treePane.getChildren().clear();

        operationLabel.setText("Operation: Clear");
        messageLabel.setText("Tree clear ho gaya.");
        traversalLabel.setText("Traversal Output: ");

        loopLabel.setText("Loop Step: ");
        currentLabel.setText("Current Node: ");
        compareLabel.setText("Compare: ");
        directionLabel.setText("Direction: ");
        pathLabel.setText("Path: ");

        codeArea.setText("root = null;");
    }

    private int[] getInputArray() {

        try {
            String text = arrayField.getText().trim();

            if (text.isEmpty()) {
                messageLabel.setText("Please array enter karo. Example: 50,30,70,20,40,60,80");
                return null;
            }

            String[] parts = text.split("[,\\s]+");
            int[] arr = new int[parts.length];

            for (int i = 0; i < parts.length; i++) {
                arr[i] = Integer.parseInt(parts[i]);
            }

            return arr;
        }
        catch (NumberFormatException e) {
            messageLabel.setText("Array me sirf integer values do. Example: 50,30,70,20");
            return null;
        }
    }

    private Integer getSingleValue() {

        try {
            String text = valueField.getText().trim();

            if (text.isEmpty()) {
                messageLabel.setText("Please value enter karo.");
                return null;
            }

            return Integer.parseInt(text);
        }
        catch (NumberFormatException e) {
            messageLabel.setText("Only integer value enter karo.");
            return null;
        }
    }

    private void playSteps() {

        stopTimeline();

        if (currentSteps == null || currentSteps.isEmpty()) {
            return;
        }

        stepIndex = 0;
        isPlaying = true;

        showCurrentStepAndScheduleNext();
    }

    private void showCurrentStepAndScheduleNext() {

        if (!isPlaying) {
            return;
        }

        if (currentSteps == null || stepIndex >= currentSteps.size()) {
            stopTimeline();
            return;
        }

        TreeStep step = currentSteps.get(stepIndex);
        showStep(step);
        stepIndex++;

        if (stepIndex >= currentSteps.size()) {
            stopTimeline();
            return;
        }

        double delay = getCurrentSpeedDelay();

        timeline = new Timeline(new KeyFrame(Duration.seconds(delay), e -> {
            showCurrentStepAndScheduleNext();
        }));

        timeline.setCycleCount(1);
        timeline.play();
    }

    private void showStep(TreeStep step) {

        drawTree(step);

        operationLabel.setText("Operation: " + step.getOperation());
        messageLabel.setText(step.getMessage());

        if (step.getTraversalOutput() != null && !step.getTraversalOutput().isEmpty()) {
            traversalLabel.setText("Traversal Output: " + step.getTraversalOutput());
        }

        loopLabel.setText("Loop Step: " + step.getLoopStep());

        if (step.getCurrentNodeValue() == -1) {
            currentLabel.setText("Current Node: null");
        }
        else {
            currentLabel.setText("Current Node: " + step.getCurrentNodeValue());
        }

        compareLabel.setText("Compare: " + step.getCompareMessage());
        directionLabel.setText("Direction: " + step.getDirectionMessage());
        pathLabel.setText(step.getPathMessage());

        codeArea.setText("Line " + step.getCodeLine() + ": " + step.getCodeMessage());
    }

    private void stopTimeline() {

        isPlaying = false;

        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    private void drawTree(TreeStep step) {

        treePane.getChildren().clear();

        int[] values = step.getTreeValues();

        if (values == null || values.length == 0) {
            return;
        }

        TreeDrawNode root = null;

        for (int value : values) {
            root = insertDrawNode(root, value);
        }

        drawNode(root, 750, 55, 320, step.getActiveValue(), step.getTargetValue());
    }

    private TreeDrawNode insertDrawNode(TreeDrawNode root, int value) {

        if (root == null) {
            return new TreeDrawNode(value);
        }

        TreeDrawNode current = root;

        while (true) {

            if (value < current.value) {

                if (current.left == null) {
                    current.left = new TreeDrawNode(value);
                    break;
                }

                current = current.left;
            }
            else if (value > current.value) {

                if (current.right == null) {
                    current.right = new TreeDrawNode(value);
                    break;
                }

                current = current.right;
            }
            else {
                break;
            }
        }

        return root;
    }

    private void drawNode(
            TreeDrawNode node,
            double x,
            double y,
            double gap,
            int activeValue,
            int targetValue
    ) {

        if (node == null) {
            return;
        }

        if (node.left != null) {

            double childX = x - gap;
            double childY = y + 85;

            Line line = new Line(x, y + 25, childX, childY - 25);
            line.setStrokeWidth(2);
            line.setStyle("-fx-stroke: #8d99ae;");

            treePane.getChildren().add(line);

            drawNode(node.left, childX, childY, gap / 2, activeValue, targetValue);
        }

        if (node.right != null) {

            double childX = x + gap;
            double childY = y + 85;

            Line line = new Line(x, y + 25, childX, childY - 25);
            line.setStrokeWidth(2);
            line.setStyle("-fx-stroke: #8d99ae;");

            treePane.getChildren().add(line);

            drawNode(node.right, childX, childY, gap / 2, activeValue, targetValue);
        }

        Circle circle = new Circle(x, y, 25);

        if (node.value == activeValue) {
            circle.setStyle("-fx-fill: #ffb703; -fx-stroke: #22223b; -fx-stroke-width: 3;");
        }
        else if (node.value == targetValue) {
            circle.setStyle("-fx-fill: #90e0ef; -fx-stroke: #22223b; -fx-stroke-width: 2;");
        }
        else {
            circle.setStyle("-fx-fill: #2b2d42; -fx-stroke: #22223b; -fx-stroke-width: 2;");
        }

        Text text = new Text(String.valueOf(node.value));

        if (String.valueOf(node.value).length() == 1) {
            text.setX(x - 4);
        }
        else if (String.valueOf(node.value).length() == 2) {
            text.setX(x - 8);
        }
        else {
            text.setX(x - 12);
        }

        text.setY(y + 5);
        text.setStyle("-fx-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

        treePane.getChildren().addAll(circle, text);
    }

    private static class TreeDrawNode {

        int value;
        TreeDrawNode left;
        TreeDrawNode right;

        TreeDrawNode(int value) {
            this.value = value;
        }
    }
}
package com.dsavisualizer.controller;

import java.util.List;

import com.dsavisualizer.algorithm.sorting.BubbleSort;
import com.dsavisualizer.model.SortingStep;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SortingController {

    private static final int MAX_INPUT_SIZE = 15;

    private int[] arr;

    private HBox barContainer;
    private Label statusLabel;
    private Label iLabel;
    private Label jLabel;
    private TextField inputField;

    private Slider speedSlider;
    private Label speedLabel;

    private List<SortingStep> steps;
    private int currentStepIndex = 0;

    public Scene createSortingScene(Stage stage) {

        Label title = new Label("Bubble Sort Visualizer");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        Label inputInfo = new Label("Enter numbers separated by comma. Max 15 numbers allowed:");
        inputInfo.setStyle("-fx-font-size: 15px;");

        inputField = new TextField();
        inputField.setPromptText("Example: 50,120,80,200,30");
        inputField.setMaxWidth(500);
        inputField.setStyle("-fx-font-size: 15px;");

        Button generateBtn = new Button("Generate Array");
        generateBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button startBtn = new Button("Start Bubble Sort");
        startBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-font-size: 14px;");

        statusLabel = new Label("Enter array and click Generate Array");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        iLabel = new Label("i = -");
        jLabel = new Label("j = -");

        iLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");
        jLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: orange;");

        HBox pointerBox = new HBox(30);
        pointerBox.setAlignment(Pos.CENTER);
        pointerBox.getChildren().addAll(iLabel, jLabel);

        speedLabel = new Label("Speed: 0.8 sec");
        speedLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        speedSlider = new Slider(0.1, 2.0, 0.8);
        speedSlider.setMaxWidth(300);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(0.5);
        speedSlider.setBlockIncrement(0.1);

        speedSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            speedLabel.setText(String.format("Speed: %.1f sec", newValue.doubleValue()));
        });

        VBox speedBox = new VBox(5);
        speedBox.setAlignment(Pos.CENTER);
        speedBox.getChildren().addAll(speedLabel, speedSlider);

        barContainer = new HBox(12);
        barContainer.setAlignment(Pos.BOTTOM_CENTER);
        barContainer.setPrefHeight(380);

        generateBtn.setOnAction(e -> generateArrayFromInput());

        startBtn.setOnAction(e -> startBubbleSortVisualization());

        backBtn.setOnAction(e -> {
            HomeController homeController = new HomeController();
            stage.setScene(homeController.createHomeScene(stage));
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(generateBtn, startBtn, backBtn);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.getChildren().addAll(
                title,
                inputInfo,
                inputField,
                buttonBox,
                pointerBox,
                speedBox,
                statusLabel,
                barContainer
        );

        return new Scene(root, 1000, 650);
    }

    private void generateArrayFromInput() {

        try {
            String input = inputField.getText();

            if (input == null || input.trim().isEmpty()) {
                statusLabel.setText("Input empty hai. Example: 50,120,80,200");
                return;
            }

            String[] parts = input.split(",");

            if (parts.length > MAX_INPUT_SIZE) {
                statusLabel.setText("Maximum " + MAX_INPUT_SIZE + " numbers allowed hain");
                return;
            }

            if (parts.length < 2) {
                statusLabel.setText("Minimum 2 numbers enter karo");
                return;
            }

            arr = new int[parts.length];

            for (int k = 0; k < parts.length; k++) {
                arr[k] = Integer.parseInt(parts[k].trim());

                if (arr[k] <= 0) {
                    statusLabel.setText("Only positive numbers enter karo");
                    return;
                }
            }

            currentStepIndex = 0;
            steps = null;

            iLabel.setText("i = -");
            jLabel.setText("j = -");

            statusLabel.setText("Array Generated. Size = " + arr.length);

            drawBars(arr, -1, -1, -1, -1);

        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid input. Sirf numbers comma se do. Example: 10,20,30");
        }
    }

    private void startBubbleSortVisualization() {

        if (arr == null || arr.length == 0) {
            statusLabel.setText("Please generate array first");
            return;
        }

        BubbleSort bubbleSort = new BubbleSort();
        steps = bubbleSort.generateSteps(arr);

        currentStepIndex = 0;
        statusLabel.setText("Bubble Sort Started");

        playNextStep();
    }

    private void playNextStep() {

        if (currentStepIndex >= steps.size()) {

            statusLabel.setText("Bubble Sort Completed");
            iLabel.setText("i = done");
            jLabel.setText("j = done");

            if (steps != null && !steps.isEmpty()) {

                SortingStep lastStep = steps.get(steps.size() - 1);
                int[] sortedArray = lastStep.getArrayState();

                arr = sortedArray;

                drawBars(sortedArray, -1, -1, -1, 0);
            }

            return;
        }

        SortingStep step = steps.get(currentStepIndex);

        int[] currentArray = step.getArrayState();
        arr = currentArray;

        int i = step.getI();
        int j = step.getJ();

        iLabel.setText(i == -1 ? "i = done" : "i = " + i);
        jLabel.setText(j == -1 ? "j = done" : "j = " + j);

        int compareIndex1 = j;
        int compareIndex2 = -1;

        if (j != -1) {
            compareIndex2 = j + 1;
        }

        if (i == -1 && j == -1) {
            statusLabel.setText("Bubble Sort Completed");
        } else if (step.isSwapped()) {
            statusLabel.setText(
                    "i = " + i + ", j = " + j +
                            " | Compared index " + j + " and " + (j + 1) +
                            " → Swap happened"
            );
        } else {
            statusLabel.setText(
                    "i = " + i + ", j = " + j +
                            " | Compared index " + j + " and " + (j + 1) +
                            " → No swap"
            );
        }

        drawBars(currentArray, compareIndex1, compareIndex2, i, step.getSortedStartIndex());

        double speed = speedSlider.getValue();

        PauseTransition pause = new PauseTransition(Duration.seconds(speed));
        pause.setOnFinished(e -> {
            currentStepIndex++;
            playNextStep();
        });

        pause.play();
    }

    private void drawBars(
            int[] currentArray,
            int compareIndex1,
            int compareIndex2,
            int currentI,
            int sortedStartIndex
    ) {

        barContainer.getChildren().clear();

        if (currentArray == null || currentArray.length == 0) {
            return;
        }

        int maxValue = getMaxValue(currentArray);
        int totalBars = currentArray.length;

        double barWidth = 50;

        if (totalBars > 8) {
            barWidth = 40;
        }

        if (totalBars > 12) {
            barWidth = 30;
        }

        for (int k = 0; k < currentArray.length; k++) {

            VBox barBox = new VBox(4);
            barBox.setAlignment(Pos.BOTTOM_CENTER);

            Label valueLabel = new Label(String.valueOf(currentArray[k]));
            valueLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

            double barHeight = ((double) currentArray[k] / maxValue) * 230;

            Rectangle bar = new Rectangle(barWidth, barHeight);

            if (k == compareIndex1 || k == compareIndex2) {
                bar.setFill(Color.ORANGE);
            } else if (sortedStartIndex != -1 && k >= sortedStartIndex) {
                bar.setFill(Color.GREEN);
            } else {
                bar.setFill(Color.DARKBLUE);
            }

            Label indexLabel = new Label("idx " + k);
            indexLabel.setStyle("-fx-font-size: 11px;");

            Label pointerLabel = new Label(" ");
            pointerLabel.setMinHeight(18);

            if (k == compareIndex1) {
                pointerLabel.setText("j ↑");
                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: orange;");
            } else if (k == compareIndex2) {
                pointerLabel.setText("j+1 ↑");
                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: red;");
            } else if (currentI != -1 && k == currentI) {
                pointerLabel.setText("i ↑");
                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: green;");
            } else if (sortedStartIndex != -1 && k >= sortedStartIndex) {
                pointerLabel.setText("sorted");
                pointerLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: green;");
            }

            barBox.getChildren().addAll(valueLabel, bar, indexLabel, pointerLabel);
            barContainer.getChildren().add(barBox);
        }
    }

    private int getMaxValue(int[] currentArray) {

        int max = currentArray[0];

        for (int value : currentArray) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }
}
package com.dsavisualizer.controller;

import java.util.List;

import com.dsavisualizer.algorithm.sorting.BubbleSort;
import com.dsavisualizer.algorithm.sorting.InsertionSort;
import com.dsavisualizer.algorithm.sorting.SelectionSort;
import com.dsavisualizer.model.SortingStep;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
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
    private Label title;
    private Label statusLabel;
    private Label iLabel;
    private Label jLabel;
    private TextField inputField;

    private Slider speedSlider;
    private Label speedLabel;

    private ComboBox<String> algorithmBox;
    private TextArea codeArea;

    private List<SortingStep> steps;
    private int currentStepIndex = 0;

    private String selectedAlgorithm = "Bubble Sort";

    public Scene createSortingScene(Stage stage) {

        title = new Label("Sorting Visualizer");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label inputInfo = new Label("Enter numbers separated by comma. Max 15 numbers allowed:");
        inputInfo.setStyle("-fx-font-size: 15px; -fx-text-fill: black;");

        inputField = new TextField();
        inputField.setPromptText("Example: 50,120,80,200,30");
        inputField.setMaxWidth(500);
        inputField.setStyle("-fx-font-size: 15px;");

        algorithmBox = new ComboBox<>();
        algorithmBox.getItems().addAll(
                "Bubble Sort",
                "Selection Sort",
                "Insertion Sort"
        );
        algorithmBox.setValue("Bubble Sort");
        algorithmBox.setPrefWidth(250);
        algorithmBox.setStyle("-fx-font-size: 14px;");

        codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setWrapText(false);
        codeArea.setPrefWidth(360);
        codeArea.setPrefHeight(520);
        codeArea.setText(getAlgorithmCode(selectedAlgorithm));
        codeArea.setStyle(
                "-fx-font-family: 'Consolas';" +
                        "-fx-font-size: 12px;" +
                        "-fx-control-inner-background: white;" +
                        "-fx-text-fill: black;" +
                        "-fx-highlight-fill: #d9d9d9;" +
                        "-fx-highlight-text-fill: black;" +
                        "-fx-border-color: #cfcfcf;" +
                        "-fx-border-width: 1;"
        );

        Button generateBtn = new Button("Generate Array");
        generateBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button startBtn = new Button("Start Sorting");
        startBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-font-size: 14px;");

        statusLabel = new Label("Select algorithm, enter array and click Generate Array");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

        iLabel = new Label("i = -");
        jLabel = new Label("j = -");

        iLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");
        jLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: orange;");

        HBox pointerBox = new HBox(30);
        pointerBox.setAlignment(Pos.CENTER);
        pointerBox.getChildren().addAll(iLabel, jLabel);

        speedLabel = new Label("Speed: 0.8 sec");
        speedLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

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
        barContainer.setMinWidth(650);

        generateBtn.setOnAction(e -> generateArrayFromInput());

        startBtn.setOnAction(e -> startSortingVisualization());

        backBtn.setOnAction(e -> {
            HomeController homeController = new HomeController();
            stage.setScene(homeController.createHomeScene(stage));
        });

        HBox algorithmBoxContainer = new HBox(10);
        algorithmBoxContainer.setAlignment(Pos.CENTER);

        Label algorithmLabel = new Label("Choose Algorithm:");
        algorithmLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black;");

        algorithmBoxContainer.getChildren().addAll(algorithmLabel, algorithmBox);

        algorithmBox.setOnAction(e -> {
            selectedAlgorithm = algorithmBox.getValue();

            title.setText(selectedAlgorithm + " Visualizer");
            statusLabel.setText(selectedAlgorithm + " selected. Generate array and start.");

            codeArea.setText(getAlgorithmCode(selectedAlgorithm));

            resetVisualizationState();
        });

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(generateBtn, startBtn, backBtn);

        Label codeTitle = new Label("Algorithm Logic Code");
        codeTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: black;");

        VBox codePanel = new VBox(10);
        codePanel.setAlignment(Pos.TOP_CENTER);
        codePanel.setPadding(new Insets(10));
        codePanel.setPrefWidth(380);
        codePanel.setMaxWidth(380);
        codePanel.getChildren().addAll(codeTitle, codeArea);

        VBox visualPanel = new VBox(15);
        visualPanel.setAlignment(Pos.TOP_CENTER);
        visualPanel.setPadding(new Insets(10));
        visualPanel.setPrefWidth(760);
        visualPanel.getChildren().addAll(
                algorithmBoxContainer,
                inputInfo,
                inputField,
                buttonBox,
                pointerBox,
                speedBox,
                statusLabel,
                barContainer
        );

        HBox mainContent = new HBox(45);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getChildren().addAll(codePanel, visualPanel);

        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: white;");
        root.getChildren().addAll(
                title,
                mainContent
        );

        return new Scene(root, 1250, 720);
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

            statusLabel.setText("Array Generated. Size = " + arr.length + " | Algorithm = " + selectedAlgorithm);

            drawBars(arr, -1, -1, -1, -1);

        } catch (NumberFormatException ex) {
            statusLabel.setText("Invalid input. Sirf numbers comma se do. Example: 10,20,30");
        }
    }

    private void startSortingVisualization() {

        if (arr == null || arr.length == 0) {
            statusLabel.setText("Please generate array first");
            return;
        }

        if (selectedAlgorithm.equals("Bubble Sort")) {

            BubbleSort bubbleSort = new BubbleSort();
            steps = bubbleSort.generateSteps(arr);

        } else if (selectedAlgorithm.equals("Selection Sort")) {

            SelectionSort selectionSort = new SelectionSort();
            steps = selectionSort.generateSteps(arr);

        } else if (selectedAlgorithm.equals("Insertion Sort")) {

            InsertionSort insertionSort = new InsertionSort();
            steps = insertionSort.generateSteps(arr);

        } else {

            statusLabel.setText("This algorithm is not added yet");
            return;
        }

        currentStepIndex = 0;
        statusLabel.setText(selectedAlgorithm + " Started");

        playNextStep();
    }

    private void playNextStep() {

        if (currentStepIndex >= steps.size()) {

            statusLabel.setText(selectedAlgorithm + " Completed");
            iLabel.setText("i = done");
            jLabel.setText("j = done");

            if (steps != null && !steps.isEmpty()) {

                SortingStep lastStep = steps.get(steps.size() - 1);
                int[] sortedArray = lastStep.getArrayState();

                arr = sortedArray;

                drawBars(sortedArray, -1, -1, -1, lastStep.getSortedStartIndex());
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

        int compareIndex1;
        int compareIndex2;

        if (selectedAlgorithm.equals("Bubble Sort")) {

            compareIndex1 = j;
            compareIndex2 = -1;

            if (j != -1) {
                compareIndex2 = j + 1;
            }

            updateBubbleSortStatus(step, i, j);

        } else if (selectedAlgorithm.equals("Selection Sort")) {

            compareIndex1 = i;
            compareIndex2 = j;

            updateSelectionSortStatus(step, i, j);

        } else if (selectedAlgorithm.equals("Insertion Sort")) {

            compareIndex1 = i;
            compareIndex2 = j;

            updateInsertionSortStatus(step, i, j);

        } else {

            compareIndex1 = -1;
            compareIndex2 = -1;
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

    private void updateBubbleSortStatus(SortingStep step, int i, int j) {

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
    }

    private void updateSelectionSortStatus(SortingStep step, int i, int j) {

        if (i == -1 && j == -1) {
            statusLabel.setText("Selection Sort Completed");
        } else if (step.isSwapped()) {
            statusLabel.setText(
                    "Current index = " + i +
                            ", Minimum index = " + j +
                            " → Minimum element swapped"
            );
        } else {
            statusLabel.setText(
                    "Minimum index = " + i +
                            ", Checking index = " + j +
                            " → Finding smallest element"
            );
        }
    }

    private void updateInsertionSortStatus(SortingStep step, int i, int j) {

        if (i == -1 && j == -1) {
            statusLabel.setText("Insertion Sort Completed");
        } else if (step.isSwapped()) {
            statusLabel.setText(
                    "Key index = " + i +
                            ", Checking index = " + j +
                            " → Bigger element shifted right"
            );
        } else {
            statusLabel.setText(
                    "Key index = " + i +
                            ", Insert position = " + j +
                            " → Key placed in sorted left part"
            );
        }
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
            valueLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: black;");

            double barHeight = ((double) currentArray[k] / maxValue) * 230;

            Rectangle bar = new Rectangle(barWidth, barHeight);

            if (k == compareIndex1 || k == compareIndex2) {
                bar.setFill(Color.ORANGE);
            } else if (isSortedIndex(k, sortedStartIndex)) {
                bar.setFill(Color.GREEN);
            } else {
                bar.setFill(Color.DARKBLUE);
            }

            Label indexLabel = new Label("idx " + k);
            indexLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: black;");

            Label pointerLabel = new Label(" ");
            pointerLabel.setMinHeight(18);

            if (k == compareIndex1) {

                if (selectedAlgorithm.equals("Selection Sort")) {
                    pointerLabel.setText("min ↑");
                } else if (selectedAlgorithm.equals("Insertion Sort")) {
                    pointerLabel.setText("key ↑");
                } else {
                    pointerLabel.setText("j ↑");
                }

                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: orange;");

            } else if (k == compareIndex2) {

                if (selectedAlgorithm.equals("Selection Sort")) {
                    pointerLabel.setText("check ↑");
                } else if (selectedAlgorithm.equals("Insertion Sort")) {
                    pointerLabel.setText("check ↑");
                } else {
                    pointerLabel.setText("j+1 ↑");
                }

                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: red;");

            } else if (currentI != -1 && k == currentI) {

                pointerLabel.setText("i ↑");
                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: green;");

            } else if (isSortedIndex(k, sortedStartIndex)) {

                pointerLabel.setText("sorted");
                pointerLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: green;");
            }

            barBox.getChildren().addAll(valueLabel, bar, indexLabel, pointerLabel);
            barContainer.getChildren().add(barBox);
        }
    }

    private boolean isSortedIndex(int index, int sortedStartIndex) {

        if (sortedStartIndex == -1) {
            return false;
        }

        if (selectedAlgorithm.equals("Bubble Sort")) {
            return index >= sortedStartIndex;
        }

        if (selectedAlgorithm.equals("Selection Sort")) {
            return index <= sortedStartIndex;
        }

        if (selectedAlgorithm.equals("Insertion Sort")) {
            return index <= sortedStartIndex;
        }

        return false;
    }

    private void resetVisualizationState() {

        currentStepIndex = 0;
        steps = null;

        iLabel.setText("i = -");
        jLabel.setText("j = -");

        if (arr != null && arr.length > 0) {
            drawBars(arr, -1, -1, -1, -1);
        }
    }

    private String getAlgorithmCode(String algorithmName) {

        if (algorithmName.equals("Bubble Sort")) {
            return getBubbleSortCode();
        }

        if (algorithmName.equals("Selection Sort")) {
            return getSelectionSortCode();
        }

        if (algorithmName.equals("Insertion Sort")) {
            return getInsertionSortCode();
        }

        return "Code logic not added yet.";
    }

    private String getBubbleSortCode() {

        return """
Bubble Sort Logic:

for (int i = 0; i < n - 1; i++) {

    for (int j = 0; j < n - i - 1; j++) {

        if (arr[j] > arr[j + 1]) {

            swap arr[j] and arr[j + 1];
        }
    }
}
""";
    }

    private String getSelectionSortCode() {

        return """
Selection Sort Logic:

for (int i = 0; i < n - 1; i++) {

    int minIndex = i;

    for (int j = i + 1; j < n; j++) {

        if (arr[j] < arr[minIndex]) {

            minIndex = j;
        }
    }

    if (minIndex != i) {

        swap arr[i] and arr[minIndex];
    }
}
""";
    }

    private String getInsertionSortCode() {

        return """
Insertion Sort Logic:

for (int i = 1; i < n; i++) {

    int key = arr[i];
    int j = i - 1;

    while (j >= 0 && arr[j] > key) {

        arr[j + 1] = arr[j];
        j--;
    }

    arr[j + 1] = key;
}
""";
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
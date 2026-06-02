package com.dsavisualizer.controller;

import java.util.List;

import com.dsavisualizer.algorithm.sorting.BubbleSort;
import com.dsavisualizer.algorithm.sorting.InsertionSort;
import com.dsavisualizer.algorithm.sorting.MergeSort;
import com.dsavisualizer.algorithm.sorting.QuickSort;
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

    private VBox mergeDetailsPanel;
    private HBox leftTempContainer;
    private HBox rightTempContainer;
    private Label mergePhaseLabel;
    private Label mergeRangeLabel;

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

    private SortingStep currentVisibleStep;

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
                "Insertion Sort",
                "Merge Sort",
                "Quick Sort"
        );
        algorithmBox.setValue("Bubble Sort");
        algorithmBox.setPrefWidth(250);
        algorithmBox.setStyle("-fx-font-size: 14px;");

        codeArea = new TextArea();
        codeArea.setEditable(false);
        codeArea.setWrapText(false);
        codeArea.setPrefWidth(360);
        codeArea.setPrefHeight(560);
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
        statusLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: black;");

        iLabel = new Label("i = -");
        jLabel = new Label("j = -");

        iLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: green;");
        jLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: orange;");

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
        barContainer.setPrefHeight(150);
        barContainer.setMinWidth(650);

        createMergeDetailsPanel();

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

        VBox visualPanel = new VBox(10);
        visualPanel.setAlignment(Pos.TOP_CENTER);
        visualPanel.setPadding(new Insets(10));
        visualPanel.setPrefWidth(880);
        visualPanel.getChildren().addAll(
                algorithmBoxContainer,
                inputInfo,
                inputField,
                buttonBox,
                pointerBox,
                speedBox,
                statusLabel,
                mergeDetailsPanel,
                barContainer
        );

        HBox mainContent = new HBox(35);
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.getChildren().addAll(codePanel, visualPanel);

        VBox root = new VBox(12);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: white;");
        root.getChildren().addAll(title, mainContent);

        return new Scene(root, 1380, 780);
    }

    private void createMergeDetailsPanel() {

        mergePhaseLabel = new Label("");
        mergePhaseLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        mergeRangeLabel = new Label("");
        mergeRangeLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: black;");

        Label leftTitle = new Label("Left Temp Array");
        leftTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: purple;");

        Label rightTitle = new Label("Right Temp Array");
        rightTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: orange;");

        leftTempContainer = new HBox(8);
        leftTempContainer.setAlignment(Pos.CENTER);

        rightTempContainer = new HBox(8);
        rightTempContainer.setAlignment(Pos.CENTER);

        VBox leftBox = new VBox(4);
        leftBox.setAlignment(Pos.CENTER);
        leftBox.getChildren().addAll(leftTitle, leftTempContainer);

        VBox rightBox = new VBox(4);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.getChildren().addAll(rightTitle, rightTempContainer);

        HBox tempArraysBox = new HBox(35);
        tempArraysBox.setAlignment(Pos.CENTER);
        tempArraysBox.getChildren().addAll(leftBox, rightBox);

        mergeDetailsPanel = new VBox(7);
        mergeDetailsPanel.setAlignment(Pos.CENTER);
        mergeDetailsPanel.setPadding(new Insets(8));
        mergeDetailsPanel.setStyle(
                "-fx-border-color: #d9d9d9;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-background-color: #f8f8f8;"
        );

        mergeDetailsPanel.getChildren().addAll(
                mergePhaseLabel,
                mergeRangeLabel,
                tempArraysBox
        );

        mergeDetailsPanel.setVisible(false);
        mergeDetailsPanel.setManaged(false);
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
            currentVisibleStep = null;

            iLabel.setText("i = -");
            jLabel.setText("j = -");

            statusLabel.setText("Array Generated. Size = " + arr.length + " | Algorithm = " + selectedAlgorithm);

            clearMergeDetails();
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

        } else if (selectedAlgorithm.equals("Merge Sort")) {

            MergeSort mergeSort = new MergeSort();
            steps = mergeSort.generateSteps(arr);

        } else if (selectedAlgorithm.equals("Quick Sort")) {

            QuickSort quickSort = new QuickSort();
            steps = quickSort.generateSteps(arr);

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
                currentVisibleStep = lastStep;

                if (selectedAlgorithm.equals("Merge Sort")) {
                    drawMergeDetails(lastStep);
                } else {
                    clearMergeDetails();
                }

                drawBars(sortedArray, -1, -1, -1, lastStep.getSortedStartIndex());
            }

            return;
        }

        SortingStep step = steps.get(currentStepIndex);
        currentVisibleStep = step;

        int[] currentArray = step.getArrayState();
        arr = currentArray;

        int i = step.getI();
        int j = step.getJ();

        int compareIndex1;
        int compareIndex2;

        if (selectedAlgorithm.equals("Bubble Sort")) {

            iLabel.setText(i == -1 ? "i = done" : "i = " + i);
            jLabel.setText(j == -1 ? "j = done" : "j = " + j);

            compareIndex1 = j;
            compareIndex2 = -1;

            if (j != -1) {
                compareIndex2 = j + 1;
            }

            clearMergeDetails();
            updateBubbleSortStatus(step, i, j);

        } else if (selectedAlgorithm.equals("Selection Sort")) {

            iLabel.setText(i == -1 ? "i = done" : "i = " + i);
            jLabel.setText(j == -1 ? "j = done" : "j = " + j);

            compareIndex1 = i;
            compareIndex2 = j;

            clearMergeDetails();
            updateSelectionSortStatus(step, i, j);

        } else if (selectedAlgorithm.equals("Insertion Sort")) {

            iLabel.setText(i == -1 ? "i = done" : "i = " + i);
            jLabel.setText(j == -1 ? "j = done" : "j = " + j);

            compareIndex1 = i;
            compareIndex2 = j;

            clearMergeDetails();
            updateInsertionSortStatus(step, i, j);

        } else if (selectedAlgorithm.equals("Merge Sort")) {

            iLabel.setText(step.getLeft() == -1 ? "left = -" : "left = " + step.getLeft());
            jLabel.setText(step.getRight() == -1 ? "right = -" : "right = " + step.getRight());

            compareIndex1 = i;
            compareIndex2 = j;

            drawMergeDetails(step);
            updateMergeSortStatus(step);

        } else if (selectedAlgorithm.equals("Quick Sort")) {

            iLabel.setText(step.getPartitionIndex() == -1 ? "partition = -" : "partition = " + step.getPartitionIndex());
            jLabel.setText(step.getJ() == -1 ? "compare = -" : "compare = " + step.getJ());

            compareIndex1 = step.getPivotIndex();
            compareIndex2 = step.getJ();

            clearMergeDetails();
            updateQuickSortStatus(step);

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

    private void updateQuickSortStatus(SortingStep step) {

        String phase = step.getPhase();

        if (phase.equals("START")) {
            statusLabel.setText("Quick Sort Started → Pivot choose karke partition karenge");

        } else if (phase.equals("CALL")) {
            statusLabel.setText(
                    "Quick Sort call → current range [" +
                            step.getLeft() + ", " + step.getRight() + "]"
            );

        } else if (phase.equals("PIVOT_SELECTED")) {
            statusLabel.setText(
                    "Pivot selected → index " + step.getPivotIndex() +
                            ", value = " + step.getArrayState()[step.getPivotIndex()]
            );

        } else if (phase.equals("COMPARE")) {

            int compareIndex = step.getJ();
            int pivotIndex = step.getPivotIndex();

            statusLabel.setText(
                    "Compare → arr[" + compareIndex + "] = " +
                            step.getArrayState()[compareIndex] +
                            " with pivot arr[" + pivotIndex + "] = " +
                            step.getArrayState()[pivotIndex]
            );

        } else if (phase.equals("SWAP_BEFORE")) {
            statusLabel.setText(
                    "Smaller than pivot found → swap index " +
                            step.getSwapIndex1() + " and " + step.getSwapIndex2()
            );

        } else if (phase.equals("SWAP_AFTER")) {
            statusLabel.setText("Swap done → smaller element left side me aa gaya");

        } else if (phase.equals("PIVOT_SWAP_BEFORE")) {
            statusLabel.setText("Partition complete → pivot ko correct position par rakh rahe hain");

        } else if (phase.equals("PIVOT_SWAP_AFTER")) {
            statusLabel.setText("Pivot placed correctly at index " + step.getPivotIndex());

        } else if (phase.equals("PIVOT_FIXED")) {
            statusLabel.setText(
                    "Pivot fixed → index " + step.getPivotIndex() +
                            " ab apni correct sorted position par hai"
            );

        } else if (phase.equals("SINGLE")) {
            statusLabel.setText(
                    "Single element range → index " + step.getLeft() +
                            " already sorted hai"
            );

        } else if (phase.equals("DONE")) {
            statusLabel.setText("Quick Sort Completed");

        } else {
            statusLabel.setText("Quick Sort Running");
        }
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

    private void updateMergeSortStatus(SortingStep step) {

        String phase = step.getPhase();

        if (phase.equals("START")) {
            statusLabel.setText("Merge Sort Started → First divide array into smaller parts");
        } else if (phase.equals("DIVIDE")) {
            statusLabel.setText(
                    "Divide phase → Range " + step.getLeft() +
                            " to " + step.getRight() +
                            ", mid = " + step.getMid()
            );
        } else if (phase.equals("SINGLE")) {
            statusLabel.setText(
                    "Single element reached → index " + step.getLeft() +
                            " is already sorted"
            );
        } else if (phase.equals("MERGE_START")) {
            statusLabel.setText(
                    "Merge phase started → Merging range " +
                            step.getLeft() + " to " + step.getRight()
            );
        } else if (phase.equals("COMPARE")) {
            statusLabel.setText(
                    "Compare phase → comparing original index " +
                            step.getI() + " and " + step.getJ()
            );
        } else if (phase.equals("WRITE_LEFT")) {
            statusLabel.setText(
                    "Write phase → left value is smaller, writing at index " +
                            step.getWriteIndex()
            );
        } else if (phase.equals("WRITE_RIGHT")) {
            statusLabel.setText(
                    "Write phase → right value is smaller, writing at index " +
                            step.getWriteIndex()
            );
        } else if (phase.equals("COPY_LEFT")) {
            statusLabel.setText(
                    "Copy leftover → copying remaining left value at index " +
                            step.getWriteIndex()
            );
        } else if (phase.equals("COPY_RIGHT")) {
            statusLabel.setText(
                    "Copy leftover → copying remaining right value at index " +
                            step.getWriteIndex()
            );
        } else if (phase.equals("MERGED")) {
            statusLabel.setText(
                    "Merged successfully → range " +
                            step.getLeft() + " to " + step.getRight() +
                            " is sorted now"
            );
        } else if (phase.equals("DONE")) {
            statusLabel.setText("Merge Sort Completed");
        } else {
            statusLabel.setText("Merge Sort Running");
        }
    }

    private void drawMergeDetails(SortingStep step) {

        if (!selectedAlgorithm.equals("Merge Sort")) {
            clearMergeDetails();
            return;
        }

        mergeDetailsPanel.setVisible(true);
        mergeDetailsPanel.setManaged(true);

        mergePhaseLabel.setText("Phase: " + step.getPhase());

        if (step.getLeft() != -1 && step.getRight() != -1) {
            mergeRangeLabel.setText(
                    "Current Range: left = " + step.getLeft() +
                            ", mid = " + step.getMid() +
                            ", right = " + step.getRight() +
                            ", write index = " + step.getWriteIndex()
            );
        } else {
            mergeRangeLabel.setText("Current Range: -");
        }

        drawTempArray(leftTempContainer, step.getLeftTempArray(), step.getLeftPointer(), true);
        drawTempArray(rightTempContainer, step.getRightTempArray(), step.getRightPointer(), false);
    }

    private void clearMergeDetails() {

        if (mergeDetailsPanel != null) {
            mergeDetailsPanel.setVisible(false);
            mergeDetailsPanel.setManaged(false);
        }

        if (leftTempContainer != null) {
            leftTempContainer.getChildren().clear();
        }

        if (rightTempContainer != null) {
            rightTempContainer.getChildren().clear();
        }

        if (mergePhaseLabel != null) {
            mergePhaseLabel.setText("");
        }

        if (mergeRangeLabel != null) {
            mergeRangeLabel.setText("");
        }
    }

    private void drawTempArray(HBox container, int[] tempArray, int pointer, boolean isLeftArray) {

        container.getChildren().clear();

        if (tempArray == null || tempArray.length == 0) {
            Label emptyLabel = new Label("Not created yet");
            emptyLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");
            container.getChildren().add(emptyLabel);
            return;
        }

        for (int index = 0; index < tempArray.length; index++) {

            VBox box = new VBox(3);
            box.setAlignment(Pos.CENTER);

            Label value = new Label(String.valueOf(tempArray[index]));
            value.setAlignment(Pos.CENTER);
            value.setMinSize(34, 28);

            if (index == pointer) {
                value.setStyle(
                        "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-color: #ef233c;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 1;"
                );
            } else if (isLeftArray) {
                value.setStyle(
                        "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-color: #6a4c93;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 1;"
                );
            } else {
                value.setStyle(
                        "-fx-font-size: 13px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-color: #f77f00;" +
                                "-fx-border-color: black;" +
                                "-fx-border-width: 1;"
                );
            }

            Label idx = new Label("t" + index);
            idx.setStyle("-fx-font-size: 10px; -fx-text-fill: black;");

            Label pointerLabel = new Label(index == pointer ? "↑" : " ");
            pointerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: red;");

            box.getChildren().addAll(value, idx, pointerLabel);
            container.getChildren().add(box);
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

            double barHeight = ((double) currentArray[k] / maxValue) * 120;

            Rectangle bar = new Rectangle(barWidth, barHeight);

            if (selectedAlgorithm.equals("Merge Sort")) {

                if (currentVisibleStep != null && k == currentVisibleStep.getWriteIndex()) {
                    bar.setFill(Color.LIMEGREEN);
                } else if (k == compareIndex1 || k == compareIndex2) {
                    bar.setFill(Color.ORANGE);
                } else if (isInsideMergeRange(k)) {
                    bar.setFill(Color.MEDIUMPURPLE);
                } else {
                    bar.setFill(Color.DARKBLUE);
                }

            } else if (selectedAlgorithm.equals("Quick Sort")) {

                if (currentVisibleStep != null && currentVisibleStep.getPhase().equals("DONE")) {
                    bar.setFill(Color.GREEN);
                } else if (currentVisibleStep != null && k == currentVisibleStep.getPivotIndex()) {
                    bar.setFill(Color.RED);
                } else if (currentVisibleStep != null &&
                        (k == currentVisibleStep.getSwapIndex1() || k == currentVisibleStep.getSwapIndex2())) {
                    bar.setFill(Color.ORANGE);
                } else if (k == compareIndex2) {
                    bar.setFill(Color.YELLOW);
                } else if (isInsideQuickSortRange(k)) {
                    bar.setFill(Color.MEDIUMPURPLE);
                } else {
                    bar.setFill(Color.DARKBLUE);
                }

            } else {

                if (k == compareIndex1 || k == compareIndex2) {
                    bar.setFill(Color.ORANGE);
                } else if (isSortedIndex(k, sortedStartIndex)) {
                    bar.setFill(Color.GREEN);
                } else {
                    bar.setFill(Color.DARKBLUE);
                }
            }

            Label indexLabel = new Label("idx " + k);
            indexLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: black;");

            Label pointerLabel = new Label(" ");
            pointerLabel.setMinHeight(18);

            if (selectedAlgorithm.equals("Quick Sort")
                    && currentVisibleStep != null
                    && k == currentVisibleStep.getPivotIndex()) {

                pointerLabel.setText("pivot");
                pointerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: red;");

            } else if (selectedAlgorithm.equals("Quick Sort")
                    && currentVisibleStep != null
                    && k == currentVisibleStep.getJ()) {

                pointerLabel.setText("compare");
                pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: orange;");

            } else if (selectedAlgorithm.equals("Quick Sort")
                    && currentVisibleStep != null
                    && k == currentVisibleStep.getPartitionIndex()) {

                pointerLabel.setText("partition");
                pointerLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: green;");

            } else if (selectedAlgorithm.equals("Quick Sort") && isInsideQuickSortRange(k)) {

                pointerLabel.setText("range");
                pointerLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: purple;");

            } else if (k == compareIndex1) {

                if (selectedAlgorithm.equals("Selection Sort")) {
                    pointerLabel.setText("min ↑");
                } else if (selectedAlgorithm.equals("Insertion Sort")) {
                    pointerLabel.setText("key ↑");
                } else if (selectedAlgorithm.equals("Merge Sort")) {
                    pointerLabel.setText("L ↑");
                } else {
                    pointerLabel.setText("j ↑");
                }

                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: orange;");

            } else if (k == compareIndex2) {

                if (selectedAlgorithm.equals("Selection Sort")) {
                    pointerLabel.setText("check ↑");
                } else if (selectedAlgorithm.equals("Insertion Sort")) {
                    pointerLabel.setText("check ↑");
                } else if (selectedAlgorithm.equals("Merge Sort")) {
                    pointerLabel.setText("R ↑");
                } else {
                    pointerLabel.setText("j+1 ↑");
                }

                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: red;");

            } else if (selectedAlgorithm.equals("Merge Sort")
                    && currentVisibleStep != null
                    && k == currentVisibleStep.getWriteIndex()) {

                pointerLabel.setText("write ↑");
                pointerLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: green;");

            } else if (currentI != -1 && k == currentI && !selectedAlgorithm.equals("Merge Sort")) {

                pointerLabel.setText("i ↑");
                pointerLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: green;");

            } else if (!selectedAlgorithm.equals("Merge Sort") && isSortedIndex(k, sortedStartIndex)) {

                pointerLabel.setText("sorted");
                pointerLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: green;");

            } else if (selectedAlgorithm.equals("Merge Sort") && isInsideMergeRange(k)) {

                pointerLabel.setText("range");
                pointerLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-text-fill: purple;");
            }

            barBox.getChildren().addAll(valueLabel, bar, indexLabel, pointerLabel);
            barContainer.getChildren().add(barBox);
        }
    }

    private boolean isInsideMergeRange(int index) {

        if (currentVisibleStep == null) {
            return false;
        }

        int left = currentVisibleStep.getLeft();
        int right = currentVisibleStep.getRight();

        if (left == -1 || right == -1) {
            return false;
        }

        return index >= left && index <= right;
    }

    private boolean isInsideQuickSortRange(int index) {

        if (currentVisibleStep == null) {
            return false;
        }

        int left = currentVisibleStep.getLeft();
        int right = currentVisibleStep.getRight();

        if (left == -1 || right == -1) {
            return false;
        }

        return index >= left && index <= right;
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
        currentVisibleStep = null;

        iLabel.setText("i = -");
        jLabel.setText("j = -");

        clearMergeDetails();

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

        if (algorithmName.equals("Merge Sort")) {
            return getMergeSortCode();
        }

        if (algorithmName.equals("Quick Sort")) {
            return getQuickSortCode();
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

    private String getMergeSortCode() {

        return """
Merge Sort Logic:

Variables:

left  = current range ka starting index
right = current range ka ending index
mid   = middle index

i = left temp array ka pointer
j = right temp array ka pointer
k = original array me write pointer

Divide:

if (left >= right) {
    return;
}

mid = left + (right - left) / 2;

left part  = left to mid
right part = mid + 1 to right

Merge:

leftSize  = mid - left + 1;
rightSize = right - mid;

for (int x = 0; x < leftSize; x++) {
    leftArray[x] = arr[left + x];
}

for (int y = 0; y < rightSize; y++) {
    rightArray[y] = arr[mid + 1 + y];
}

int i = 0;
int j = 0;
int k = left;

while (i < leftSize && j < rightSize) {

    if (leftArray[i] <= rightArray[j]) {
        arr[k] = leftArray[i];
        i++;
    } else {
        arr[k] = rightArray[j];
        j++;
    }

    k++;
}

while (i < leftSize) {
    arr[k] = leftArray[i];
    i++;
    k++;
}

while (j < rightSize) {
    arr[k] = rightArray[j];
    j++;
    k++;
}
""";
    }

    private String getQuickSortCode() {

        return """
Quick Sort Logic:

Quick Sort divide and conquer algorithm hai.

Main idea:

1. Pivot choose karo
2. Pivot se chhote elements left side lao
3. Pivot se bade elements right side rakho
4. Pivot ko correct position par place karo
5. Left aur right part ko recursively sort karo

Code Logic:

quickSort(arr, low, high) {

    if (low < high) {

        pivotIndex = partition(arr, low, high);

        quickSort(arr, low, pivotIndex - 1);

        quickSort(arr, pivotIndex + 1, high);
    }
}

Partition Logic:

pivot = arr[high];
partitionIndex = low - 1;

for (currentIndex = low; currentIndex < high; currentIndex++) {

    if (arr[currentIndex] < pivot) {

        partitionIndex++;

        swap arr[partitionIndex] and arr[currentIndex];
    }
}

swap arr[partitionIndex + 1] and arr[high];

return partitionIndex + 1;
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
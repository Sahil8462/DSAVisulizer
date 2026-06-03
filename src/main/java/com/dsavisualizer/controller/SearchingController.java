package com.dsavisualizer.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.algorithm.searching.BinarySearch;
import com.dsavisualizer.algorithm.searching.LinearSearch;
import com.dsavisualizer.model.SearchingStep;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SearchingController {

    private TextField arrayInputField;
    private TextField targetInputField;

    private ComboBox<String> algorithmChoiceBox;

    private TextArea algorithmCodeArea;

    private HBox arrayBox;

    private Label messageLabel;
    private Label statusLabel;
    private Label targetLabel;
    private Label currentIndexLabel;
    private Label foundIndexLabel;
    private Label speedValueLabel;

    private Button generateArrayButton;
    private Button startSearchingButton;
    private Button nextStepButton;
    private Button autoPlayButton;
    private Button resetButton;
    private Button backButton;

    private Slider speedSlider;

    private int[] currentArray;

    private List<SearchingStep> steps = new ArrayList<>();
    private int currentStepIndex = 0;

    private Timeline timeline;
    private boolean isAutoPlaying = false;

    public Scene createSearchingScene(Stage stage) {

        Label titleLabel = new Label("Searching Visualizer");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1d3557;");

        Label logicTitle = new Label("Algorithm Logic Code");
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

        VBox leftPanel = new VBox(12);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(25, 10, 10, 25));
        leftPanel.getChildren().addAll(logicTitle, algorithmCodeArea);

        algorithmChoiceBox = new ComboBox<>();
        algorithmChoiceBox.getItems().addAll("Linear Search", "Binary Search");
        algorithmChoiceBox.setValue("Linear Search");
        algorithmChoiceBox.setPrefWidth(250);

        algorithmChoiceBox.setOnAction(e -> {
            stopTimeline();
            steps.clear();
            currentStepIndex = 0;
            autoPlayButton.setText("Auto Play");

            updateAlgorithmLogicCode();

            if ("Binary Search".equals(algorithmChoiceBox.getValue())) {
                messageLabel.setText("Binary Search selected. Array will be sorted automatically when searching starts.");
            } else {
                messageLabel.setText("Linear Search selected. Enter array, target and click Generate Array.");
            }
        });

        Label chooseAlgorithmLabel = new Label("Choose Algorithm:");
        chooseAlgorithmLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        HBox chooseAlgorithmBox = new HBox(12);
        chooseAlgorithmBox.setAlignment(Pos.CENTER);
        chooseAlgorithmBox.getChildren().addAll(chooseAlgorithmLabel, algorithmChoiceBox);

        Label inputLabel = new Label("Enter numbers separated by comma. Max 15 numbers allowed:");
        inputLabel.setStyle("-fx-font-size: 14px;");

        arrayInputField = new TextField();
        arrayInputField.setPromptText("Example: 10,25,8,40,15,30");
        arrayInputField.setPrefWidth(500);
        arrayInputField.setText("10,25,8,40,15,30");

        targetInputField = new TextField();
        targetInputField.setPromptText("Target");
        targetInputField.setPrefWidth(120);
        targetInputField.setText("15");

        HBox inputBox = new HBox(12);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(arrayInputField, targetInputField);

        generateArrayButton = createButton("Generate Array");
        startSearchingButton = createButton("Start Searching");
        nextStepButton = createButton("Next Step");
        autoPlayButton = createButton("Auto Play");
        resetButton = createButton("Reset");
        backButton = createButton("Back");

        generateArrayButton.setOnAction(e -> generateArray());
        startSearchingButton.setOnAction(e -> startSearching());
        nextStepButton.setOnAction(e -> showNextStep());
        autoPlayButton.setOnAction(e -> handleAutoPlay());
        resetButton.setOnAction(e -> resetVisualizer());
        backButton.setOnAction(e -> goBackToHome(stage));

        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(
                generateArrayButton,
                startSearchingButton,
                nextStepButton,
                autoPlayButton,
                resetButton,
                backButton
        );

        speedValueLabel = new Label("Speed: 0.8 sec");
        speedValueLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        speedSlider = new Slider(200, 1500, 800);
        speedSlider.setPrefWidth(300);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(300);

        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double seconds = newVal.doubleValue() / 1000.0;
            speedValueLabel.setText(String.format("Speed: %.1f sec", seconds));

            if (isAutoPlaying) {
                stopTimeline();
                autoPlayButton.setText("Auto Play");
            }
        });

        HBox speedBox = new HBox(15);
        speedBox.setAlignment(Pos.CENTER);
        speedBox.getChildren().addAll(speedValueLabel, speedSlider);

        arrayBox = new HBox(12);
        arrayBox.setAlignment(Pos.CENTER);
        arrayBox.setPadding(new Insets(30));
        arrayBox.setMinHeight(180);
        arrayBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #d9d9d9;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;"
        );

        targetLabel = createInfoLabel("Target: -");
        currentIndexLabel = createInfoLabel("Current Index: -");
        foundIndexLabel = createInfoLabel("Found Index: -");
        statusLabel = createInfoLabel("Status: Waiting");

        HBox infoBox = new HBox(18);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getChildren().addAll(
                targetLabel,
                currentIndexLabel,
                foundIndexLabel,
                statusLabel
        );

        messageLabel = new Label("Select algorithm, enter array and click Generate Array.");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(780);
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
                chooseAlgorithmBox,
                inputLabel,
                inputBox,
                buttonBox,
                speedBox,
                arrayBox,
                infoBox,
                messageLabel
        );

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        root.setLeft(leftPanel);
        root.setCenter(centerPanel);

        updateAlgorithmLogicCode();
        generateDefaultArray();

        return new Scene(root, 1300, 720);
    }

    private void generateArray() {

        stopTimeline();

        try {
            currentArray = parseArrayInput();
            int target = Integer.parseInt(targetInputField.getText().trim());

            steps.clear();
            currentStepIndex = 0;

            SearchingStep step = new SearchingStep(
                    Arrays.copyOf(currentArray, currentArray.length),
                    -1,
                    target,
                    -1,
                    "START",
                    "Array generated successfully. Now click Start Searching."
            );

            showStep(step);

            statusLabel.setText("Status: Array Generated");
            messageLabel.setText("Array generated successfully. Now click Start Searching.");
            autoPlayButton.setText("Auto Play");

        } catch (Exception e) {
            messageLabel.setText("Invalid input. Example array: 10,25,8,40 and target: 40");
        }
    }

    private void startSearching() {

        stopTimeline();

        try {
            currentArray = parseArrayInput();

            int target = Integer.parseInt(targetInputField.getText().trim());
            String selectedAlgorithm = algorithmChoiceBox.getValue();

            if ("Linear Search".equals(selectedAlgorithm)) {

                LinearSearch linearSearch = new LinearSearch();
                steps = linearSearch.generateSteps(currentArray, target);

                currentStepIndex = 0;

                if (!steps.isEmpty()) {
                    showStep(steps.get(currentStepIndex));
                    currentStepIndex++;
                }

                messageLabel.setText("Linear Search started. Click Next Step or Auto Play.");
            }

            else if ("Binary Search".equals(selectedAlgorithm)) {

                int[] sortedArray = Arrays.copyOf(currentArray, currentArray.length);
                Arrays.sort(sortedArray);
                currentArray = sortedArray;

                arrayInputField.setText(arrayToText(currentArray));

                BinarySearch binarySearch = new BinarySearch();
                steps = binarySearch.generateSteps(currentArray, target);

                currentStepIndex = 0;

                if (!steps.isEmpty()) {
                    showStep(steps.get(currentStepIndex));
                    currentStepIndex++;
                }

                messageLabel.setText("Binary Search started. Array sorted automatically for Binary Search.");
            }

        } catch (Exception e) {
            messageLabel.setText("Invalid input. Please generate array again.");
        }
    }

    private void showNextStep() {

        if (steps == null || steps.isEmpty()) {
            messageLabel.setText("First click Start Searching to generate searching steps.");
            return;
        }

        if (currentStepIndex >= steps.size()) {
            messageLabel.setText("Searching completed.");
            stopTimeline();
            autoPlayButton.setText("Auto Play");
            return;
        }

        SearchingStep step = steps.get(currentStepIndex);
        showStep(step);
        currentStepIndex++;
    }

    private void handleAutoPlay() {

        if (steps == null || steps.isEmpty()) {
            messageLabel.setText("First click Start Searching to generate searching steps.");
            return;
        }

        if (isAutoPlaying) {
            stopTimeline();
            autoPlayButton.setText("Auto Play");
            return;
        }

        isAutoPlaying = true;
        autoPlayButton.setText("Pause");

        timeline = new Timeline(
                new KeyFrame(Duration.millis(speedSlider.getValue()), e -> {
                    if (currentStepIndex < steps.size()) {
                        showStep(steps.get(currentStepIndex));
                        currentStepIndex++;
                    } else {
                        stopTimeline();
                        autoPlayButton.setText("Auto Play");
                        messageLabel.setText("Searching completed.");
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void showStep(SearchingStep step) {

        arrayBox.getChildren().clear();

        int[] arr = step.getArrayState();

        for (int i = 0; i < arr.length; i++) {
            VBox elementBox = createArrayElement(arr[i], i, step);
            arrayBox.getChildren().add(elementBox);
        }

        targetLabel.setText("Target: " + step.getTarget());

        if (step.getCurrentIndex() == -1) {
            currentIndexLabel.setText("Current Index: -");
        } else {
            currentIndexLabel.setText("Current Index: " + step.getCurrentIndex());
        }

        if (step.getFoundIndex() == -1) {
            foundIndexLabel.setText("Found Index: -");
        } else {
            foundIndexLabel.setText("Found Index: " + step.getFoundIndex());
        }

        statusLabel.setText("Status: " + step.getPhase());
        messageLabel.setText(step.getMessage());
    }

    private VBox createArrayElement(int value, int index, SearchingStep step) {

        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setAlignment(Pos.CENTER);
        valueLabel.setPrefSize(65, 65);
        valueLabel.setStyle(getElementStyle(index, step));

        Label indexLabel = new Label("Index " + index);
        indexLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #444444;");

        Label pointerLabel = new Label(" ");
        pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold;");

        if (index == step.getFoundIndex() && "FOUND".equals(step.getPhase())) {
            pointerLabel.setText("Found");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #2a9d8f;");
        }

        else if (index == step.getCurrentIndex() && "CHECKING".equals(step.getPhase())) {
            if (step.isBinarySearchStep()) {
                pointerLabel.setText("Mid");
                pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6a4c93;");
            } else {
                pointerLabel.setText("Checking");
                pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #d62828;");
            }
        }

        else if (index == step.getCurrentIndex() && "NOT_MATCHED".equals(step.getPhase())) {
            pointerLabel.setText("Not Match");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #e63946;");
        }

        else if (index == step.getLow() && index == step.getMid() && index == step.getHigh()) {
            pointerLabel.setText("L M H");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6a4c93;");
        }

        else if (index == step.getLow() && index == step.getMid()) {
            pointerLabel.setText("L M");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6a4c93;");
        }

        else if (index == step.getMid() && index == step.getHigh()) {
            pointerLabel.setText("M H");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6a4c93;");
        }

        else if (index == step.getLow()) {
            pointerLabel.setText("Low");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #f77f00;");
        }

        else if (index == step.getMid()) {
            pointerLabel.setText("Mid");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #6a4c93;");
        }

        else if (index == step.getHigh()) {
            pointerLabel.setText("High");
            pointerLabel.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #0077b6;");
        }

        VBox box = new VBox(7);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(valueLabel, indexLabel, pointerLabel);

        return box;
    }

    private String getElementStyle(int index, SearchingStep step) {

        String baseStyle =
                "-fx-font-size: 19px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-border-radius: 12;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-width: 2;" +
                        "-fx-alignment: center;";

        if ("FOUND".equals(step.getPhase()) && index == step.getFoundIndex()) {
            return baseStyle +
                    "-fx-background-color: #2a9d8f;" +
                    "-fx-border-color: #1b4332;" +
                    "-fx-text-fill: white;";
        }

        if ("CHECKING".equals(step.getPhase()) && index == step.getCurrentIndex()) {
            return baseStyle +
                    "-fx-background-color: #ffd166;" +
                    "-fx-border-color: #f77f00;" +
                    "-fx-text-fill: black;";
        }

        if ("MOVE_LEFT".equals(step.getPhase()) && index == step.getMid()) {
            return baseStyle +
                    "-fx-background-color: #ffb703;" +
                    "-fx-border-color: #fb8500;" +
                    "-fx-text-fill: black;";
        }

        if ("MOVE_RIGHT".equals(step.getPhase()) && index == step.getMid()) {
            return baseStyle +
                    "-fx-background-color: #ffb703;" +
                    "-fx-border-color: #fb8500;" +
                    "-fx-text-fill: black;";
        }

        if ("RANGE_UPDATED".equals(step.getPhase()) && step.hasLowPointer() && step.hasHighPointer()) {

            if (index >= step.getLow() && index <= step.getHigh()) {
                return baseStyle +
                        "-fx-background-color: #a8dadc;" +
                        "-fx-border-color: #457b9d;" +
                        "-fx-text-fill: #1d3557;";
            }

            return baseStyle +
                    "-fx-background-color: #adb5bd;" +
                    "-fx-border-color: #6c757d;" +
                    "-fx-text-fill: white;";
        }

        if ("NOT_MATCHED".equals(step.getPhase()) && index <= step.getCurrentIndex()) {
            return baseStyle +
                    "-fx-background-color: #e63946;" +
                    "-fx-border-color: #9d0208;" +
                    "-fx-text-fill: white;";
        }

        if ("CHECKING".equals(step.getPhase()) && step.isLinearSearchStep() && index < step.getCurrentIndex()) {
            return baseStyle +
                    "-fx-background-color: #e63946;" +
                    "-fx-border-color: #9d0208;" +
                    "-fx-text-fill: white;";
        }

        if ("FOUND".equals(step.getPhase()) && step.isLinearSearchStep() && index < step.getFoundIndex()) {
            return baseStyle +
                    "-fx-background-color: #e63946;" +
                    "-fx-border-color: #9d0208;" +
                    "-fx-text-fill: white;";
        }

        if ("NOT_FOUND".equals(step.getPhase())) {
            return baseStyle +
                    "-fx-background-color: #adb5bd;" +
                    "-fx-border-color: #6c757d;" +
                    "-fx-text-fill: white;";
        }

        if (step.isBinarySearchStep() && step.hasLowPointer() && step.hasHighPointer()) {

            if (step.isIndexOutsideBinaryRange(index)) {
                return baseStyle +
                        "-fx-background-color: #adb5bd;" +
                        "-fx-border-color: #6c757d;" +
                        "-fx-text-fill: white;";
            }

            if (index == step.getLow()) {
                return baseStyle +
                        "-fx-background-color: #f4a261;" +
                        "-fx-border-color: #e76f51;" +
                        "-fx-text-fill: black;";
            }

            if (index == step.getHigh()) {
                return baseStyle +
                        "-fx-background-color: #90e0ef;" +
                        "-fx-border-color: #0077b6;" +
                        "-fx-text-fill: black;";
            }
        }

        return baseStyle +
                "-fx-background-color: #a8dadc;" +
                "-fx-border-color: #457b9d;" +
                "-fx-text-fill: #1d3557;";
    }

    private int[] parseArrayInput() {

        String input = arrayInputField.getText().trim();

        if (input.isEmpty()) {
            throw new IllegalArgumentException("Array input is empty");
        }

        String[] parts = input.split(",");

        if (parts.length > 15) {
            throw new IllegalArgumentException("Max 15 numbers allowed");
        }

        int[] arr = new int[parts.length];

        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i].trim());
        }

        return arr;
    }

    private String arrayToText(int[] arr) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i]);

            if (i < arr.length - 1) {
                builder.append(",");
            }
        }

        return builder.toString();
    }

    private void updateAlgorithmLogicCode() {

        String selectedAlgorithm = algorithmChoiceBox.getValue();

        if ("Linear Search".equals(selectedAlgorithm)) {

            algorithmCodeArea.setText(
                    "Linear Search Logic:\n\n" +
                            "int linearSearch(int arr[], int n, int target) {\n\n" +
                            "    for (int i = 0; i < n; i++) {\n\n" +
                            "        if (arr[i] == target) {\n" +
                            "            return i;\n" +
                            "        }\n\n" +
                            "    }\n\n" +
                            "    return -1;\n" +
                            "}\n\n\n" +
                            "Working:\n\n" +
                            "1. Start from index 0.\n" +
                            "2. Compare every element with target.\n" +
                            "3. If match found, return index.\n" +
                            "4. If loop ends, target is not found.\n\n" +
                            "Time Complexity: O(n)\n" +
                            "Space Complexity: O(1)"
            );

        } else {

            algorithmCodeArea.setText(
                    "Binary Search Logic:\n\n" +
                            "int binarySearch(int arr[], int n, int target) {\n\n" +
                            "    int low = 0;\n" +
                            "    int high = n - 1;\n\n" +
                            "    while (low <= high) {\n\n" +
                            "        int mid = low + (high - low) / 2;\n\n" +
                            "        if (arr[mid] == target) {\n" +
                            "            return mid;\n" +
                            "        }\n\n" +
                            "        if (target < arr[mid]) {\n" +
                            "            high = mid - 1;\n" +
                            "        }\n" +
                            "        else {\n" +
                            "            low = mid + 1;\n" +
                            "        }\n" +
                            "    }\n\n" +
                            "    return -1;\n" +
                            "}\n\n\n" +
                            "Working:\n\n" +
                            "1. Binary Search needs sorted array.\n" +
                            "2. Find mid index.\n" +
                            "3. If target is smaller, move left.\n" +
                            "4. If target is greater, move right.\n" +
                            "5. Repeat until found or range becomes empty.\n\n" +
                            "Time Complexity: O(log n)\n" +
                            "Space Complexity: O(1)"
            );
        }
    }

    private void generateDefaultArray() {

        currentArray = new int[]{10, 25, 8, 40, 15, 30};

        SearchingStep defaultStep = new SearchingStep(
                Arrays.copyOf(currentArray, currentArray.length),
                -1,
                15,
                -1,
                "START",
                "Select algorithm, enter array and click Generate Array."
        );

        showStep(defaultStep);

        targetLabel.setText("Target: 15");
        currentIndexLabel.setText("Current Index: -");
        foundIndexLabel.setText("Found Index: -");
        statusLabel.setText("Status: Waiting");
        messageLabel.setText("Select algorithm, enter array and click Generate Array.");
    }

    private Button createButton(String text) {

        Button button = new Button(text);

        button.setPrefSize(135, 42);
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

    private void resetVisualizer() {

        stopTimeline();

        steps.clear();
        currentStepIndex = 0;

        arrayInputField.setText("10,25,8,40,15,30");
        targetInputField.setText("15");
        algorithmChoiceBox.setValue("Linear Search");

        updateAlgorithmLogicCode();
        generateDefaultArray();

        autoPlayButton.setText("Auto Play");
    }

    private void stopTimeline() {

        if (timeline != null) {
            timeline.stop();
        }

        isAutoPlaying = false;
    }

    private void goBackToHome(Stage stage) {

        stopTimeline();

        HomeController homeController = new HomeController();
        Scene homeScene = homeController.createHomeScene(stage);
        stage.setScene(homeScene);
    }
}
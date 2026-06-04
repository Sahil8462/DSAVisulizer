package com.dsavisualizer.model;

public class TreeStep {

    private int[] treeValues;

    private int activeValue;
    private int targetValue;

    private String operation;
    private String message;
    private String traversalOutput;

    private String codeMessage;
    private int codeLine;

    private int loopStep;
    private int currentNodeValue;
    private String compareMessage;
    private String directionMessage;
    private String pathMessage;

    public TreeStep(
            int[] treeValues,
            int activeValue,
            int targetValue,
            String operation,
            String message,
            String traversalOutput,
            String codeMessage,
            int codeLine,
            int loopStep,
            int currentNodeValue,
            String compareMessage,
            String directionMessage,
            String pathMessage
    ) {
        this.treeValues = treeValues;
        this.activeValue = activeValue;
        this.targetValue = targetValue;
        this.operation = operation;
        this.message = message;
        this.traversalOutput = traversalOutput;
        this.codeMessage = codeMessage;
        this.codeLine = codeLine;
        this.loopStep = loopStep;
        this.currentNodeValue = currentNodeValue;
        this.compareMessage = compareMessage;
        this.directionMessage = directionMessage;
        this.pathMessage = pathMessage;
    }

    public int[] getTreeValues() {
        return treeValues;
    }

    public int getActiveValue() {
        return activeValue;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public String getOperation() {
        return operation;
    }

    public String getMessage() {
        return message;
    }

    public String getTraversalOutput() {
        return traversalOutput;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public int getCodeLine() {
        return codeLine;
    }

    public int getLoopStep() {
        return loopStep;
    }

    public int getCurrentNodeValue() {
        return currentNodeValue;
    }

    public String getCompareMessage() {
        return compareMessage;
    }

    public String getDirectionMessage() {
        return directionMessage;
    }

    public String getPathMessage() {
        return pathMessage;
    }
}
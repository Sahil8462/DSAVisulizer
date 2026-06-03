package com.dsavisualizer.model;

import java.util.ArrayList;
import java.util.List;

public class StackStep {

    private List<Integer> stackState;

    private String operation;
    private int value;
    private int topIndex;

    private String phase;
    private String message;

    public StackStep(
            List<Integer> stackState,
            String operation,
            int value,
            int topIndex,
            String phase,
            String message
    ) {
        this.stackState = new ArrayList<>(stackState);
        this.operation = operation;
        this.value = value;
        this.topIndex = topIndex;
        this.phase = phase;
        this.message = message;
    }

    public List<Integer> getStackState() {
        return stackState;
    }

    public void setStackState(List<Integer> stackState) {
        this.stackState = new ArrayList<>(stackState);
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getTopIndex() {
        return topIndex;
    }

    public void setTopIndex(int topIndex) {
        this.topIndex = topIndex;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStackEmpty() {
        return stackState == null || stackState.isEmpty();
    }

    public boolean hasTopElement() {
        return topIndex != -1;
    }

    public int getStackSize() {
        if (stackState == null) {
            return 0;
        }

        return stackState.size();
    }
}
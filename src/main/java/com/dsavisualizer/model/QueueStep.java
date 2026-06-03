package com.dsavisualizer.model;

import java.util.ArrayList;
import java.util.List;

public class QueueStep {

    private List<Integer> queueState;

    private String operation;
    private int value;

    private int frontIndex;
    private int rearIndex;

    private String phase;
    private String message;

    public QueueStep(
            List<Integer> queueState,
            String operation,
            int value,
            int frontIndex,
            int rearIndex,
            String phase,
            String message
    ) {
        this.queueState = new ArrayList<>(queueState);
        this.operation = operation;
        this.value = value;
        this.frontIndex = frontIndex;
        this.rearIndex = rearIndex;
        this.phase = phase;
        this.message = message;
    }

    public List<Integer> getQueueState() {
        return queueState;
    }

    public void setQueueState(List<Integer> queueState) {
        this.queueState = new ArrayList<>(queueState);
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

    public int getFrontIndex() {
        return frontIndex;
    }

    public void setFrontIndex(int frontIndex) {
        this.frontIndex = frontIndex;
    }

    public int getRearIndex() {
        return rearIndex;
    }

    public void setRearIndex(int rearIndex) {
        this.rearIndex = rearIndex;
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

    public boolean isQueueEmpty() {
        return queueState == null || queueState.isEmpty();
    }

    public boolean hasFrontElement() {
        return frontIndex != -1;
    }

    public boolean hasRearElement() {
        return rearIndex != -1;
    }

    public int getQueueSize() {
        if (queueState == null) {
            return 0;
        }

        return queueState.size();
    }
}
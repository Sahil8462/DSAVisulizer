package com.dsavisualizer.model;

public class LinkedListStep {

    private int[] nodeValues;

    private int currentIndex;
    private int activeArrowIndex;

    private int headIndex;
    private int tailIndex;

    private int newNodeValue;
    private int insertPosition;

    private boolean doublyLinkedList;

    private String operation;
    private String message;
    private String pointerMessage;

    public LinkedListStep(
            int[] nodeValues,
            int currentIndex,
            int headIndex,
            int tailIndex,
            String operation,
            String message
    ) {
        this.nodeValues = nodeValues;
        this.currentIndex = currentIndex;
        this.activeArrowIndex = currentIndex;
        this.headIndex = headIndex;
        this.tailIndex = tailIndex;
        this.newNodeValue = -1;
        this.insertPosition = -1;
        this.doublyLinkedList = false;
        this.operation = operation;
        this.message = message;
        this.pointerMessage = "";
    }

    public LinkedListStep(
            int[] nodeValues,
            int currentIndex,
            int activeArrowIndex,
            int headIndex,
            int tailIndex,
            int newNodeValue,
            int insertPosition,
            boolean doublyLinkedList,
            String operation,
            String message,
            String pointerMessage
    ) {
        this.nodeValues = nodeValues;
        this.currentIndex = currentIndex;
        this.activeArrowIndex = activeArrowIndex;
        this.headIndex = headIndex;
        this.tailIndex = tailIndex;
        this.newNodeValue = newNodeValue;
        this.insertPosition = insertPosition;
        this.doublyLinkedList = doublyLinkedList;
        this.operation = operation;
        this.message = message;
        this.pointerMessage = pointerMessage;
    }

    public int[] getNodeValues() {
        return nodeValues;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getActiveArrowIndex() {
        return activeArrowIndex;
    }

    public int getHeadIndex() {
        return headIndex;
    }

    public int getTailIndex() {
        return tailIndex;
    }

    public int getNewNodeValue() {
        return newNodeValue;
    }

    public int getInsertPosition() {
        return insertPosition;
    }

    public boolean isDoublyLinkedList() {
        return doublyLinkedList;
    }

    public String getOperation() {
        return operation;
    }

    public String getMessage() {
        return message;
    }

    public String getPointerMessage() {
        return pointerMessage;
    }
}
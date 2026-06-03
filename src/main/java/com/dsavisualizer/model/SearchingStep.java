package com.dsavisualizer.model;

public class SearchingStep {

    private int[] arrayState;

    private int currentIndex;
    private int target;
    private int foundIndex;

    private String phase;
    private String message;

    private int low;
    private int mid;
    private int high;

    public SearchingStep(
            int[] arrayState,
            int currentIndex,
            int target,
            int foundIndex,
            String phase,
            String message
    ) {
        this.arrayState = arrayState;
        this.currentIndex = currentIndex;
        this.target = target;
        this.foundIndex = foundIndex;
        this.phase = phase;
        this.message = message;

        this.low = -1;
        this.mid = -1;
        this.high = -1;
    }

    public SearchingStep(
            int[] arrayState,
            int currentIndex,
            int target,
            int foundIndex,
            String phase,
            String message,
            int low,
            int mid,
            int high
    ) {
        this.arrayState = arrayState;
        this.currentIndex = currentIndex;
        this.target = target;
        this.foundIndex = foundIndex;
        this.phase = phase;
        this.message = message;

        this.low = low;
        this.mid = mid;
        this.high = high;
    }

    public int[] getArrayState() {
        return arrayState;
    }

    public void setArrayState(int[] arrayState) {
        this.arrayState = arrayState;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getFoundIndex() {
        return foundIndex;
    }

    public void setFoundIndex(int foundIndex) {
        this.foundIndex = foundIndex;
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

    public int getLow() {
        return low;
    }

    public void setLow(int low) {
        this.low = low;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public boolean hasLowPointer() {
        return low != -1;
    }

    public boolean hasMidPointer() {
        return mid != -1;
    }

    public boolean hasHighPointer() {
        return high != -1;
    }

    public boolean isFound() {
        return foundIndex != -1;
    }

    public boolean isLinearSearchStep() {
        return low == -1 && mid == -1 && high == -1;
    }

    public boolean isBinarySearchStep() {
        return low != -1 || mid != -1 || high != -1;
    }

    public boolean isIndexOutsideBinaryRange(int index) {
        if (!isBinarySearchStep()) {
            return false;
        }

        if (low == -1 || high == -1) {
            return false;
        }

        return index < low || index > high;
    }
}
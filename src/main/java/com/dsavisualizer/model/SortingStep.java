package com.dsavisualizer.model;

public class SortingStep {

    private int[] arrayState;
    private int i;
    private int j;
    private boolean swapped;
    private int sortedStartIndex;

    public SortingStep(int[] arrayState, int i, int j, boolean swapped, int sortedStartIndex) {
        this.arrayState = arrayState;
        this.i = i;
        this.j = j;
        this.swapped = swapped;
        this.sortedStartIndex = sortedStartIndex;
    }

    public int[] getArrayState() {
        return arrayState;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public boolean isSwapped() {
        return swapped;
    }

    public int getSortedStartIndex() {
        return sortedStartIndex;
    }
}
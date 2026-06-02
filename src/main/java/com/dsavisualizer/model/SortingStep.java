package com.dsavisualizer.model;

public class SortingStep {

    private int[] arrayState;
    private int i;
    private int j;
    private boolean swapped;
    private int sortedStartIndex;

    private int left;
    private int mid;
    private int right;
    private int k;

    private String phase;
    private int[] leftTempArray;
    private int[] rightTempArray;
    private int leftPointer;
    private int rightPointer;
    private int writeIndex;

    public SortingStep(int[] arrayState, int i, int j, boolean swapped, int sortedStartIndex) {
        this.arrayState = arrayState;
        this.i = i;
        this.j = j;
        this.swapped = swapped;
        this.sortedStartIndex = sortedStartIndex;

        this.left = -1;
        this.mid = -1;
        this.right = -1;
        this.k = -1;

        this.phase = "";
        this.leftTempArray = null;
        this.rightTempArray = null;
        this.leftPointer = -1;
        this.rightPointer = -1;
        this.writeIndex = -1;
    }

    public SortingStep(
            int[] arrayState,
            int i,
            int j,
            boolean swapped,
            int sortedStartIndex,
            int left,
            int mid,
            int right,
            int k,
            String phase,
            int[] leftTempArray,
            int[] rightTempArray,
            int leftPointer,
            int rightPointer,
            int writeIndex
    ) {
        this.arrayState = arrayState;
        this.i = i;
        this.j = j;
        this.swapped = swapped;
        this.sortedStartIndex = sortedStartIndex;

        this.left = left;
        this.mid = mid;
        this.right = right;
        this.k = k;

        this.phase = phase;
        this.leftTempArray = leftTempArray;
        this.rightTempArray = rightTempArray;
        this.leftPointer = leftPointer;
        this.rightPointer = rightPointer;
        this.writeIndex = writeIndex;
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

    public int getLeft() {
        return left;
    }

    public int getMid() {
        return mid;
    }

    public int getRight() {
        return right;
    }

    public int getK() {
        return k;
    }

    public String getPhase() {
        return phase;
    }

    public int[] getLeftTempArray() {
        return leftTempArray;
    }

    public int[] getRightTempArray() {
        return rightTempArray;
    }

    public int getLeftPointer() {
        return leftPointer;
    }

    public int getRightPointer() {
        return rightPointer;
    }

    public int getWriteIndex() {
        return writeIndex;
    }
}
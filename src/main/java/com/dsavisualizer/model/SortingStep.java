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

    private int pivotIndex;
    private int partitionIndex;
    private int swapIndex1;
    private int swapIndex2;

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

        this.pivotIndex = -1;
        this.partitionIndex = -1;
        this.swapIndex1 = -1;
        this.swapIndex2 = -1;
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

        this.pivotIndex = -1;
        this.partitionIndex = -1;
        this.swapIndex1 = -1;
        this.swapIndex2 = -1;
    }

    public SortingStep(
            int[] arrayState,
            int left,
            int right,
            int pivotIndex,
            int comparingIndex,
            int partitionIndex,
            int swapIndex1,
            int swapIndex2,
            boolean swapped,
            String phase
    ) {
        this.arrayState = arrayState;

        this.i = partitionIndex;
        this.j = comparingIndex;
        this.swapped = swapped;
        this.sortedStartIndex = -1;

        this.left = left;
        this.mid = -1;
        this.right = right;
        this.k = -1;

        this.phase = phase;
        this.leftTempArray = null;
        this.rightTempArray = null;
        this.leftPointer = -1;
        this.rightPointer = -1;
        this.writeIndex = -1;

        this.pivotIndex = pivotIndex;
        this.partitionIndex = partitionIndex;
        this.swapIndex1 = swapIndex1;
        this.swapIndex2 = swapIndex2;
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

    public int getPivotIndex() {
        return pivotIndex;
    }

    public int getPartitionIndex() {
        return partitionIndex;
    }

    public int getSwapIndex1() {
        return swapIndex1;
    }

    public int getSwapIndex2() {
        return swapIndex2;
    }
}
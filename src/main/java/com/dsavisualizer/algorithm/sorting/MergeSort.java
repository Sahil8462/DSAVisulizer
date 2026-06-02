package com.dsavisualizer.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SortingStep;

public class MergeSort {

    private List<SortingStep> steps;

    public List<SortingStep> generateSteps(int[] inputArray) {

        steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);

        addMergeStep(
                arr,
                -1,
                -1,
                false,
                -1,
                -1,
                -1,
                -1,
                -1,
                "START",
                null,
                null,
                -1,
                -1,
                -1
        );

        mergeSort(arr, 0, arr.length - 1);

        addMergeStep(
                arr,
                -1,
                -1,
                false,
                arr.length - 1,
                0,
                -1,
                arr.length - 1,
                -1,
                "DONE",
                null,
                null,
                -1,
                -1,
                -1
        );

        return steps;
    }

    private void mergeSort(int[] arr, int left, int right) {

        if (left >= right) {

            addMergeStep(
                    arr,
                    left,
                    right,
                    false,
                    -1,
                    left,
                    left,
                    right,
                    -1,
                    "SINGLE",
                    null,
                    null,
                    -1,
                    -1,
                    -1
            );

            return;
        }

        int mid = left + (right - left) / 2;

        addMergeStep(
                arr,
                left,
                right,
                false,
                -1,
                left,
                mid,
                right,
                -1,
                "DIVIDE",
                null,
                null,
                -1,
                -1,
                -1
        );

        mergeSort(arr, left, mid);
        mergeSort(arr, mid + 1, right);

        merge(arr, left, mid, right);
    }

    private void merge(int[] arr, int left, int mid, int right) {

        int leftSize = mid - left + 1;
        int rightSize = right - mid;

        int[] leftArray = new int[leftSize];
        int[] rightArray = new int[rightSize];

        for (int x = 0; x < leftSize; x++) {
            leftArray[x] = arr[left + x];
        }

        for (int y = 0; y < rightSize; y++) {
            rightArray[y] = arr[mid + 1 + y];
        }

        addMergeStep(
                arr,
                left,
                right,
                false,
                -1,
                left,
                mid,
                right,
                left,
                "MERGE_START",
                Arrays.copyOf(leftArray, leftArray.length),
                Arrays.copyOf(rightArray, rightArray.length),
                -1,
                -1,
                left
        );

        int i = 0;
        int j = 0;
        int k = left;

        while (i < leftSize && j < rightSize) {

            addMergeStep(
                    arr,
                    left + i,
                    mid + 1 + j,
                    false,
                    -1,
                    left,
                    mid,
                    right,
                    k,
                    "COMPARE",
                    Arrays.copyOf(leftArray, leftArray.length),
                    Arrays.copyOf(rightArray, rightArray.length),
                    i,
                    j,
                    k
            );

            if (leftArray[i] <= rightArray[j]) {

                arr[k] = leftArray[i];

                addMergeStep(
                        arr,
                        left + i,
                        -1,
                        true,
                        k,
                        left,
                        mid,
                        right,
                        k,
                        "WRITE_LEFT",
                        Arrays.copyOf(leftArray, leftArray.length),
                        Arrays.copyOf(rightArray, rightArray.length),
                        i,
                        j,
                        k
                );

                i++;

            } else {

                arr[k] = rightArray[j];

                addMergeStep(
                        arr,
                        -1,
                        mid + 1 + j,
                        true,
                        k,
                        left,
                        mid,
                        right,
                        k,
                        "WRITE_RIGHT",
                        Arrays.copyOf(leftArray, leftArray.length),
                        Arrays.copyOf(rightArray, rightArray.length),
                        i,
                        j,
                        k
                );

                j++;
            }

            k++;
        }

        while (i < leftSize) {

            arr[k] = leftArray[i];

            addMergeStep(
                    arr,
                    left + i,
                    -1,
                    true,
                    k,
                    left,
                    mid,
                    right,
                    k,
                    "COPY_LEFT",
                    Arrays.copyOf(leftArray, leftArray.length),
                    Arrays.copyOf(rightArray, rightArray.length),
                    i,
                    -1,
                    k
            );

            i++;
            k++;
        }

        while (j < rightSize) {

            arr[k] = rightArray[j];

            addMergeStep(
                    arr,
                    -1,
                    mid + 1 + j,
                    true,
                    k,
                    left,
                    mid,
                    right,
                    k,
                    "COPY_RIGHT",
                    Arrays.copyOf(leftArray, leftArray.length),
                    Arrays.copyOf(rightArray, rightArray.length),
                    -1,
                    j,
                    k
            );

            j++;
            k++;
        }

        addMergeStep(
                arr,
                left,
                right,
                false,
                right,
                left,
                mid,
                right,
                -1,
                "MERGED",
                null,
                null,
                -1,
                -1,
                -1
        );
    }

    private void addMergeStep(
            int[] arr,
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
        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                i,
                j,
                swapped,
                sortedStartIndex,
                left,
                mid,
                right,
                k,
                phase,
                leftTempArray,
                rightTempArray,
                leftPointer,
                rightPointer,
                writeIndex
        ));
    }
}
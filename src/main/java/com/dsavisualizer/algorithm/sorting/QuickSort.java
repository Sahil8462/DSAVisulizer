package com.dsavisualizer.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SortingStep;

public class QuickSort {

    private List<SortingStep> steps;

    public List<SortingStep> generateSteps(int[] inputArray) {

        steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);

        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                0,
                arr.length - 1,
                -1,
                -1,
                -1,
                -1,
                -1,
                false,
                "START"
        ));

        quickSort(arr, 0, arr.length - 1);

        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                -1,
                false,
                "DONE"
        ));

        return steps;
    }

    private void quickSort(int[] arr, int low, int high) {

        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                low,
                high,
                -1,
                -1,
                -1,
                -1,
                -1,
                false,
                "CALL"
        ));

        if (low < high) {

            int pivotIndex = partition(arr, low, high);

            steps.add(new SortingStep(
                    Arrays.copyOf(arr, arr.length),
                    low,
                    high,
                    pivotIndex,
                    -1,
                    -1,
                    -1,
                    -1,
                    false,
                    "PIVOT_FIXED"
            ));

            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);

        } else {

            if (low == high) {
                steps.add(new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        low,
                        high,
                        low,
                        -1,
                        -1,
                        -1,
                        -1,
                        false,
                        "SINGLE"
                ));
            }
        }
    }

    private int partition(int[] arr, int low, int high) {

        int pivot = arr[high];
        int partitionIndex = low - 1;

        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                low,
                high,
                high,
                -1,
                partitionIndex,
                -1,
                -1,
                false,
                "PIVOT_SELECTED"
        ));

        for (int currentIndex = low; currentIndex < high; currentIndex++) {

            steps.add(new SortingStep(
                    Arrays.copyOf(arr, arr.length),
                    low,
                    high,
                    high,
                    currentIndex,
                    partitionIndex,
                    -1,
                    -1,
                    false,
                    "COMPARE"
            ));

            if (arr[currentIndex] < pivot) {

                partitionIndex++;

                steps.add(new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        low,
                        high,
                        high,
                        currentIndex,
                        partitionIndex,
                        partitionIndex,
                        currentIndex,
                        true,
                        "SWAP_BEFORE"
                ));

                int temp = arr[partitionIndex];
                arr[partitionIndex] = arr[currentIndex];
                arr[currentIndex] = temp;

                steps.add(new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        low,
                        high,
                        high,
                        currentIndex,
                        partitionIndex,
                        partitionIndex,
                        currentIndex,
                        true,
                        "SWAP_AFTER"
                ));
            }
        }

        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                low,
                high,
                high,
                -1,
                partitionIndex,
                partitionIndex + 1,
                high,
                true,
                "PIVOT_SWAP_BEFORE"
        ));

        int temp = arr[partitionIndex + 1];
        arr[partitionIndex + 1] = arr[high];
        arr[high] = temp;

        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                low,
                high,
                partitionIndex + 1,
                -1,
                partitionIndex + 1,
                partitionIndex + 1,
                high,
                true,
                "PIVOT_SWAP_AFTER"
        ));

        return partitionIndex + 1;
    }
}
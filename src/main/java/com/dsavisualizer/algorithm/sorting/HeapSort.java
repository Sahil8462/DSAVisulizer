package com.dsavisualizer.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SortingStep;

public class HeapSort {

    public List<SortingStep> generateSteps(int[] inputArray) {

        List<SortingStep> steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);
        int n = arr.length;

        steps.add(
                new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        -1,
                        false,
                        n,
                        "START"
                )
        );

        // Step 1: Build Max Heap
        for (int rootIndex = n / 2 - 1; rootIndex >= 0; rootIndex--) {

            steps.add(
                    new SortingStep(
                            Arrays.copyOf(arr, arr.length),
                            rootIndex,
                            -1,
                            false,
                            n,
                            "BUILD_HEAP"
                    )
            );

            heapify(arr, n, rootIndex, steps);
        }

        // Step 2: Extract largest element one by one
        for (int lastIndex = n - 1; lastIndex > 0; lastIndex--) {

            steps.add(
                    new SortingStep(
                            Arrays.copyOf(arr, arr.length),
                            0,
                            lastIndex,
                            false,
                            lastIndex + 1,
                            "ROOT_MAX"
                    )
            );

            int temp = arr[0];
            arr[0] = arr[lastIndex];
            arr[lastIndex] = temp;

            steps.add(
                    new SortingStep(
                            Arrays.copyOf(arr, arr.length),
                            0,
                            lastIndex,
                            true,
                            lastIndex,
                            "ROOT_SWAP"
                    )
            );

            heapify(arr, lastIndex, 0, steps);
        }

        steps.add(
                new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        -1,
                        false,
                        0,
                        "DONE"
                )
        );

        return steps;
    }

    private void heapify(int[] arr, int heapSize, int rootIndex, List<SortingStep> steps) {

        int largestIndex = rootIndex;

        int leftChildIndex = 2 * rootIndex + 1;
        int rightChildIndex = 2 * rootIndex + 2;

        if (leftChildIndex < heapSize) {

            steps.add(
                    new SortingStep(
                            Arrays.copyOf(arr, arr.length),
                            rootIndex,
                            leftChildIndex,
                            false,
                            heapSize,
                            "COMPARE_LEFT"
                    )
            );

            if (arr[leftChildIndex] > arr[largestIndex]) {
                largestIndex = leftChildIndex;
            }
        }

        if (rightChildIndex < heapSize) {

            steps.add(
                    new SortingStep(
                            Arrays.copyOf(arr, arr.length),
                            largestIndex,
                            rightChildIndex,
                            false,
                            heapSize,
                            "COMPARE_RIGHT"
                    )
            );

            if (arr[rightChildIndex] > arr[largestIndex]) {
                largestIndex = rightChildIndex;
            }
        }

        if (largestIndex != rootIndex) {

            int temp = arr[rootIndex];
            arr[rootIndex] = arr[largestIndex];
            arr[largestIndex] = temp;

            steps.add(
                    new SortingStep(
                            Arrays.copyOf(arr, arr.length),
                            rootIndex,
                            largestIndex,
                            true,
                            heapSize,
                            "HEAPIFY_SWAP"
                    )
            );

            heapify(arr, heapSize, largestIndex, steps);
        }
    }
}
package com.dsavisualizer.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SortingStep;

public class InsertionSort {

    public List<SortingStep> generateSteps(int[] inputArray) {

        List<SortingStep> steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);
        int n = arr.length;

        // Initial state
        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                -1,
                -1,
                false,
                0
        ));

        for (int i = 1; i < n; i++) {

            int key = arr[i];
            int j = i - 1;

            // Current key picked
            steps.add(new SortingStep(
                    Arrays.copyOf(arr, arr.length),
                    i,
                    j,
                    false,
                    i
            ));

            while (j >= 0 && arr[j] > key) {

                arr[j + 1] = arr[j];

                // Shift right
                steps.add(new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        i,
                        j,
                        true,
                        i
                ));

                j--;
            }

            arr[j + 1] = key;

            // Key inserted at correct position
            steps.add(new SortingStep(
                    Arrays.copyOf(arr, arr.length),
                    i,
                    j + 1,
                    false,
                    i
            ));
        }

        // Final sorted state
        steps.add(new SortingStep(
                Arrays.copyOf(arr, arr.length),
                -1,
                -1,
                false,
                n - 1
        ));

        return steps;
    }
}
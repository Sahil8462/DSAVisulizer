package com.dsavisualizer.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SortingStep;

public class BubbleSort {

    public List<SortingStep> generateSteps(int[] inputArray) {

        List<SortingStep> steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            boolean anySwapHappened = false;

            for (int j = 0; j < n - i - 1; j++) {

                boolean swapped = false;

                if (arr[j] > arr[j + 1]) {

                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    swapped = true;
                    anySwapHappened = true;
                }

                int sortedStartIndex = n - i;

                steps.add(
                        new SortingStep(
                                Arrays.copyOf(arr, arr.length),
                                i,
                                j,
                                swapped,
                                sortedStartIndex
                        )
                );
            }

            // Agar ek full pass me koi swap nahi hua,
            // iska matlab array already sorted hai.
            if (!anySwapHappened) {
                break;
            }
        }

        // Final sorted array step
        steps.add(
                new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        -1,
                        false,
                        0
                )
        );

        return steps;
    }
}
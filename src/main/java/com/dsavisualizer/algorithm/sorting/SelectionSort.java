package com.dsavisualizer.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SortingStep;

public class SelectionSort {

    public List<SortingStep> generateSteps(int[] inputArray) {

        List<SortingStep> steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);
        int n = arr.length;

        for (int i = 0; i < n - 1; i++) {

            int minIndex = i;

            // Current round me sorted part i se pehle tak hota hai
            int sortedEndIndex = i - 1;

            // Minimum element find karna
            for (int j = i + 1; j < n; j++) {

                boolean swapped = false;

                steps.add(
                        new SortingStep(
                                Arrays.copyOf(arr, arr.length),
                                minIndex,
                                j,
                                swapped,
                                sortedEndIndex
                        )
                );

                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            // Minimum element ko current position i par lana
            if (minIndex != i) {

                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;

                boolean swapped = true;

                steps.add(
                        new SortingStep(
                                Arrays.copyOf(arr, arr.length),
                                i,
                                minIndex,
                                swapped,
                                i
                        )
                );

            } else {

                boolean swapped = false;

                steps.add(
                        new SortingStep(
                                Arrays.copyOf(arr, arr.length),
                                i,
                                minIndex,
                                swapped,
                                i
                        )
                );
            }
        }

        // Final sorted array
        steps.add(
                new SortingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        -1,
                        false,
                        n - 1
                )
        );

        return steps;
    }
}
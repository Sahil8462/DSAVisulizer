package com.dsavisualizer.algorithm.searching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SearchingStep;

public class BinarySearch {

    public List<SearchingStep> generateSteps(int[] inputArray, int target) {

        List<SearchingStep> steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);

        int low = 0;
        int high = arr.length - 1;
        int foundIndex = -1;

        steps.add(
                new SearchingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        target,
                        foundIndex,
                        "START",
                        "Binary Search started. Array must be sorted.",
                        low,
                        -1,
                        high
                )
        );

        while (low <= high) {

            int mid = low + (high - low) / 2;

            steps.add(
                    new SearchingStep(
                            Arrays.copyOf(arr, arr.length),
                            mid,
                            target,
                            foundIndex,
                            "CHECKING",
                            "Checking mid index " + mid + ": arr[" + mid + "] = " + arr[mid] + ", target = " + target,
                            low,
                            mid,
                            high
                    )
            );

            if (arr[mid] == target) {

                foundIndex = mid;

                steps.add(
                        new SearchingStep(
                                Arrays.copyOf(arr, arr.length),
                                mid,
                                target,
                                foundIndex,
                                "FOUND",
                                "Target " + target + " found at index " + mid + ".",
                                low,
                                mid,
                                high
                        )
                );

                return steps;
            }

            if (target < arr[mid]) {

                steps.add(
                        new SearchingStep(
                                Arrays.copyOf(arr, arr.length),
                                mid,
                                target,
                                foundIndex,
                                "MOVE_LEFT",
                                target + " is smaller than " + arr[mid] + ". Move high to mid - 1.",
                                low,
                                mid,
                                high
                        )
                );

                high = mid - 1;

                steps.add(
                        new SearchingStep(
                                Arrays.copyOf(arr, arr.length),
                                -1,
                                target,
                                foundIndex,
                                "RANGE_UPDATED",
                                "New searching range: low = " + low + ", high = " + high,
                                low,
                                -1,
                                high
                        )
                );

            } else {

                steps.add(
                        new SearchingStep(
                                Arrays.copyOf(arr, arr.length),
                                mid,
                                target,
                                foundIndex,
                                "MOVE_RIGHT",
                                target + " is greater than " + arr[mid] + ". Move low to mid + 1.",
                                low,
                                mid,
                                high
                        )
                );

                low = mid + 1;

                steps.add(
                        new SearchingStep(
                                Arrays.copyOf(arr, arr.length),
                                -1,
                                target,
                                foundIndex,
                                "RANGE_UPDATED",
                                "New searching range: low = " + low + ", high = " + high,
                                low,
                                -1,
                                high
                        )
                );
            }
        }

        steps.add(
                new SearchingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        target,
                        foundIndex,
                        "NOT_FOUND",
                        "Target " + target + " not found in the array.",
                        low,
                        -1,
                        high
                )
        );

        return steps;
    }
}
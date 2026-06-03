package com.dsavisualizer.algorithm.searching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.SearchingStep;

public class LinearSearch {

    public List<SearchingStep> generateSteps(int[] inputArray, int target) {

        List<SearchingStep> steps = new ArrayList<>();

        int[] arr = Arrays.copyOf(inputArray, inputArray.length);
        int foundIndex = -1;

        steps.add(
                new SearchingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        target,
                        foundIndex,
                        "START",
                        "Linear Search started. We will check elements from left to right."
                )
        );

        for (int i = 0; i < arr.length; i++) {

            steps.add(
                    new SearchingStep(
                            Arrays.copyOf(arr, arr.length),
                            i,
                            target,
                            foundIndex,
                            "CHECKING",
                            "Checking index " + i + ": arr[" + i + "] = " + arr[i] + ", target = " + target
                    )
            );

            if (arr[i] == target) {

                foundIndex = i;

                steps.add(
                        new SearchingStep(
                                Arrays.copyOf(arr, arr.length),
                                i,
                                target,
                                foundIndex,
                                "FOUND",
                                "Target " + target + " found at index " + i + "."
                        )
                );

                return steps;
            }

            steps.add(
                    new SearchingStep(
                            Arrays.copyOf(arr, arr.length),
                            i,
                            target,
                            foundIndex,
                            "NOT_MATCHED",
                            arr[i] + " is not equal to " + target + ". Move to next index."
                    )
            );
        }

        steps.add(
                new SearchingStep(
                        Arrays.copyOf(arr, arr.length),
                        -1,
                        target,
                        foundIndex,
                        "NOT_FOUND",
                        "Target " + target + " not found in the array."
                )
        );

        return steps;
    }
}
package com.dsavisualizer.algorithm.linkedlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dsavisualizer.model.LinkedListStep;

public class LinkedListOperation {

    public List<LinkedListStep> generateTraversalSteps(int[] inputArray) {
        return generateTraversalSteps(inputArray, false);
    }

    public List<LinkedListStep> generateTraversalSteps(int[] inputArray, boolean isDoubly) {

        List<LinkedListStep> steps = new ArrayList<>();

        if (inputArray.length == 0) {
            steps.add(createStep(
                    new int[]{},
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    isDoubly,
                    "EMPTY",
                    "Linked List is empty",
                    "HEAD points to NULL"
            ));
            return steps;
        }

        int headIndex = 0;
        int tailIndex = inputArray.length - 1;

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                headIndex,
                tailIndex,
                -1,
                -1,
                isDoubly,
                "CREATE",
                getListName(isDoubly) + " created from input array",
                "HEAD points to first node: " + inputArray[0]
        ));

        for (int i = 0; i < inputArray.length; i++) {

            String pointerMessage;

            if (isDoubly) {
                pointerMessage = getDoublyPointerMessage(inputArray, i);
            }
            else {
                pointerMessage = getSinglyPointerMessage(inputArray, i);
            }

            steps.add(createStep(
                    inputArray,
                    i,
                    i,
                    headIndex,
                    tailIndex,
                    -1,
                    -1,
                    isDoubly,
                    "TRAVERSE",
                    "Current node visited: " + inputArray[i],
                    pointerMessage
            ));
        }

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                headIndex,
                tailIndex,
                -1,
                -1,
                isDoubly,
                "DONE",
                "Traversal completed",
                "Traversal reached NULL"
        ));

        return steps;
    }

    public List<LinkedListStep> generateSearchSteps(int[] inputArray, int target) {
        return generateSearchSteps(inputArray, target, false);
    }

    public List<LinkedListStep> generateSearchSteps(int[] inputArray, int target, boolean isDoubly) {

        List<LinkedListStep> steps = new ArrayList<>();

        if (inputArray.length == 0) {
            steps.add(createStep(
                    new int[]{},
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    -1,
                    isDoubly,
                    "EMPTY",
                    "Linked List is empty",
                    "Search cannot start because HEAD points to NULL"
            ));
            return steps;
        }

        int headIndex = 0;
        int tailIndex = inputArray.length - 1;

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                headIndex,
                tailIndex,
                -1,
                -1,
                isDoubly,
                "SEARCH_START",
                "Searching target value: " + target,
                "Search starts from HEAD node: " + inputArray[0]
        ));

        for (int i = 0; i < inputArray.length; i++) {

            if (inputArray[i] == target) {
                steps.add(createStep(
                        inputArray,
                        i,
                        -1,
                        headIndex,
                        tailIndex,
                        -1,
                        -1,
                        isDoubly,
                        "FOUND",
                        "Target " + target + " found at node index " + i,
                        inputArray[i] + " == " + target + ", value found"
                ));
                return steps;
            }

            String moveMessage;

            if (i == inputArray.length - 1) {
                moveMessage = inputArray[i] + " target nahi hai, aur next NULL hai. Target not found.";
            }
            else {
                moveMessage = inputArray[i] + " target nahi hai. Move to next node " + inputArray[i + 1];
            }

            steps.add(createStep(
                    inputArray,
                    i,
                    i,
                    headIndex,
                    tailIndex,
                    -1,
                    -1,
                    isDoubly,
                    "SEARCHING",
                    "Checking node: " + inputArray[i],
                    moveMessage
            ));
        }

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                headIndex,
                tailIndex,
                -1,
                -1,
                isDoubly,
                "NOT_FOUND",
                "Target " + target + " not found",
                "Search reached NULL"
        ));

        return steps;
    }

    public List<LinkedListStep> generateInsertAtBeginningSteps(int[] inputArray, int newValue) {
        return generateInsertAtBeginningSteps(inputArray, newValue, false);
    }

    public List<LinkedListStep> generateInsertAtBeginningSteps(int[] inputArray, int newValue, boolean isDoubly) {

        List<LinkedListStep> steps = new ArrayList<>();

        int oldHeadIndex = inputArray.length == 0 ? -1 : 0;
        int oldTailIndex = inputArray.length == 0 ? -1 : inputArray.length - 1;

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                oldHeadIndex,
                oldTailIndex,
                -1,
                -1,
                isDoubly,
                "BEFORE_INSERT",
                "Before insertion at beginning",
                inputArray.length == 0
                        ? "List is empty. HEAD points to NULL"
                        : "Old HEAD points to " + inputArray[0]
        ));

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                oldHeadIndex,
                oldTailIndex,
                newValue,
                0,
                isDoubly,
                "CREATE_NEW_NODE",
                "New node created with value: " + newValue,
                isDoubly
                        ? "newNode.prev = NULL and newNode.next will point to old HEAD"
                        : "newNode.next will point to old HEAD"
        ));

        int[] updatedArray = new int[inputArray.length + 1];
        updatedArray[0] = newValue;

        for (int i = 0; i < inputArray.length; i++) {
            updatedArray[i + 1] = inputArray[i];
        }

        steps.add(createStep(
                updatedArray,
                0,
                0,
                0,
                updatedArray.length - 1,
                newValue,
                0,
                isDoubly,
                "CONNECT_NEW_NODE",
                newValue + " inserted before old HEAD",
                isDoubly
                        ? newValue + ".next points to old HEAD and oldHead.prev points back to " + newValue
                        : newValue + ".next points to old HEAD"
        ));

        steps.add(createStep(
                updatedArray,
                0,
                -1,
                0,
                updatedArray.length - 1,
                -1,
                0,
                isDoubly,
                "HEAD_UPDATED",
                "HEAD updated to new node: " + newValue,
                "HEAD now points to " + newValue
        ));

        steps.add(createStep(
                updatedArray,
                -1,
                -1,
                0,
                updatedArray.length - 1,
                -1,
                -1,
                isDoubly,
                "DONE",
                "Insertion at beginning completed",
                "Final linked list ready"
        ));

        return steps;
    }

    public List<LinkedListStep> generateInsertAtEndSteps(int[] inputArray, int newValue) {
        return generateInsertAtEndSteps(inputArray, newValue, false);
    }

    public List<LinkedListStep> generateInsertAtEndSteps(int[] inputArray, int newValue, boolean isDoubly) {

        List<LinkedListStep> steps = new ArrayList<>();

        if (inputArray.length == 0) {
            return generateInsertAtBeginningSteps(inputArray, newValue, isDoubly);
        }

        int headIndex = 0;
        int tailIndex = inputArray.length - 1;

        steps.add(createStep(
                inputArray,
                tailIndex,
                -1,
                headIndex,
                tailIndex,
                -1,
                -1,
                isDoubly,
                "GO_TO_TAIL",
                "Go to TAIL node because new node will be added at end",
                "Current TAIL is " + inputArray[tailIndex]
        ));

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                headIndex,
                tailIndex,
                newValue,
                inputArray.length,
                isDoubly,
                "CREATE_NEW_NODE",
                "New node created with value: " + newValue,
                isDoubly
                        ? "newNode.prev will point to old TAIL and newNode.next = NULL"
                        : "newNode.next = NULL"
        ));

        int[] updatedArray = new int[inputArray.length + 1];

        for (int i = 0; i < inputArray.length; i++) {
            updatedArray[i] = inputArray[i];
        }

        updatedArray[updatedArray.length - 1] = newValue;

        steps.add(createStep(
                updatedArray,
                updatedArray.length - 2,
                updatedArray.length - 2,
                0,
                updatedArray.length - 1,
                newValue,
                updatedArray.length - 1,
                isDoubly,
                "CONNECT_TAIL_NEXT",
                "Old TAIL next connected to new node",
                isDoubly
                        ? inputArray[inputArray.length - 1] + ".next points to " + newValue + " and " + newValue + ".prev points back to " + inputArray[inputArray.length - 1]
                        : inputArray[inputArray.length - 1] + ".next points to " + newValue
        ));

        steps.add(createStep(
                updatedArray,
                updatedArray.length - 1,
                -1,
                0,
                updatedArray.length - 1,
                -1,
                updatedArray.length - 1,
                isDoubly,
                "TAIL_UPDATED",
                "TAIL updated to new node: " + newValue,
                newValue + ".next points to NULL"
        ));

        steps.add(createStep(
                updatedArray,
                -1,
                -1,
                0,
                updatedArray.length - 1,
                -1,
                -1,
                isDoubly,
                "DONE",
                "Insertion at end completed",
                "Final linked list ready"
        ));

        return steps;
    }

    public List<LinkedListStep> generateInsertAtPositionSteps(int[] inputArray, int newValue, int position) {
        return generateInsertAtPositionSteps(inputArray, newValue, position, false);
    }

    public List<LinkedListStep> generateInsertAtPositionSteps(int[] inputArray, int newValue, int position, boolean isDoubly) {

        List<LinkedListStep> steps = new ArrayList<>();

        if (position <= 0) {
            return generateInsertAtBeginningSteps(inputArray, newValue, isDoubly);
        }

        if (position >= inputArray.length) {
            return generateInsertAtEndSteps(inputArray, newValue, isDoubly);
        }

        int headIndex = 0;
        int tailIndex = inputArray.length - 1;

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                headIndex,
                tailIndex,
                -1,
                position,
                isDoubly,
                "BEFORE_INSERT",
                "Insert value " + newValue + " at position " + position,
                "We need previous node at index " + (position - 1)
        ));

        for (int i = 0; i < position; i++) {
            steps.add(createStep(
                    inputArray,
                    i,
                    i,
                    headIndex,
                    tailIndex,
                    -1,
                    position,
                    isDoubly,
                    "FIND_POSITION",
                    "Moving using next pointer. Current node index: " + i,
                    i == position - 1
                            ? "Previous node found: " + inputArray[i]
                            : inputArray[i] + ".next points to " + inputArray[i + 1]
            ));
        }

        steps.add(createStep(
                inputArray,
                -1,
                -1,
                headIndex,
                tailIndex,
                newValue,
                position,
                isDoubly,
                "CREATE_NEW_NODE",
                "New node created with value: " + newValue,
                "New node will be inserted between " + inputArray[position - 1] + " and " + inputArray[position]
        ));

        int[] updatedArray = new int[inputArray.length + 1];

        for (int i = 0; i < position; i++) {
            updatedArray[i] = inputArray[i];
        }

        updatedArray[position] = newValue;

        for (int i = position; i < inputArray.length; i++) {
            updatedArray[i + 1] = inputArray[i];
        }

        steps.add(createStep(
                updatedArray,
                position,
                position,
                0,
                updatedArray.length - 1,
                newValue,
                position,
                isDoubly,
                "NEW_NODE_NEXT_UPDATE",
                "New node next pointer updated",
                isDoubly
                        ? newValue + ".next points to " + inputArray[position] + " and " + newValue + ".prev points to " + inputArray[position - 1]
                        : newValue + ".next points to old node " + inputArray[position]
        ));

        steps.add(createStep(
                updatedArray,
                position - 1,
                position - 1,
                0,
                updatedArray.length - 1,
                -1,
                position,
                isDoubly,
                "PREVIOUS_NEXT_UPDATE",
                "Previous node next pointer updated",
                inputArray[position - 1] + ".next now points to new node " + newValue
        ));

        if (isDoubly) {
            steps.add(createStep(
                    updatedArray,
                    position + 1,
                    -1,
                    0,
                    updatedArray.length - 1,
                    -1,
                    position,
                    true,
                    "NEXT_PREV_UPDATE",
                    "Next node prev pointer updated",
                    inputArray[position] + ".prev now points back to new node " + newValue
            ));
        }

        steps.add(createStep(
                updatedArray,
                -1,
                -1,
                0,
                updatedArray.length - 1,
                -1,
                -1,
                isDoubly,
                "DONE",
                "Insertion at position completed",
                "Final linked list ready"
        ));

        return steps;
    }

    private LinkedListStep createStep(
            int[] values,
            int currentIndex,
            int activeArrowIndex,
            int headIndex,
            int tailIndex,
            int newNodeValue,
            int insertPosition,
            boolean isDoubly,
            String operation,
            String message,
            String pointerMessage
    ) {
        return new LinkedListStep(
                Arrays.copyOf(values, values.length),
                currentIndex,
                activeArrowIndex,
                headIndex,
                tailIndex,
                newNodeValue,
                insertPosition,
                isDoubly,
                operation,
                message,
                pointerMessage
        );
    }

    private String getListName(boolean isDoubly) {
        if (isDoubly) {
            return "Doubly Linked List";
        }
        return "Singly Linked List";
    }

    private String getSinglyPointerMessage(int[] values, int index) {
        if (index == values.length - 1) {
            return values[index] + ".next points to NULL";
        }

        return values[index] + ".next points to " + values[index + 1];
    }

    private String getDoublyPointerMessage(int[] values, int index) {

        String prevMessage;
        String nextMessage;

        if (index == 0) {
            prevMessage = values[index] + ".prev points to NULL";
        }
        else {
            prevMessage = values[index] + ".prev points to " + values[index - 1];
        }

        if (index == values.length - 1) {
            nextMessage = values[index] + ".next points to NULL";
        }
        else {
            nextMessage = values[index] + ".next points to " + values[index + 1];
        }

        return prevMessage + " | " + nextMessage;
    }
}
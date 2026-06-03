package com.dsavisualizer.algorithm.stackqueue;

import java.util.ArrayList;
import java.util.List;

import com.dsavisualizer.model.StackStep;

public class StackOperation {

    private List<Integer> stack;
    private final int maxSize;

    public StackOperation(int maxSize) {
        this.stack = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public StackStep push(int value) {

        if (stack.size() >= maxSize) {
            return new StackStep(
                    stack,
                    "PUSH",
                    value,
                    stack.size() - 1,
                    "OVERFLOW",
                    "Stack Overflow! Maximum stack size is " + maxSize + "."
            );
        }

        stack.add(value);

        return new StackStep(
                stack,
                "PUSH",
                value,
                stack.size() - 1,
                "PUSHED",
                value + " pushed into stack. New top is " + value + "."
        );
    }

    public StackStep pop() {

        if (stack.isEmpty()) {
            return new StackStep(
                    stack,
                    "POP",
                    -1,
                    -1,
                    "UNDERFLOW",
                    "Stack Underflow! Cannot pop because stack is empty."
            );
        }

        int removedValue = stack.remove(stack.size() - 1);

        return new StackStep(
                stack,
                "POP",
                removedValue,
                stack.size() - 1,
                "POPPED",
                removedValue + " popped from stack."
        );
    }

    public StackStep peek() {

        if (stack.isEmpty()) {
            return new StackStep(
                    stack,
                    "PEEK",
                    -1,
                    -1,
                    "EMPTY",
                    "Stack is empty. No top element available."
            );
        }

        int topValue = stack.get(stack.size() - 1);

        return new StackStep(
                stack,
                "PEEK",
                topValue,
                stack.size() - 1,
                "PEEKED",
                "Top element is " + topValue + ". Peek does not remove the element."
        );
    }

    public StackStep reset() {

        stack.clear();

        return new StackStep(
                stack,
                "RESET",
                -1,
                -1,
                "EMPTY",
                "Stack reset successfully. Stack is empty now."
        );
    }

    public StackStep getInitialStep() {

        return new StackStep(
                stack,
                "START",
                -1,
                -1,
                "EMPTY",
                "Stack is empty. Push operation inserts element at the top."
        );
    }

    public List<Integer> getStack() {
        return new ArrayList<>(stack);
    }

    public int getSize() {
        return stack.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public boolean isFull() {
        return stack.size() >= maxSize;
    }
}
package com.dsavisualizer.algorithm.stackqueue;

import java.util.ArrayList;
import java.util.List;

import com.dsavisualizer.model.QueueStep;

public class QueueOperation {

    private List<Integer> queue;
    private final int maxSize;

    public QueueOperation(int maxSize) {
        this.queue = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public QueueStep enqueue(int value) {

        if (queue.size() >= maxSize) {
            return new QueueStep(
                    queue,
                    "ENQUEUE",
                    value,
                    getFrontIndex(),
                    getRearIndex(),
                    "OVERFLOW",
                    "Queue Overflow! Maximum queue size is " + maxSize + "."
            );
        }

        queue.add(value);

        return new QueueStep(
                queue,
                "ENQUEUE",
                value,
                getFrontIndex(),
                getRearIndex(),
                "ENQUEUED",
                value + " added to queue from rear side."
        );
    }

    public QueueStep dequeue() {

        if (queue.isEmpty()) {
            return new QueueStep(
                    queue,
                    "DEQUEUE",
                    -1,
                    -1,
                    -1,
                    "UNDERFLOW",
                    "Queue Underflow! Cannot dequeue because queue is empty."
            );
        }

        int removedValue = queue.remove(0);

        return new QueueStep(
                queue,
                "DEQUEUE",
                removedValue,
                getFrontIndex(),
                getRearIndex(),
                "DEQUEUED",
                removedValue + " removed from queue from front side."
        );
    }

    public QueueStep peek() {

        if (queue.isEmpty()) {
            return new QueueStep(
                    queue,
                    "PEEK",
                    -1,
                    -1,
                    -1,
                    "EMPTY",
                    "Queue is empty. No front element available."
            );
        }

        int frontValue = queue.get(0);

        return new QueueStep(
                queue,
                "PEEK",
                frontValue,
                getFrontIndex(),
                getRearIndex(),
                "PEEKED",
                "Front element is " + frontValue + ". Peek does not remove the element."
        );
    }

    public QueueStep reset() {

        queue.clear();

        return new QueueStep(
                queue,
                "RESET",
                -1,
                -1,
                -1,
                "EMPTY",
                "Queue reset successfully. Queue is empty now."
        );
    }

    public QueueStep getInitialStep() {

        return new QueueStep(
                queue,
                "START",
                -1,
                -1,
                -1,
                "EMPTY",
                "Queue is empty. Enqueue operation inserts element from rear side."
        );
    }

    private int getFrontIndex() {
        if (queue.isEmpty()) {
            return -1;
        }

        return 0;
    }

    private int getRearIndex() {
        if (queue.isEmpty()) {
            return -1;
        }

        return queue.size() - 1;
    }

    public List<Integer> getQueue() {
        return new ArrayList<>(queue);
    }

    public int getSize() {
        return queue.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean isFull() {
        return queue.size() >= maxSize;
    }
}
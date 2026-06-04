package com.dsavisualizer.algorithm.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.dsavisualizer.model.TreeStep;

public class TreeOperation {

    private static class TreeNode {

        int data;
        TreeNode left;
        TreeNode right;

        TreeNode(int data) {
            this.data = data;
        }
    }

    private TreeNode root;
    private List<TreeStep> steps;

    public TreeOperation() {
        root = null;
        steps = new ArrayList<>();
    }

    public List<TreeStep> buildTreeFromArray(int[] values) {

        steps = new ArrayList<>();
        root = null;

        addStep(
                -1,
                -1,
                "BUILD TREE",
                "Array input mila. Ab array ke har element ko BST me insert karenge.",
                "",
                "for each value in array -> insert(value)",
                1,
                0,
                -1,
                "Array traversal start",
                "Array ke elements one by one insert honge",
                "Path: empty"
        );

        int arrayIndex = 0;

        for (int value : values) {

            addStep(
                    value,
                    value,
                    "BUILD TREE",
                    "Array index " + arrayIndex + " par value " + value + " hai. Ab is value ko BST me insert karenge.",
                    "",
                    "value = arr[index]",
                    2,
                    arrayIndex,
                    value,
                    "arr[" + arrayIndex + "] = " + value,
                    "Insert operation call hoga",
                    "Array Index: " + arrayIndex
            );

            insertWithSteps(value);

            arrayIndex++;
        }

        addStep(
                -1,
                -1,
                "BUILD TREE",
                "Complete array se Binary Search Tree build ho gaya.",
                "",
                "BST build completed",
                3,
                arrayIndex,
                -1,
                "Array traversal complete",
                "All values inserted",
                "Path: completed"
        );

        return steps;
    }

    public List<TreeStep> insertValue(int value) {

        steps = new ArrayList<>();
        insertWithSteps(value);
        return steps;
    }

    private void insertWithSteps(int value) {

        int loopStep = 1;
        String path = "";

        if (root == null) {

            root = new TreeNode(value);

            addStep(
                    value,
                    value,
                    "INSERT",
                    "Tree empty tha. Isliye " + value + " root node ban gaya.",
                    "",
                    "if root == null -> root = new Node(value)",
                    1,
                    loopStep,
                    value,
                    "root == null",
                    "Root create",
                    "Path: " + value
            );

            return;
        }

        TreeNode current = root;
        path = String.valueOf(current.data);

        while (true) {

            addStep(
                    current.data,
                    value,
                    "INSERT",
                    "Loop step " + loopStep + ": " + value + " ko current node " + current.data + " se compare kar rahe hain.",
                    "",
                    "while current != null",
                    2,
                    loopStep,
                    current.data,
                    value + " ? " + current.data,
                    "Compare current node",
                    "Path: " + path
            );

            if (value < current.data) {

                addStep(
                        current.data,
                        value,
                        "INSERT",
                        value + " < " + current.data + " isliye LEFT side jayenge.",
                        "",
                        "if value < current.data",
                        3,
                        loopStep,
                        current.data,
                        value + " < " + current.data + " = true",
                        "Move LEFT",
                        "Path: " + path + " -> LEFT"
                );

                if (current.left == null) {

                    current.left = new TreeNode(value);

                    addStep(
                            value,
                            value,
                            "INSERT",
                            current.data + " ke left me " + value + " insert ho gaya.",
                            "",
                            "current.left = new Node(value)",
                            4,
                            loopStep,
                            current.data,
                            "left child == null",
                            "Insert at LEFT",
                            "Path: " + path + " -> " + value
                    );

                    break;
                }

                current = current.left;
                path = path + " -> " + current.data;
            }
            else if (value > current.data) {

                addStep(
                        current.data,
                        value,
                        "INSERT",
                        value + " > " + current.data + " isliye RIGHT side jayenge.",
                        "",
                        "else if value > current.data",
                        5,
                        loopStep,
                        current.data,
                        value + " > " + current.data + " = true",
                        "Move RIGHT",
                        "Path: " + path + " -> RIGHT"
                );

                if (current.right == null) {

                    current.right = new TreeNode(value);

                    addStep(
                            value,
                            value,
                            "INSERT",
                            current.data + " ke right me " + value + " insert ho gaya.",
                            "",
                            "current.right = new Node(value)",
                            6,
                            loopStep,
                            current.data,
                            "right child == null",
                            "Insert at RIGHT",
                            "Path: " + path + " -> " + value
                    );

                    break;
                }

                current = current.right;
                path = path + " -> " + current.data;
            }
            else {

                addStep(
                        current.data,
                        value,
                        "INSERT",
                        value + " == " + current.data + ". Duplicate value insert nahi hogi.",
                        "",
                        "duplicate value ignored",
                        7,
                        loopStep,
                        current.data,
                        value + " == " + current.data,
                        "Duplicate found",
                        "Path: " + path
                );

                break;
            }

            loopStep++;
        }
    }

    public List<TreeStep> searchValue(int value) {

        steps = new ArrayList<>();

        int loopStep = 1;
        String path = "";

        if (root == null) {

            addStep(
                    -1,
                    value,
                    "SEARCH",
                    "Tree empty hai. Search possible nahi hai.",
                    "",
                    "if root == null -> not found",
                    1,
                    loopStep,
                    -1,
                    "root == null",
                    "Search stop",
                    "Path: empty"
            );

            return steps;
        }

        TreeNode current = root;
        path = String.valueOf(current.data);

        while (current != null) {

            addStep(
                    current.data,
                    value,
                    "SEARCH",
                    "Loop step " + loopStep + ": abhi current node " + current.data + " hai.",
                    "",
                    "while current != null",
                    2,
                    loopStep,
                    current.data,
                    value + " ? " + current.data,
                    "Compare target with current",
                    "Path: " + path
            );

            if (value == current.data) {

                addStep(
                        current.data,
                        value,
                        "SEARCH",
                        value + " == " + current.data + ". Value tree me mil gayi.",
                        "",
                        "if value == current.data -> found",
                        3,
                        loopStep,
                        current.data,
                        value + " == " + current.data + " = true",
                        "FOUND",
                        "Path: " + path
                );

                return steps;
            }
            else if (value < current.data) {

                addStep(
                        current.data,
                        value,
                        "SEARCH",
                        value + " < " + current.data + ". Ab LEFT subtree me search karenge.",
                        "",
                        "else if value < current.data -> current = current.left",
                        4,
                        loopStep,
                        current.data,
                        value + " < " + current.data + " = true",
                        "Move LEFT",
                        "Path: " + path + " -> LEFT"
                );

                current = current.left;

                if (current != null) {
                    path = path + " -> " + current.data;
                }
            }
            else {

                addStep(
                        current.data,
                        value,
                        "SEARCH",
                        value + " > " + current.data + ". Ab RIGHT subtree me search karenge.",
                        "",
                        "else -> current = current.right",
                        5,
                        loopStep,
                        current.data,
                        value + " > " + current.data + " = true",
                        "Move RIGHT",
                        "Path: " + path + " -> RIGHT"
                );

                current = current.right;

                if (current != null) {
                    path = path + " -> " + current.data;
                }
            }

            loopStep++;
        }

        addStep(
                -1,
                value,
                "SEARCH",
                value + " tree me nahi mila. Current null ho gaya.",
                "",
                "current == null -> not found",
                6,
                loopStep,
                -1,
                "current == null",
                "NOT FOUND",
                "Path: " + path
        );

        return steps;
    }

    public List<TreeStep> deleteValue(int value) {

        steps = new ArrayList<>();

        addStep(
                -1,
                value,
                "DELETE",
                value + " ko delete karne ke liye pehle BST me search karenge.",
                "",
                "delete(root, value)",
                1,
                1,
                -1,
                "Start delete comparison",
                "Search node to delete",
                "Path: root"
        );

        root = deleteNode(root, value, 1, "");

        addStep(
                -1,
                value,
                "DELETE",
                "Delete operation complete ho gaya.",
                "",
                "return updated root",
                2,
                0,
                -1,
                "Delete complete",
                "Tree updated",
                "Path: completed"
        );

        return steps;
    }

    private TreeNode deleteNode(TreeNode node, int value, int loopStep, String path) {

        if (node == null) {

            addStep(
                    -1,
                    value,
                    "DELETE",
                    value + " tree me present nahi hai.",
                    "",
                    "if node == null -> return null",
                    3,
                    loopStep,
                    -1,
                    "node == null",
                    "Stop delete",
                    "Path: " + path
            );

            return null;
        }

        if (path.isEmpty()) {
            path = String.valueOf(node.data);
        }
        else {
            path = path + " -> " + node.data;
        }

        addStep(
                node.data,
                value,
                "DELETE",
                "Loop step " + loopStep + ": " + value + " ko current node " + node.data + " se compare kar rahe hain.",
                "",
                "compare value with node.data",
                4,
                loopStep,
                node.data,
                value + " ? " + node.data,
                "Compare for delete",
                "Path: " + path
        );

        if (value < node.data) {

            addStep(
                    node.data,
                    value,
                    "DELETE",
                    value + " < " + node.data + ". Left subtree me delete search hoga.",
                    "",
                    "node.left = delete(node.left, value)",
                    5,
                    loopStep,
                    node.data,
                    value + " < " + node.data + " = true",
                    "Move LEFT",
                    "Path: " + path + " -> LEFT"
            );

            node.left = deleteNode(node.left, value, loopStep + 1, path);
        }
        else if (value > node.data) {

            addStep(
                    node.data,
                    value,
                    "DELETE",
                    value + " > " + node.data + ". Right subtree me delete search hoga.",
                    "",
                    "node.right = delete(node.right, value)",
                    6,
                    loopStep,
                    node.data,
                    value + " > " + node.data + " = true",
                    "Move RIGHT",
                    "Path: " + path + " -> RIGHT"
            );

            node.right = deleteNode(node.right, value, loopStep + 1, path);
        }
        else {

            addStep(
                    node.data,
                    value,
                    "DELETE",
                    value + " == " + node.data + ". Delete hone wala node mil gaya.",
                    "",
                    "node found",
                    7,
                    loopStep,
                    node.data,
                    value + " == " + node.data + " = true",
                    "NODE FOUND",
                    "Path: " + path
            );

            if (node.left == null && node.right == null) {

                addStep(
                        node.data,
                        value,
                        "DELETE",
                        "Ye leaf node hai. Direct remove kar denge.",
                        "",
                        "if no child -> return null",
                        8,
                        loopStep,
                        node.data,
                        "left == null && right == null",
                        "Delete leaf node",
                        "Path: " + path
                );

                return null;
            }

            if (node.left == null) {

                addStep(
                        node.data,
                        value,
                        "DELETE",
                        "Is node ka sirf right child hai. Right child iski jagah aa jayega.",
                        "",
                        "if left == null -> return right",
                        9,
                        loopStep,
                        node.data,
                        "left == null",
                        "Replace with right child",
                        "Path: " + path
                );

                return node.right;
            }

            if (node.right == null) {

                addStep(
                        node.data,
                        value,
                        "DELETE",
                        "Is node ka sirf left child hai. Left child iski jagah aa jayega.",
                        "",
                        "if right == null -> return left",
                        10,
                        loopStep,
                        node.data,
                        "right == null",
                        "Replace with left child",
                        "Path: " + path
                );

                return node.left;
            }

            TreeNode successor = findMin(node.right);

            addStep(
                    successor.data,
                    value,
                    "DELETE",
                    "Node ke 2 child hain. Right subtree ka minimum yani inorder successor " + successor.data + " use hoga.",
                    "",
                    "successor = findMin(node.right)",
                    11,
                    loopStep,
                    successor.data,
                    "two children case",
                    "Find inorder successor",
                    "Path: " + path + " -> right subtree min"
            );

            node.data = successor.data;

            addStep(
                    node.data,
                    value,
                    "DELETE",
                    "Node data successor " + successor.data + " se replace ho gaya.",
                    "",
                    "node.data = successor.data",
                    12,
                    loopStep,
                    node.data,
                    "replace deleted value",
                    "Replace node data",
                    "Path: " + path
            );

            node.right = deleteNode(node.right, successor.data, loopStep + 1, path);
        }

        return node;
    }

    private TreeNode findMin(TreeNode node) {

        TreeNode current = node;

        while (current.left != null) {
            current = current.left;
        }

        return current;
    }

    public List<TreeStep> inorderTraversal() {

        steps = new ArrayList<>();
        List<Integer> output = new ArrayList<>();

        addStep(
                -1,
                -1,
                "INORDER",
                "Inorder Traversal start: Left → Root → Right",
                "",
                "inorder(left), visit root, inorder(right)",
                1,
                0,
                -1,
                "Traversal start",
                "Go Left First",
                "Path: root"
        );

        inorder(root, output, 1, "");

        addStep(
                -1,
                -1,
                "INORDER",
                "Inorder complete. BST ka inorder output sorted order me aata hai.",
                output.toString(),
                "Traversal completed",
                2,
                0,
                -1,
                "Traversal complete",
                "Stop",
                "Path: completed"
        );

        return steps;
    }

    private void inorder(TreeNode node, List<Integer> output, int loopStep, String path) {

        if (node == null) {
            return;
        }

        String currentPath;

        if (path.isEmpty()) {
            currentPath = String.valueOf(node.data);
        }
        else {
            currentPath = path + " -> " + node.data;
        }

        addStep(
                node.data,
                -1,
                "INORDER",
                "Step " + loopStep + ": Node " + node.data + " par aaye. Pehle LEFT subtree visit hoga.",
                output.toString(),
                "inorder(node.left)",
                3,
                loopStep,
                node.data,
                "current node = " + node.data,
                "Move LEFT",
                "Path: " + currentPath + " -> LEFT"
        );

        inorder(node.left, output, loopStep + 1, currentPath);

        output.add(node.data);

        addStep(
                node.data,
                -1,
                "INORDER",
                "Left complete. Ab current/root node " + node.data + " visit ho gaya.",
                output.toString(),
                "visit current node",
                4,
                loopStep,
                node.data,
                "visit " + node.data,
                "ADD to output",
                "Path: " + currentPath
        );

        addStep(
                node.data,
                -1,
                "INORDER",
                "Ab node " + node.data + " ka RIGHT subtree visit hoga.",
                output.toString(),
                "inorder(node.right)",
                5,
                loopStep,
                node.data,
                "current node = " + node.data,
                "Move RIGHT",
                "Path: " + currentPath + " -> RIGHT"
        );

        inorder(node.right, output, loopStep + 1, currentPath);
    }

    public List<TreeStep> preorderTraversal() {

        steps = new ArrayList<>();
        List<Integer> output = new ArrayList<>();

        addStep(
                -1,
                -1,
                "PREORDER",
                "Preorder Traversal start: Root → Left → Right",
                "",
                "visit root, preorder(left), preorder(right)",
                1,
                0,
                -1,
                "Traversal start",
                "Visit Root First",
                "Path: root"
        );

        preorder(root, output, 1, "");

        addStep(
                -1,
                -1,
                "PREORDER",
                "Preorder complete. Isme root sabse pehle visit hota hai.",
                output.toString(),
                "Traversal completed",
                2,
                0,
                -1,
                "Traversal complete",
                "Stop",
                "Path: completed"
        );

        return steps;
    }

    private void preorder(TreeNode node, List<Integer> output, int loopStep, String path) {

        if (node == null) {
            return;
        }

        String currentPath;

        if (path.isEmpty()) {
            currentPath = String.valueOf(node.data);
        }
        else {
            currentPath = path + " -> " + node.data;
        }

        output.add(node.data);

        addStep(
                node.data,
                -1,
                "PREORDER",
                "Step " + loopStep + ": Preorder me pehle current/root node " + node.data + " visit hota hai.",
                output.toString(),
                "visit current node",
                3,
                loopStep,
                node.data,
                "visit " + node.data,
                "ADD to output",
                "Path: " + currentPath
        );

        addStep(
                node.data,
                -1,
                "PREORDER",
                "Ab node " + node.data + " ka LEFT subtree visit hoga.",
                output.toString(),
                "preorder(node.left)",
                4,
                loopStep,
                node.data,
                "current node = " + node.data,
                "Move LEFT",
                "Path: " + currentPath + " -> LEFT"
        );

        preorder(node.left, output, loopStep + 1, currentPath);

        addStep(
                node.data,
                -1,
                "PREORDER",
                "Left complete. Ab node " + node.data + " ka RIGHT subtree visit hoga.",
                output.toString(),
                "preorder(node.right)",
                5,
                loopStep,
                node.data,
                "current node = " + node.data,
                "Move RIGHT",
                "Path: " + currentPath + " -> RIGHT"
        );

        preorder(node.right, output, loopStep + 1, currentPath);
    }

    public List<TreeStep> postorderTraversal() {

        steps = new ArrayList<>();
        List<Integer> output = new ArrayList<>();

        addStep(
                -1,
                -1,
                "POSTORDER",
                "Postorder Traversal start: Left → Right → Root",
                "",
                "postorder(left), postorder(right), visit root",
                1,
                0,
                -1,
                "Traversal start",
                "Left First",
                "Path: root"
        );

        postorder(root, output, 1, "");

        addStep(
                -1,
                -1,
                "POSTORDER",
                "Postorder complete. Isme root sabse last me visit hota hai.",
                output.toString(),
                "Traversal completed",
                2,
                0,
                -1,
                "Traversal complete",
                "Stop",
                "Path: completed"
        );

        return steps;
    }

    private void postorder(TreeNode node, List<Integer> output, int loopStep, String path) {

        if (node == null) {
            return;
        }

        String currentPath;

        if (path.isEmpty()) {
            currentPath = String.valueOf(node.data);
        }
        else {
            currentPath = path + " -> " + node.data;
        }

        addStep(
                node.data,
                -1,
                "POSTORDER",
                "Step " + loopStep + ": Node " + node.data + " par aaye. Pehle LEFT subtree visit hoga.",
                output.toString(),
                "postorder(node.left)",
                3,
                loopStep,
                node.data,
                "current node = " + node.data,
                "Move LEFT",
                "Path: " + currentPath + " -> LEFT"
        );

        postorder(node.left, output, loopStep + 1, currentPath);

        addStep(
                node.data,
                -1,
                "POSTORDER",
                "Left complete. Ab node " + node.data + " ka RIGHT subtree visit hoga.",
                output.toString(),
                "postorder(node.right)",
                4,
                loopStep,
                node.data,
                "current node = " + node.data,
                "Move RIGHT",
                "Path: " + currentPath + " -> RIGHT"
        );

        postorder(node.right, output, loopStep + 1, currentPath);

        output.add(node.data);

        addStep(
                node.data,
                -1,
                "POSTORDER",
                "Left aur right dono complete. Ab current/root node " + node.data + " visit ho gaya.",
                output.toString(),
                "visit current node",
                5,
                loopStep,
                node.data,
                "visit " + node.data,
                "ADD to output",
                "Path: " + currentPath
        );
    }

    public List<TreeStep> levelOrderTraversal() {

        steps = new ArrayList<>();
        List<Integer> output = new ArrayList<>();

        if (root == null) {

            addStep(
                    -1,
                    -1,
                    "LEVEL ORDER",
                    "Tree empty hai.",
                    "",
                    "if root == null",
                    1,
                    0,
                    -1,
                    "root == null",
                    "Stop",
                    "Path: empty"
            );

            return steps;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int loopStep = 1;

        addStep(
                root.data,
                -1,
                "LEVEL ORDER",
                "Level Order start. Isme Queue ka use hota hai.",
                output.toString(),
                "queue.add(root)",
                2,
                loopStep,
                root.data,
                "Queue: root",
                "Add root in queue",
                "Queue Path: " + root.data
        );

        while (!queue.isEmpty()) {

            TreeNode current = queue.remove();
            output.add(current.data);

            addStep(
                    current.data,
                    -1,
                    "LEVEL ORDER",
                    "Loop step " + loopStep + ": Queue se " + current.data + " remove hua aur visit ho gaya.",
                    output.toString(),
                    "current = queue.remove()",
                    3,
                    loopStep,
                    current.data,
                    "remove " + current.data,
                    "VISIT node",
                    "Visited Output: " + output
            );

            if (current.left != null) {

                queue.add(current.left);

                addStep(
                        current.left.data,
                        -1,
                        "LEVEL ORDER",
                        current.data + " ka left child " + current.left.data + " queue me add hua.",
                        output.toString(),
                        "queue.add(current.left)",
                        4,
                        loopStep,
                        current.left.data,
                        "left child exists",
                        "ADD LEFT CHILD",
                        "Queue add: " + current.left.data
                );
            }

            if (current.right != null) {

                queue.add(current.right);

                addStep(
                        current.right.data,
                        -1,
                        "LEVEL ORDER",
                        current.data + " ka right child " + current.right.data + " queue me add hua.",
                        output.toString(),
                        "queue.add(current.right)",
                        5,
                        loopStep,
                        current.right.data,
                        "right child exists",
                        "ADD RIGHT CHILD",
                        "Queue add: " + current.right.data
                );
            }

            loopStep++;
        }

        addStep(
                -1,
                -1,
                "LEVEL ORDER",
                "Level Order Traversal complete.",
                output.toString(),
                "queue empty -> stop",
                6,
                loopStep,
                -1,
                "queue.isEmpty() == true",
                "Stop",
                "Output: " + output
        );

        return steps;
    }

    public void clearTree() {
        root = null;
    }

    private void addStep(
            int activeValue,
            int targetValue,
            String operation,
            String message,
            String traversalOutput,
            String codeMessage,
            int codeLine,
            int loopStep,
            int currentNodeValue,
            String compareMessage,
            String directionMessage,
            String pathMessage
    ) {
        steps.add(new TreeStep(
                getTreeArray(),
                activeValue,
                targetValue,
                operation,
                message,
                traversalOutput,
                codeMessage,
                codeLine,
                loopStep,
                currentNodeValue,
                compareMessage,
                directionMessage,
                pathMessage
        ));
    }

    public int[] getTreeArray() {

        List<Integer> values = new ArrayList<>();
        storePreorder(root, values);

        int[] arr = new int[values.size()];

        for (int i = 0; i < values.size(); i++) {
            arr[i] = values.get(i);
        }

        return arr;
    }

    private void storePreorder(TreeNode node, List<Integer> values) {

        if (node == null) {
            return;
        }

        values.add(node.data);
        storePreorder(node.left, values);
        storePreorder(node.right, values);
    }
}
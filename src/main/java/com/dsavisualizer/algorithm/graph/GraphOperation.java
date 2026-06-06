package com.dsavisualizer.algorithm.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import com.dsavisualizer.model.GraphStep;

public class GraphOperation {

    private static final int INF = 999999;

    public Map<String, List<String>> buildUnweightedGraph(String input, boolean directed) {

        Map<String, List<String>> graph = new LinkedHashMap<>();

        if (input == null || input.trim().isEmpty()) {
            return graph;
        }

        String[] edges = input.split(",");

        for (String edge : edges) {

            edge = edge.trim();

            if (!edge.contains("-")) {
                continue;
            }

            String[] parts = edge.split("-");

            if (parts.length != 2) {
                continue;
            }

            String from = parts[0].trim().toUpperCase();
            String to = parts[1].trim().toUpperCase();

            if (from.isEmpty() || to.isEmpty()) {
                continue;
            }

            graph.putIfAbsent(from, new ArrayList<>());
            graph.putIfAbsent(to, new ArrayList<>());

            graph.get(from).add(to);

            if (!directed) {
                graph.get(to).add(from);
            }
        }

        for (String node : graph.keySet()) {
            Collections.sort(graph.get(node));
        }

        return graph;
    }

    public Map<String, Map<String, Integer>> buildWeightedGraph(String input, boolean directed) {

        Map<String, Map<String, Integer>> graph = new LinkedHashMap<>();

        if (input == null || input.trim().isEmpty()) {
            return graph;
        }

        String[] edges = input.split(",");

        for (String edge : edges) {

            edge = edge.trim();

            if (!edge.contains("-") || !edge.contains(":")) {
                continue;
            }

            String[] edgeAndWeight = edge.split(":");

            if (edgeAndWeight.length != 2) {
                continue;
            }

            String edgePart = edgeAndWeight[0].trim();
            String weightPart = edgeAndWeight[1].trim();

            int weight;

            try {
                weight = Integer.parseInt(weightPart);
            } catch (Exception e) {
                continue;
            }

            String[] nodes = edgePart.split("-");

            if (nodes.length != 2) {
                continue;
            }

            String from = nodes[0].trim().toUpperCase();
            String to = nodes[1].trim().toUpperCase();

            if (from.isEmpty() || to.isEmpty()) {
                continue;
            }

            graph.putIfAbsent(from, new LinkedHashMap<>());
            graph.putIfAbsent(to, new LinkedHashMap<>());

            graph.get(from).put(to, weight);

            if (!directed) {
                graph.get(to).put(from, weight);
            }
        }

        return graph;
    }

    public List<GraphStep> generateBfsSteps(Map<String, List<String>> graph, String startNode) {

        List<GraphStep> steps = new ArrayList<>();

        if (graph.isEmpty()) {
            return steps;
        }

        startNode = fixStartNode(graph, startNode);

        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        visited.add(startNode);
        queue.add(startNode);

        steps.add(createStep(
                "BFS",
                startNode,
                null,
                null,
                null,
                visited,
                new ArrayList<>(queue),
                null,
                null,
                false,
                "Start node " + startNode + " ko visited mark karke Queue me add kiya.",
                "BFS real world me nearest path ya level-wise search ke liye use hota hai.",
                "visited.add(start); queue.add(start);",
                1
        ));

        while (!queue.isEmpty()) {

            String current = queue.poll();

            steps.add(createStep(
                    "BFS",
                    current,
                    null,
                    null,
                    null,
                    visited,
                    new ArrayList<>(queue),
                    null,
                    null,
                    false,
                    "Queue se " + current + " remove hua. Ab iske neighbours check honge.",
                    "Jaise social network me pehle direct friends check hote hain.",
                    "current = queue.poll();",
                    2
            ));

            for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {

                steps.add(createStep(
                        "BFS",
                        current,
                        neighbor,
                        current,
                        neighbor,
                        visited,
                        new ArrayList<>(queue),
                        null,
                        null,
                        false,
                        current + " ka neighbour " + neighbor + " check ho raha hai.",
                        "BFS ek level complete karta hai, phir next level par jata hai.",
                        "for (neighbor : graph.get(current))",
                        3
                ));

                if (!visited.contains(neighbor)) {

                    visited.add(neighbor);
                    queue.add(neighbor);

                    steps.add(createStep(
                            "BFS",
                            current,
                            neighbor,
                            current,
                            neighbor,
                            visited,
                            new ArrayList<>(queue),
                            null,
                            null,
                            false,
                            neighbor + " visited nahi tha. Isliye visited mark karke Queue me add kiya.",
                            "Unweighted graph me BFS shortest number of edges find kar sakta hai.",
                            "if (!visited.contains(neighbor)) { visited.add(neighbor); queue.add(neighbor); }",
                            4
                    ));
                } else {

                    steps.add(createStep(
                            "BFS",
                            current,
                            neighbor,
                            current,
                            neighbor,
                            visited,
                            new ArrayList<>(queue),
                            null,
                            null,
                            false,
                            neighbor + " already visited hai. Isliye skip.",
                            "Already visited nodes ko repeat nahi karte warna infinite loop ho sakta hai.",
                            "else skip;",
                            5
                    ));
                }
            }
        }

        steps.add(createStep(
                "BFS",
                null,
                null,
                null,
                null,
                visited,
                new ArrayList<>(),
                null,
                null,
                false,
                "BFS complete. Final visited order: " + visited,
                "BFS level by level graph traversal karta hai.",
                "BFS finished;",
                6
        ));

        return steps;
    }

    public List<GraphStep> generateDfsSteps(Map<String, List<String>> graph, String startNode) {

        List<GraphStep> steps = new ArrayList<>();

        if (graph.isEmpty()) {
            return steps;
        }

        startNode = fixStartNode(graph, startNode);

        Set<String> visited = new LinkedHashSet<>();
        Stack<String> stack = new Stack<>();

        stack.push(startNode);

        steps.add(createStep(
                "DFS",
                startNode,
                null,
                null,
                null,
                visited,
                new ArrayList<>(stack),
                null,
                null,
                false,
                "Start node " + startNode + " ko Stack me push kiya.",
                "DFS real world me maze solving, folder search aur backtracking me use hota hai.",
                "stack.push(start);",
                1
        ));

        while (!stack.isEmpty()) {

            String current = stack.pop();

            steps.add(createStep(
                    "DFS",
                    current,
                    null,
                    null,
                    null,
                    visited,
                    new ArrayList<>(stack),
                    null,
                    null,
                    false,
                    "Stack se " + current + " pop hua.",
                    "DFS ek direction me deep jata hai.",
                    "current = stack.pop();",
                    2
            ));

            if (!visited.contains(current)) {

                visited.add(current);

                steps.add(createStep(
                        "DFS",
                        current,
                        null,
                        null,
                        null,
                        visited,
                        new ArrayList<>(stack),
                        null,
                        null,
                        false,
                        current + " ko visited mark kiya.",
                        "DFS visited nodes ko track karta hai taaki repeat na ho.",
                        "visited.add(current);",
                        3
                ));

                List<String> neighbors = new ArrayList<>(graph.getOrDefault(current, new ArrayList<>()));
                Collections.reverse(neighbors);

                for (String neighbor : neighbors) {

                    steps.add(createStep(
                            "DFS",
                            current,
                            neighbor,
                            current,
                            neighbor,
                            visited,
                            new ArrayList<>(stack),
                            null,
                            null,
                            false,
                            current + " ka neighbour " + neighbor + " check ho raha hai.",
                            "DFS neighbour ko stack me dalta hai aur deep explore karta hai.",
                            "for (neighbor : graph.get(current))",
                            4
                    ));

                    if (!visited.contains(neighbor)) {

                        stack.push(neighbor);

                        steps.add(createStep(
                                "DFS",
                                current,
                                neighbor,
                                current,
                                neighbor,
                                visited,
                                new ArrayList<>(stack),
                                null,
                                null,
                                false,
                                neighbor + " visited nahi hai. Isliye Stack me push kiya.",
                                "Stack LIFO hota hai, last added node pehle process hota hai.",
                                "if (!visited.contains(neighbor)) stack.push(neighbor);",
                                5
                        ));
                    } else {

                        steps.add(createStep(
                                "DFS",
                                current,
                                neighbor,
                                current,
                                neighbor,
                                visited,
                                new ArrayList<>(stack),
                                null,
                                null,
                                false,
                                neighbor + " already visited hai. Isliye skip.",
                                "DFS me visited check zaroori hota hai.",
                                "else skip;",
                                6
                        ));
                    }
                }
            }
        }

        steps.add(createStep(
                "DFS",
                null,
                null,
                null,
                null,
                visited,
                new ArrayList<>(),
                null,
                null,
                false,
                "DFS complete. Final visited order: " + visited,
                "DFS depth-wise traversal karta hai.",
                "DFS finished;",
                7
        ));

        return steps;
    }

    public List<GraphStep> generateDijkstraSteps(Map<String, Map<String, Integer>> graph, String startNode) {

        List<GraphStep> steps = new ArrayList<>();

        if (graph.isEmpty()) {
            return steps;
        }

        startNode = fixWeightedStartNode(graph, startNode);

        Map<String, Integer> distance = new LinkedHashMap<>();
        Map<String, String> parent = new LinkedHashMap<>();
        Set<String> visited = new LinkedHashSet<>();

        for (String node : graph.keySet()) {
            distance.put(node, INF);
            parent.put(node, "-");
        }

        distance.put(startNode, 0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        pq.add(startNode);

        steps.add(createStep(
                "DIJKSTRA",
                startNode,
                null,
                null,
                null,
                visited,
                priorityQueueView(pq, distance),
                distance,
                parent,
                false,
                "Initial distance set hua. Start node " + startNode + " ka distance 0 hai, baaki infinity.",
                "Dijkstra Google Maps jaisa shortest route nikalne me use hota hai.",
                "dist[start] = 0; pq.add(start);",
                1
        ));

        while (!pq.isEmpty()) {

            String current = pq.poll();

            if (visited.contains(current)) {
                continue;
            }

            visited.add(current);

            steps.add(createStep(
                    "DIJKSTRA",
                    current,
                    null,
                    null,
                    null,
                    visited,
                    priorityQueueView(pq, distance),
                    distance,
                    parent,
                    false,
                    "Priority Queue se sabse chhota distance wala node " + current + " nikla.",
                    "Dijkstra hamesha minimum distance wale node ko pehle process karta hai.",
                    "current = pq.poll(); visited.add(current);",
                    2
            ));

            for (String neighbor : graph.getOrDefault(current, new LinkedHashMap<>()).keySet()) {

                int weight = graph.get(current).get(neighbor);
                int newDistance = distance.get(current) + weight;

                steps.add(createStep(
                        "DIJKSTRA",
                        current,
                        neighbor,
                        current,
                        neighbor,
                        visited,
                        priorityQueueView(pq, distance),
                        distance,
                        parent,
                        false,
                        current + " se " + neighbor + " ka edge weight " + weight + " hai. New distance = " + distance.get(current) + " + " + weight + " = " + newDistance,
                        "Route planning me har road ka weight distance/time/cost ho sakta hai.",
                        "newDistance = dist[current] + weight;",
                        3
                ));

                if (newDistance < distance.get(neighbor)) {

                    distance.put(neighbor, newDistance);
                    parent.put(neighbor, current);
                    pq.add(neighbor);

                    steps.add(createStep(
                            "DIJKSTRA",
                            current,
                            neighbor,
                            current,
                            neighbor,
                            visited,
                            priorityQueueView(pq, distance),
                            distance,
                            parent,
                            false,
                            neighbor + " ka distance update hua. Old distance se better path mila.",
                            "Jab short route milta hai to distance table update hota hai.",
                            "if (newDistance < dist[neighbor]) update;",
                            4
                    ));
                } else {

                    steps.add(createStep(
                            "DIJKSTRA",
                            current,
                            neighbor,
                            current,
                            neighbor,
                            visited,
                            priorityQueueView(pq, distance),
                            distance,
                            parent,
                            false,
                            neighbor + " ke liye better path nahi mila. Distance same rahega.",
                            "Dijkstra unnecessary bad path ko ignore karta hai.",
                            "else no update;",
                            5
                    ));
                }
            }
        }

        steps.add(createStep(
                "DIJKSTRA",
                null,
                null,
                null,
                null,
                visited,
                new ArrayList<>(),
                distance,
                parent,
                false,
                "Dijkstra complete. Final shortest distance table ready hai.",
                "Is table se start node se har node ka shortest path milta hai.",
                "Dijkstra finished;",
                6
        ));

        return steps;
    }

    public List<GraphStep> generateCycleDetectionSteps(Map<String, List<String>> graph) {

        List<GraphStep> steps = new ArrayList<>();

        if (graph.isEmpty()) {
            return steps;
        }

        Set<String> visited = new LinkedHashSet<>();
        Map<String, String> parent = new LinkedHashMap<>();
        boolean[] cycleFound = {false};

        for (String node : graph.keySet()) {
            parent.put(node, "-");
        }

        for (String node : graph.keySet()) {

            if (!visited.contains(node)) {

                steps.add(createStep(
                        "CYCLE",
                        node,
                        null,
                        null,
                        null,
                        visited,
                        new ArrayList<>(),
                        null,
                        parent,
                        false,
                        "New component start: " + node,
                        "Cycle detection disconnected graph me bhi har component check karta hai.",
                        "if (!visited.contains(node)) dfs(node, parent);",
                        1
                ));

                dfsCycle(node, "-", graph, visited, parent, steps, cycleFound);

                if (cycleFound[0]) {
                    break;
                }
            }
        }

        if (cycleFound[0]) {
            steps.add(createStep(
                    "CYCLE",
                    null,
                    null,
                    null,
                    null,
                    visited,
                    new ArrayList<>(),
                    null,
                    parent,
                    true,
                    "Cycle Found!",
                    "Real world me dependency loop, deadlock, course prerequisite loop detect karne me cycle detection use hota hai.",
                    "cycle found;",
                    8
            ));
        } else {
            steps.add(createStep(
                    "CYCLE",
                    null,
                    null,
                    null,
                    null,
                    visited,
                    new ArrayList<>(),
                    null,
                    parent,
                    false,
                    "No Cycle Found.",
                    "Graph safe hai. Koi circular dependency nahi mila.",
                    "no cycle;",
                    9
            ));
        }

        return steps;
    }

    private void dfsCycle(
            String current,
            String parentNode,
            Map<String, List<String>> graph,
            Set<String> visited,
            Map<String, String> parent,
            List<GraphStep> steps,
            boolean[] cycleFound
    ) {
        if (cycleFound[0]) {
            return;
        }

        visited.add(current);
        parent.put(current, parentNode);

        steps.add(createStep(
                "CYCLE",
                current,
                null,
                null,
                null,
                visited,
                new ArrayList<>(),
                null,
                parent,
                false,
                current + " ko visited mark kiya. Parent = " + parentNode,
                "Cycle detection me parent ka role important hota hai.",
                "visited.add(current); parent[current] = parent;",
                2
        ));

        for (String neighbor : graph.getOrDefault(current, new ArrayList<>())) {

            steps.add(createStep(
                    "CYCLE",
                    current,
                    neighbor,
                    current,
                    neighbor,
                    visited,
                    new ArrayList<>(),
                    null,
                    parent,
                    false,
                    current + " ka neighbour " + neighbor + " check ho raha hai.",
                    "Agar visited neighbour parent nahi hai to cycle hoti hai.",
                    "for (neighbor : graph.get(current))",
                    3
            ));

            if (!visited.contains(neighbor)) {

                steps.add(createStep(
                        "CYCLE",
                        current,
                        neighbor,
                        current,
                        neighbor,
                        visited,
                        new ArrayList<>(),
                        null,
                        parent,
                        false,
                        neighbor + " visited nahi hai. DFS call karege.",
                        "DFS cycle detection deep jaakar loop check karta hai.",
                        "if (!visited.contains(neighbor)) dfs(neighbor, current);",
                        4
                ));

                dfsCycle(neighbor, current, graph, visited, parent, steps, cycleFound);
            } else if (!neighbor.equals(parentNode)) {

                cycleFound[0] = true;

                steps.add(createStep(
                        "CYCLE",
                        current,
                        neighbor,
                        current,
                        neighbor,
                        visited,
                        new ArrayList<>(),
                        null,
                        parent,
                        true,
                        neighbor + " already visited hai aur parent bhi nahi hai. Isliye cycle found.",
                        "Example: A-B-C-A jaise loop me yahi condition true hoti hai.",
                        "else if (neighbor != parent) cycleFound = true;",
                        5
                ));

                return;
            } else {

                steps.add(createStep(
                        "CYCLE",
                        current,
                        neighbor,
                        current,
                        neighbor,
                        visited,
                        new ArrayList<>(),
                        null,
                        parent,
                        false,
                        neighbor + " parent node hai. Isliye cycle nahi maana jayega.",
                        "Undirected graph me parent edge ko ignore karna padta hai.",
                        "else parent edge ignore;",
                        6
                ));
            }
        }
    }

    private String fixStartNode(Map<String, List<String>> graph, String startNode) {

        if (startNode == null || startNode.trim().isEmpty()) {
            return graph.keySet().iterator().next();
        }

        startNode = startNode.trim().toUpperCase();

        if (!graph.containsKey(startNode)) {
            return graph.keySet().iterator().next();
        }

        return startNode;
    }

    private String fixWeightedStartNode(Map<String, Map<String, Integer>> graph, String startNode) {

        if (startNode == null || startNode.trim().isEmpty()) {
            return graph.keySet().iterator().next();
        }

        startNode = startNode.trim().toUpperCase();

        if (!graph.containsKey(startNode)) {
            return graph.keySet().iterator().next();
        }

        return startNode;
    }

    private List<String> priorityQueueView(PriorityQueue<String> pq, Map<String, Integer> distance) {

        List<String> list = new ArrayList<>();

        for (String node : pq) {
            list.add(node + "(" + formatDistance(distance.get(node)) + ")");
        }

        list.sort((a, b) -> {
            String nodeA = a.substring(0, a.indexOf("("));
            String nodeB = b.substring(0, b.indexOf("("));
            return distance.get(nodeA) - distance.get(nodeB);
        });

        return list;
    }

    private GraphStep createStep(
            String algorithmName,
            String currentNode,
            String neighborNode,
            String activeEdgeFrom,
            String activeEdgeTo,
            Set<String> visitedNodes,
            List<String> activeStructure,
            Map<String, Integer> distanceMap,
            Map<String, String> parentMap,
            boolean cycleFound,
            String message,
            String realWorldMessage,
            String codeMessage,
            int codeLine
    ) {
        return new GraphStep(
                algorithmName,
                currentNode,
                neighborNode,
                activeEdgeFrom,
                activeEdgeTo,
                visitedNodes == null ? new LinkedHashSet<>() : new LinkedHashSet<>(visitedNodes),
                activeStructure == null ? new ArrayList<>() : new ArrayList<>(activeStructure),
                distanceMap == null ? new LinkedHashMap<>() : new LinkedHashMap<>(distanceMap),
                parentMap == null ? new LinkedHashMap<>() : new LinkedHashMap<>(parentMap),
                cycleFound,
                message,
                realWorldMessage,
                codeMessage,
                codeLine
        );
    }

    private String formatDistance(int value) {
        if (value >= INF) {
            return "∞";
        }
        return String.valueOf(value);
    }
}
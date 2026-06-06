package com.dsavisualizer.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphStep {

    private String algorithmName;

    private String currentNode;
    private String neighborNode;

    private String activeEdgeFrom;
    private String activeEdgeTo;

    private Set<String> visitedNodes;
    private List<String> activeStructure;

    private Map<String, Integer> distanceMap;
    private Map<String, String> parentMap;

    private boolean cycleFound;

    private String message;
    private String realWorldMessage;
    private String codeMessage;

    private int codeLine;

    public GraphStep(
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
        this.algorithmName = algorithmName;
        this.currentNode = currentNode;
        this.neighborNode = neighborNode;
        this.activeEdgeFrom = activeEdgeFrom;
        this.activeEdgeTo = activeEdgeTo;
        this.visitedNodes = visitedNodes;
        this.activeStructure = activeStructure;
        this.distanceMap = distanceMap;
        this.parentMap = parentMap;
        this.cycleFound = cycleFound;
        this.message = message;
        this.realWorldMessage = realWorldMessage;
        this.codeMessage = codeMessage;
        this.codeLine = codeLine;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public String getNeighborNode() {
        return neighborNode;
    }

    public String getActiveEdgeFrom() {
        return activeEdgeFrom;
    }

    public String getActiveEdgeTo() {
        return activeEdgeTo;
    }

    public Set<String> getVisitedNodes() {
        return visitedNodes;
    }

    public List<String> getActiveStructure() {
        return activeStructure;
    }

    public Map<String, Integer> getDistanceMap() {
        return distanceMap;
    }

    public Map<String, String> getParentMap() {
        return parentMap;
    }

    public boolean isCycleFound() {
        return cycleFound;
    }

    public String getMessage() {
        return message;
    }

    public String getRealWorldMessage() {
        return realWorldMessage;
    }

    public String getCodeMessage() {
        return codeMessage;
    }

    public int getCodeLine() {
        return codeLine;
    }
}
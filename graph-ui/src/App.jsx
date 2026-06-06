import { useEffect, useState } from "react";

import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  MarkerType,
} from "@xyflow/react";

import "@xyflow/react/dist/style.css";
import "./App.css";

const cityNodes = [
  { id: "home", name: "Home", position: { x: 80, y: 230 }, data: { label: "🏠 Home" } },
  { id: "school", name: "School", position: { x: 300, y: 80 }, data: { label: "🏫 School" } },
  { id: "hospital", name: "Hospital", position: { x: 300, y: 350 }, data: { label: "🏥 Hospital" } },
  { id: "market", name: "Market", position: { x: 570, y: 220 }, data: { label: "🏬 Market" } },
  { id: "station", name: "Station", position: { x: 830, y: 100 }, data: { label: "🚉 Station" } },
  { id: "airport", name: "Airport", position: { x: 830, y: 360 }, data: { label: "✈️ Airport" } },
];

const cityEdges = [
  { id: "home-school", source: "home", target: "school", label: "4 km", weight: 4 },
  { id: "home-hospital", source: "home", target: "hospital", label: "2 km", weight: 2 },
  { id: "school-market", source: "school", target: "market", label: "3 km", weight: 3 },
  { id: "hospital-market", source: "hospital", target: "market", label: "1 km", weight: 1 },
  { id: "market-station", source: "market", target: "station", label: "5 km", weight: 5 },
  { id: "market-airport", source: "market", target: "airport", label: "8 km", weight: 8 },
  { id: "station-airport", source: "station", target: "airport", label: "6 km", weight: 6 },
];

function getName(id) {
  const node = cityNodes.find((item) => item.id === id);
  return node ? node.name : id;
}

function getEdgeId(a, b) {
  const edge = cityEdges.find(
    (item) =>
      (item.source === a && item.target === b) ||
      (item.source === b && item.target === a)
  );

  return edge ? edge.id : "";
}

function buildGraph() {
  const graph = {};

  cityNodes.forEach((node) => {
    graph[node.id] = [];
  });

  cityEdges.forEach((edge) => {
    graph[edge.source].push({
      node: edge.target,
      weight: edge.weight,
      edgeId: edge.id,
    });

    graph[edge.target].push({
      node: edge.source,
      weight: edge.weight,
      edgeId: edge.id,
    });
  });

  return graph;
}

function generateBfsSteps(start, target) {
  const graph = buildGraph();
  const queue = [start];
  const visited = new Set([start]);
  const parent = {};
  const steps = [];

  steps.push({
    title: `Start BFS from ${getName(start)}`,
    current: start,
    visited: Array.from(visited),
    activeEdges: [],
    structure: queue.map(getName),
    found: false,
    explanation: `BFS ${getName(start)} se start hua. ${getName(start)} ko Queue me add kiya.`,
    realWorld: "BFS nearest connected places ko pehle search karta hai.",
  });

  while (queue.length > 0) {
    const current = queue.shift();

    steps.push({
      title: `Process ${getName(current)}`,
      current,
      visited: Array.from(visited),
      activeEdges: [],
      structure: queue.map(getName),
      found: current === target,
      explanation: `${getName(current)} Queue se nikla. Ab iske connected places check honge.`,
      realWorld: "Queue FIFO hoti hai: jo pehle aaya wo pehle process hota hai.",
    });

    if (current === target) {
      steps.push({
        title: `Target found: ${getName(target)}`,
        current,
        visited: Array.from(visited),
        activeEdges: [],
        structure: queue.map(getName),
        found: true,
        explanation: `BFS ko target ${getName(target)} mil gaya.`,
        realWorld: "Unweighted graph me BFS minimum number of roads wala path find kar sakta hai.",
      });
      break;
    }

    for (const neighbour of graph[current]) {
      steps.push({
        title: `Check road ${getName(current)} → ${getName(neighbour.node)}`,
        current,
        visited: Array.from(visited),
        activeEdges: [neighbour.edgeId],
        structure: queue.map(getName),
        found: false,
        explanation: `${getName(current)} se ${getName(neighbour.node)} road check ho raha hai.`,
        realWorld: "BFS har level ke neighbours ko systematically check karta hai.",
      });

      if (!visited.has(neighbour.node)) {
        visited.add(neighbour.node);
        parent[neighbour.node] = current;
        queue.push(neighbour.node);

        steps.push({
          title: `${getName(neighbour.node)} added into Queue`,
          current: neighbour.node,
          visited: Array.from(visited),
          activeEdges: [neighbour.edgeId],
          structure: queue.map(getName),
          found: neighbour.node === target,
          explanation: `${getName(neighbour.node)} pehle visited nahi tha, isliye Queue me add hua.`,
          realWorld: "Search operation me newly discovered places list me add hote hain.",
        });

        if (neighbour.node === target) {
          break;
        }
      }
    }
  }

  return steps;
}

function generateDfsSteps(start, target) {
  const graph = buildGraph();
  const stack = [start];
  const visited = new Set();
  const steps = [];

  steps.push({
    title: `Start DFS from ${getName(start)}`,
    current: start,
    visited: [],
    activeEdges: [],
    structure: stack.map(getName),
    found: false,
    explanation: `DFS ${getName(start)} se start hua. ${getName(start)} Stack me push hua.`,
    realWorld: "DFS ek route me deep jaata hai, jaise gali ke andar andar search.",
  });

  while (stack.length > 0) {
    const current = stack.pop();

    if (visited.has(current)) {
      continue;
    }

    visited.add(current);

    steps.push({
      title: `Visit ${getName(current)}`,
      current,
      visited: Array.from(visited),
      activeEdges: [],
      structure: stack.map(getName),
      found: current === target,
      explanation: `${getName(current)} Stack se pop hua aur visited mark hua.`,
      realWorld: "Stack LIFO hota hai: jo last me gaya wo pehle process hota hai.",
    });

    if (current === target) {
      steps.push({
        title: `Target found: ${getName(target)}`,
        current,
        visited: Array.from(visited),
        activeEdges: [],
        structure: stack.map(getName),
        found: true,
        explanation: `DFS ko target ${getName(target)} mil gaya.`,
        realWorld: "DFS target milte hi stop ho sakta hai.",
      });
      break;
    }

    const neighbours = [...graph[current]].reverse();

    for (const neighbour of neighbours) {
      steps.push({
        title: `Check road ${getName(current)} → ${getName(neighbour.node)}`,
        current,
        visited: Array.from(visited),
        activeEdges: [neighbour.edgeId],
        structure: stack.map(getName),
        found: false,
        explanation: `${getName(current)} se ${getName(neighbour.node)} road check ho raha hai.`,
        realWorld: "DFS ek route ko deep explore karta hai.",
      });

      if (!visited.has(neighbour.node)) {
        stack.push(neighbour.node);

        steps.push({
          title: `${getName(neighbour.node)} pushed into Stack`,
          current: neighbour.node,
          visited: Array.from(visited),
          activeEdges: [neighbour.edgeId],
          structure: stack.map(getName),
          found: false,
          explanation: `${getName(neighbour.node)} visited nahi tha, isliye Stack me push hua.`,
          realWorld: "DFS future exploration ke liye nodes stack me rakhta hai.",
        });
      }
    }
  }

  return steps;
}

function generateDijkstraSteps(start, target) {
  const graph = buildGraph();
  const distances = {};
  const parent = {};
  const visited = new Set();
  const pq = [];
  const steps = [];

  cityNodes.forEach((node) => {
    distances[node.id] = Infinity;
    parent[node.id] = null;
  });

  distances[start] = 0;
  pq.push(start);

  steps.push({
    title: `Start Dijkstra from ${getName(start)}`,
    current: start,
    visited: [],
    activeEdges: [],
    structure: [`${getName(start)}(0)`],
    distances: { ...distances },
    found: false,
    explanation: `${getName(start)} ka distance 0 hai. Baaki sab infinity.`,
    realWorld: "Dijkstra Google Maps jaisa shortest distance path find karta hai.",
  });

  while (pq.length > 0) {
    pq.sort((a, b) => distances[a] - distances[b]);
    const current = pq.shift();

    if (visited.has(current)) {
      continue;
    }

    visited.add(current);

    steps.push({
      title: `Pick nearest ${getName(current)}`,
      current,
      visited: Array.from(visited),
      activeEdges: [],
      structure: pq.map((node) => `${getName(node)}(${distances[node]})`),
      distances: { ...distances },
      found: current === target,
      explanation: `Priority Queue se sabse kam distance wala place ${getName(current)} nikla.`,
      realWorld: "Dijkstra hamesha shortest confirmed place ko pehle process karta hai.",
    });

    if (current === target) {
      steps.push({
        title: `Shortest path found to ${getName(target)}`,
        current,
        visited: Array.from(visited),
        activeEdges: buildPathEdges(parent, start, target),
        structure: [],
        distances: { ...distances },
        found: true,
        explanation: `${getName(start)} se ${getName(target)} ka shortest distance ${distances[target]} km hai.`,
        realWorld: "Final green/red highlighted route best route hota hai.",
      });
      break;
    }

    for (const neighbour of graph[current]) {
      const newDistance = distances[current] + neighbour.weight;

      steps.push({
        title: `Relax road ${getName(current)} → ${getName(neighbour.node)}`,
        current,
        visited: Array.from(visited),
        activeEdges: [neighbour.edgeId],
        structure: pq.map((node) => `${getName(node)}(${distances[node]})`),
        distances: { ...distances },
        found: false,
        explanation: `${getName(current)} se ${getName(neighbour.node)} distance check: ${distances[current]} + ${neighbour.weight} = ${newDistance}`,
        realWorld: "Agar naya route chhota ho, to distance table update hota hai.",
      });

      if (newDistance < distances[neighbour.node]) {
        distances[neighbour.node] = newDistance;
        parent[neighbour.node] = current;
        pq.push(neighbour.node);

        steps.push({
          title: `Update ${getName(neighbour.node)} distance`,
          current: neighbour.node,
          visited: Array.from(visited),
          activeEdges: [neighbour.edgeId],
          structure: pq.map((node) => `${getName(node)}(${distances[node]})`),
          distances: { ...distances },
          found: neighbour.node === target,
          explanation: `${getName(neighbour.node)} ka better distance mila: ${newDistance} km.`,
          realWorld: "Navigation apps repeatedly best distance update karte hain.",
        });
      }
    }
  }

  return steps;
}

function buildPathEdges(parent, start, target) {
  const pathEdges = [];
  let current = target;

  while (current && current !== start) {
    const previous = parent[current];

    if (!previous) {
      break;
    }

    pathEdges.push(getEdgeId(previous, current));
    current = previous;
  }

  return pathEdges;
}

function generateCycleSteps() {
  return [
    {
      title: "Start cycle detection",
      current: "home",
      visited: ["home"],
      activeEdges: [],
      structure: ["Home"],
      found: false,
      explanation: "Cycle detection DFS se hota hai. Home visited mark hua.",
      realWorld: "City me circular route check karna cycle detection jaisa hai.",
    },
    {
      title: "Move Home → School",
      current: "school",
      visited: ["home", "school"],
      activeEdges: ["home-school"],
      structure: ["Home", "School"],
      found: false,
      explanation: "School visited nahi tha, isliye DFS School par gaya.",
      realWorld: "Search path aage badh raha hai.",
    },
    {
      title: "Move School → Market",
      current: "market",
      visited: ["home", "school", "market"],
      activeEdges: ["school-market"],
      structure: ["Home", "School", "Market"],
      found: false,
      explanation: "Market visited nahi tha, isliye DFS Market par gaya.",
      realWorld: "Parent node ko track karna important hota hai.",
    },
    {
      title: "Move Market → Hospital",
      current: "hospital",
      visited: ["home", "school", "market", "hospital"],
      activeEdges: ["hospital-market"],
      structure: ["Home", "School", "Market", "Hospital"],
      found: false,
      explanation: "Hospital visited nahi tha, isliye DFS Hospital par gaya.",
      realWorld: "Search ek path me deep ja rahi hai.",
    },
    {
      title: "Cycle found",
      current: "home",
      visited: ["home", "school", "market", "hospital"],
      activeEdges: ["home-hospital"],
      structure: ["Home", "School", "Market", "Hospital"],
      found: true,
      explanation: "Hospital se Home already visited mila aur Home parent nahi hai. Isliye cycle found.",
      realWorld: "City road loop: Home → School → Market → Hospital → Home.",
      cycleFound: true,
    },
  ];
}

function formatDistance(value) {
  if (value === Infinity) {
    return "∞";
  }

  return value;
}

function App() {
  const [algorithm, setAlgorithm] = useState("BFS");
  const [startNode, setStartNode] = useState("home");
  const [targetNode, setTargetNode] = useState("airport");
  const [stepIndex, setStepIndex] = useState(0);
  const [isPlaying, setIsPlaying] = useState(false);
  const [steps, setSteps] = useState(generateBfsSteps("home", "airport"));

  const step = steps[stepIndex];

  useEffect(() => {
    if (!isPlaying) {
      return;
    }

    const timer = setTimeout(() => {
      setStepIndex((oldIndex) => {
        if (oldIndex >= steps.length - 1) {
          setIsPlaying(false);
          return oldIndex;
        }

        return oldIndex + 1;
      });
    }, 1200);

    return () => clearTimeout(timer);
  }, [isPlaying, stepIndex, steps.length]);

  function runAlgorithm() {
    const newSteps = algorithm === "BFS"
      ? generateBfsSteps(startNode, targetNode)
      : algorithm === "DFS"
      ? generateDfsSteps(startNode, targetNode)
      : algorithm === "Dijkstra"
      ? generateDijkstraSteps(startNode, targetNode)
      : generateCycleSteps();

    setSteps(newSteps);
    setStepIndex(0);
    setIsPlaying(false);
  }

  function nextStep() {
    if (stepIndex < steps.length - 1) {
      setStepIndex(stepIndex + 1);
    } else {
      setIsPlaying(false);
    }
  }

  function previousStep() {
    if (stepIndex > 0) {
      setStepIndex(stepIndex - 1);
    }
  }

  function resetStep() {
    setStepIndex(0);
    setIsPlaying(false);
  }

  function togglePlay() {
    if (stepIndex >= steps.length - 1) {
      setStepIndex(0);
    }

    setIsPlaying((oldValue) => !oldValue);
  }

  const nodes = cityNodes.map((node) => {
    const isVisited = step.visited.includes(node.id);
    const isCurrent = step.current === node.id;
    const isStart = startNode === node.id;
    const isTarget = targetNode === node.id;

    return {
      ...node,
      style: {
        width: 150,
        height: 55,
        borderRadius: 16,
        border: isCurrent
          ? "4px solid #ffb703"
          : isTarget
          ? "4px solid #e63946"
          : isStart
          ? "4px solid #4361ee"
          : "2px solid #1d3557",
        background: isCurrent
          ? "#ffd166"
          : isVisited
          ? "#95d5b2"
          : "#ffffff",
        color: "#1d3557",
        fontWeight: 900,
        boxShadow: isCurrent
          ? "0 0 24px rgba(255, 183, 3, 0.95)"
          : "0 8px 18px rgba(0,0,0,0.14)",
      },
    };
  });

  const edges = cityEdges.map((edge) => {
    const isActive = step.activeEdges.includes(edge.id);

    return {
      ...edge,
      animated: isActive,
      markerEnd: {
        type: MarkerType.ArrowClosed,
      },
      style: {
        strokeWidth: isActive ? 8 : 3,
        stroke: isActive ? "#e63946" : "#7a869a",
      },
      labelStyle: {
        fontWeight: 900,
        fill: "#1d3557",
      },
      labelBgStyle: {
        fill: "#fff3b0",
      },
    };
  });

  const structureName =
    algorithm === "BFS"
      ? "Queue"
      : algorithm === "DFS"
      ? "Stack"
      : algorithm === "Dijkstra"
      ? "Priority Queue"
      : "DFS Call Stack";

  return (
    <div className="page">
      <div className="topbar">
        <div>
          <h1>City Graph Algorithm Visualizer</h1>
          <p>Start aur Target select karo, phir algorithm run karke animation dekho.</p>
        </div>

        <div className="actions">
          <select value={algorithm} onChange={(event) => setAlgorithm(event.target.value)}>
            <option value="BFS">BFS Search</option>
            <option value="DFS">DFS Search</option>
            <option value="Dijkstra">Dijkstra Shortest Path</option>
            <option value="Cycle">Cycle Detection</option>
          </select>

          <select value={startNode} onChange={(event) => setStartNode(event.target.value)}>
            {cityNodes.map((node) => (
              <option key={node.id} value={node.id}>
                Start: {node.name}
              </option>
            ))}
          </select>

          <select value={targetNode} onChange={(event) => setTargetNode(event.target.value)}>
            {cityNodes.map((node) => (
              <option key={node.id} value={node.id}>
                Target: {node.name}
              </option>
            ))}
          </select>

          <button onClick={runAlgorithm}>Run</button>
          <button onClick={previousStep}>Previous</button>
          <button onClick={togglePlay}>{isPlaying ? "Pause" : "Auto Play"}</button>
          <button onClick={nextStep}>Next</button>
          <button onClick={resetStep}>Reset</button>
        </div>
      </div>

      <div className="main">
        <div className="mapCard">
          <ReactFlow nodes={nodes} edges={edges} fitView>
            <Background />
            <Controls />
            <MiniMap />
          </ReactFlow>
        </div>

        <div className="sidePanel">
          <h2>{algorithm} Working</h2>

          <div className="infoBox">
            <b>
              Step {stepIndex + 1} / {steps.length}
            </b>
            <span>{step.title}</span>
          </div>

          <div className="infoBox">
            <b>Start → Target</b>
            <span>
              {getName(startNode)} → {getName(targetNode)}
            </span>
          </div>

          <div className="infoBox">
            <b>Current place</b>
            <span>{getName(step.current)}</span>
          </div>

          <div className="infoBox">
            <b>{structureName}</b>
            <span>
              {step.structure.length ? step.structure.join(" → ") : "Empty"}
            </span>
          </div>

          <div className="infoBox">
            <b>Visited places</b>
            <span>
              {step.visited.length
                ? step.visited.map(getName).join(" → ")
                : "None"}
            </span>
          </div>

          {algorithm === "Dijkstra" && step.distances && (
            <div className="infoBox">
              <b>Distance table</b>
              <div className="distanceGrid">
                {Object.entries(step.distances).map(([place, distance]) => (
                  <span key={place}>
                    {getName(place)}: {formatDistance(distance)}
                  </span>
                ))}
              </div>
            </div>
          )}

          {algorithm === "Cycle" && (
            <div className={step.cycleFound ? "infoBox danger" : "infoBox"}>
              <b>Cycle status</b>
              <span>{step.cycleFound ? "Cycle Found" : "Checking..."}</span>
            </div>
          )}

          {step.found && algorithm !== "Cycle" && (
            <div className="infoBox success">
              <b>Search status</b>
              <span>Target Found</span>
            </div>
          )}

          <div className="infoBox explanation">
            <b>Algorithm logic</b>
            <span>{step.explanation}</span>
          </div>

          <div className="infoBox real">
            <b>Real-world meaning</b>
            <span>{step.realWorld}</span>
          </div>

          <div className="infoBox legend">
            <b>Color meaning</b>
            <span>Blue border = Start place</span>
            <span>Red border = Target place</span>
            <span>Yellow = Current place</span>
            <span>Green = Visited place</span>
            <span>Red road = Active searching road</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
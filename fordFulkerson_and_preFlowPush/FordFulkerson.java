
/**
 * created on November 12th, 2023
 * implemented by Sai Kiran Nandipati

 */

/**
 * Copyright SAI KIRAN NANDIPATI November 12th, 2023. 
 *
 * This code is the property of Sai Kiran Nandipati. No part of this code
 * may be copied, modified, or distributed without the express written permission
 * of the copyright owner.
 */

import java.io.*;
import java.util.*;

/**
 * Represents an edge in a flow network.
 */

class Edge {
	int to;
	float capacity;
	float flow;
	boolean isReversed;

}

/**
 * Represents a flow network graph and implements the Ford-Fulkerson algorithm.
 */
class Graph {

	public HashMap<Integer, List<Edge>> adjList;

	private int source = 0;
	public int sink;

	/**
	 * Constructs a graph with a specified number of vertices.
	 *
	 * @param n The number of vertices in the graph (excluding the source).
	 */
	public Graph(int n) {
		adjList = new HashMap<Integer, List<Edge>>();
		sink = n;
	}

	/**
	 * Adds a directed edge from one vertex to another with a specified capacity and
	 * initial flow.
	 *
	 * @param from     The source vertex.
	 * @param to       The destination vertex.
	 * @param capacity The maximum capacity of the edge.
	 * @param flow     The initial flow through the edge.
	 */
	public void addEdge(int from, int to, float capacity, float flow) {
		Edge edge = new Edge();
		edge.to = to;
		edge.flow = flow;
		edge.capacity = capacity;
		edge.isReversed = false;

		if (!adjList.containsKey(from)) {
			adjList.put(from, new ArrayList<>());
		}
		adjList.get(from).add(edge);

		Edge revEdge = new Edge();
		revEdge.to = from;
		revEdge.flow = capacity;
		revEdge.capacity = capacity;
		revEdge.isReversed = true;

		if (!adjList.containsKey(to)) {
			adjList.put(to, new ArrayList<>());
		}
		adjList.get(to).add(revEdge);
	}

	/**
	 * Returns the index of the edge in the adjacency list based on the source,
	 * destination, and whether it's reversed.
	 *
	 * @param fromNode The source vertex.
	 * @param toNode   The destination vertex.
	 * @param isRev    Indicates whether the edge is reversed.
	 * @return The index of the edge in the adjacency list, or -1 if not found.
	 */

	public Integer getEdgeIndex(int fromNode, int toNode, boolean isRev) {
		for (int from : adjList.keySet())
			if (from == fromNode) {
				List<Edge> edges = adjList.get(from);
				for (int i = 0; i < edges.size(); i++) {
					Edge edge = edges.get(i);
					if (edge.to == toNode && edge.isReversed == isRev)
						return i;
				}
			}
		return -1;
	}

	/**
	 * Displays the graph with information about vertices, edges, capacities, flows,
	 * and reversal status.
	 */

	public void showGraph() {
		System.out.println("Graph...");
		for (int from : adjList.keySet()) {
			System.out.println(from + ": ");
			for (Edge edge : adjList.get(from)) {
				System.out.println("--> " + edge.to + " " + edge.capacity + " " + edge.flow + " " + edge.isReversed);
			}
		}
	}

	/**
	 * Performs a Breadth-First Search (BFS) to find an augmenting path in the
	 * residual graph.
	 *
	 * @return A list of Float arrays representing the augmenting path.
	 */

	public List<Float[]> getPath() {
		// bfs
		Queue<Integer> queue = new LinkedList<>();
		Set<Integer> visited = new HashSet<>();
		Map<Integer, Float[]> parentMap = new HashMap<>();

		queue.add(source);
		visited.add(source);
		boolean pathFound = false;

		// continue till the que is empty
		while (!queue.isEmpty()) {
			int current = queue.poll();
			List<Edge> edges = adjList.get(current);

			if (edges != null) {
				for (Edge edge : edges) {
					int neighbor = edge.to;
					if (!visited.contains(neighbor) && edge.flow < edge.capacity && edge.capacity - edge.flow > 0) {
						queue.add(neighbor);
						visited.add(neighbor);
						Float[] val = { (float) current, edge.capacity - edge.flow,
								(float) (edge.isReversed == true ? 1 : 0) };
						parentMap.put(neighbor, val);

						if (neighbor == sink) {
							pathFound = true;
							break;
						}
					}
				}
			}

			if (pathFound) {
				break;
			}
		}

		// Reconstruct the path if found, first value is the bottle neck
		float minVal = Float.MAX_VALUE;
		if (pathFound) {
			List<Float[]> path = new ArrayList<>();
			int current = sink;
			while (current != (float) source) {
				Float[] parentEdgeData = parentMap.get(current);
				if (minVal > parentEdgeData[1]) {
					minVal = parentEdgeData[1];
				}
				Float[] val = { parentEdgeData[0], (float) current, parentEdgeData[1], parentEdgeData[2] };
				path.add(val);
				current = parentEdgeData[0].intValue();
			}
			Float[] val = { minVal };
			path.add(val);
			Collections.reverse(path);
			return path;
		} else {
			// No path found
			return null;
		}
	}

	/**
	 * Augments the flow along a given path by a specified bottleneck capacity.
	 *
	 * @param path       The list representing the augmenting path.
	 * @param bottleNeck The bottleneck capacity of the path.
	 */

	public void augment(List<Float[]> path, float bottleNeck) {

		int from, to;
		boolean isRev;

		for (Float[] pathEdge : path) {
			from = pathEdge[0].intValue();
			to = pathEdge[1].intValue();
			isRev = pathEdge[3].intValue() == 1 ? true : false;

			int edgeIndex = getEdgeIndex(from, to, isRev);
			if (edgeIndex != -1) {
				Edge edge = adjList.get(from).get(edgeIndex);
				float newFlow = edge.flow;

				if (!edge.isReversed) {
					newFlow += bottleNeck;
				} else {
					newFlow -= bottleNeck;
				}
				if (newFlow >= 0 && newFlow <= edge.capacity) {
					edge.flow = newFlow;
					adjList.get(from).set(edgeIndex, edge);
				}
			}

			// rev edge update
			isRev = pathEdge[3].intValue() == 1 ? false : true;
			edgeIndex = getEdgeIndex(to, from, isRev);
			if (edgeIndex != -1) {
				Edge edge = adjList.get(to).get(edgeIndex);
				float newFlow = edge.flow;

				if (!edge.isReversed) {
					newFlow += bottleNeck;
				} else {
					newFlow -= bottleNeck;
				}
				if (newFlow >= 0 && newFlow <= edge.capacity) {
					edge.flow = newFlow;
					adjList.get(to).set(edgeIndex, edge);
				}
			}
		}
	}

	/**
	 * Displays the flow network by printing the capacity of each edge in the
	 * original graph.
	 */

	public void showFlow() {
		System.out.printf("   Flow network:\n   %5s", "");
		for (int i = 0; i <= sink; i++) {
			System.out.printf("%4d:", i);
		}
		System.out.printf("\n   %5s", "");
		for (int i = 0; i <= sink; i++) {
			System.out.print("-----");
		}
		System.out.println();
		for (int i = 0; i <= sink; i++) {
			System.out.printf("   %4d:", i, "");
			for (int j = 0; j <= sink; j++) {
				int edgeFound = 0;
				for (Edge edge : adjList.get(i)) {
					if (edge.to == j && edge.isReversed == false) {
						if (edge.capacity > 0) {
							edgeFound = 1;
							System.out.printf("%5d", (int) edge.capacity);
						}
					}
				}
				if (edgeFound == 0) {
					System.out.printf(" %4s", "-");
				}
			}
			System.out.println();
		}
	}

	/**
	 * Displays the maximum flow in the network by printing the flow values of edges
	 * in the original graph.
	 */

	public void showMaxFlow() {

		System.out.printf("\n   Maximum flow:\n   %5s", "");
		for (int i = 0; i <= sink; i++) {
			System.out.printf("%4d:", i);
		}
		System.out.printf("\n   %5s", "");
		for (int i = 0; i <= sink; i++) {
			System.out.print("-----");
		}
		System.out.println();
		for (int i = 0; i <= sink; i++) {
			System.out.printf("   %4d:", i, "");
			for (int j = 0; j <= sink; j++) {
				int edgeFound = 0;
				for (Edge edge : adjList.get(i)) {
					if (edge.to == j && edge.isReversed == false) {
						if (edge.flow > 0) {
							edgeFound = 1;
							System.out.printf("%5d", (int) edge.flow);
						}
					}
				}
				if (edgeFound == 0) {
					System.out.printf(" %4s", "-");
				}
			}
			System.out.println();
		}
	}
}

/**
 * Implements the Ford-Fulkerson algorithm to find the maximum flow in a flow
 * network.
 */

public class FordFulkerson {

	/**
	 * Finds the maximum flow in the given graph using the Ford-Fulkerson algorithm.
	 *
	 * @param graph The flow network graph.
	 */

	public static void findMaxFlow(Graph graph) {
		long startTime = System.currentTimeMillis();
		List<Float[]> path = graph.getPath();
		Float maxFlow = (float) 0;
		float bottleNeck;
		while (path != null) {
			bottleNeck = path.get(0)[0];
			maxFlow += bottleNeck;

			graph.augment(path.subList(1, path.size()), bottleNeck);
			path = graph.getPath();
		}
		long endTime = System.currentTimeMillis();

		if (graph.sink <= 9) {
			graph.showFlow();
			graph.showMaxFlow();
		}

		System.out.println(String.format("   Max flow ==> %d (%d ms)\n", maxFlow.intValue(), endTime - startTime));
	}

	/**
	 * Reads input from a file, constructs the graph, and finds the maximum flow for
	 * each graph in the input file.
	 *
	 * @param fileName The name of the input file.
	 * @throws IOException If there is an issue reading the file.
	 */

	public static void readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		Graph graph = null;
		boolean graphStarted = false;
		int count = 1;

		while ((line = br.readLine()) != null) {
			if (line.startsWith("**")) {
				int nVertices = Integer.parseInt(line.split("=")[1].split(",")[0]);
				graph = new Graph(nVertices - 1);
			} else if (line.startsWith("(u")) {
				graphStarted = true;
			} else if (line.startsWith("--")) {
				graphStarted = false;
				System.out.println(String.format("** G%d:\t|V|=%d", count++, graph.sink + 1));
				findMaxFlow(graph);
			} else {
				if (graphStarted) {
					line = line.replace(")", "").replace("(", "").replace("}", "").trim();

					int from = Integer.parseInt(line.split(",")[0].trim());
					int to = Integer.parseInt(line.split(",")[1].trim());
					float capacity = Float.parseFloat(line.split(",")[2].trim());
					graph.addEdge(from, to, capacity, 0);
				}
			}
		}
	}

	/**
	 * The entry point of the program.
	 *
	 * @param args Command-line arguments (expects the name of the input file).
	 * @throws IOException If there is an issue reading the file.
	 */

	public static void main(String[] args) throws IOException {
		System.out.println("Ford-Fulkerson’s algorithm\n");
		readFile(args[0]);
		System.out.println("by SAI KIRAN NANDIPATI");
	}
}

// Program end Date: November 16th 2023

/**
 * created on November 16th, 2023
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
 * Represents an edge in the graph.
 */
class Edge {
	int to;
	float capacity;
	float flow;
	boolean isReversed;
	Edge otherEdge = null;

}

/**
 * Represents the graph and contains methods for graph operations.
 */
class Graph {
	public HashMap<Integer, List<Edge>> adjList;
	public int[] vertexHeight;
	public float[] vertexExcessFlow;
	public int[] vertexCurrentEdge;

	public int sink;

	/**
	 * Constructor for the Graph class.
	 *
	 * @param n The number of vertices in the graph.
	 */

	public Graph(int n) {
		sink = n;

		adjList = new HashMap<Integer, List<Edge>>();
		for (int i = 0; i <= sink; i++)
			adjList.put(i, new ArrayList<>());

		vertexHeight = new int[sink + 1];
		// height of source is n
		vertexHeight[0] = sink + 1;
		for (int i = 1; i <= sink; i++)
			vertexHeight[i] = 0;
		vertexExcessFlow = new float[sink + 1];

		vertexCurrentEdge = new int[sink + 1];
		for (int i = 1; i <= sink; i++)
			vertexCurrentEdge[i] = 0;
	}

	/**
	 * Adds an edge to the graph and its reverse counterpart.
	 *
	 * @param from     The source vertex of the edge.
	 * @param to       The destination vertex of the edge.
	 * @param capacity The maximum flow that the edge can carry.
	 * @param flow     The current flow through the edge.
	 */

	public void addEdge(int from, int to, float capacity, float flow) {
		Edge edge = new Edge();
		Edge revEdge = new Edge();
		edge.to = to;
		edge.flow = flow;
		edge.capacity = capacity;
		edge.isReversed = false;
		edge.otherEdge = revEdge;

		adjList.get(from).add(edge);

		revEdge.to = from;
		revEdge.flow = 0;
		revEdge.capacity = flow;
		revEdge.isReversed = true;
		revEdge.otherEdge = edge;

		adjList.get(to).add(revEdge);
	}

	/**
	 * Finds the index of a specific edge in the adjacency list.
	 *
	 * @param fromNode The source vertex of the edge.
	 * @param toNode   The destination vertex of the edge.
	 * @param isRev    Indicates whether the edge is reversed.
	 * @return The index of the edge in the adjacency list.
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
	 * Displays information about the graph, including vertex height and excess
	 * flow.
	 */

	public void showGraph() {
		System.out.println("Graph...");
		for (int from : adjList.keySet()) {
			System.out.println(from + ": height : " + vertexHeight[from] + " excess: " + vertexExcessFlow[from]);
			for (Edge edge : adjList.get(from)) {
				System.out.println("--> " + edge.to + " " + edge.capacity + " " + edge.flow + " " + edge.isReversed);
			}
		}
	}

	/**
	 * Resets the excess flow at each vertex based on the current flow in the graph.
	 */

	public void resetExcessFlow() {
		for (int i = 0; i <= sink; i++) {
			vertexExcessFlow[i] = 0;
		}

		for (int from : adjList.keySet()) {
			for (Edge edge : adjList.get(from)) {
				if (edge.flow > 0 && edge.isReversed == false) {
					// add the flow for the vertex into edge.to
					vertexExcessFlow[edge.to] += edge.flow;
					// add the flow for the vertex out of edge.from
					vertexExcessFlow[from] -= edge.flow;
				}
			}
		}
	}

	/**
	 * Displays the flow network in a tabular format.
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
	 * Displays the maximum flow achieved in the graph.
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

	/**
	 * Updates the flow and capacity of a specific edge.
	 *
	 * @param from    The source vertex of the edge.
	 * @param to      The destination vertex of the edge.
	 * @param isRev   Indicates whether the edge is reversed.
	 * @param newCap  The new capacity value for the edge.
	 * @param newFlow The new flow value for the edge.
	 */

	public void updateEdge(int from, int to, boolean isRev, float newCap, float newFlow) {
		int edgeIndex = getEdgeIndex(from, to, isRev);
		if (edgeIndex != -1) {
			Edge edge = adjList.get(from).get(edgeIndex);
			edge.flow = newFlow;
			if (newCap != -1)
				edge.capacity = newCap;
			adjList.get(from).set(edgeIndex, edge);
		}
	}

}

/**
 * Contains the main algorithm and serves as the entry point of the program.
 */

public class preflowPush {
	/**
	 * Implements the push operation, moving excess flow from a vertex to its
	 * neighbors.
	 *
	 * @param graph The graph on which the push operation is performed.
	 * @param from  The source vertex for the push operation.
	 * @return 1 if an edge is found for the push operation, 0 otherwise.
	 */

	public static int push(Graph graph, int from) {
		int edgeFound = 0;
		// iterate through each edge going out of fromNode
		List<Edge> edgeList = graph.adjList.get(from);
		for (Edge edgeVal : edgeList.subList(graph.vertexCurrentEdge[from], edgeList.size())) {
			// if from vertex is at higher height
			if (graph.vertexHeight[from] > graph.vertexHeight[edgeVal.to]) {
				// forward edge
				if (edgeVal.isReversed == false) {
					float newFlow = Math.min(graph.vertexExcessFlow[from], edgeVal.capacity - edgeVal.flow);
					if (newFlow > 0) {
						if (newFlow == edgeVal.capacity - edgeVal.flow) {
							graph.vertexCurrentEdge[from]++;
						}
						edgeFound = 1;
						graph.vertexExcessFlow[from] -= newFlow;
						graph.vertexExcessFlow[edgeVal.to] += newFlow;

						edgeVal.flow += newFlow;
						edgeVal.otherEdge.capacity = edgeVal.flow;
					}
				}
				// backward edge
				else {
					float newFlow = Math.min(graph.vertexExcessFlow[from], edgeVal.capacity);
					if (newFlow > 0) {
						if (newFlow == edgeVal.capacity) {
							graph.vertexCurrentEdge[from]++;
						}
						edgeFound = 1;
						graph.vertexExcessFlow[from] -= newFlow;
						graph.vertexExcessFlow[edgeVal.to] += newFlow;

						edgeVal.capacity -= newFlow;
						edgeVal.otherEdge.flow = edgeVal.capacity;
					}
				}
			}
		}
		return edgeFound;
	}

	/**
	 * Main algorithm that finds the maximum flow using the preflow-push approach.
	 *
	 * @param graph The graph on which the algorithm is executed.
	 */

	public static void findMaxFlow(Graph graph) {
		long startTime = System.currentTimeMillis();

		startTime = System.currentTimeMillis();

		// prefow push
		graph.resetExcessFlow();
		List<Integer> excessNodes = new ArrayList<>();
		for (int i = 0; i < graph.sink; i++) {
			if (graph.vertexExcessFlow[i] > 0) {
				excessNodes.add(i);
			}
		}
		while (excessNodes.size() > 0) {
			// itertion
			for (int from : excessNodes) {
				int edgeFound = push(graph, from);
				if (edgeFound == 0) {
					// relabel
					graph.vertexHeight[from] += 1;
					graph.vertexCurrentEdge[from] = 0;
				}
			}

			excessNodes = new ArrayList<>();
			for (int i = 0; i < graph.sink; i++) {
				if (graph.vertexExcessFlow[i] > 0) {
					excessNodes.add(i);
				}
			}
		}
		long endTime = System.currentTimeMillis();

		if (graph.sink <= 9) {
			graph.showFlow();
			graph.showMaxFlow();
		}

		System.out.println(String.format("   Max flow ==> %d (%d ms)\n", (int) graph.vertexExcessFlow[graph.sink],
				endTime - startTime));
	}

	/**
	 * Reads input from a file to initialize the graph.
	 *
	 * @param fileName The name of the file containing the graph information.
	 * @throws IOException If there is an error reading the file.
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
					if (from == 0) {
						// flow from source is same as capacity
						graph.addEdge(from, to, capacity, capacity);
					} else {
						graph.addEdge(from, to, capacity, 0);
					}
				}
			}
		}
	}

	/**
	 * The entry point of the program, reading the file and executing the
	 * preflow-push algorithm.
	 *
	 * @param args Command line arguments.
	 * @throws IOException If there is an error reading the file.
	 */

	public static void main(String[] args) throws IOException {
		System.out.println("Preflow-Push algorithm\n");
		readFile(args[0]);
		System.out.println("by SAI KIRAN NANDIPATI");
	}
}

// Program end Date: November 19th 2023

/**
 * created on October 13th, 2023
 * implemented by Sai Kiran Nandipati

 */

/**
 * Copyright SAI KIRAN NANDIPATI October 13th, 2023. 
 *
 * This code is the property of Sai Kiran Nandipati. No part of this code
 * may be copied, modified, or distributed without the express written permission
 * of the copyright owner.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * The `Edmond` class contains methods and logic for finding minimum arborescences
 * in graphs.
 */
public class Edmond {

	/**
     * A utility class for graph operations.
     */
	
	public static class GraphUtils {
		/**
         * Find unique cycles in the given graph.
         *
         * @param fStar The input graph represented as an adjacency list.
         * @return A set of lists representing unique cycles.
         */

		public static Set<List<String>> findUniqueCycles(HashMap<String, List<String>> fStar) {
			Set<String> visited = new HashSet<>();
			Set<List<String>> cycles = new LinkedHashSet<>(); // Using LinkedHashSet for uniqueness

			for (String key : fStar.keySet()) {
				List<String> path = new ArrayList<>();
				dfs(key, path, visited, fStar, cycles);
			}

			return cycles;
		}

		/**
         * Depth-first search to find cycles in the graph.
         *
         * @param node     The current node in the search.
         * @param path     The current path being explored.
         * @param visited  A set of visited nodes.
         * @param fStar    The input graph represented as an adjacency list.
         * @param cycles   The set to store found cycles.
         */
		
		private static void dfs(String node, List<String> path, Set<String> visited,
				HashMap<String, List<String>> fStar, Set<List<String>> cycles) {
			if (path.contains(node)) {
				List<String> cycle = new ArrayList<>(path.subList(path.indexOf(node), path.size()));
				Collections.sort(cycle);
				cycles.add(cycle);
				return;
			}
			if (visited.contains(node)) {
				return;
			}
			visited.add(node);

			for (String neighborNode : fStar.get(node)) {
				List<String> newPath = new ArrayList<>(path);
				newPath.add(node);
				dfs(neighborNode, newPath, visited, fStar, cycles);
			}

			visited.remove(node);
		}
		/**
         * Check if the given graph is an arborescence.
         *
         * @param fStar           The input graph represented as an adjacency list.
         * @param fStarReversed   The reversed input graph represented as an adjacency list.
         * @return A set of lists representing unique cycles or null if it's an arborescence.
         */

		public static Set<List<String>> isArborescence(HashMap<String, List<String>> fStar,
				HashMap<String, List<String>> fStarReveresed) {

			// Check for cycles
			Set<List<String>> cycles = findUniqueCycles(fStar);
			// Any vertex with more than 1 in degree
			int isInDegreeMoreThanTwo = 0;
			int totalEdges = 0;
			for (String key : fStarReveresed.keySet()) {
				if (fStarReveresed.get(key).size() > 1) {
					isInDegreeMoreThanTwo = 1;
					break;
				}
				totalEdges += fStarReveresed.get(key).size();

			}
			if (cycles.size() > 0)
				return cycles;
			if (isInDegreeMoreThanTwo == 1 || totalEdges >= fStar.keySet().size())
				return null;
			return null;
		}
	}

	private static BufferedReader br;
	
	/**
     * Read a graph from a file and return a list of graphs.
     *
     * @param fileName The name of the file to read.
     * @return A list of graphs represented as adjacency lists.
     * @throws NumberFormatException If there's an issue with number parsing.
     * @throws IOException           If there's an issue reading the file.
     */

	public static List<HashMap<String, List<List<String>>>> readGraphFile(String fileName)
			throws NumberFormatException, IOException {
		List<HashMap<String, List<List<String>>>> graphs = new ArrayList<>();
		HashMap<String, List<List<String>>> graph = null;

		br = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim().replace(" ", "");
			;
			if (line.startsWith("**")) {
				// New graph
				int nVertices = Integer.parseInt(line.split("=")[1].trim());
				// System.out.println(nVertices);
				graph = new HashMap<>();
				for (Integer i = 0; i < nVertices; i++) {
					graph.put(i.toString(), new ArrayList<>());
				}
			} else if (line.startsWith("--")) {
				// End graph
				if (graph != null) {
					graphs.add(graph);
				}
			} else if (line.startsWith("(u"))
				continue;
			else if (line.startsWith("(")) {
				// New edge
				line = line.replace("(", "").replace(")", "").replace("}", "").replace("*", "");

				String[] parts = line.split(",");
				// System.out.println(line);
				String vertex1 = parts[0].trim();
				String vertex2 = parts[1].trim();
				String weight = parts[2].trim();
				if (graph != null) {
					List<String> tmp = new ArrayList<>();
					tmp.add(vertex1);
					tmp.add(weight);
					graph.get(vertex2).add(tmp);
				}
			}
		}
		return graphs;
	}

	/**
     * Get the minimum edge from a list of edges.
     *
     * @param listOfLists A list of edges, where each edge is represented as a list of strings.
     * @return The weight of the minimum edge.
     */
	
	public static Double getMinEdge(List<List<String>> listOfLists) {
		PriorityQueue<List<String>> priorityQueue = new PriorityQueue<>(
				(listA, listB) -> Double.compare(Double.parseDouble(listA.get(1)), Double.parseDouble(listB.get(1))));

		// Add all the inner lists to the priority queue.
		priorityQueue.addAll(listOfLists);

		// Retrieve the list with the minimum second element.
		List<String> minList = priorityQueue.poll();
		if (minList != null) {
			return Double.parseDouble(minList.get(1));
		}
		return 0d;
	}

	/**
     * Append reduced weight to the graph by subtracting the minimum edge weight from each edge.
     *
     * @param graph The input graph represented as an adjacency list.
     * @return The graph with reduced edge weights.
     */
	
	public static HashMap<String, List<List<String>>> appendReducedWeight(HashMap<String, List<List<String>>> graph) {
		for (String key : graph.keySet()) {
			List<List<String>> edges = graph.get(key);
			Double minWeight = getMinEdge(edges);
			for (List<String> edge : edges) {
				String value = Double.toString(Double.parseDouble(edge.get(1)) - minWeight);
				edge.add(value);
			}
		}
		return graph;
	}
	/**
     * Get the forward and reverse star graphs (FStar and FStarReversed) from the input graph.
     *
     * @param graph The input graph represented as an adjacency list.
     * @return A list containing the input graph, FStar, and FStarReversed.
     */
	public static List<Object> getFStar(HashMap<String, List<List<String>>> graph) {
		HashMap<String, List<String>> FStar = new HashMap<>();
		HashMap<String, List<String>> FStarReversed = new HashMap<>();
		for (String key : graph.keySet()) {
			FStar.put(key, new ArrayList<>());
			FStarReversed.put(key, new ArrayList<>());
		}
		for (String key : graph.keySet()) {
			for (List<String> edge : graph.get(key)) {
				String u = key;
				String v = edge.get(0);
				String weight = edge.get(2);
				if (Double.parseDouble(weight) == 0.0) {
					if (!FStar.containsKey(v))
						FStar.put(v, new ArrayList<>());

					FStar.get(v).add(u);
					if (!FStarReversed.containsKey(v))
						FStarReversed.put(v, new ArrayList<>());
					FStarReversed.get(u).add(v);
					FStarReversed.get(u).add(edge.get(1));
					FStarReversed.get(u).add(edge.get(2));
					edge.add("fStar");
					break;
				}
			}
		}
		List<Object> values = new ArrayList<>();
		values.add(graph);
		values.add(FStar);
		values.add(FStarReversed);
		return values;
	}
	 /**
     * Get the node within a cycle that replaces a given node, or return the original node.
     *
     * @param cycles A set of unique cycles.
     * @param node   The node to be replaced.
     * @return The replacement node within the cycle or the original node if not in a cycle.
     */
	public static String getCycleNode(Set<List<String>> cycles, String node) {

		if (cycles != null) {
			for (List<String> cycle : cycles) {
				String newNode = String.join("-", cycle);
				if (cycle.contains(node)) {
					return newNode;
				}
			}
		}
		return node;
	}
	/**
     * Contract cycles in the graph and return a new contracted graph.
     *
     * @param graph        The input graph represented as an adjacency list.
     * @param cycles       A set of unique cycles.
     * @param edgeMapping  A mapping of edges before and after contraction.
     * @return A list containing the contracted graph and the edge mapping.
     */
	public static List<Object> contractCycles(HashMap<String, List<List<String>>> graph, Set<List<String>> cycles,
			HashMap<List<String>, List<String>> edgeMapping) {
		HashMap<String, List<List<String>>> newGraph = new HashMap<>();

		HashMap<List<String>, Double> edgeWeightMap = new HashMap<>();

		for (String key : graph.keySet()) {
			List<List<String>> edges = graph.get(key);
			if (edges.size() == 0) {
				String u = getCycleNode(cycles, key);
				if (!newGraph.containsKey(u)) {
					List<List<String>> newEdges = new ArrayList<>();
					newGraph.put(u, newEdges);
				}
			}
			for (List<String> edge : edges) {
				String u = getCycleNode(cycles, key);
				String v = getCycleNode(cycles, edge.get(0));
				String weight = edge.get(2);
				if (!u.equals(v)) {
					List<String> newEdge = new ArrayList<>();
					newEdge.add(v);
					newEdge.add(weight);

					List<String> oldEdge = new ArrayList<>();
					List<String> cycleEdge = new ArrayList<>();
					oldEdge.add(key);
					oldEdge.add(edge.get(0));
					oldEdge.add(edge.get(1));

					cycleEdge.add(u);
					cycleEdge.add(v);
					cycleEdge.add(weight);

					edgeMapping.put(cycleEdge, oldEdge);

					if (!edgeWeightMap.containsKey(cycleEdge.subList(0, 2)))
						edgeWeightMap.put(cycleEdge.subList(0, 2), Double.parseDouble(cycleEdge.get(2)));
					else {
						// if lower value then update the weight
						if (Double.parseDouble(cycleEdge.get(2)) < edgeWeightMap.get(cycleEdge.subList(0, 2))) {
							edgeWeightMap.put(cycleEdge.subList(0, 2), Double.parseDouble(cycleEdge.get(2)));
						}
					}

				}
			}
		}

		for (List<String> edge : edgeWeightMap.keySet()) {
			String u = edge.get(0);
			List<String> newEdge = new ArrayList<>();
			newEdge.add(edge.get(1));
			newEdge.add(edgeWeightMap.get(edge).toString());

			if (!newGraph.containsKey(u)) {
				List<List<String>> newEdges = new ArrayList<>();
				newGraph.put(u, newEdges);
			}
			newGraph.get(u).add(newEdge);
		}

		List<Object> tmp = new ArrayList<>();
		tmp.add(newGraph);
		tmp.add(edgeMapping);
		return tmp;
	}
	 /**
     * Main recursion for finding minimum arborescences in the graph.
     *
     * @param graph   The input graph represented as an adjacency list.
     * @param verbose A boolean flag indicating whether to print verbose output.
     * @return The resulting arborescence or null if cycles exist.
     */
	@SuppressWarnings("unchecked")
	public static HashMap<String, List<List<String>>> mainRecursion(HashMap<String, List<List<String>>> graph,
			boolean verbose) {
		if (verbose) {
			System.out.println("****** Iteration Started...\n");
			System.out.println("Graph: " + graph);
		}

		graph = appendReducedWeight(graph);

		if (verbose)
			System.out.println("Reduced Weight Added: " + graph);

		List<Object> values = getFStar(graph);
		graph = (HashMap<String, List<List<String>>>) values.get(0);
		HashMap<String, List<String>> FStar = (HashMap<String, List<String>>) values.get(1);
		HashMap<String, List<String>> FStarReversed = (HashMap<String, List<String>>) values.get(2);
		if (verbose) {
			System.out.println("FStar Graph: " + graph);
			System.out.println("FStar: " + FStar);
			System.out.println("FStarReversed: " + FStarReversed);
		}

		Set<List<String>> cycles = GraphUtils.isArborescence(FStar, FStarReversed);
		if (cycles != null && cycles.size() > 0) {
			if (verbose)
				System.out.println("Cycles in F_star: " + cycles);

			// create edge mapping as back tracking for expansion
			HashMap<List<String>, List<String>> edgeMapping = new HashMap<>();

			// create vertex_mapping
			HashMap<String, String> vertexMap = new HashMap<>();

			for (String key : graph.keySet()) {
				vertexMap.put(key, key);
				for (List<String> cycle : cycles) {
					String superNode = String.join("-", cycle);
					if (superNode.indexOf(key) != -1)
						vertexMap.put(key, superNode);
				}
			}
			// contract cycles
			values = contractCycles(graph, cycles, edgeMapping);
			HashMap<String, List<List<String>>> newGraph = (HashMap<String, List<List<String>>>) values.get(0);
			edgeMapping = (HashMap<List<String>, List<String>>) values.get(1);

			if (verbose) {
				System.out.println("Vertex map: " + vertexMap);
				System.out.println("Edge map: " + edgeMapping);
				System.out.println("Contracted Graph: " + newGraph);
			}

			HashMap<String, List<List<String>>> returnedGraph = mainRecursion(newGraph, verbose);
			
			if (verbose) {
				System.out.println("\n>>>>>>> \n\tExpansion Started...\n>>>>>>>\nll");

				System.out.println("Actual Graph: " + graph);
				// System.out.println("\nContracted Graph: " + newGraph);
				System.out.println("\nReturned Graph: " + returnedGraph);
				System.out.println("\nedgeMapping: " + edgeMapping);
			}

			// this stores the list of nodes for each super node and list of all edges
			// within the cycle
			HashMap<String, List<List<String>>> cycleMap = new HashMap<>();
			for (List<String> key : cycles) {
				List<List<String>> tmp = new ArrayList<>();
				tmp.add(key);
				cycleMap.put(String.join("-", key), tmp);
				// add list of edges for each cycle
				for (String nodeKey : graph.keySet()) {
					for (List<String> edge : graph.get(nodeKey)) {
						if (key.indexOf(nodeKey) != -1 && key.indexOf(edge.get(0)) != -1) {
							List<String> newEdge = new ArrayList<>();
							newEdge.add(nodeKey);
							newEdge.add(edge.get(0));
							newEdge.add(edge.get(1));
							cycleMap.get(String.join("-", key)).add(newEdge);
						}
					}
				}
			}
			if (verbose) {
				System.out.println("\nCycles: " + cycles);
				System.out.println("\ncycleMap: " + cycleMap);
			}

			// new final graph
			HashMap<String, List<List<String>>> newFinalGraph = new HashMap<>();
			for (String key : returnedGraph.keySet()) {
				for (List<String> edge : returnedGraph.get(key)) {
					// add the correspinding edge stored in edgemapping
					Boolean isKeyCycle = cycleMap.keySet().contains(key) ? true : false;
					Boolean isEdgeNodeCycle = cycleMap.keySet().contains(edge.get(0)) ? true : false;

					List<String> oldEdgeKey = new ArrayList<>();
					oldEdgeKey.add(key);
					oldEdgeKey.add(edge.get(0));
					oldEdgeKey.add(edge.get(1));
					oldEdgeKey = edgeMapping.get(oldEdgeKey);

					List<String> newEdgeKey = new ArrayList<>();
					newEdgeKey.add(oldEdgeKey.get(1));
					newEdgeKey.add(oldEdgeKey.get(2));

					if (newFinalGraph.keySet().contains(oldEdgeKey.get(0))) {
						newFinalGraph.get(oldEdgeKey.get(0)).add(newEdgeKey);
					} else {
						newFinalGraph.put(oldEdgeKey.get(0), new ArrayList<>());
						newFinalGraph.get(oldEdgeKey.get(0)).add(newEdgeKey);
					}

					// Type of edge
					if (isKeyCycle && isEdgeNodeCycle)
						edge.add("within");
					else if ((!isKeyCycle && isEdgeNodeCycle) || (isKeyCycle && !isEdgeNodeCycle)) {

						if (!isKeyCycle && isEdgeNodeCycle)
							edge.add("outwards");
						else if (isKeyCycle && !isEdgeNodeCycle)
							edge.add("inwards");
					} else
						edge.add("nonCycle");
				}
			}
			// add edges within cycle
			for (String cycle : cycleMap.keySet()) {
				for (List<String> edge : cycleMap.get(cycle).subList(1, cycleMap.get(cycle).size())) {
					if (!newFinalGraph.containsKey(edge.get(0))) {
						String key = edge.get(0);
						List<List<String>> value = new ArrayList<>();
						List<String> tmp = new ArrayList<>();
						tmp.add(edge.get(1));
						tmp.add(edge.get(2));
						value.add(tmp);
						newFinalGraph.put(key, value);
					}
				}
			}
			if (verbose) {
				System.out.println("updated returnedGraph" + returnedGraph);
				System.out.println("updated newFinalGraph" + newFinalGraph);
			}
			return newFinalGraph;

		} else {

			HashMap<String, List<List<String>>> tmpGraph = new HashMap<>();
			for (String key : FStarReversed.keySet()) {
				tmpGraph.put(key, new ArrayList<>());
				List<String> edges = FStarReversed.get(key);
				if (edges.size() > 0)
					tmpGraph.get(key).add(edges);
			}
			return tmpGraph;
		}

	}

	/**
     * Clean the final graph, remove duplicate edges, and display the result.
     *
     * @param finalGraph The final graph represented as a list of edges.
     * @param nVertices  The number of vertices in the graph.
     * @param count      The count of the current graph.
     * @param runtime    The runtime of the computation.
     */
	
	public static void cleanFinalGraph(List<List<String>> finalGraph, Integer nVertices, Integer count, long runtime) {
		// Define a custom comparator
		Comparator<List<String>> customComparator = new Comparator<List<String>>() {
			@Override
			public int compare(List<String> list1, List<String> list2) {
				// Compare the first elements
				int compareFirst = Integer.compare(
						Integer.parseInt(list1.get(0)), Integer.parseInt(list2.get(0)));

				// If the first elements are equal, compare the second elements
				if (compareFirst == 0) {
					return Integer.compare(Integer.parseInt(
							list1.get(1)), Integer.parseInt(list2.get(1)));
				}
				return compareFirst;
			}
		};

		Set<List<String>> uniqueLists = new HashSet<>();
		List<List<String>> sortedUniqueLists = new ArrayList<>();

		for (List<String> innerList : finalGraph) {
			if (uniqueLists.add(innerList)) {
				sortedUniqueLists.add(innerList);
			}
		}

		Collections.sort(sortedUniqueLists, customComparator);

		System.out.println(String.format("G%d: |V|=%d -----------------------\n\tArborescence --", count, nVertices));
		Integer c = 1;
		Double totalWeight = 0.0;
		for (List<String> innerList : sortedUniqueLists) {
			Double val = Double.parseDouble(innerList.get(2));
			totalWeight += val;
			System.out.println("\t\t" + c.toString() + ": (" + innerList.get(0) + ", " + innerList.get(1) + ", "
					+ String.format("%.5f", (val)) + ")");
			c++;
		}
		System.out.println(String.format("\tTotal weight: %.4f (%d ms)", totalWeight, runtime));
		System.out.println(String.format("\tTotal weight: %.5f", totalWeight));
	}
	 /**
     * The main method to execute the minimum arborescence algorithm on a set of graphs.
     *
     * @param args The command-line arguments, where the first argument is the input file name.
     * @throws NumberFormatException If there's an issue with number parsing.
     * @throws IOException           If there's an issue reading the file.
     */
	public static void main(String[] args) throws NumberFormatException, IOException {

		Boolean verbose = false;
		Integer count = 1;
		String fileName = args[0];
		List<HashMap<String, List<List<String>>>> graphs = readGraphFile(fileName);
		System.out.println("Minimum Arborescences in " + fileName + "\n");
		// Write a loop for each graph
		for (HashMap<String, List<List<String>>> graph : graphs) {
			Integer nVertices = graph.keySet().size();
			long startTime = System.currentTimeMillis();
			graph = mainRecursion(graph, verbose);

			long endTime = System.currentTimeMillis();
			long runtime = endTime - startTime;

			// Remove edges with multiple inedges - choose the first one
			for (String key : graph.keySet()) {
				List<List<String>> edges = graph.get(key);
				if (edges.size() > 1) {
					List<List<String>> tmpEdges = new ArrayList<>();
					tmpEdges.add(edges.get(0));
					graph.put(key, tmpEdges);
				}
			}

			List<List<String>> finalGraph = new ArrayList<>();
			for (String key : graph.keySet()) {
				for (List<String> edge : graph.get(key)) {
					List<String> tmp = new ArrayList<>();
					tmp.add(edge.get(0));
					tmp.add(key);
					tmp.add(edge.get(1));
					finalGraph.add(tmp);
				}
			}

			cleanFinalGraph(finalGraph, nVertices, count++, runtime);

		}
	    System.out.println("\n*** Asg 4 by SAI KIRAN NANDIPATI.");
	}
	


}


/**
 * created on September 26th, 2023
 * implemented by Sai Kiran Nandipati

 */

/**
 * Copyright SAI KIRAN NANDIPATI September 27, 2023. All rights reserved.
 *
 * This code is the property of Sai Kiran Nandipati. No part of this code
 * may be copied, modified, or distributed without the express written permission
 * of the copyright owner.
 */
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * The Edge class represents an edge in a graph, connecting two vertices with a
 * weight.
 *
 * @author <SAI KIRAN NANDIPATI>
 *
 */

class Edge {
    Vertex source;
    Vertex destination;
    double weight;

    /**
     * Constructs a new Edge object with the given source, destination, and weight.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @param weight      The weight associated with the edge.
     */

    public Edge(Vertex source, Vertex destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Compares this edge to another edge based on their weights.
     *
     * @param other The other edge to compare to.
     * @return A negative integer if this edge has a smaller weight, a positive
     *         integer if it has a larger weight,
     *         or zero if the weights are equal.
     */

    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }
}

/**
 * /**
 * The Vertex class represents a vertex in a graph, containing information about
 * its unique identifier
 * and a list of neighboring edges.
 *
 *
 * @author <SAI KIRAN NANDIPATI>
 *
 */

class Vertex {
    int vertex;
    ArrayList<Edge> neighbors;

    /**
     * Constructs a new Vertex object with the given unique identifier.
     *
     * @param vertex The unique identifier of the vertex.
     */
    public Vertex(int vertex) {
        this.vertex = vertex;
        neighbors = new ArrayList<>();
    }
}

/**
 *
 * The DisjointSet class represents a disjoint-set data structure (also known as
 * a union-find data structure).
 * It is used to maintain a collection of disjoint sets and provides operations
 * to merge sets and find the representative
 * element of a set.
 *
 *
 * @author <SAI KIRAN NANDIPATI>
 *
 */
class DisjointSet {
    private int[] parent;

    /**
     * Constructs a new DisjointSet object with the given size.
     *
     * @param size The number of elements for which disjoint sets need to be
     *             maintained.
     */
    public DisjointSet(int size) {
        parent = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    /**
     * Find the representative (root) element of the set to which the specified
     * element belongs.
     *
     * @param element The element for which to find the representative element.
     * @return The representative (root) element of the set to which the specified
     *         element belongs.
     */
    public int find(int element) {
        if (parent[element] == element) {
            return element;
        }
        return find(parent[element]);
    }

    /**
     * Union (merge) two sets by making one the parent of the other.
     *
     * @param x The element from the first set.
     * @param y The element from the second set.
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        parent[rootX] = rootY;
    }
}

/**
 * The Graph class represents a graph data structure using an adjacency list
 * representation.
 * It provides methods for adding edges, computing a minimum spanning tree using
 * Prim's algorithm,
 * and computing a minimum spanning tree using Kruskal's algorithm.
 *
 * @author <SAI KIRAN NANDIPATI>
 *
 */
class Graph {
    ArrayList<Vertex> vertices;
    ArrayList<ArrayList<Integer>> adjacencyList;

    /**
     * Constructs a new Graph object with the specified number of vertices.
     *
     * @param numVertices The number of vertices in the graph.
     */

    public Graph(int numVertices) {
        vertices = new ArrayList<>(numVertices);
        adjacencyList = new ArrayList<>(numVertices);

        for (int i = 0; i < numVertices; i++) {
            vertices.add(new Vertex(i));
            adjacencyList.add(new ArrayList<>());
        }
    }

    /**
     * Adds an edge to the graph between two vertices with the specified source,
     * destination, and weight.
     *
     * @param srcVertex The index of the source vertex.
     * @param dstVertex The index of the destination vertex.
     * @param weight    The weight of the edge.
     */

    public void addEdge(int srcVertex, int dstVertex, double weight) {
        Vertex source = vertices.get(srcVertex);
        Vertex destination = vertices.get(dstVertex);
        Edge edge = new Edge(source, destination, weight);
        Edge revEdge = new Edge(destination, source, weight);

        source.neighbors.add(edge);
        destination.neighbors.add(revEdge);
        adjacencyList.get(srcVertex).add(dstVertex); // Update adjacency list
    }

    /**
     * Computes the minimum spanning tree of the graph using Prim's algorithm.
     *
     * @return An ArrayList of Edges representing the minimum spanning tree.
     */

    public ArrayList<Edge> primsMinimumSpanningTree() {
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        Set<Vertex> visited = new HashSet<>();
        ArrayList<Edge> mst = new ArrayList<>();

        Vertex startVertex = vertices.get(0);
        visited.add(startVertex);

        for (Edge edge : startVertex.neighbors) {
            minHeap.add(edge);
        }

        while (!minHeap.isEmpty() && visited.size() < vertices.size()) {
            Edge minEdge = minHeap.poll();
            // Vertex source = minEdge.source;
            Vertex destination = minEdge.destination;

            if (visited.contains(destination)) {
                continue;
            }

            visited.add(destination);
            mst.add(minEdge);

            for (Edge neighborEdge : destination.neighbors) {
                if (!visited.contains(neighborEdge.destination)) {
                    minHeap.add(neighborEdge);
                }
            }
        }

        return mst;
    }

    /**
     * Computes the minimum spanning tree of the graph using Kruskal's algorithm.
     *
     * @return An ArrayList of Edges representing the minimum spanning tree.
     */
    public ArrayList<Edge> krushkalsMinimumSpanningTree() {
        int edgesAccepted = 0;
        DisjointSet ds = new DisjointSet(vertices.size());
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(vertices.size(), Comparator.comparingDouble(e -> e.weight));
        ArrayList<Edge> mst = new ArrayList<>();

        for (Vertex vertex : vertices) {
            minHeap.addAll(vertex.neighbors);
        }

        while (!minHeap.isEmpty() && edgesAccepted < vertices.size() - 1) {
            Edge minEdge = minHeap.poll();
            int u = minEdge.source.vertex;
            int v = minEdge.destination.vertex;

            int uset = ds.find(u);
            int vset = ds.find(v);

            if (uset != vset) {
                edgesAccepted++;
                ds.union(uset, vset);
                mst.add(minEdge);
            }
        }

        return mst;
    }

}

/**
 * date & time: sep 27th 6:00 pm
 * 
 * The minSPT class is responsible for reading a file containing graph
 * information, finding minimum spanning trees
 * using Kruskal's and Prim's algorithms, and displaying the results.
 *
 */

public class minSPT {

    /**
     * Reads graph information from a file and returns an ArrayList of Graph
     * objects.
     *
     * @param fileName The name of the file containing graph data.
     * @return An ArrayList of Graph objects parsed from the input file.
     * @throws IOException If there is an error reading the file.
     */

    public static ArrayList<Graph> readFile(String fileName) throws IOException {
        // Create an ArrayList to store the parsed Graph objects.
        ArrayList<Graph> graphs = new ArrayList<>();
        BufferedReader reader = null;

        try {
            // Open the file for reading.
            Reader f = new FileReader(fileName);
            reader = new BufferedReader(f);
            String line;
            Graph currentGraph = null;

            while ((line = reader.readLine()) != null) {
                // Check if the line indicates the start of a new graph.
                if (line.startsWith("** G")) {
                    // Create a new Graph object and add it to the ArrayList.

                    // Parse the vertices from the line and add them to the current graph.
                    int startIndex = line.indexOf("|V|=");

                    if (startIndex != -1) {
                        // Extract the substring after "|V|="
                        String substring = line.substring(startIndex + 4);

                        // Find the first space character in the substring
                        int endIndex = substring.indexOf(" ");

                        if (endIndex != -1) {
                            // Extract the number as a substring
                            String numString = substring.substring(0, endIndex);

                            try {
                                // Parse the substring as an integer and add it to the list
                                int numVertices = Integer.parseInt(numString);
                                currentGraph = new Graph(numVertices);
                                graphs.add(currentGraph);

                            } catch (NumberFormatException e) {
                                System.out.println("Unable to parse the number of vertices.");
                            }
                        }
                    }

                } else if (line.startsWith("(u, v, w) E = {")) {
                    // Parse the edges of the current graph until a delimiter is encountered.
                    while (!((line = reader.readLine().trim()).equals("----------------"))) {
                        String[] edge = line.trim().replace("(", "").replace(")", "").replace("}", "").split(",");
                        int src = Integer.parseInt(edge[0].trim());
                        int dst = Integer.parseInt(edge[1].trim());
                        double weight = Double.parseDouble(edge[2].trim());
                        currentGraph.addEdge(src, dst, weight);
                    }
                }
            }
        } catch (FileNotFoundException ife) {
            System.out.println("Unable to open file: " + fileName);
            System.out.println("Closing the program.");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number format in the input file.");
        } catch (IOException ioe) {
            System.out.println("Error reading the file: " + fileName);
        } finally {
            // Close the file reader if it's open.
            if (reader != null) {
                reader.close();
            }
        }

        // Return the ArrayList of parsed Graph objects.
        return graphs;
    }

    /**
     * The main method of the minSPT class that reads a file, finds minimum spanning
     * trees of the graphs using Kruskal's
     * and Prim's algorithms, and displays the results.
     *
     * @param args The command-line arguments. The first argument should be the name
     *             of the input file.
     *
     */

    public static void main(String[] args) {
        try {
            ArrayList<Graph> graphs = readFile(args[0]);
            // ArrayList<Graph> graphs=readFile("udwGs.txt");

            int count = 0;
            System.out.println("Minimum Spanning Trees of graphs in " + args[0] + "\r\n");

            // Iterate through all graphs
            for (Graph g : graphs) {
                count++;
                // System.out.print(count+"---->");
                System.out.println("** " + "G" + count + " :");

                ArrayList<Edge> mst = g.krushkalsMinimumSpanningTree();

                double totalWeight = 0.0;

                System.out.println("Kruskal’s algorithm:");

                for (Edge edge : mst) {
                    System.out.printf("\t(%d, %d, %.3f)\n", edge.source.vertex, edge.destination.vertex, edge.weight);
                    totalWeight += edge.weight;
                }
                System.out.printf("\t==> Total weight %.3f\n\n", totalWeight);

                double totalWeight1 = 0.0;
                ArrayList<Edge> mst1 = (g.primsMinimumSpanningTree());

                System.out.println("Prim’s algorithm:");

                for (Edge edge : mst1) {

                    System.out.printf("\t(%d, %d, %.3f)\n", edge.source.vertex, edge.destination.vertex, edge.weight);
                    totalWeight1 += edge.weight;
                }
                System.out.printf("\t==> Total weight %.3f\n\n", totalWeight1);

            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        System.out.println("\n*** Asg 3 by SAI KIRAN NANDIPATI.");
    }

}
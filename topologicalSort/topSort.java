
/**
 * created on September 3, 2023
 * implemented by Sai Kiran Nandipati

 */

import java.io.*;
import java.util.*;

/**
 * <Graph class is used to create obejects of type graphs
 * based on the vertices and the edhes provided and had some additional
 * functionalities
 * like obtains the topologicalorder of a DAG>
 *
 * @author <SAI KIRAN NANDIPATI>
 *
 */

class Graph {

    private ArrayList<Integer> vertexList;
    private ArrayList<ArrayList<Integer>> adjacencyList;
    private HashMap<Integer, Integer> indegreeMap;

    /**
     * constructor for the class Graph
     * ->initializes all the private variables declared in the Graph class.
     *
     */
    public Graph() {

        vertexList = new ArrayList<Integer>();
        adjacencyList = new ArrayList<ArrayList<Integer>>();
        indegreeMap = new HashMap<>();

    }

    /**
     * addVertex method add the vertex to the vertx list,adjecency listr and
     * indegree map.
     * 
     * @param vertex- a vertex of the graph of type integer
     */

    public void addVertex(int vertex) {
        vertexList.add(vertex);
        adjacencyList.add(new ArrayList<>());
        indegreeMap.put(vertex, 0);

    }

    /**
     * addEdge method will add an edge between two vertices
     * 
     * @param source                   -- source vertex
     * @param destination--destination vertex
     */

    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
        indegreeMap.put(destination, indegreeMap.get(destination) + 1); // Increase indegree of the destination
    }

    /**
     * getNeighbors method will gets all the neighbours of the provided vertex
     * 
     * @param vertex- a vertex of the graph of type integer
     */

    public ArrayList<Integer> getNeighbors(int vertex) {
        return adjacencyList.get(vertex);
    }

    /**
     * getVertexCount method gets the number of vertices in the graph
     * 
     * @param vertex- a vertex of the graph of type integer
     */

    public int getVertexCount() {
        return adjacencyList.size();
    }

    /**
     * getIndegree method gets the indegree of a vertex
     * 
     * @param vertex- a vertex of the graph of type integer
     */

    public int getIndegree(int vertex) {
        return indegreeMap.get(vertex);
    }

    /**
     * Finds and returns the topological order of vertices in a directed acyclic
     * graph (DAG).
     * If the graph is not acyclic, it returns an empty list.
     *
     * @return An ArrayList of integers representing the topological order of
     *         vertices.
     */

    public ArrayList<Integer> findTopologicalOrder() {
        ArrayList<Integer> topologicalOrder = new ArrayList<>();

        // Create a priority queue to store vertices with in-degree 0
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int vertex = 0; vertex < adjacencyList.size(); vertex++) {
            if (indegreeMap.get(vertex) == 0) {
                queue.offer(vertex); // adding element to the queue.
            }
        }
        if (queue.isEmpty()) {
            System.out.print(" No in-degree 0 vertices; not an acyclic graph.");
        } else {

            // Perform topological sorting
            while (!queue.isEmpty()) {

                int vertex = queue.poll();
                topologicalOrder.add(vertex);

                // Remove this vertex and update in-degrees of its neighbors
                ArrayList<Integer> neighbors = adjacencyList.get(vertex);

                for (int neighbor : neighbors) {
                    indegreeMap.put(neighbor, indegreeMap.get(neighbor) - 1);
                    if (indegreeMap.get(neighbor) == 0) {
                        queue.offer(neighbor);
                    }

                }

            }
        }

        // If the topological order size is less than the number of vertices, there is a
        // cycle
        if (topologicalOrder.size() > 0 && topologicalOrder.size() < adjacencyList.size()) {
            for (int i : topologicalOrder) {
                System.out.print(i + " ->");
            }

            System.out.print(" no more in-degree 0 vertex; not an acyclic graph.");
            return new ArrayList<>();
        }

        return topologicalOrder;
    }

}

/**
 * topSort Class will do the file handling part
 * and prints the topological order of all the graphs
 */

public class topSort {

    /**
     * Reads a file containing graph data and constructs Graph objects.
     *
     * @param fileName The name of the input file to read.
     * @return An ArrayList of Graph objects parsed from the file.
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
                    currentGraph = new Graph();
                    graphs.add(currentGraph);

                    // Parse the vertices from the line and add them to the current graph.
                    String[] vertices = line.substring(line.indexOf("{") + 1, line.indexOf('}')).trim().split(" ");
                    for (String vertex : vertices) {
                        currentGraph.addVertex(Integer.parseInt(vertex));
                    }
                } else if (line.startsWith("(u, v) E = {")) {
                    // Parse the edges of the current graph until a delimiter is encountered.
                    while (!((line = reader.readLine().trim()).equals("----------------"))) {
                        String[] edge = line.trim().replace("(", "").replace(")", "").replace("}", "").split(",");
                        int u = Integer.parseInt(edge[0].trim());
                        int v = Integer.parseInt(edge[1].trim());
                        currentGraph.addEdge(u, v);
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
     * Main method for topSort.
     * 
     * @param args-> gets the name of the file from the command line.
     */
    public static void main(String[] args) {
        try {
            ArrayList<Graph> graphs = readFile(args[0]);
            int count = 0;
            System.out.println("Topological Orders:");
            for (Graph g : graphs) {
                count++;
                System.out.print("G" + count + ": ");
                for (Integer item : g.findTopologicalOrder()) {

                    System.out.print(item + " ");
                }
                System.out.print("\n");
            }
            System.out.println("\n*** Asg 1 by SAI KIRAN NANDIPATI.");
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}

/**
 * created on September 16, 2023
 * implemented by Sai Kiran Nandipati
 */

/**
 * Copyright SAI KIRAN NANDIPATI September 16, 2023. All rights reserved.
 *
 * This code is the property of [Your Name or Company]. No part of this code
 * may be copied, modified, or distributed without the express written permission
 * of the copyright owner.
 */

import java.io.*;
import java.util.*;

/**
 * <Graph class is used to create objects of type graphs
 * based on the vertices and the edges provided and had some additional
 * functionalities
 * like gets the BFS and DFS graph traversal orders for a graph
 *
 * @author <SAI KIRAN NANDIPATI>
 *
 */

/*
 * September 16, 2023 4:00pm
 */
class Graph {

    private ArrayList<Integer> vertexList;
    private ArrayList<ArrayList<Integer>> adjacencyList;

    /**
     * constructor for the class Graph
     * ->initializes all the private variables declared in the Graph class.
     *
     */
    public Graph() {

        vertexList = new ArrayList<Integer>();
        adjacencyList = new ArrayList<ArrayList<Integer>>();

    }

    /**
     * addVertex method add the vertex to the vertex list,adjacency list and
     * indegree map.
     * 
     * @param vertex- a vertex of the graph of type integer
     */

    public void addVertex(int vertex) {
        vertexList.add(vertex);
        adjacencyList.add(new ArrayList<>());

    }

    /**
     * addEdge method will add an edge between two vertices
     * 
     * @param source                   -- source vertex
     * @param destination--destination vertex
     */

    public void addEdge(int source, int destination) {
        adjacencyList.get(source).add(destination);
        adjacencyList.get(destination).add(source);
        // Since the given graphs are undirected we are adding edhe from source to
        // destination and vice versa
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
     * Perform Breadth-First Search (BFS) starting from a given vertex.
     * date & time: sep 17th 3:00 pm
     * 
     * @param startVertex The starting vertex for BFS.
     * @return A list of vertices visited in BFS order.
     */

    public ArrayList<Integer> breadthFirstSearch(int startVertex) {
        ArrayList<Integer> visitedVertices = new ArrayList<>();
        Queue<Integer> BFSqueue = new LinkedList<>();
        boolean[] visited = new boolean[adjacencyList.size()]; // keeps track of visited vertices

        // Mark the start vertex as visited and enqueue it
        visited[startVertex] = true;
        BFSqueue.add(startVertex);

        while (!BFSqueue.isEmpty()) {
            int currentVertex = BFSqueue.poll();
            visitedVertices.add(currentVertex);

            // Visit all unvisited neighbors of the current vertex
            for (int neighbor : adjacencyList.get(currentVertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    BFSqueue.add(neighbor);
                }
            }
        }

        return visitedVertices;
    }

    /**
     * Performs a depth-first search (DFS) traversal of the graph starting from the
     * specified vertex.
     * date & time: sep 18th 10:00 am
     * 
     * @param startVertex The index of the vertex to start the DFS traversal from.
     * @return A list of visited vertices in the order they were encountered during
     *         the DFS traversal.
     */

    public ArrayList<Integer> deapthFirstSearch(int startVertex) {
        ArrayList<Integer> visitedVertices = new ArrayList<>();
        Stack<Integer> dfsStack = new Stack<>();
        boolean[] visited = new boolean[adjacencyList.size()];

        // Mark the start vertex as visited and enqueue it

        dfsStack.push(startVertex);

        while (!dfsStack.isEmpty()) {
            int currentVertex = dfsStack.pop();
            if (!visited[currentVertex]) {
                visited[currentVertex] = true;

                visitedVertices.add(currentVertex);

                // Visit all unvisited neighbors of the current vertex

                for (int i = adjacencyList.get(currentVertex).size() - 1; i >= 0; i--) {
                    int neighbor = adjacencyList.get(currentVertex).get(i);

                    dfsStack.push(neighbor);
                }
            }

        }

        return visitedVertices;
    }
}

/**
 * date & time: sep 16th 6:00 pm
 * The `graphcc` class is responsible for reading graph data from a file,
 * constructing Graph objects,
 * and performing connected components analysis using both Breadth-First Search
 * (BFS) and Depth-First Search (DFS).
 * It reads graph data from a specified input file and prints the connected
 * components of each graph
 * in the file along with their BFS and DFS traversals.
 */

public class graphcc {

    /**
     * Reads a file containing graph data and constructs Graph objects.
     * date & time: sep 18th 5:00 pm
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
                                for (int i = 0; i < numVertices; i++) {
                                    currentGraph.addVertex(i);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Unable to parse the number of vertices.");
                            }
                        }
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
     * Main method for graphcc.
     * date & time: sep 17th 6:00 pm
     * 
     * @param args-> gets the name of the file from the command line.
     */
    public static void main(String[] args) {
        try {
            ArrayList<Graph> graphs = readFile(args[0]);

            int count = 0;
            System.out.println("Connected components of graphs in" + args[0] + "\n");

            // Iterate through all graphs
            for (Graph g : graphs) {

                count++;
                System.out.println("** " + "G" + count + " ’s connected components:");

                // Initialize a boolean array to keep track of visited vertices
                boolean[] bfsVisited = new boolean[g.getVertexCount()];
                boolean[] dfsVisited = new boolean[g.getVertexCount()];
                System.out.println("Breadth First Search:");

                for (int vertex = 0; vertex < g.getVertexCount(); vertex++) {

                    if (!bfsVisited[vertex]) {
                        // Perform BFS starting from the unvisited vertex
                        ArrayList<Integer> bfsOrder = g.breadthFirstSearch(vertex);

                        for (Integer item : bfsOrder) {
                            System.out.print(item + " ");
                        }
                        System.out.println();

                        // Mark all visited vertices in this connected component for BFS
                        for (Integer visitedVertex : bfsOrder) {
                            bfsVisited[visitedVertex] = true;
                        }
                    }
                }

                System.out.println("Depth First Search:");

                for (int vertex = 0; vertex < g.getVertexCount(); vertex++) {

                    if (!dfsVisited[vertex]) {
                        // Perform DFS starting from the unvisited vertex
                        ArrayList<Integer> dfsOrder = g.deapthFirstSearch(vertex);

                        for (Integer item : dfsOrder) {
                            System.out.print(item + " ");
                        }
                        System.out.println();

                        // Mark all visited vertices in this connected component for DFS
                        for (Integer visitedVertex : dfsOrder) {
                            dfsVisited[visitedVertex] = true;
                        }
                    }
                }
                System.out.println();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
        System.out.println("\n*** Asg 1 by SAI KIRAN NANDIPATI.");
    }

}
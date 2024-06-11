# Graph Class and Topological Sorting

This repository contains Java code for a `Graph` class and a `topSort` class for performing topological sorting on directed acyclic graphs (DAGs). The `Graph` class allows the creation of graph objects and provides methods for adding vertices, edges, and finding the topological order of vertices in a DAG. The `topSort` class reads graph data from a file and prints the topological order of all graphs.

## Graph Class

The `Graph` class is used to create objects representing graphs. It provides the following functionalities:

- `addVertex(int vertex)`: Adds a vertex to the graph.
- `addEdge(int source, int destination)`: Adds an edge between two vertices.
- `getNeighbors(int vertex)`: Gets all the neighbors of a given vertex.
- `getVertexCount()`: Gets the number of vertices in the graph.
- `getIndegree(int vertex)`: Gets the indegree of a vertex.
- `findTopologicalOrder()`: Finds and returns the topological order of vertices in a DAG.

## topSort Class

The `topSort` class handles file input and prints the topological order of graphs read from the input file. It provides the following functionalities:

- `readFile(String fileName)`: Reads a file containing graph data and constructs `Graph` objects.
- `main(String[] args)`: Main method for executing topological sorting on graphs read from a file.

## Usage

To use the code, compile the Java files and run the `topSort` class with the name of the input file as a command-line argument.

```bash
javac topSort.java
java topSort input.txt

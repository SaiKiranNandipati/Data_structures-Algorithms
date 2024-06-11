# Minimum Spanning Tree Algorithms

This Java program implements two algorithms, Kruskal's algorithm and Prim's algorithm, for finding the minimum spanning tree (MST) of a graph. It also includes classes for graph representation and disjoint-set data structure.

## Implementation Details

- **Created**: September 26th, 2023
- **Implemented By**: Sai Kiran Nandipati
- **Last Updated**: September 27th, 2023

## Overview

The program consists of several classes:
- **Edge**: Represents an edge in a graph, storing the source vertex, destination vertex, and weight of the edge.
- **Vertex**: Represents a vertex in a graph, containing a unique identifier and a list of neighboring edges.
- **DisjointSet**: Represents a disjoint-set data structure used for implementing Kruskal's algorithm.
- **Graph**: Represents a graph data structure using an adjacency list representation. Provides methods for adding edges and computing minimum spanning trees using Kruskal's and Prim's algorithms.
- **minSPT**: Main class responsible for reading graph information from a file, finding minimum spanning trees using Kruskal's and Prim's algorithms, and displaying the results.

## Classes Overview

### Edge Class

- Represents an edge in a graph.
- Contains fields for source vertex, destination vertex, and weight.
- Provides a method to compare edges based on their weights.

### Vertex Class

- Represents a vertex in a graph.
- Contains fields for a unique identifier and a list of neighboring edges.

### DisjointSet Class

- Represents a disjoint-set data structure (union-find).
- Provides methods for finding the representative element of a set and merging sets.

### Graph Class

- Represents a graph data structure using an adjacency list.
- Provides methods for adding edges and computing minimum spanning trees using Kruskal's and Prim's algorithms.

### minSPT Class

- Responsible for reading graph information from a file, finding minimum spanning trees using Kruskal's and Prim's algorithms, and displaying the results.
- Parses input files containing graph data, constructs graphs, and computes MSTs using the implemented algorithms.

## Usage

1. **Compile**: Compile the Java source file `minSPT.java`.

   ```bash
   javac minSPT.java

   **Run**: Run the code
   ```bash
   java minSPT input.txt


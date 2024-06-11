# Minimum Arborescence Algorithm / Edmonds Algorithm


## Overview
This Java code implements the Minimum Arborescence algorithm to find minimum arborescences in graphs. The algorithm is based on Edmonds' algorithm and efficiently computes the minimum spanning arborescence rooted at a specified vertex.

## Implementation Details
- **Author:** Sai Kiran Nandipati
- **Created:** October 13th, 2023

## Usage
To use this code, follow these steps:

1. Compile the `Edmond.java` file using a Java compiler.
2. Run the compiled class file with the input file name as a command-line argument.

### Input File Format
The input file should contain the graph data in a specific format:
- Each graph is separated by a line starting with `**` followed by the number of vertices (e.g., `** nVertices = 5`).
- Each edge is represented as a line starting with `(`, followed by the source vertex, destination vertex, and edge weight (e.g., `(u, v, w)`).
- Each graph ends with a line starting with `--`.



## Methods
- `findUniqueCycles(fStar)`: Finds unique cycles in the given graph.
- `isArborescence(fStar, fStarReversed)`: Checks if the given graph is an arborescence.
- `readGraphFile(fileName)`: Reads a graph from a file and returns a list of graphs represented as adjacency lists.
- `mainRecursion(graph, verbose)`: Main recursion for finding minimum arborescences in the graph.
- `cleanFinalGraph(finalGraph, nVertices, count, runtime)`: Cleans the final graph, removes duplicate edges, and displays the result.





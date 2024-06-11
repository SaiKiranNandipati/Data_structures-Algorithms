# BFS & DFS

## Project Overview

This project involves the creation and manipulation of graph structures using adjacency lists. It includes functionalities for constructing graphs from input files, adding vertices and edges, and performing Breadth-First Search (BFS) and Depth-First Search (DFS) traversals. Additionally, it extracts and processes graph information from given input strings.

## Features

- **Graph Operations**:
  - Add vertices and edges to the graph.
  - Retrieve neighbors of a vertex.
  - Get the count of vertices in the graph.
  - Perform BFS and DFS traversals.
  
- **Graph Information Extraction**:
  - Extract the number of vertices from graph description strings.

## File Descriptions

- **`GraphInfoExtractor.java`**: Extracts the number of vertices from lines describing graphs.
- **`Graphcc.java`**: Main class for reading graph data from a file, constructing Graph objects, and performing connected components analysis using BFS and DFS.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher.

### Building the Project

1. **Clone the repository**:
    ```sh
    git clone https://github.com/SaiKiranNandipati/Data_structures-Algorithms.git
    cd BFS_DFS
    ```

2. **Compile the Java files**:
    ```sh
    javac GraphInfoExtractor.java
    javac Graphcc.java
    ```

### Running the Graph Information Extractor

1. **Run the GraphInfoExtractor**:
    ```sh
    java GraphInfoExtractor
    ```

   This will process the sample input lines and print the extracted number of vertices.

### Running the Graph Processing and Traversal

1. **Prepare an input file** (e.g., `graph_data.txt`) with the following format:
    ```
    ** G1: |V|=10  V={0,1,...,9}
    (u, v) E = {
    (0,1)
    (1,2)
    ...
    }
    ----------------
    ```

2. **Run the Graphcc program**:
    ```sh
    java graphcc udGraphs.txt
    ```

   This will read the graph data from the specified file, perform BFS and DFS traversals, and print the connected components of each graph along with their traversal orders.

## Example

### Sample Input (udGraphs.txt):

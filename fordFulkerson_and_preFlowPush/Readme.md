# Maximum Flow Algorithms

This project contains implementations of two maximum flow algorithms: Ford-Fulkerson's algorithm and the Preflow-Push algorithm. Both algorithms are used to find the maximum flow in a flow network graph.

## Contents

1. [Introduction](#introduction)
2. [Implementation](#implementation)
3. [Usage](#usage)
4. [File Structure](#file-structure)
5. [References](#references)

## Introduction

Maximum flow algorithms are used to find the maximum amount of flow that can be sent from a source node to a sink node in a flow network. The Ford-Fulkerson algorithm and the Preflow-Push algorithm are two classic approaches to solving this problem.

## Implementation

### Ford-Fulkerson Algorithm

The Ford-Fulkerson algorithm is implemented in Java and consists of two main classes:

- `FordFulkerson.java`: This class contains the implementation of the Ford-Fulkerson algorithm. It includes methods to construct a flow network graph, find augmenting paths, and update the flow along these paths until no more augmenting paths can be found.

- `Graph.java`: This class represents the flow network graph and contains methods for adding edges, finding paths, augmenting flows, and displaying the graph.

### Preflow-Push Algorithm

The Preflow-Push algorithm is also implemented in Java and consists of two main classes:

- `preflowPush.java`: This class contains the implementation of the Preflow-Push algorithm. It includes methods to perform the push operation and the relabel operation until no more excess flow remains in the graph.

- `Graph.java`: Similar to the Ford-Fulkerson implementation, this class represents the flow network graph and contains methods for adding edges, updating flows, and displaying the graph.

## Usage

To use the maximum flow algorithms:

1. Compile the Java files using a Java compiler.
2. Run the compiled Java files, providing the input file containing the flow network graph as a command-line argument.

Example usage:

```bash
java FordFulkerson input.txt
java preflowPush input.txt

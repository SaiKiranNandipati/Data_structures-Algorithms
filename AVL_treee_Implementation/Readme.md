# AVL Tree Dictionary and Spell Checker

## Project Overview

This project implements a dictionary using an AVL Tree data structure. The dictionary supports basic operations such as adding entries, finding entries, and printing the dictionary in order. Additionally, the project includes a spell checker program that checks the spelling of words in a text file against the dictionary and provides suggestions for misspelled words.

## Features

- **Dictionary Operations**: 
  - Add an entry to the dictionary
  - Find an entry in the dictionary
  - Print the dictionary in order
  - Print the structure of the dictionary

- **Spell Checker**:
  - Checks the spelling of words in an input file
  - Provides suggestions for misspelled words by:
    - Adding one letter to the word
    - Removing one letter from the word
    - Exchanging adjacent characters

## File Descriptions

- **`Dictionary.h`**: Header file for the Dictionary class, defining the data structure and public/private methods.
- **`Dictionary.cpp`**: Implementation file for the Dictionary class, including methods for adding, finding, and printing entries, as well as AVL Tree balancing operations.
- **`startingspellchecker.cpp`**: Main program file for the spell checker. It reads input files, builds the dictionary, checks the spelling of words, and provides suggestions for corrections.

## Getting Started

### Prerequisites

- C++ compiler (e.g., g++, clang)
- Standard C++ library

### Building the Project

1. **Clone the repository**:
    ```sh
    git clone https://github.com/SaiKiranNandipati/Data_structures-Algorithms.git
    cd /AVL_treee_Implementation
    ```

2. **Compile the source files**:
    ```sh
    g++ -o spellchecker startingspellchecker.cpp Dictionary.cpp
    ```

### Running the Spell Checker

1. **Prepare the input files**:
   - `wordListFile.txt`: A list of correctly spelled words, one per line.
   - `inputFile.txt`: A file containing text to be spell-checked.
   - `dictStructureFile.txt`: An output file to store the structure of the dictionary (for grading purposes).

2. **Run the spell checker**:
    ```sh
    ./spellchecker wordListFile.txt inputFile.txt dictStructureFile.txt
    ```

### Example

Given 
-'wordListFile.txt': dictStructure-avl-large.txt
-'inputFile.txt': test-dictionary-avl.txt

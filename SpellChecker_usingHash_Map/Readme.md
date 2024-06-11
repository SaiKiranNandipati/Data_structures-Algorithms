# Spell Checker using Hash Dictionary

## Overview
This project implements a spell checker using a hash dictionary. The program takes three command line arguments: two input files and one output file. The input files consist of a list of correctly spelled words and a file to check for correct spelling. The output file contains the structure of the dictionary created for grading purposes. The program outputs a list of incorrectly spelled words along with suggestions for each word.

## Files
- `Hash_Dictionary.cpp`: Implementation file for the Dictionary class.
- `Hash_Dictionary.h`: Header file for the Dictionary class.
- `startingSpellChecker.cpp`: Main program to perform spell checking.
- `test_dictionary.cpp`: Test file to verify the functionality of the Dictionary class.

## Dictionary Class
The `Dictionary` class uses a hash table to store entries. It supports the following operations:
- **Add an Entry**: Adds a new entry to the dictionary.
- **Find an Entry**: Checks if an entry exists in the dictionary.
- **Print Dictionary In Order**: Prints all entries in the dictionary in alphabetical order.
- **Print Dictionary Keys**: Prints the structure of the dictionary.
- **Clear**: Clears the dictionary.
- **Copy**: Creates a deep copy of the dictionary.
- **Rehash**: Rehashes the dictionary to accommodate more entries.

## Spell Checker Program
The `startingSpellChecker.cpp` file contains the main function which performs the following tasks:
1. **Build Dictionary**: Reads the list of correctly spelled words from an input file and builds the dictionary.
2. **Check Spelling**: Reads the file to check for correct spelling and outputs the incorrectly spelled words along with suggestions.
3. **Write Dictionary Structure**: Writes the structure of the dictionary to an output file.

### Validation_script.sh
  The `Validation_script.sh` helps to validate whether the programe is correctly written or not!

## Usage
To compile and run the program, follow these steps:

### Compile
```sh
g++ -o spellChecker startingSpellChecker.cpp Hash_Dictionary.cpp -std=c++11
### Run
./spellChecker wordListFile.txt inputFile.txt outputFile.txt
### Test
g++ -o testDictionary test_dictionary.cpp Hash_Dictionary.cpp -std=c++11
./testDictionary
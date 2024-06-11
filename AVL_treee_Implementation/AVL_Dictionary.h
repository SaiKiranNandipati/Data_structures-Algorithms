// An dictionary created using a basic binary search tree
// By Sai Kiran Nandipati
#include <string>

#ifndef DICTIONARY_H
#define DICTIONARY_H

class Dictionary
{
private:
    struct Node // struct because I want everything to be public within the Node
    {
        std::string* data;
        Node* left;
        Node* right;

        Node(std::string*& theData) : data(theData), left(nullptr), right(nullptr) {}
    };

    Node* root;

    // any additional instance variable declarations go here

public:
    // Constructor
    Dictionary() : root(nullptr) {}

    // Copy constructor
    Dictionary(const Dictionary& orig) { this->root = copy(orig.root); }

    // Destructor
    ~Dictionary() { clear(root); }

    // Assignment operator
    Dictionary& operator=(const Dictionary& rhs);

    // Check to see if the Dictionary isEmpty
    bool isEmpty() const { return root == nullptr; }

    // Add an entry
    // Precondition: the dictionary must not have the identical string already stored in the dictionary
    // Postcondition: the string has been added to the dictionary
    void addEntry(std::string* anEntry);

    // Determine whether the string is in the dictionary
    // Returns true if the string is stored in the dictionary and false otherwise
    bool findEntry(const std::string& key) const;

    // Print entries in order
    // Calls printEntry on each Entry in order
    void printDictionaryInOrder(std::ostream& outputStream) const;

    // Prints the dictionary keys only, demonstrating the dictionary structure
    // For the binary search tree, this is an easy to do tree format
    void printDictionaryKeys(std::ostream& outputStream) const;

private:
    // clear helper method for copy constructor and assignment operator
    void clear(Node* node);

    // copy helper method for destructor and assignment operator
    Node* copy(Node* origNode);

    // recursive helper method for insertion
    void insert(std::string* value, Node*& curPtr);

    // recursive helper for printDictionaryInOrder
    void printInOrder(std::ostream& outputStream, Node* curNode) const;

    // tree printer helper -- recursive function to print the tree structure
    void printTree(std::ostream& outputStream, Node* curNode, int depth) const;

    // any additional function prototypes go here

    Node* findParent(const std::string& key) const;
    
    int get_height(Node* node);

    int get_difference(Node*& curNode);

    Node* balance(Node*& curNode);
    
    Node* rr_rotate(Node* curNode);
    
    Node* ll_rotate(Node* curNode);
};

#endif
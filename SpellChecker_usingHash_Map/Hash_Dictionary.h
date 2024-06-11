// An dictionary created using a basic binary search tree
// By Sai Kiran Nandipati
#include <string>

#ifndef DICTIONARY_H
#define DICTIONARY_H

class Dictionary
{
private:
    int table_size, total_keys, R;
    int current_iter;
    std::string** root;
    const int list_of_table_size[14] = {101, 211, 431, 863, 1733, 3469, 6947, 13901, 27803, 55609, 111227, 222461, 444929, 889871};
    const int list_of_R[14] = {97, 199, 421, 859, 1723, 3467, 6917, 13883, 27799, 55603, 111217, 222437, 444901, 889829};
    

public:
    // Constructor
    Dictionary() {
        total_keys = 0;
        current_iter = 0;
        set_size_and_prime();
        root = new std::string*[table_size]();
    }

    // Copy constructor
    Dictionary(const Dictionary& orig) { 
        this->root = copy(orig.root, orig.table_size); 
        this->table_size = orig.table_size;
        this->total_keys = orig.total_keys;
        this->R = orig.R;
        this->current_iter = orig.current_iter;
    }

    // Destructor
    ~Dictionary() { clear(); }//root); }

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
    void clear();//Node* node);

    // copy helper method for destructor and assignment operator
    std::string** copy(std::string** root, int table_size);

    // recursive helper method for insertion
    void insert(std::string* value);//, Node*& curPtr);

    // recursive helper for printDictionaryInOrder
    void printInOrder(std::ostream& outputStream) const;//, Node* curNode) const;

    // tree printer helper -- recursive function to print the tree structure
    void printTree(std::ostream& outputStream) const;//, Node* curNode, int depth) const;

    // any additional function prototypes go here
    void set_size_and_prime();

    unsigned int string_hash(const std::string& key);

    void rehash();
    
};

#endif
// Implementation file for the Dictionary class
// Stores data in a basic binary search tree
// By Sai Kiran Nandipati

#include "Dictionary.h"
#include <vector>
using namespace std;

Dictionary& Dictionary::operator=(const Dictionary& rhs)
{
    this->root = copy(rhs.root, rhs.table_size); 
    this->table_size = rhs.table_size;
    this->total_keys = rhs.total_keys;
    this->R = rhs.R;
    this->current_iter = rhs.current_iter;
    return *this;
}

void Dictionary::addEntry(string* anEntry)
{
    // call private recursive insert method
    insert(anEntry);
}

bool Dictionary::findEntry(const string& key) const
{
    for(int i=0;i<table_size;i++)
        if(root[i])
            if(*(root[i])==key)
                return true;
    return false;
}

void Dictionary::printDictionaryInOrder(ostream& outputStream) const
{
    // if (root)
    if(total_keys>0)
        printInOrder(outputStream);//, root);
}

void Dictionary::printDictionaryKeys(ostream& outputStream) const
{
    if(total_keys>0)
        printTree(outputStream);//, root, 0);
}

// this is doing a postOrder traversal of the tree, deleting the string and then Node in each node of the tree
// the Dictionary is taking responsibility for the Entry objects, because they have to be dynamically allocated
// and only the tree has them all
void Dictionary::clear()
{
    for(int i=0;i<table_size;i++){
        if(root[i]){
            delete root[i];
            root[i] = nullptr;
        }
    }
    delete[] root;
}

// this is doing a postOrder traversal of the original tree, making a copy of each node
string** Dictionary::copy(string** root, int table_size)
{
    if (root)
    { 
        string** newRoot = new string*[table_size]();

        for(int i=0;i<table_size;i++){
            if(root[i]){
                newRoot[i] = new string(*(root[i]));
                *(newRoot[i]) = *(root[i]);
            }
            else
                newRoot[i] = nullptr;
        }
        return newRoot;
    }
    else
    {
        return nullptr;
    }
}

void Dictionary::insert(string* anEntry)
{
    // Rehash immediately before adding an entry that will push the table above 50% full.
    if(total_keys+1 > table_size/2)
        rehash();

    // find string hash
    unsigned int hash_x = string_hash(*anEntry);
    // find offset
    int hash_2 = 1 + (hash_x%R);

    unsigned int location = hash_x%table_size;

    // keep probing till empty or increase the size.
    while(root[location]!=nullptr)
        location = (location + hash_2)%table_size;

    root[location] = anEntry;
    total_keys++;
}

// this is doing an inOrder traversal of the tree, calling printEntry on each entry in the tree
void Dictionary::printInOrder(ostream& outputStream) const//, Node* curNode) const
{
    // copy the table to a vector , sort this and then print the dictionary
    vector<string> table_copy;
    for(int i=0;i<table_size;i++){
        if(root[i]){
            table_copy.push_back(*(root[i]));
        }
    }
    sort(table_copy.begin(), table_copy.end());

    string endOfLine = "\n"; // dealing with weird compiler issue
    string filler = ": ";
    for(int i=0;i<table_copy.size();i++){
        outputStream<<table_copy[i]<<endOfLine;
    }
}

// this is doing a preOrder traversal of the tree, printing it out in a way that shows the structure
void Dictionary::printTree(ostream& outputStream) const//, Node* curNode, int depth) const
{
    string endOfLine = "\n"; // dealing with weird compiler issue
    string filler = ": ";
    for(int i=0;i<table_size;i++){
        outputStream<<std::to_string(i)<<filler;
        if(root[i])
            outputStream<<*(root[i]);
        outputStream<<endOfLine;
    }
}

// additional private function implementations go here

void Dictionary::set_size_and_prime(){
    table_size = list_of_table_size[current_iter];
    R = list_of_R[current_iter];
    current_iter++;
}

void Dictionary::rehash(){
    // keep track of old size and old root
    int old_table_size = table_size;
    string** old_root = root;

    // update new size and new hash key
    set_size_and_prime();

    // initialize a new array and total keys in them
    root = new string*[table_size]();
    total_keys = 0;


    // add older element to new location
    for(int i=0;i<old_table_size;i++)
        if(old_root[i]){
            // rehash the string to new array
            addEntry(old_root[i]);
            old_root[i] = nullptr;
        }

    // free the older root
    delete[] old_root;
}

unsigned int Dictionary::string_hash(const string& key){
    unsigned int hashVal = 0;
    for( char ch : key )
        hashVal = 37 * hashVal + ch;

    return hashVal ;//% table_size;
}
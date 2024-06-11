// Program to perform spell checking
// The program will take 3 command line parameters, the names of two input files and one output file.
// The input files will be
//      1) a list of correctly spelled words, which will be stored in a dictionary
//      2) a file to check for correct spelling
// The output file will show the structure of the dictionary that was created (done for grading purposes)
// The program will output a list of incorrectly spelled words and suggestions for each word.
// By Sai Kiran Nandipati

#include "Dictionary.h"
#include <iostream>
#include <fstream>
#include <ctype.h>
using namespace std;

// function prototypes

// opens and reads the word list input file, building the dictionary
void buildDictionary(const string& inFileName, Dictionary& dict);

// opens the input file and checks the spelling of the input file, producing output to standard out
void checkSpelling(const string& inFileName, const Dictionary& dict);

// open the dictionary structure file and write the structure of the dictionary to it
void writeDictionaryStructure(const string& outFileName, const Dictionary& dict);

// your additional function prototypes go here
int suggest1(int wordLength, Dictionary dict, string curWord, int tot_correction);
int suggest2(int wordLength, Dictionary dict, string curWord, int tot_correction);
int suggest3(int wordLength, Dictionary dict, string curWord, int tot_correction);

int main(int argc, char** argv)
{
    // gather the file name arguments
    if (argc < 4)
    {
        // we didn't get enough arguments, so complain and quit
        cout << "Usage: " << argv[0] << " wordListFile inputFile dictionaryStructureFile" << endl;
        exit(1);
    }

    string wordListFileName = argv[1];
    string inputFileName = argv[2];
    string dictFileName = argv[3];

    Dictionary dict; // set up the dictionary

    // build the dictionary
    buildDictionary(wordListFileName, dict);
    
    // write the dictionary to the concordance file
    checkSpelling(inputFileName, dict);

    // write the dictionary structure to the dictionary structure file
    writeDictionaryStructure(dictFileName, dict);
}

void buildDictionary(const string& inFileName, Dictionary& dict)
{
    string curWord = "";

    // open the file
    ifstream infile(inFileName);
    if (infile.fail())
    {
        cerr << "Could not open " << inFileName << " for input" << endl;
        exit(1);
    }

    // YOUR CODE GOES HERE -- feel free to also add additional function(s) if desired
    char ch;
    int flag = 0;
    while(infile){ 
        infile.get(ch);
        ch = ch>='A' && ch<='Z'?tolower(ch):ch;
        if(ch>='a' && ch<='z'){
            if(flag==0)
                flag=1;
            curWord += ch;
        }
        else{
            if(flag==1){
                flag=0;

                // add to dictionary
                if(dict.findEntry(curWord)==false){
                    string* newString = new string;
                    *newString = curWord;
                    dict.addEntry(newString);
                }
                // reset the buffer
                curWord = "";
            }
        }
    }
    // close the file
    infile.close();
}

void checkSpelling(const string& inFileName, const Dictionary& dict)
{
    string curWord = "";
    int lineNo = 1;
    ifstream infile(inFileName);
    if (infile.fail())
    {
        cerr << "Could not open " << inFileName << " for input\n";
        return;
    }
    char ch;
    int flag = 0;
    int wordLength = 0;
    // YOUR CODE GOES HERE -- you will want to add additional function(s) to further break down this logic 
    
    while(infile){

        infile.get(ch);
        ch = ch>='A' && ch<='Z'?tolower(ch):ch;
        if(ch>='a' && ch<='z'){
            if(flag==0)
                flag=1;
            curWord += ch;
            wordLength++;
        }
        else{
            if(flag==1){
                flag=0;
                // do word checks and suggestions
                if(dict.findEntry(curWord)==false){
                    cout<<curWord<<" on line "<<lineNo<<endl;
                    int tot_correction = 0;
                    tot_correction = suggest1(wordLength, dict, curWord, tot_correction);
                    tot_correction = suggest2(wordLength, dict, curWord, tot_correction);
                    tot_correction = suggest3(wordLength, dict, curWord, tot_correction);

                    if(tot_correction==0)
                        cout<<"No suggestions found"<<endl;
                }
                curWord = "";
                wordLength = 0;
                // cout<<endl;
            }
        }
        lineNo = ch=='\n'?lineNo+1:lineNo;
    }
    infile.close();
}

void writeDictionaryStructure(const string& outFileName, const Dictionary& dict)
{
    ofstream outfile(outFileName);
    if (outfile.fail())
    {
        cerr << "Could not open " << outFileName << " for output\n";
        cerr << "Dictionary structure file not written " << endl;
        return;
    }
    dict.printDictionaryKeys(outfile);
    outfile.close();
}

// your additional function implementations go here
int suggest1(int wordLength, Dictionary dict, string curWord, int tot_correction){
    // 1. Add one letter to the word (at any position)
    for(int i=0;i<wordLength;i++){
        for(char ch='a';ch<='z';ch++){
            // add char ch at position i of string currWord
            if(dict.findEntry(curWord.substr(0, i)+ch+curWord.substr(i, wordLength-i))){
                tot_correction++;
                if(tot_correction==1)
                    cout<<"Suggested corrections:"<<endl;
                cout<<"\t"<<curWord.substr(0, i)+ch+curWord.substr(i, wordLength-i)<<endl;
            }
        }
    }
    return tot_correction;
}

int suggest2(int wordLength, Dictionary dict, string curWord, int tot_correction){
    // 2. Remove one letter from the word
    for(int i=0;i<=wordLength;i++){
        // no need of removing same characters
        if(i>0 && curWord[i]==curWord[i-1]){
            continue;
        }
        if(dict.findEntry(curWord.substr(0, i-1)+curWord.substr(i, wordLength-1))){
                tot_correction++;
                if(tot_correction==1)
                    cout<<"Suggested corrections:"<<endl;
                cout<<"\t"<<curWord.substr(0, i-1)+curWord.substr(i, wordLength-1)<<endl;
            }
    }
    return tot_correction;
}

int suggest3(int wordLength, Dictionary dict, string curWord, int tot_correction){
    // 3. Exchange adjacent characters
    for(int i=0;i<wordLength-1;i++){
        if(curWord[i]!=curWord[i+1]){
            if(dict.findEntry(curWord.substr(0, i)+curWord[i+1]+curWord[i]+curWord.substr(i+2, wordLength-i))){
                tot_correction++;
                if(tot_correction==1)
                    cout<<"Suggested corrections:"<<endl;
                cout<<"\t"<<curWord.substr(0, i)+curWord[i+1]+curWord[i]+curWord.substr(i+2, wordLength-i)<<endl;
            }
        }
    }
    return tot_correction;
}
#!/bin/bash
# # # # # # # # # # # #
# Nick Dunne          #
# IT 279 - Program 3  #
# # # # # # # # # # # #
# Created in F2020 and edited in S2021 to provide a temporary testing environment for students.

dictIndex=(        0                                              )
dictionaries=(    "BST_Dictionary" "AVL_Dictionary" "Hash_Dictionary" )
dictionaryNames=(                "bst"                      "avl"            "hash" )

#Send message
echo "##########################################################################################################################"
echo "This is a validation script you can use for IT 279 to verify your files will compile correctly."
echo "This script is meant for Program 3 and will only work for Program 3."
echo "You will give the name of a ZIP file, and this script will attempt to extract and compile it for you."
echo "Created by Nick Dunne, for IT 279 with Professor Califf."
echo "##########################################################################################################################"
echo "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
echo "NOTES:"
echo "This program does not check whether you have submitted extra files."
echo "Do NOT submit any extra files. It may have an impact on your grade."
echo "All includes of the dictionary must be done with #include \"Dictionary.h\" so that the script grabs the right header file."
echo "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"

#Input file directory and output file directory
inputdir=.
tempdir=./IT279temp
stufiles=./IT279temp/stu_files
compiledir=./IT279temp/compiledir

#Setting default compilation parameters. Expected that students don't follow instructions
compileDictionary=""
compileMainProgram="startingSpellChecker.cpp"
compileTestProgram="test_dictionary.cpp"
programName="spellchecker"

#avl/hash pattern
yesPattern='^[Yy](.*)'

#Delete temp directory
rm -rf $tempdir
echo "Creating temporary directory..."
mkdir "$tempdir"
mkdir "$stufiles"
mkdir "$compiledir"

echo "--------"
echo "##### To save time, you are able to type the ZIP file name in as a parameter to this script. That is the only parameter."

if ! [ "$1" ]; then
    read -p "##### Enter the path of a ZIP file: " zip_file
else
    zip_file="$1"
    echo "##### ZIP name grabbed from parameters."
fi

if [ -f $zip_file ]; then
    echo "##### Unzipping submission contents..."
    unzip $zip_file -d "${stufiles}/" 1> /dev/null

    #Now, copy all of the necessary files to the compile directory
    cp $inputdir/*.cpp $compiledir/
    cp $inputdir/*.h $compiledir/

    read -p "##### Will you be submitting a AVL tree dictionary? (Y/N): " prompt_avl
    read -p "##### Will you be submitting a Hash table dictionary? (Y/N): " prompt_hash
    echo

    if [[ "$prompt_avl" =~ $yesPattern ]]; then
        dictIndex+=(1);
    fi

    if [[ "$prompt_hash" =~ $yesPattern ]]; then
        dictIndex+=(2);
    fi

    # Move the BST dictionary to the stu files directory to work with the for loop
    mv $compiledir/Dictionary.cpp $stufiles/BST_Dictionary.cpp
    mv $compiledir/Dictionary.h $stufiles/BST_Dictionary.h

    # Copy the other needed files for the dictionary programs
    if [ -f "$stufiles/startingSpellChecker.cpp" ];
        cp $stufiles/startingSpellChecker.cpp $compiledir/
    then
        pushd $compiledir 1> /dev/null

        for index in ${dictIndex[@]}; do
            #Keep track of whether the files are correctly named
            namedCorrectly=1

            for ext in ".h" ".cpp"; do
                #Grab the name we need to compare with
                curFile="${dictionaries[index]}${ext}"

                #Make sure that the files are named correctly.
                if ! [ -f "${stufiles}/${curFile}" ]; then
                    echo "##### $curFile was not found, the file is missing or the name isn't correct. Please fix and retry the script."
                    echo -e "##### If you have submitted your file(s) in a folder inside the ZIP file, it will not be found correctly.\n"
                    namedCorrectly=0
                else
                    cp "${stufiles}/${curFile}" "${compiledir}/Dictionary${ext}"
                fi
            done

            if [ $namedCorrectly = 1 ]; then
                #By this point we know we can compile the program
                echo "##### Compiling ${dictionaryNames[index]}/$programName..."
                g++ -o 0_${dictionaryNames[index]}.out Dictionary.cpp $compileDictionary $compileMainProgram
                
                exitcode="$?"

                if [ $exitcode = 0 ]; then
                    echo "##### Compile successful for ${dictionaryNames[index]}/$programName."
                else
                    echo "##### Compiler errors detected for ${dictionaryNames[index]}/$programName, there is an issue with your code."
                fi

                #Compile the test programs as well
                if ! [ $index = 0 ]; then
                    echo "##### Compiling ${dictionaryNames[index]}/test..."
                    g++ -o 0_test_${dictionaryNames[index]}.out Dictionary.cpp $compileDictionary $compileTestProgram

                    exitcode="$?"

                    if [ $exitcode = 0 ]; then
                        echo "##### Compile successful for ${dictionaryNames[index]}/test."
                    else
                        echo "##### Compiler errors detected for ${dictionaryNames[index]}/test, there is an issue with your code."
                    fi

                fi
            fi

            rm -f Dictionary.*
        done
        popd 1> /dev/null

        echo "##### Script complete... cleaning up."

        echo "##### As a reminder, this is NOT a guarantee that your program works. This is just a helper you can use to do a simple compile test."
        echo "##### If your programs were unable to compile with this script, you must fix them before you submit your code. Please retry the script with changes."
    else
        echo "##### Your startingSpellChecker.cpp file was not found. Please add/rename the file and retry the script."
    fi
else
    echo "##### Your ZIP file was not found. Please check the full file path of your zip and try again."
fi

#Delete temp directory
rm -rf $tempdir
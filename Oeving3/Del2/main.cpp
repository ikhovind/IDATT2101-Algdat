#include <iostream>
#include "binaryTree.cpp"
#include <cmath>

string multiplyString(string string1, int factor){
    string temp = string1;
    for(int i = 1; i<factor; i++){
        string1+=temp;
    }
    return string1;
}
int main()
{
    using namespace std;
    static binaryTree* root2 = NULL;
    //side 123
    //https://www.geeksforgeeks.org/level-order-tree-traversal/
    string words;
    cout << "Enter words separated by a space:\n";
    //cin >> words;
    string tempword = "";
    binaryTree* root;
    root = insertNode(NULL, "hode");
    insertNode(root, "bein");
    insertNode(root, "hals");
    insertNode(root, "arm");
    insertNode(root, "tann");
    insertNode(root, "hånd");
    insertNode(root, "tå");
    string test = "hånd";
    string test1 = "hond";
    //cout << (test > test1) <<"\n";
    //cout << root->rightChild->leftChild->leftChild->value << "\n";
    for(int i = 1; i<5; i++){
        cout << printGivenLevel(root,i, 64) << "\n";
    }
    /*
    //method to enter words and insert them into the tree
    string display = "";
    for(int i = 1; i<5; i++){
        //cout << 64/pow(2,i-1) << "\n";
        int j;
        for(int k = 0; k < printGivenLevel(root, i).size();k++){
            j = 64/pow(2,i-1);
            display += multiplyString(" ", (j/2));
            display += printGivenLevel(root,i).at(k);
            display += multiplyString(" ", (j/2));
        }
        display += "\n";
        //display += " \n";
    }
    cout << display;
     */
}



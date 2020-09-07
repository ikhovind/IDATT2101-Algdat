#include <iostream>
#include "binaryTree.cpp"
int main()
{
    using namespace std;
    static binaryTree* root2 = NULL;
    //side 123
    //https://www.geeksforgeeks.org/level-order-tree-traversal/
    //string words;
    cout << "Enter words separated by a space:\n";
    binaryTree* root = insertNode(root2, "b");
    insertNode(root, "c");
    insertNode(root, "d");
    insertNode(root, "e");
    insertNode(root, "a");
    //fungerer for 1, 2, 3, men ikke 4
    cout << printGivenLevel(root,1);
}

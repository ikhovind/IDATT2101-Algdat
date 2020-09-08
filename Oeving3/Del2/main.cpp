#include <iostream>
#include "binaryTree.cpp"
#include <cmath>

int main(int argc, char *argv[])
{
    using namespace std;
    static binaryTree* root2 = NULL;
    binaryTree* root;

    root = insertNode(NULL, argv[1]);

    for(int i = 2; i< argc; i++){
        insertNode(root, argv[i]);
    }

    for(int i = 1; i<5; i++){
        cout << printGivenLevel(root,i, 64) << "\n";
    }
}
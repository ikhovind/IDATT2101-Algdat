#include <iostream>
using namespace std;
struct binaryTree{
    binaryTree* parent;
    binaryTree* rightChild;
    binaryTree* leftChild;
    string value;
};
binaryTree* insertNode(binaryTree* rootPointer, string value){
    binaryTree* node = new binaryTree;
    node->value = value;
    if(rootPointer == NULL){
        rootPointer = node;
        binaryTree* compareToNode = rootPointer;
    }
    else{
        binaryTree* compareToNode = rootPointer;
        //sammenligner hver karakter med hver karakter i hodet og påfølgende noder
        int i = 0;
        while (i < node->value.size()){
            //if it comes before
            if(node->value.at(i) < compareToNode->value.at(i) && compareToNode->leftChild == NULL){
                compareToNode->leftChild = node;
                node->parent = compareToNode;
                return node;
            } else if (node->value.at(i) < compareToNode->value.at(i) && compareToNode->leftChild != NULL){
                compareToNode = compareToNode->leftChild;
            }
            //if it comes after and the right child is null it will be the new right child
            else if(compareToNode->rightChild == NULL && node->value.at(i) > compareToNode->value.at(i)){
                compareToNode->rightChild = node;
                node->parent = compareToNode;
                return node;
            }
            //if the new node comes after and the right child is not null we compare to the right child
            else if(node->value.at(i) > compareToNode->value.at(i)){
                compareToNode = compareToNode->rightChild;
            }
            //for loop only increases if previous char was equal
            if(node->value.at(i) == compareToNode->value.at(i)){
                i++;
            }
            if(i == node->value.size() -1){
                if(compareToNode->rightChild == NULL){
                    compareToNode->rightChild = node;
                    node->parent = compareToNode;
                    return node;
                }
                else{
                    compareToNode = compareToNode->rightChild;
                }
                i = 0;
            }
        }
    }
}


string printGivenLevel(binaryTree* root, int level) {
    string levelToString = "";
    if (root == NULL && level == 1) {
        cout << "test\n";
        levelToString += " ";
    }
    else if (level == 1){
        levelToString+= root->value;
    }
    if (level > 1) {
        if(root->leftChild != NULL){
            levelToString += printGivenLevel(root->leftChild, level - 1);
        }
        else{
            levelToString += printGivenLevel(NULL, level - 1);

        }
        if(root->rightChild != NULL){
            levelToString += printGivenLevel(root->rightChild, level - 1);
        }
        else{
            levelToString += printGivenLevel(NULL, level - 1);
        }
    }
    return levelToString;
}

string formatNode(int width, binaryTree* currentNode) {
    string toString = "";
    if(width < 8) return toString;
    cout << "bredden er: " << width << "\n";

    if(currentNode == NULL) {
        toString += " ";
    }
    else{
        toString += currentNode->value;
    }
    if(currentNode == NULL){
        toString +=formatNode(width / 2, NULL);
        toString += formatNode(width / 2, NULL);
    }
    else{
        toString +=formatNode(width / 2, currentNode->leftChild);
        toString += formatNode(width / 2, currentNode->rightChild);
    }
    return toString;
}


string getLevelOrder(binaryTree* rootPointer) {
    binaryTree *currentNode = rootPointer;
    string toString = "";

    return formatNode(64, rootPointer);

}




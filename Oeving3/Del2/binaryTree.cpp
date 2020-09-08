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
            if(node->value < compareToNode->value && compareToNode->leftChild == NULL){
                compareToNode->leftChild = node;
                node->parent = compareToNode;
                return node;
            } else if (node->value < compareToNode->value && compareToNode->leftChild != NULL){
                compareToNode = compareToNode->leftChild;
            }
            //if it comes after and the right child is null it will be the new right child
            else if(compareToNode->rightChild == NULL && node->value > compareToNode->value){
                compareToNode->rightChild = node;
                node->parent = compareToNode;
                return node;
            }
            //if the new node comes after and the right child is not null we compare to the right child
            else if(node->value > compareToNode->value){
                compareToNode = compareToNode->rightChild;
            }
            //for loop only increases if previous char was equal
            if(node->value == compareToNode->value){
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


string printGivenLevel(binaryTree* root, int level, int width) {
    string levelToString = "";
    if (root == NULL && level == 1) {
        for(int i = 0; i < width; i++){
            levelToString += " ";
        }
    }
    else if (level == 1 && root != NULL){
        for(int i = 0; i<width; i++){
            if(i < (width-root->value.size())/2 || i > (width+root->value.size())/2){
                levelToString += " ";
            }
            else{
                levelToString+= root->value;
                i+= root->value.size();
            }
        }
    }
    if (level > 1) {
        if (root != NULL) {
            if (root->leftChild != NULL) {
                levelToString += printGivenLevel(root->leftChild, level - 1, width/2);
            } else {
                levelToString += printGivenLevel(NULL, level - 1, width/2);
            }
            if (root->rightChild != NULL) {
                levelToString += printGivenLevel(root->rightChild, level - 1, width/2);
            } else {
                levelToString += printGivenLevel(NULL, level - 1, width/2);
            }
        }
        //lager to barn selv om noden er null for at disse skal ta plass i grafikken
        else{
            levelToString += printGivenLevel(NULL, level - 1, width/2);
            levelToString += printGivenLevel(NULL, level - 1, width/2);
        }
    }
    return levelToString;
}



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
        //setter root like noden
        return node;
    }
    else{
        binaryTree* compareToNode = rootPointer;
        //sammenligner hvert med ordet i hodet og påfølgende noder
        int i = 0;
        while (i < node->value.size()){
            //hvis det nye ordet er tidligere alfabetisk og venstre bein er ledig
            if(node->value < compareToNode->value && compareToNode->leftChild == NULL){
                compareToNode->leftChild = node;
                node->parent = compareToNode;
                return node;
                //dersom den er tidligere alfabetisk og venstre bein ikke er ledig
            } else if (node->value < compareToNode->value && compareToNode->leftChild != NULL){
                //så sammenlignes med venstre bein
                compareToNode = compareToNode->leftChild;
            }
            //hvis den kommer etterpå og høyre bein er ledig
            else if(compareToNode->rightChild == NULL && node->value > compareToNode->value){
                compareToNode->rightChild = node;
                node->parent = compareToNode;
                return node;
            }
            //hvis den er større og høyre bein ikke er ledig
            else if(node->value > compareToNode->value){
                //sammenlignes med høyre bein
                compareToNode = compareToNode->rightChild;
            }
            //if the words are equal it is placed to the right
            if(node->value == compareToNode->value){
                if(compareToNode->rightChild == NULL){
                    compareToNode->rightChild = node;
                    node->parent = compareToNode;
                    return node;
                }
                else{
                    compareToNode = compareToNode->rightChild;
                }
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



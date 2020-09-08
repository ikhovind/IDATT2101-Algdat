#include <stdlib.h>     /* malloc, free, rand */
#include <string>
struct Node{
    int digit;
    struct Node* next;
    struct Node* prev;
};

void insertAtFront(struct Node** headRef, int newDigit){
    //lager ny node
    struct Node* newNode = (struct Node*)malloc(sizeof(struct Node));
    //setter verdi
    newNode->digit = newDigit;

    newNode->next = (*headRef);
    newNode->prev = NULL;

    if((*headRef) != NULL){
        (*headRef)->prev = newNode;
    }
    (*headRef) = newNode;
}

void insertAfter(struct Node* prev,int newDigit){
    struct Node* newNode =(struct Node*)malloc(sizeof(struct Node));
    newNode->digit = newDigit;
    newNode->next = prev->next;
    prev->next = newNode;
    newNode->prev = prev;
    if(newNode->next != NULL){
        newNode->next->prev = newNode;
    }
}

void insertBefore(struct Node** headRef, struct Node* nextNode, int newDigit)
{

    struct Node* newNode = (struct Node*)malloc(sizeof(struct Node));

    newNode->digit = newDigit;

    newNode->prev = nextNode->prev;

    nextNode->prev = newNode;

    newNode->next = nextNode;

    if (newNode->prev != NULL)
        newNode->prev->next = newNode;
    else
        //dersom den forrige noden er null så vil den nye noden være det nye hodet
        (*headRef) = newNode;
}

void insertAtBack(struct Node** headRef, int newDigit){
    struct Node* newNode = (struct Node*)malloc(sizeof(struct Node));

    struct Node* last = *headRef;

    newNode->digit = newDigit;

    newNode->next = NULL;

    if (*headRef == NULL) {
        newNode->prev = NULL;
        *headRef = newNode;
        return;
    }

    while (last->next != NULL)
        last = last->next;

    last->next = newNode;

    newNode->prev = last;
}

void addDigitToNode(struct Node** headRef, struct Node* node, int newDigit){
    //dersom svaret blir under ti så endrer det ikke noen andre noder
    if(node->digit + newDigit < 10){
        node->digit += newDigit;
    }
    else{
        //dersom den som kommer før er null så må det settes in et ny node som kan ta det ekstra tallet
        if(node->prev == NULL){
            insertBefore(headRef,node,0);
        }
        //legger til 1 i den forrige noden rekursivt
        addDigitToNode(headRef,node->prev,1);
        node->digit += newDigit -10;
    }
}

void deleteNode(struct Node** headRef, struct Node* node){

    if(node->prev == NULL && node->next == NULL){
        node = NULL;
        delete node;
        return;
    }
    if(node->prev == NULL){
        (*headRef) = node->next;
        node->next->prev = NULL;
        delete node;
        return;
    }
    if(node->next == NULL){
        (*headRef) = node->prev;
        node->prev->next = NULL;
        delete node;
        return;
    }
    node->prev->next = node->next;
    node->next->prev = node->prev;
    delete(node);
}

void subtractDigitFromNode(struct Node** headRef, struct Node* node, int newDigit){
    //dersom tallet ikke blir negativt så slipper man å tenke på de andre nodene
    if(node->digit - newDigit >= 0){
        node->digit -= newDigit;
    }
    else{
        //dersom forrige er null så må det settes inn for å ta resten
        if(node->prev == NULL && node->digit - newDigit <= -10){
            insertBefore(headRef,node,0);
            subtractDigitFromNode(headRef,node->prev,1);
            node->digit -= newDigit +10;
        }else{
            node->digit -= newDigit;
        }
    }

    //deletes excess zeroes
    struct Node* first = *headRef;
    struct Node* toBeDeleted;
    while (first != NULL && first->digit == 0) {
        toBeDeleted = first;
        first = first->next;
        deleteNode(headRef,toBeDeleted);
    }
}

void addListToList(struct Node** firstHeadRef, struct Node** secondHeadRef){
    struct Node* firstNode = *firstHeadRef;
    struct Node* secondNode = *secondHeadRef;

    struct Node* firstLast;
    while (firstNode != NULL) {
        firstLast = firstNode;
        firstNode = firstNode->next;
    }

    struct Node* secondLast;
    while (secondNode != NULL) {
        secondLast = secondNode;
        secondNode = secondNode->next;
    }
    //if both numbers have a digit or only the first
    while(firstLast != NULL && secondLast != NULL || secondLast != NULL){
        //if the second number is longer than the first
        if(firstLast == NULL){
            insertAtFront(firstHeadRef, secondLast->digit);
        }
        else{
            addDigitToNode(firstHeadRef,firstLast,secondLast->digit);
            firstLast = firstLast->prev;
        }
        secondLast = secondLast->prev;
    }
}

void subtractListFromList(struct Node** firstHeadRef, struct Node** secondHeadRef){
    struct Node* firstNode = *firstHeadRef;
    struct Node* firstLast;
    while (firstNode != NULL) {
        firstLast = firstNode;
        firstNode = firstNode->next;
    }

    struct Node* secondNode = *secondHeadRef;
    struct Node* secondLast;
    while (secondNode != NULL) {
        secondLast = secondNode;
        secondNode = secondNode->next;
    }

    while(firstLast != NULL && secondLast != NULL){
        subtractDigitFromNode(firstHeadRef,firstLast,secondLast->digit);
        firstLast = firstLast->prev;
        secondLast = secondLast->prev;
    }

}
std::string toString(struct Node* node)
{
    std::string toString = "";
    struct Node* last;
    while (node != NULL) {
        toString += std::to_string(node->digit);
        node = node->next;
    }
    return toString;
}


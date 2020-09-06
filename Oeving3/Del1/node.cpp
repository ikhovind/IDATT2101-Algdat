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
    if(node->digit + newDigit < 10){
        node->digit += newDigit;
    }
    else{
        if(node->prev == NULL){
            insertBefore(headRef,node,0);
        }
        addDigitToNode(headRef,node->prev,1);
        node->digit += newDigit -10;
    }
}
void deleteNode(struct Node** headRef, struct Node* node){

    if(node->prev == NULL && node->next == NULL){
        node = NULL;
        return;
    }
    if(node->prev == NULL){
        (*headRef) = node->next;
        node->next->prev = NULL;
        return;
    }
    if(node->next == NULL){
        (*headRef) = node->prev;
        node->prev->next = NULL;
        return;
    }
    node->prev->next = node->next;
    node->next->prev = node->prev;
    delete(node);
}
void subtractDigitFromNode(struct Node** headRef, struct Node* node, int newDigit){
    if(node->digit - newDigit >= 0){
        node->digit -= newDigit;
    }
    else{
        if(node->prev == NULL && node->digit - newDigit <= -10){
            insertBefore(headRef,node,0);
            subtractDigitFromNode(headRef,node->prev,1);
            node->digit -= newDigit +10;
        }else{
            node->digit -= newDigit;
        }
    }
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
        addDigitToNode(firstHeadRef,firstLast,secondLast->digit);
        firstLast = firstLast->prev;
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

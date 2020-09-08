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
//trekker fra et tall fra en node og holder styr med hvordan dette påvirker de andre tallene
void subtractDigitFromNode(struct Node** headRef, struct Node* node, int newDigit){
    //dersom tallet ikke blir negativt så slipper man å tenke på de andre nodene
    if(node->digit - newDigit >= 0){
        node->digit -= newDigit;
    }
    else{
        //dersom forrige er null så må det settes inn for å ta resten
        if(node->digit - newDigit <= 0){
            subtractDigitFromNode(headRef,node->prev,1);
            node->digit -= newDigit;
            node->digit+=10;
        }
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
    //hvis megge tallene har flere siffer, eller det andre har flere siffer
    while(firstLast != NULL && secondLast != NULL || secondLast != NULL){
        //hvis det andre tallet har flere siffer så legges de ekstra sifrene til foran på det første tallet
        if(firstLast == NULL){
            insertAtFront(firstHeadRef, secondLast->digit);
        }
        else{
            //dersom det første tallet fortsatt har siffer
            addDigitToNode(firstHeadRef,firstLast,secondLast->digit);
            firstLast = firstLast->prev;
        }
        secondLast = secondLast->prev;
    }
}
//vanskelig å få til en skikkelig løsning, men dette funker
//returnerer true hvis vi måtte trekke liste 1 fra liste 2 istedenfor motsatt
bool subtractListFromList(struct Node** firstHeadRef, struct Node** secondHeadRef){
    struct Node* firstNode = *firstHeadRef;
    struct Node* firstLast;
    int firstLength = 0;
    //finner lengde og siste node
    while (firstNode != NULL) {
        firstLength++;
        firstLast = firstNode;
        firstNode = firstNode->next;
    }

    struct Node* secondNode = *secondHeadRef;
    struct Node* secondLast;
    int secondLength = 0;
    while (secondNode != NULL) {
        secondLength++;
        secondLast = secondNode;
        secondNode = secondNode->next;
    }
    //dersom det andre tallet har flere noder
    if(secondLength > firstLength){
        //trekker det lengste tallet fra det korteste tall for at subtraksjonen skal fungere ordentlig
        while(firstLast != NULL && secondLast != NULL){
            //liste 2 minus liste 1
            subtractDigitFromNode(secondHeadRef,secondLast,firstLast->digit);
            firstLast = firstLast->prev;
            secondLast = secondLast->prev;
        }
        //vi måtte bytte om på posisjonene i regnestykket, så returnerer true
        return true;
    }
    //dersom tallene er like lange så finner vi det med størst tall
    else if(secondLength == firstLength){
        struct Node* firstBiggestNode = *firstHeadRef;
        struct Node* secondBiggestNode = *secondHeadRef;
        //finner det største tallet
        while (firstBiggestNode->next != NULL && secondBiggestNode != NULL){
            //dersom det andre tallet er større enn det første tallet
            if(firstBiggestNode->digit < secondBiggestNode->digit){
                //ta andre minus første
                while(firstLast != NULL && secondLast != NULL){
                    subtractDigitFromNode(secondHeadRef,secondLast,firstLast->digit);
                    firstLast = firstLast->prev;
                    secondLast = secondLast->prev;
                }
                //måtte bytte om på posisjonen av tallene
                return true;
            }
            //hvis det første tallet er større enn det andre tallet
            else if(firstBiggestNode->digit > secondBiggestNode->digit){
                //ta first minus second
                while(firstLast != NULL && secondLast != NULL){
                    subtractDigitFromNode(firstHeadRef,firstLast,secondLast->digit);
                    firstLast = firstLast->prev;
                    secondLast = secondLast->prev;
                }
                //byttet ikke om på posisjonen
                return false;
            }
            //dersom sifrene er like så går vi videre til neste
            firstBiggestNode = firstBiggestNode->next;
            secondBiggestNode = secondBiggestNode->next;
        }
        //dersom tallene er helt like så trekker vi fra som vanlig
        while(firstLast != NULL && secondLast != NULL){
            subtractDigitFromNode(firstHeadRef,firstLast,secondLast->digit);
            firstLast = firstLast->prev;
            secondLast = secondLast->prev;
        }
    }
    //dersom det første tallet er størst
    else{
        //first minus second
        while(firstLast != NULL && secondLast != NULL){
            subtractDigitFromNode(firstHeadRef,firstLast,secondLast->digit);
            firstLast = firstLast->prev;
            secondLast = secondLast->prev;
        }
    }
    //hvis vi kommer hit så har vi ikke byttet på rekkefølgen
    return false;
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

Node* subtractLinkedListHelper(Node* l1, Node* l2, bool& borrow)
{

    if (l1 == NULL && l2 == NULL && borrow == 0)
        return NULL;

    Node* previous
            = subtractLinkedListHelper(
                    l1 ? l1->next : NULL,
                    l2 ? l2->next : NULL, borrow);

    int d1 = l1->digit;
    int d2 = l2->digit;
    int sub = 0;

    /* if you have given the value value to next digit then
       reduce the d1 by 1 */
    if (borrow) {
        d1--;
        borrow = false;
    }

    /* If d1 < d2, then borrow the number from previous digit.
       Add 10 to d1 and set borrow = true; */
    if (d1 < d2) {
        borrow = true;
        d1 = d1 + 10;
    }

    /* subtract the digits */
    sub = d1 - d2;

    /* Create a Node with sub value */
    Node* current = newNode(sub);

    /* Set the Next pointer as Previous */
    current->next = previous;

    return current;
}

/* This API subtracts two linked lists and returns the
   linked list which shall  have the subtracted result. */
Node* subtractLinkedList(Node* l1, Node* l2)
{
    // Base Case.
    if (l1 == NULL && l2 == NULL)
        return NULL;

    // In either of the case, get the lengths of both
    // Linked list.
    int len1 = getLength(l1);
    int len2 = getLength(l2);

    Node *lNode = NULL, *sNode = NULL;

    Node* temp1 = l1;
    Node* temp2 = l2;

    // If lengths differ, calculate the smaller Node
    // and padd zeros for smaller Node and ensure both
    // larger Node and smaller Node has equal length.
    if (len1 != len2) {
        lNode = len1 > len2 ? l1 : l2;
        sNode = len1 > len2 ? l2 : l1;
        sNode = paddZeros(sNode, abs(len1 - len2));
    }

    else {
        // If both list lengths are equal, then calculate
        // the larger and smaller list. If 5-6-7 & 5-6-8
        // are linked list, then walk through linked list
        // at last Node as 7 < 8, larger Node is 5-6-8
        // and smaller Node is 5-6-7.
        while (l1 && l2) {
            if (l1->digit != l2->digit) {
                lNode = l1->digit > l2->digit ? temp1 : temp2;
                sNode = l1->digit > l2->digit ? temp2 : temp1;
                break;
            }
            l1 = l1->next;
            l2 = l2->next;
        }
    }

    // After calculating larger and smaller Node, call
    // subtractLinkedListHelper which returns the subtracted
    // linked list.
    bool borrow = false;
    return subtractLinkedListHelper(lNode, sNode, borrow);
}


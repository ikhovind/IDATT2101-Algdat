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


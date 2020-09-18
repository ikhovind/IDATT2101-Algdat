#include <stdio.h>
#include <memory.h>
#include <iostream>
#include <fstream>
#include "HashTable.h"
using namespace std;
unsigned long multhash(int k, int x);

unsigned long keyGen(placedLink *name, int length){
    using namespace std;
    unsigned long sum = 0;
    for(int i = 0; i < length; i++){
        //ganger med  7^i
        sum = sum * 7+ (name->name[i])*(7);
    }
    //får ikke til å funke uten metodekall
    return multhash(sum,7);
}

unsigned long multhash(int k, int x){
    const std::uint32_t knuth = 2654435769;
    return k * knuth >> (32-x);
}

unsigned place(placedLink name, int length){
    unsigned index = (keyGen(&name,length));
    if(hashTable[index].next == NULL){
        memcpy(hashTable[index].name, "test", sizeof("test"));
        printf("input %s\n",hashTable[index].name);
    }
    else{
        placedLink *current ={hashTable[index].next};
        /*
        while(current->next != NULL){
            printf("kollisjon på %d\n", index);
        }
        current->next = &name;
         */
    }
    return index;
}
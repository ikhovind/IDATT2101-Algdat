//
// Created by ingebrigt on 18.09.2020.
//

#ifndef DEL1_HASHTABLE_H
#define DEL1_HASHTABLE_H
struct placedLink{
    struct placedLink* next;
    unsigned char name[];
};

placedLink hashTable[128];

#endif //DEL1_HASHTABLE_H

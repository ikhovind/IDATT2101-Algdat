#include <stdio.h>
#include <memory.h>
#include <iostream>
#include <fstream>
#include "HashTable.h"
#include <list>

using namespace std;

class HashTable{
private:
    list<std::string> *table;
    int total_elements;

public:
    // Constructor to create a hash table with 'n' indices:
    HashTable(int n){
        total_elements = n;
        table = new list<std::string>[total_elements];
    }

private:

    unsigned long keyGen(std::string name, int length){
        using namespace std;
        unsigned long sum = 0;
        for(int i = 0; i < length; i++){
            //ganger med  7^i
            sum += sum * 7+ (name.at(i))*(7);
        }
        //får ikke til å funke uten metodekall
        return multHash(sum, 7);
    }

    unsigned long multHash(unsigned k, int x){
        const std::uint32_t knuth = 2654435769;
        return k * knuth >> (32-x);
    }

public:
    // Insert data in the hash table:
    void insertElement(std::string name){
        //TODO fix
        table[keyGen(name, name.string::length())].push_back(name);
    }

    void printAll(){
        // Traverse each index:
        int counter = 0;
        for(int i = 0; i < total_elements; i++) {
            int innerCounter = 0;
            cout << "Index " << i << ": ";
            // Traverse the list at current index:
            for (std::string j : table[i]){
                innerCounter++;
                cout << j << " => ";
            }
            if(innerCounter > 1){
                counter += innerCounter-1;
            }
            cout << endl;
        }
        cout << "antall kollisjoner " << counter << endl;
    }
};

